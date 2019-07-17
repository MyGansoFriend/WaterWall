package com.luckylittlesparrow.waterwall.recycler.dao

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.*
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field


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

    private val sectionParameters = SectionParams.builder()
        .headerResourceId(headerResourceId)
        .itemResourceId(itemResourceId)
        .footerResourceId(footerResourceId)
        .loadingResourceId(loadingResourceId)
        .build()

    private var section: Section<TestHeader, TestItem, TestFooter> = spy(object : TestSection(
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

    private val newItem = TestItem("new")
    private val oldItem = TestItem("old")
    private val newHeader = TestHeader("new")
    private val oldHeader = TestHeader("old")

    private val newFooter = TestFooter("new")
    private val oldFooter = TestFooter("old")
    private val diff: DiffUtilItemCallback = mock()
    @Before
    fun setUp() {
        sectionDao = SimpleSectionDao(section)

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"
    }

    @Test
    fun areContentsTheSameWithItems() {
        sectionDao.areContentsTheSame(oldItem, newItem)
        verify(diff).areContentsTheSame(oldItem, newItem)
    }

    @Test
    fun areContentsTheSameWithHeaders() {
        sectionDao.areContentsTheSame(oldHeader, newHeader)
        verify(diff).areHeadersContentsTheSame(oldHeader, newHeader)
    }

    @Test
    fun areItemsTheSameWithItems() {
        sectionDao.areItemsTheSame(oldItem, newItem)
        verify(diff).areItemsTheSame(oldItem, newItem)
    }

    @Test
    fun areItemsTheSameWithHeaders() {
        assertFalse(sectionDao.areItemsTheSame(oldHeader, newHeader))
        verify(diff).areHeadersTheSame(oldHeader, newHeader)
    }

    @Test
    fun areItemsTheSameWithFooters() {
        assertFalse(sectionDao.areItemsTheSame(oldFooter, newFooter))
        verify(diff).areFootersTheSame(oldFooter, newFooter)
    }

    @Test
    fun areContentTheSameWithFooters() {
        assertFalse(sectionDao.areContentsTheSame(oldFooter, newFooter))
        verify(diff).areFootersContentsTheSame(oldFooter, newFooter)
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
        verify(section).hasHeader

        sectionDao.hasFooter()
        verify(section).hasFooter

        sectionDao.isVisible()
        verify(section).isVisible

        sectionDao.state()
        verify(section).state

        sectionDao.key()
        verify(section).sectionKey
    }

    @Test
    fun hasItems() {
        assertFalse(sectionDao.hasItems(newItem, oldItem))
        assertTrue(sectionDao.hasItems(section.sourceList[1], section.sourceList[2]))
    }

    @Test
    fun filter() {
        assertNull(sectionDao.filter("s"))
    }

    @Test
    fun getVisibleItemsList_Expanded() {
        section.state = SectionState.LOADED
        assertEquals(sectionDao.getVisibleItemsList(), section.sourceList)

        section.state = SectionState.LOADING
        assertEquals(sectionDao.getVisibleItemsList(), section.sourceList.subList(0, 2))
    }

    @Test
    fun getVisibleItemsList_Collapsed() {
        getField().set(section, false)
        assertEquals(sectionDao.getVisibleItemsList(), listOf(section.sourceList.first()))
    }

    private fun getField(): Field {
        val field = Section::class.java.getDeclaredField("isExpanded")
        field.isAccessible = true
        return field
    }

}