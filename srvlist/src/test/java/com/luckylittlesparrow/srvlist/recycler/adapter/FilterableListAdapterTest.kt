package com.luckylittlesparrow.srvlist.recycler.adapter

import android.os.Looper.getMainLooper
import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.srvlist.recycler.base.BaseListAdapter
import com.luckylittlesparrow.srvlist.recycler.base.SectionMediator
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionMediator
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import com.luckylittlesparrow.srvlist.recycler.testdata.SectionFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import java.lang.reflect.Field


@RunWith(RobolectricTestRunner::class)
class FilterableListAdapterTest {
    private lateinit var adapter: FilterableSectionedAdapter
    private val sectionMediator: SectionMediator = mock()
    private lateinit var sectionStateCallback: SectionStateCallback

    private val captor = ArgumentCaptor.forClass(DiffUtil.DiffResult::class.java)

    @Before
    fun setUp() {
        shadowOf(getMainLooper()).idle()
        adapter = spy(FilterableSectionedAdapter())
        adapter.supportStickyHeader = true
        adapter.sectionMediator = sectionMediator
        sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback
    }

    @Test
    fun init() {
        adapter = FilterableSectionedAdapter()
        assertTrue(adapter.sectionMediator is FilterableSectionMediator)
    }

    @Test(expected = IllegalStateException::class)
    fun addSectionWithError() {
        val section = SectionFactory.getSection()
        adapter.addSection(section)
        verify(sectionMediator, never()).addSection(section, sectionStateCallback)
    }

    @Test
    fun addSection() {
        val section = SectionFactory.getFilterableSection()
        adapter.addSection(section)
        verify(sectionMediator).addSection(section, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun addSectionWithKeyError() {
        val section = SectionFactory.getSection()
        adapter.addSection(section.key, section)
        verify(sectionMediator, never()).addSection(section.key, section, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun removeSectionError() {
        val section = SectionFactory.getSection()
        adapter.removeSection(section)
        verify(sectionMediator, never()).removeSection(section)
    }

    @Test
    fun removeSection() {
        val section = SectionFactory.getFilterableSection()
        adapter.removeSection(section)
        verify(sectionMediator).removeSection(section)
    }

    @Test
    fun addSectionWithKey() {
        val section = SectionFactory.getFilterableSection()
        adapter.addSection(section.key, section)
        verify(sectionMediator).addSection(section.key, section, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun addSectionWithKey_SupportSticky() {
        val section=SectionFactory.getFilterableSectionWithoutHeader()

        adapter.addSection(section.key, section)
        verify(sectionMediator).addSection(section.key, section, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun addSection_SupportSticky() {
        val section = SectionFactory.getFilterableSectionWithoutHeader()

        adapter.addSection(section)
        verify(sectionMediator).addSection(section, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun addSectionsError() {
        val sections = SectionFactory.getSectionList()
        adapter.addSections(sections)
        verify(sectionMediator, never()).addSections(sections, sectionStateCallback)
    }


    @Test
    fun addSectionsEmptyList() {
        adapter.addSections(listOf())
        verify(sectionMediator, never()).addSections(listOf(), sectionStateCallback)
    }


    @Test
    fun addSectionsSections() {
        val sections = SectionFactory.getFilterableSectionList()
        adapter.addSections(sections)
        verify(sectionMediator).addSections(sections, sectionStateCallback)
    }

    @Test(expected = IllegalStateException::class)
    fun addSectionsSections_Sticky() {
        val sections = SectionFactory.getFilterableSectionWithoutHeader()
        adapter.addSections(listOf(sections))
    }

//    @Test
//    fun filter() {
//        doNothing().whenever(adapter).dispatchUpdates(capture(captor))
//        val list = TestItemsFactory.getNames()
//        val sections = SectionFactory.getFilterableSectionList()
//
//        whenever(sectionMediator.getSectionList()).thenReturn(SectionFactory.getFilterableDAOSectionList())
//
//        adapter.filter(list[2].name)
//
//        assertNotNull(captor.value)
//
//    }

    fun getField(fieldName: String): Field {
        val field = BaseListAdapter::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field
    }

}