package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.srvlist.recycler.testdata.TestSection
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SectionStateTest {

    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    @Test
    fun initSectionWithBundleAddMoreItemsWithNotLoadedState() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .build()

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.sectionStateCallback = mock()
        section.key = "KEY"
        section.state = SectionState.LOADING

        section.addItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNumbersList()
            )
        )

        verify(section.sectionStateCallback, never())!!.onSectionContentAdded(
            section.key,
            TestItemsFactory.getNumbersList().size
        )
        verify(section.sectionStateCallback, never())!!.onSectionContentChanged(
            section.key
        )
    }

    @Test
    fun changeSectionState() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .build()

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.sectionStateCallback = mock()
        section.key = "KEY"
        section.state = SectionState.EMPTY

        verify(section.sectionStateCallback)!!.onSectionStateChanged(
            section.key,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeSectionStateWithEmptyResources() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.sectionStateCallback = mock()
        section.key = "KEY"
        section.state = SectionState.EMPTY

        verify(section.sectionStateCallback, never())!!.onSectionStateChanged(
            section.key,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }
}