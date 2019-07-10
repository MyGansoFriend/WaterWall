package com.luckylittlesparrow.srvlist.recycler.dao

import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSection
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionDao
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.testdata.*
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field
import org.hamcrest.core.Is.`is` as Is


@RunWith(MockitoJUnitRunner::class)
class FilterSectionDaoTest {
    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    private lateinit var sectionDao: FilterableSectionDao<*, *, *>

    private lateinit var section: FilterableSection<TestHeader, TestItem, TestFooter>

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

        section = spy(object : FilterTestSection(
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
        sectionDao = FilterableSectionDao(section)

        section.sectionStateCallback = mock()
        section.key = "KEY"
    }

    @Test
    fun getContentItem() {
        assertEquals(sectionDao.getContentItem(0), section.baseList[0])
        section.filteredList.add(newItem)
        assertEquals(sectionDao.getContentItem(0), section.filteredList[0])
    }

    @Test
    fun testFilter_PreventUnnecessaryFilter() {
        val lastSearchString = "search"
        getLastSearchStringField().set(sectionDao, lastSearchString)
        assertNull(sectionDao.filter(lastSearchString + "new"))
    }

    @Test
    fun testFilter_InitBaseList_FirstTime() {
        val lastSearchString = "search"

        sectionDao.filter(lastSearchString)
        assertThat(section.baseList, Is(section.sourceList))
    }

    @Test
    fun testFilter_InitBaseList_EmptyLastResult() {
        val lastSearchString = "search"
        getLastSearchStringField().set(sectionDao, lastSearchString)

        sectionDao.filter("new")
        assertTrue(section.baseList.isEmpty())
    }

    @Test
    fun testFilter_InitBaseList_ContainsLastResult() {
        val lastSearchString = "search"
        val expectedList = TestItemsFactory.getNames2()
        getLastSearchStringField().set(sectionDao, lastSearchString)
        section.filteredList.addAll(expectedList)
        sectionDao.filter("new")
        assertThat(section.baseList, Is(expectedList))
    }

    @Test
    fun testFilter() {
        val list = TestItemsFactory.getNames2()
        val expectedList = listOf(TestItemsFactory.header, list[2], TestItemsFactory.footer)
        val lastSearchString = (list[2] as TestItem).name

        val result = sectionDao.filter(lastSearchString)

        assertEquals(result!!.first, section.sourceList)
        assertThat(result.second, Is(expectedList))
    }

    private fun getLastSearchStringField(): Field {
        val field = FilterableSectionDao::class.java.getDeclaredField("lastSearchString")
        field.isAccessible = true
        return field
    }

}