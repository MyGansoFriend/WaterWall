package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.testdata.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.hamcrest.core.Is.`is` as Is

class FilterSectionAddItemsTest {

    @Test
    fun initSectionWithHeaderAndBundle() {
        var headerCheck = false
        var itemCheck = false
        var footerCheck = false

        val headerClickLister = { item: ItemContainer ->
            headerCheck = true
        }

        val footerClickLister = { item: ItemContainer ->
            footerCheck = true
        }

        val itemClickLister = { item: ItemContainer ->
            itemCheck = true
        }

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection(
            headerItem = TestItemsFactory.header,
            headerClickListener = headerClickLister,
            itemClickListener = itemClickLister,
            footerClickListener = footerClickLister
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        section.headerClickListener.invoke(mock())
        section.footerClickListener.invoke(mock())
        section.itemClickListener.invoke(mock())

        assertTrue(headerCheck && footerCheck && itemCheck)

        val expectedList = ArrayList<ItemContainer>()
        expectedList.add(TestItemsFactory.header)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )

        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
    }


    @Test
    fun initSectionWithBundle() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()

        assertThat(section.sourceList.size, Is(1))
        assertTrue(section.sourceList.first() is StubItem)
        assertTrue(section.baseList.isEmpty())

        section.addMoreItems(
            ItemBundle(
                headerItem = TestItemsFactory.header,
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
    }


    @Test
    fun initSectionWithEmptyBundle() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection(TestItemsFactory.header) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()
        expectedList.add(TestItemsFactory.header)

        assertThat(section.sourceList.size, Is(1))

        section.addMoreItems(
            ItemBundle(
            )
        )

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
    }


    @Test(expected = IllegalStateException::class)
    fun initSectionWithBundleForgetHeader() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertThat(section.sourceList.size, Is(1))
        assertTrue(section.sourceList.first() is StubItem)
        assertTrue(section.baseList.isEmpty())

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )
    }

    @Test
    fun initSectionWithBundleWithoutHeader() {

        val sectionParameters = SectionParams.builder()
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()

        assertThat(section.sourceList.size, Is(1))
        assertTrue(section.sourceList.first() is StubItem)
        assertTrue(section.baseList.isEmpty())

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )

        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
    }

    @Test
    fun initSectionWithBundleAddMoreItems() {

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

        section.sectionStateCallback = mock()
        section.key = "KEY"

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNumbersList()
            )
        )

        expectedList.remove(TestItemsFactory.footer)
        expectedList.addAll(TestItemsFactory.getNumbersList())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertTrue(section.sourceList.last().isFooter())
        assertTrue(section.sourceList.first().isHeader())

        assertEquals(section.sourceList, section.baseList)

        verify(section.sectionStateCallback)!!.onSectionContentAdded(
            section.key,
            TestItemsFactory.getNumbersList().size
        )
    }

    @Test
    fun initSectionWithBundleUpdateHeaderAndFooter() {

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

        section.sectionStateCallback = mock()
        section.key = "KEY"

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
        assertEquals(section.sourceList.first(), TestItemsFactory.header)
        assertEquals(section.sourceList.last(), TestItemsFactory.footer)

        assertEquals(section.baseList.first(), TestItemsFactory.header)
        assertEquals(section.baseList.last(), TestItemsFactory.footer)

        val newHeader = TestHeader("new header")
        val newFooter = TestFooter("new footer")

        section.addMoreItems(
            ItemBundle(
                headerItem = newHeader,
                footerItem = newFooter
            )
        )

        expectedList[0] = newHeader
        expectedList[expectedList.size - 1] = newFooter

        assertEquals(section.sourceList.first(), newHeader)
        assertEquals(section.sourceList.last(), newFooter)

        assertEquals(section.baseList.first(), newHeader)
        assertEquals(section.baseList.last(), newFooter)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
        assertTrue(section.sourceList.last().isFooter())
        assertTrue(section.sourceList.first().isHeader())

        assertTrue(section.baseList.last().isFooter())
        assertTrue(section.baseList.first().isHeader())

        verify(section.sectionStateCallback)!!.onSectionContentChanged(
            section.key
        )
    }

    @Test
    fun testFilter() {

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
        assertTrue(section.headerFilter("", mock()))
    }

    @Test
    fun replaceItemsWithBundleHeaderAndFooter() {

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

        section.sectionStateCallback = mock()
        section.key = "KEY"

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList, section.baseList)
        assertEquals(section.sourceList.first(), TestItemsFactory.header)
        assertEquals(section.sourceList.last(), TestItemsFactory.footer)

        assertEquals(section.baseList.first(), TestItemsFactory.header)
        assertEquals(section.baseList.last(), TestItemsFactory.footer)

        val newHeader = TestHeader("new header")
        val newFooter = TestFooter("new footer")

        section.replaceItems(
            ItemBundle(
                headerItem = newHeader,
                contentItems = TestItemsFactory.getNumbersList(),
                footerItem = newFooter
            )
        )

        val newList = ArrayList<ItemContainer>()

        newList.add(TestItemsFactory.header)
        newList.addAll(TestItemsFactory.getNumbersList())
        newList.add(TestItemsFactory.footer)

        newList[0] = newHeader
        newList[newList.size - 1] = newFooter

        assertEquals(section.sourceList.first(), newHeader)
        assertEquals(section.sourceList.last(), newFooter)

        assertEquals(section.baseList.first(), newHeader)
        assertEquals(section.baseList.last(), newFooter)

        assertEquals(section.sourceList, newList)
        assertEquals(section.sourceList, section.baseList)
        assertTrue(section.sourceList.last().isFooter())
        assertTrue(section.sourceList.first().isHeader())

        assertTrue(section.baseList.last().isFooter())
        assertTrue(section.baseList.first().isHeader())

        verify(section.sectionStateCallback)!!.onSectionContentUpdated(
            expectedList,
            newList,
            section.key
        )
    }

    @Test
    fun replaceItemsWithBundleHeaderAndFooter_NotLoaded() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .loadingResourceId(loadingResourceId)
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

        section.sectionStateCallback = mock()
        section.key = "KEY"
        section.state = SectionState.LOADING

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        val newHeader = TestHeader("new header")
        val newFooter = TestFooter("new footer")

        section.replaceItems(
            ItemBundle(
                headerItem = newHeader,
                contentItems = TestItemsFactory.getNumbersList(),
                footerItem = newFooter
            )
        )

        verify(section.sectionStateCallback, never())!!.onSectionContentUpdated(
            expectedList,
            expectedList,
            section.key
        )
    }

    @Test
    fun replaceItemsWithEmptyBundle() {

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

        section.sectionStateCallback = mock()
        section.key = "KEY"

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)


        section.replaceItems(
            ItemBundle(
            )
        )

        assertEquals(section.sourceList, expectedList)

        verify(section.sectionStateCallback, never())!!.onSectionContentUpdated(
            expectedList,
            expectedList,
            section.key
        )
    }

    @Test(expected = IllegalStateException::class)
    fun replaceItemsWithForgotHeader() {

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

        section.replaceItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNumbersList()
            )
        )

    }
}