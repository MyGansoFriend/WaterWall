package com.luckylittlesparrow.waterwall.recycler.dao

import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionDao
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.*
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback
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
            .loadingResourceId(loadingResourceId)
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
        section.sectionKey = "KEY"
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
        getField().set(sectionDao, lastSearchString)
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
        getField().set(sectionDao, lastSearchString)

        sectionDao.filter("new")
        assertTrue(section.baseList.isEmpty())
    }

    @Test
    fun testFilter_InitBaseList_ContainsLastResult() {
        val lastSearchString = "search"
        val expectedList = TestItemsFactory.getNames2()
        getField().set(sectionDao, lastSearchString)
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

    @Test
    fun testFilterSupportHeaderFilter() {
        section.supportFilterHeader = true
        val list = TestItemsFactory.getNames2()
        val lastSearchString = (list[2] as TestItem).name

        val result = sectionDao.filter(lastSearchString)

        assertEquals(result!!.first, section.baseList)
        assertEquals(result.second, section.filteredList)
    }

    @Test
    fun testFilterSupportHeaderFilterNotVisible() {
        section.supportFilterHeader = true
        section.isVisible = false
        val list = TestItemsFactory.getNames2()
        val lastSearchString = (list[2] as TestItem).name

        val result = sectionDao.filter(lastSearchString)

        assertEquals(result!!.first, ArrayList<ItemContainer>())
        assertEquals(result.second, section.filteredList)
        assertTrue(section.isVisible)
    }

    @Test
    fun getVisibleItemsList() {
        assertTrue(section.filteredList.isEmpty())
        assertEquals(sectionDao.sectionCurrentSize(), section.baseList.size)
        assertEquals(sectionDao.getVisibleItemsList(), section.baseList)

        val expectedList = TestItemsFactory.getNames2()
        section.filteredList.addAll(expectedList)

        assertEquals(sectionDao.getVisibleItemsList(), section.filteredList)

        section.state = SectionState.LOADING
        assertEquals(sectionDao.getVisibleItemsList(), section.sourceList.subList(0, 2))
    }

    private fun getField(): Field {
        val field = FilterableSectionDao::class.java.getDeclaredField("lastSearchString")
        field.isAccessible = true
        return field
    }

}