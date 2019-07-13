package com.luckylittlesparrow.waterwall.recycler.section

import android.view.View
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.*
import com.nhaarman.mockitokotlin2.mock
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

    private val errorSectionParameters = SectionParams.builder()
        .supportShowMoreFunction(true)
        .build()

    private val correctSectionParameters = SectionParams.builder()
        .headerResourceId(SectionLogicTest.headerResourceId)
        .itemResourceId(SectionLogicTest.itemResourceId)
        .footerResourceId(SectionLogicTest.footerResourceId)
        .loadingResourceId(SectionLogicTest.loadingResourceId)
        .emptyResourceId(SectionLogicTest.emptyResourceId)
        .failedResourceId(SectionLogicTest.failedResourceId)
        .supportExpandFunction(true)
        .build()

    private val errorSection = object : TestSection(
    ) {
        override fun getSectionParams(): SectionParams {
            return errorSectionParameters
        }
    }

    private val correctSection = object : TestSection(
    ) {
        override fun getSectionParams(): SectionParams {
            return correctSectionParameters
        }
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
            .supportExpandFunction(true)
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

        assertThat(section.currentSize(), Is(0))
        section.state = SectionState.LOADING
        assertThat(section.currentSize(), Is(1))

        assertThat(section.hasHeader, Is(true))
        assertThat(section.hasFooter, Is(true))
        assertThat(section.supportStates, Is(true))
        assertThat(section.supportExpandFunction, Is(true))
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

        assertThat(section.hasHeader, Is(false))
        assertThat(section.hasFooter, Is(false))
        assertThat(section.supportStates, Is(false))
        assertThat(section.supportExpandFunction, Is(false))
        assertThat(section.supportShowMore, Is(false))
        assertThat(section.collapsedItemCount, Is(4))
    }

    @Test
    fun correctSectionWithItems() {

        val itemViewHolder: BaseViewHolder<TestItem> = mock()
        val footerViewHolder: BaseViewHolder<TestFooter> = mock()
        val headerViewHolder: BaseViewHolder<TestHeader> = mock()
        val mockViewHolder: BaseViewHolder<Nothing> = mock()
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

            override fun getItemViewHolder(view: View): BaseViewHolder<TestItem> {
                return itemViewHolder
            }

            override fun getEmptyViewHolder(view: View): BaseViewHolder<Nothing> {
                return mockViewHolder
            }

            override fun getLoadingViewHolder(view: View): BaseViewHolder<Nothing> {
                return mockViewHolder
            }

            override fun getFailedViewHolder(view: View): BaseViewHolder<Nothing> {
                return mockViewHolder
            }

            override fun getFooterViewHolder(view: View): BaseViewHolder<TestFooter> {
                return footerViewHolder
            }

            override fun getHeaderViewHolder(view: View): BaseViewHolder<TestHeader> {
                return headerViewHolder
            }
        }
        assertEquals(section.getItemViewHolder(mock()), itemViewHolder)
        assertEquals(section.getFailedViewHolder(mock()), mockViewHolder)
        assertEquals(section.getEmptyViewHolder(mock()), mockViewHolder)
        assertEquals(section.getLoadingViewHolder(mock()), mockViewHolder)
        assertEquals(section.getHeaderViewHolder(mock()), headerViewHolder)
        assertEquals(section.getFooterViewHolder(mock()), footerViewHolder)

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

    @Test(expected = IllegalStateException::class)
    fun sectionGetHeaderViewHolderError() {
        errorSection.getHeaderViewHolder(mock())
    }

    @Test(expected = IllegalStateException::class)
    fun sectionGetFooterViewHolderError() {
        errorSection.getFooterViewHolder(mock())
    }

    @Test
    fun sectionGetItemViewHolderError() {
        errorSection.getItemViewHolder(mock())
    }

    @Test(expected = IllegalStateException::class)
    fun sectionGetLoadingViewHolderError() {
        errorSection.getLoadingViewHolder(mock())
    }

    @Test(expected = IllegalStateException::class)
    fun sectionGetEmptyViewHolderError() {
        errorSection.getEmptyViewHolder(mock())
    }

    @Test(expected = IllegalStateException::class)
    fun sectionGetFailedViewHolderError() {
        errorSection.getFailedViewHolder(mock())
    }

    @Test
    fun sectionGetHeaderViewHolder() {
        assertTrue(correctSection.getHeaderViewHolder(mock()) is EmptyViewHolder)
    }

    @Test
    fun sectionGetFooterViewHolder() {
        assertTrue(correctSection.getFooterViewHolder(mock()) is EmptyViewHolder)
    }

    @Test
    fun sectionGetItemViewHolder() {
        assertTrue(correctSection.getItemViewHolder(mock()) is EmptyViewHolder)
    }

    @Test
    fun sectionGetLoadingViewHolder() {
        assertTrue(correctSection.getLoadingViewHolder(mock()) is EmptyViewHolder)
    }

    @Test
    fun sectionGetEmptyViewHolder() {
        assertTrue(correctSection.getEmptyViewHolder(mock()) is EmptyViewHolder)
    }

    @Test
    fun sectionGetFailedViewHolder() {
        assertTrue(correctSection.getFailedViewHolder(mock()) is EmptyViewHolder)
    }
}