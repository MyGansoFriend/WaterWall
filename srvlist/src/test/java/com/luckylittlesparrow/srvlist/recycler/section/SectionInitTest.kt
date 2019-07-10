package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.srvlist.recycler.testdata.TestSection
import org.junit.Assert.*
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

class SectionInitTest {

    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    @Test
    fun correctSectionParams() {

        val collapsedItems = 16

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .failedResourceId(failedResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .supportExpansion(true)
            .supportShowMoreFunction(true)
            .collapsedItemCount(collapsedItems)
            .build()

        val section = object : TestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertThat(section.itemResourceId, Is(itemResourceId))
        assertThat(section.headerResourceId, Is(headerResourceId))
        assertThat(section.footerResourceId, Is(footerResourceId))
        assertThat(section.failedResourceId, Is(failedResourceId))
        assertThat(section.loadingResourceId, Is(loadingResourceId))
        assertThat(section.emptyResourceId, Is(emptyResourceId))

        assertThat(section.hasHeader(), Is(true))
        assertThat(section.hasFooter(), Is(true))
        assertThat(section.supportStates, Is(true))
        assertThat(section.supportExpansion, Is(true))
        assertThat(section.supportShowMore, Is(true))
        assertThat(section.collapsedItemCount, Is(collapsedItems))
    }

    @Test
    fun correctMinSectionParams() {

        val sectionParameters = SectionParams.builder()
            .itemResourceId(itemResourceId)
            .build()

        val section = object : TestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertThat(section.itemResourceId, Is(itemResourceId))
        assertNull(section.headerResourceId)
        assertNull(section.footerResourceId)
        assertNull(section.failedResourceId)
        assertNull(section.loadingResourceId)
        assertNull(section.emptyResourceId)

        assertThat(section.hasHeader(), Is(false))
        assertThat(section.hasFooter(), Is(false))
        assertThat(section.supportStates, Is(false))
        assertThat(section.supportExpansion, Is(false))
        assertThat(section.supportShowMore, Is(false))
        assertThat(section.collapsedItemCount, Is(4))
    }

    @Test
    fun correctSectionWithItems() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNumbersList(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()
        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNumbersList())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.originalSize(), expectedList.size)

    }

    @Test(expected = IllegalStateException::class)
    fun initSectionForgotHeader() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection(
            contentItems = TestItemsFactory.getNumbersList(),
            footerItem = TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun initSectionForgotItems() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection(
            footerItem = TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
    }
}