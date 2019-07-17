package com.luckylittlesparrow.waterwall.recycler.section

import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.waterwall.recycler.testdata.TestSection
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
        section.sectionKey = "KEY"
        section.state = SectionState.LOADING

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNumbersList()
            )
        )

        verify(section.sectionStateCallback, never())!!.onSectionContentAdded(
            section.sectionKey!!,
            TestItemsFactory.getNumbersList().size
        )
        verify(section.sectionStateCallback, never())!!.onSectionContentChanged(
            section.sectionKey!!
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
        section.sectionKey = "KEY"
        section.state = SectionState.EMPTY

        verify(section.sectionStateCallback)!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.EMPTY,
            SectionState.LOADED
        )

        section.state = SectionState.FAILED

        verify(section.sectionStateCallback)!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.FAILED,
            SectionState.EMPTY
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeSectionStateWithEmptyResources_EMPTY() {
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
        section.sectionKey = "KEY"
        section.state = SectionState.EMPTY

        verify(section.sectionStateCallback, never())!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeSectionStateWithEmptyResources_LOADING() {
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
        section.sectionKey = "KEY"
        section.state = SectionState.LOADING

        verify(section.sectionStateCallback, never())!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun changeSectionStateWithEmptyResources_FAILED() {
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
        section.sectionKey = "KEY"
        section.state = SectionState.FAILED

        verify(section.sectionStateCallback, never())!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }


    @Test(expected = IllegalArgumentException::class)
    fun changeSectionStateWithEmptyResources_LOADED() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
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
        section.sectionKey = "KEY"
        section.state = SectionState.LOADED

        verify(section.sectionStateCallback, never())!!.onSectionStateChanged(
            section.sectionKey!!,
            SectionState.EMPTY,
            SectionState.LOADED
        )
    }
}