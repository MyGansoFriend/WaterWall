package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.testdata.TestFooter
import com.luckylittlesparrow.srvlist.recycler.testdata.TestHeader
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.srvlist.recycler.testdata.TestSection
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SectionAddItemsTest {

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

        val section = object : TestSection(
            headerItem = TestItemsFactory.header
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()
        expectedList.add(TestItemsFactory.header)

        assertEquals(section.sourceList, expectedList)

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )

        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
    }


    @Test
    fun initSectionWithBundle() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()

        assertTrue(section.sourceList.isEmpty())

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
    }

    @Test(expected = IllegalStateException::class)
    fun initSectionWithBundleForgetHeader() {

        val sectionParameters = SectionParams.builder()
            .headerResourceId(headerResourceId)
            .itemResourceId(itemResourceId)
            .footerResourceId(footerResourceId)
            .build()

        val section = object : TestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        assertTrue(section.sourceList.isEmpty())

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

        val section = object : TestSection() {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }

        val expectedList = ArrayList<ItemContainer>()

        assertTrue(section.sourceList.isEmpty())

        section.addMoreItems(
            ItemBundle(
                contentItems = TestItemsFactory.getNames(),
                footerItem = TestItemsFactory.footer
            )
        )

        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
    }

    @Test
    fun initSectionWithBundleAddMoreItems() {

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

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)

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

        val expectedList = ArrayList<ItemContainer>()

        expectedList.add(TestItemsFactory.header)
        expectedList.addAll(TestItemsFactory.getNames())
        expectedList.add(TestItemsFactory.footer)

        assertEquals(section.sourceList, expectedList)
        assertEquals(section.sourceList.first(), TestItemsFactory.header)
        assertEquals(section.sourceList.last(), TestItemsFactory.footer)

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

        assertEquals(section.sourceList, expectedList)
        assertTrue(section.sourceList.last().isFooter())
        assertTrue(section.sourceList.first().isHeader())

        verify(section.sectionStateCallback)!!.onSectionContentChanged(
            section.key
        )
    }
}