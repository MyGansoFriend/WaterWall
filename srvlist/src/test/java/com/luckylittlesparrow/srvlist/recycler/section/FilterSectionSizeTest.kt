package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.testdata.FilterTestSection
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItem
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import org.junit.Assert.assertEquals
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

}