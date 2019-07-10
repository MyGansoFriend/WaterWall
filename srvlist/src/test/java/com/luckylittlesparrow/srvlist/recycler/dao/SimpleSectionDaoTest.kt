package com.luckylittlesparrow.srvlist.recycler.dao

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.srvlist.recycler.testdata.*
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SimpleSectionDaoTest {
    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    private lateinit var sectionDao: SimpleSectionDao<*, *, *>

    private lateinit var section: Section<TestHeader, TestItem, TestFooter>

    private val newItem = TestItem("new")
    private val oldItem = TestItem("old")
    private val newHeader = TestHeader("new")
    private val oldHeader = TestHeader("old")
    private val diff: DiffUtilItemCallback = mock()
    @Before
    fun setUp() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        section = spy(object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }

            override fun getDiffUtilItemCallback(): DiffUtilItemCallback {
                return diff
            }
        })
        sectionDao = SimpleSectionDao(section)

        section.sectionStateCallback = mock()
        section.key = "KEY"
    }

    @Test
    fun areContentsTheSameWithItems() {
        sectionDao.areContentsTheSame(oldItem, newItem)
        verify(diff).areContentsTheSame(oldItem, newItem)
    }

    @Test
    fun areContentsTheSameWithNotItems() {
        assertFalse(sectionDao.areContentsTheSame(oldHeader, newHeader))
        assertTrue(sectionDao.areContentsTheSame(newHeader, newHeader))

        verify(diff, never()).areContentsTheSame(oldHeader, newHeader)
    }

    @Test
    fun areItemsTheSameWithItems() {
        sectionDao.areItemsTheSame(oldItem, newItem)
        verify(diff).areItemsTheSame(oldItem, newItem)
    }

    @Test
    fun areItemsTheSameWithNotItems() {
        assertFalse(sectionDao.areItemsTheSame(oldHeader, newHeader))
        assertTrue(sectionDao.areItemsTheSame(oldHeader, oldHeader))

        verify(diff, never()).areItemsTheSame(oldHeader, newHeader)
    }

    @Test
    fun getItem() {
        var result = sectionDao.getItem(ItemContainer.ItemType.ITEM, 0)
        assertEquals(result, sectionDao.section.sourceList.first())

        result = sectionDao.getItem(ItemContainer.ItemType.ITEM, sectionDao.sectionOriginalSize() - 1)
        assertEquals(result, sectionDao.section.sourceList.last())

        result = sectionDao.getItem(ItemContainer.ItemType.HEADER, 0)
        assertEquals(result, sectionDao.section.sourceList.first())

        result = sectionDao.getItem(ItemContainer.ItemType.FOOTER, sectionDao.sectionOriginalSize() - 1)
        assertEquals(result, sectionDao.section.sourceList.last())

        result = sectionDao.getContentItem(2)
        assertEquals(result, sectionDao.section.sourceList[2])
    }

    @Test(expected = IllegalStateException::class)
    fun getItemError() {
        sectionDao.getItem(ItemContainer.ItemType.ITEM, -1)
    }

    @Test
    fun getters() {
        sectionDao.getSectionList()

        verify(section).sourceList

        sectionDao.sectionOriginalSize()
        verify(section).originalSize()

        sectionDao.hasHeader()
        verify(section).hasHeader()

        sectionDao.hasFooter()
        verify(section).hasFooter()

        sectionDao.isVisible()
        verify(section).isVisible

        sectionDao.state()
        verify(section).state

        sectionDao.key()
        verify(section).key
    }

    @Test
    fun hasItems() {
        assertFalse(sectionDao.hasItems(newItem, oldItem))
        assertTrue(sectionDao.hasItems(section.sourceList[1], section.sourceList[2]))
    }

}