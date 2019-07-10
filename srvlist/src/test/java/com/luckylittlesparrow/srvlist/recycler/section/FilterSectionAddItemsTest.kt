package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.testdata.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterSectionAddItemsTest {

    companion object {
        const val itemResourceId = 1
        const val headerResourceId = 2
        const val footerResourceId = 3
        const val failedResourceId = 4
        const val loadingResourceId = 5
        const val emptyResourceId = 6
    }

    @Test
    fun initSectionWithHeaderAndBundle() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : FilterTestSection(
            headerItem = TestItemsFactory.header
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

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

        assertTrue(section.sourceList.isEmpty())
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

        assertTrue(section.sourceList.isEmpty())
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

        assertTrue(section.sourceList.isEmpty())
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
}