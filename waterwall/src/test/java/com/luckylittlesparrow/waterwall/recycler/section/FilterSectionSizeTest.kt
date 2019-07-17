package com.luckylittlesparrow.waterwall.recycler.section

import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.FilterTestSection
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItem
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Test

class FilterSectionSizeTest {

    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    @Test(expected = IllegalStateException::class)
    fun sectionDoesntSupportExpandShowMore() {
        val collapsedItemCount = 10
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportExpandFunction(true)
            .supportShowMoreFunction(true)
            .collapsedItemCount(collapsedItemCount)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

    }

    @Test
    fun sectionCurrentListSizeCollapsedNotLoaded() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames() as List<TestItem>,
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.state = SectionState.LOADING

        var collapsedSize = 0
        if (section.hasHeader) collapsedSize++
        if (section.hasFooter) collapsedSize++

        assertEquals(section.currentSize(), collapsedSize + 1)
    }

    @Test
    fun sectionCurrentListSizeExpanded() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames() as List<TestItem>,
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertEquals(section.currentSize(), section.sourceList.size)
    }

    @Test
    fun sectionCurrentListSizeAfterFilter() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames() as List<TestItem>,
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertEquals(section.currentSize(), section.baseList.size)

        section.filteredList.addAll(section.baseList.subList(0, 3))

        assertEquals(section.currentSize(), section.filteredList.size)
    }

    @Test
    fun sectionCurrentListSizeStub() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .build()

        val section = object : FilterTestSection(
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertEquals(section.currentSize(), 0)
        section.state = SectionState.LOADING
        assertEquals(section.currentSize(), 1)
    }

    @Test
    fun clearSection() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        val previousList = section.getAllItems()

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        assertFalse(section.baseList.isEmpty())

        assertFalse(section.isEmpty())
        section.clearSection()
        assertTrue(section.isEmpty())
        assertTrue(section.baseList.isEmpty())

        verify(section.sectionStateCallback)!!.onSectionContentUpdated(
            previousList,
            section.sourceList,
            section.sectionKey!!
        )
    }

}