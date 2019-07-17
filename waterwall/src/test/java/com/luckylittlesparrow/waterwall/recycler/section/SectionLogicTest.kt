package com.luckylittlesparrow.waterwall.recycler.section

import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.waterwall.recycler.testdata.TestSection
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Assert.*
import org.junit.Test

class SectionLogicTest {

    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    @Test
    fun isEmpty() {
        val sectionParameters = SectionParams.builder()
            .itemResourceId(itemResourceId)
            .build()

        val section = object : TestSection(
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        assertTrue(section.isEmpty())
    }

    @Test
    fun sectionOnExpandClick() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportExpandFunction(true)
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

        section.onExpandClickListener.invoke()

        assertFalse(section.isExpanded)

        verify(section.sectionStateCallback)!!.onSectionExpandChange(
            section.sectionKey!!,
            false
        )
    }

    @Test
    fun sectionOnExpandWithClosedByDefaultClick() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportExpandFunction(true)
            .isExpandedByDefault(false)
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

        section.onExpandClickListener.invoke()

        assertTrue(section.isExpanded)

        verify(section.sectionStateCallback)!!.onSectionExpandChange(
            section.sectionKey!!,
            true
        )
    }

    @Test(expected = IllegalStateException::class)
    fun sectionOnExpandInitFail() {
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

        section.onExpandClickListener.invoke()

        verify(section.sectionStateCallback)!!.onSectionExpandChange(
            section.sectionKey!!,
            true
        )
    }

    @Test(expected = IllegalStateException::class)
    fun sectionOnShowMoreInitFail() {
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

        section.onShowMoreClickListener.invoke()
    }

    @Test
    fun sectionOnShowMoreByDefaultClick() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportShowMoreFunction(true)
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

        section.onShowMoreClickListener.invoke()

        assertTrue(section.isShowMoreClicked)

        verify(section.sectionStateCallback)!!.onSectionShowMoreChange(
            section.sectionKey!!,
            4,
            true
        )
    }

    @Test
    fun sectionOnShowMoreClick() {
        val collapsedItemCount = 10
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportShowMoreFunction(true)
            .collapsedItemCount(collapsedItemCount)
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

        assertEquals(section.currentSize(), collapsedItemCount)

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        section.onShowMoreClickListener.invoke()

        assertEquals(section.currentSize(), section.sourceList.size)

        assertTrue(section.isShowMoreClicked)

        verify(section.sectionStateCallback)!!.onSectionShowMoreChange(
            section.sectionKey!!,
            collapsedItemCount,
            true
        )

        section.onShowMoreClickListener.invoke()

        assertFalse(section.isShowMoreClicked)

        verify(section.sectionStateCallback)!!.onSectionShowMoreChange(
            section.sectionKey!!,
            collapsedItemCount,
            false
        )
    }

    @Test
    fun sectionCurrentListSizeCollapsed() {
        val collapsedItemCount = 10
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportExpandFunction(true)
            .collapsedItemCount(collapsedItemCount)
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

        section.onExpandClickListener.invoke()
        var collapsedSize = 0
        if (section.hasHeader) collapsedSize++
        if (section.hasFooter) collapsedSize++

        assertEquals(section.currentSize(), collapsedSize)
    }

    @Test
    fun sectionCurrentListSizeCollapsedWithoutHeaderFooter() {
        val collapsedItemCount = 10
        val sectionParameters = SectionParams.builder()
            .itemResourceId(itemResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportExpandFunction(true)
            .collapsedItemCount(collapsedItemCount)
            .build()

        val section = object : TestSection(
            contentItems = TestItemsFactory.getNames()
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        section.onExpandClickListener.invoke()
        var collapsedSize = 0

        assertEquals(section.currentSize(), collapsedSize)
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

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
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

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertEquals(section.currentSize(), section.sourceList.size)
    }

    @Test
    fun sectionCurrentListSizeExpandedWithShowMore() {

        val defaultCollapsedItems = 5

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportShowMoreFunction(true)
            .collapsedItemCount(defaultCollapsedItems)
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

        assertEquals(section.currentSize(), defaultCollapsedItems)
    }

    @Test
    fun sectionReplaceItems() {

        val defaultCollapsedItems = 5

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportShowMoreFunction(true)
            .collapsedItemCount(defaultCollapsedItems)
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

        section.state = SectionState.LOADED
        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        assertFalse(section.isEmpty())

        assertEquals(section.getAllItems(), section.sourceList)

        val previousList = section.getAllItems()
        val newList = ArrayList<ItemContainer>()

        newList.add(TestItemsFactory.header)
        newList.addAll(TestItemsFactory.getNumbersList())
        newList.add(TestItemsFactory.footer)

        section.submitItems(
            ItemBundle(
                TestItemsFactory.header,
                TestItemsFactory.getNumbersList(),
                TestItemsFactory.footer
            )
        )
        assertNotEquals(previousList, section.sourceList)
        assertEquals(section.sourceList.size, newList.size)


        verify(section.sectionStateCallback)!!.onSectionContentUpdated(
            previousList,
            newList,
            section.sectionKey!!
        )
    }

    @Test
    fun sectionReplaceItemsNotLoaded() {

        val defaultCollapsedItems = 5

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .supportShowMoreFunction(true)
            .collapsedItemCount(defaultCollapsedItems)
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

        section.state = SectionState.LOADING
        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        val previousList = section.getAllItems()
        val newList = ArrayList<ItemContainer>()

        newList.add(TestItemsFactory.header)
        newList.addAll(TestItemsFactory.getNumbersList())
        newList.add(TestItemsFactory.footer)

        section.submitItems(
            ItemBundle(
                TestItemsFactory.header,
                TestItemsFactory.getNumbersList(),
                TestItemsFactory.footer
            )
        )

        assertNotEquals(previousList, section.sourceList)
        assertEquals(section.sourceList.size, newList.size)


        verify(section.sectionStateCallback, never())!!.onSectionContentUpdated(
            previousList,
            newList,
            section.sectionKey!!
        )
    }

    @Test
    fun sectionReplaceZeroItems() {

        val defaultCollapsedItems = 5

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .loadingResourceId(loadingResourceId)
            .emptyResourceId(emptyResourceId)
            .failedResourceId(failedResourceId)
            .supportShowMoreFunction(true)
            .collapsedItemCount(defaultCollapsedItems)
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

        section.state = SectionState.LOADING
        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        val previousList = section.getAllItems()
        val newList = ArrayList<ItemContainer>()

        newList.add(TestItemsFactory.header)
        newList.addAll(TestItemsFactory.getNumbersList())
        newList.add(TestItemsFactory.footer)

        assertFalse(section.sourceList.first() is StubItem)

        section.submitItems(
            ItemBundle(
            )
        )
        assertEquals(section.sourceList, previousList)

        verify(section.sectionStateCallback, never())!!.onSectionContentUpdated(
            previousList,
            newList,
            section.sectionKey!!
        )
    }

    @Test
    fun clearSection() {
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
        val previousList = section.getAllItems()

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        assertFalse(section.isEmpty())
        assertTrue(section.isNotEmpty())
        section.clearSection()
        assertTrue(section.isEmpty())
        assertFalse(section.isNotEmpty())

        verify(section.sectionStateCallback)!!.onSectionContentUpdated(
            previousList,
            section.sourceList,
            section.sectionKey!!
        )
    }

    @Test
    fun clearEmptySection() {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection(
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        val previousList = section.getAllItems()

        section.sectionStateCallback = mock()
        section.sectionKey = "KEY"

        assertTrue(section.isEmpty())
        assertFalse(section.isNotEmpty())
        section.clearSection()

        verifyNoMoreInteractions(section.sectionStateCallback)
    }
}