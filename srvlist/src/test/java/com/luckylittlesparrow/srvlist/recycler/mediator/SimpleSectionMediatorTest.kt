package com.luckylittlesparrow.srvlist.recycler.mediator

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.simple.SimpleSectionMediator
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import com.luckylittlesparrow.srvlist.recycler.testdata.SectionFactory
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItem
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Test

class SimpleSectionMediatorTest {

    private val sectionMediator = SimpleSectionMediator()
    private val sectionStateCallback: SectionStateCallback = mock()

    @Test
    fun addSectionWithKey() {
        val key = "key"
        val section = SectionFactory.getSection()
        val result = sectionMediator.addSection(key, section, sectionStateCallback)

        assertEquals(key, result)
        assertEquals(section, sectionMediator.getSectionByKey(key).section)
        assertEquals(sectionStateCallback, sectionMediator.getSectionByKey(key).section.sectionStateCallback)
    }

    @Test
    fun addSection() {
        val section = SectionFactory.getSection()
        val result = sectionMediator.addSection(section, sectionStateCallback)

        assertEquals(section, sectionMediator.getSectionByKey(result).section)
        assertEquals(sectionStateCallback, sectionMediator.getSectionByKey(result).section.sectionStateCallback)
    }

    @Test
    fun addSections() {
        val sectionList = SectionFactory.getSectionList()
        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertEquals(sectionList.size, sectionMediator.getSectionList().size)

        val resultList = sectionMediator.getSectionList()

        for (i in 0 until sectionList.size) {
            assertTrue(resultList.containsKey(sectionList[i].key))
        }
    }

    @Test
    fun removeSection() {
        val sectionList = SectionFactory.getSectionList()
        val sectionToRemove = sectionList[10]
        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertTrue(sectionMediator.removeSection(sectionToRemove))

        assertFalse(sectionMediator.containsSection(sectionToRemove))

        assertFalse(sectionMediator.removeSection(sectionToRemove))
    }

    @Test
    fun removeSectionWithKey() {
        val sectionList = SectionFactory.getSectionList()
        val sectionToRemove = sectionList[10]
        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertTrue(sectionMediator.removeSection(sectionToRemove.key))

        assertFalse(sectionMediator.containsSection(sectionToRemove.key))

        assertFalse(sectionMediator.removeSection(sectionToRemove.key))
    }

    @Test
    fun clearList() {
        val sectionList = SectionFactory.getSectionList()

        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertEquals(sectionMediator.getSectionList().size, sectionList.size)

        sectionMediator.clearList()

        assertTrue(sectionMediator.getSectionList().isEmpty())
    }

    @Test
    fun getAllItemsList() {
        val sectionList = SectionFactory.getSectionList()

        var items = 0
        sectionList.forEach {
            items += it.sourceList.size
        }

        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertEquals(sectionMediator.getAllItemsList().size, items)

    }

    @Test
    fun getSectionByKey() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[10]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val section = sectionMediator.getSectionByKey(expectedSection.key).section

        assertEquals(expectedSection, section)

    }

    @Test(expected = NoSuchElementException::class)
    fun getSectionByWrongKey() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[10]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val section = sectionMediator.getSectionByKey("key").section
    }


    @Test
    fun containsSection() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[10]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        var result = sectionMediator.containsSection(expectedSection)

        assertTrue(result)

        result = sectionMediator.containsSection(expectedSection.key)

        assertTrue(result)

        result = sectionMediator.containsSection("key")

        assertFalse(result)
    }

    @Test
    fun getVisibleItemCount() {
        val sectionList = SectionFactory.getSectionList()

        var items = 0
        sectionList.forEach {
            items += it.sourceList.size
        }

        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertEquals(sectionMediator.getVisibleItemCount(), items)


        sectionList[2].isVisible = false
        sectionList[4].isVisible = false

        items -= sectionList[2].sourceList.size
        items -= sectionList[4].sourceList.size

        assertEquals(sectionMediator.getVisibleItemCount(), items)
    }

    @Test
    fun getSectionByPosition() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = sectionList[0].sourceList.size + 4

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getSectionByItemPosition(itemPosition).section

        assertEquals(result, expectedSection)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getSectionByWrongPosition() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = 10002

        sectionMediator.addSections(sectionList, sectionStateCallback)

        sectionMediator.getSectionByItemPosition(itemPosition)
    }


    @Test
    fun getSectionPosition() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getSectionPosition(expectedSection)

        assertEquals(result, itemPosition)
    }

    @Test
    fun getPositionInSection() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getPositionInSection(itemPosition)

        assertEquals(result, 1)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getPositionInSectionInvalid() {
        val sectionList = SectionFactory.getSectionList()

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getPositionInSection(-1)
    }


    @Test
    fun getItemByPosition() {
        val sectionList = SectionFactory.getSectionList()
        val expectedItem = sectionList[1].sourceList[1]

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getItemByPosition(itemPosition)

        assertEquals(result, expectedItem)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getItemByPositionInvalid() {
        val sectionList = SectionFactory.getSectionList()
        val expectedItem = sectionList[1].sourceList[1]

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)
        val result = sectionMediator.getItemByPosition(-1)
    }

    @Test
    fun getSectionByItems() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        var result =
            sectionMediator.getSectionByItems(expectedSection.sourceList[1], expectedSection.sourceList[2])!!.section

        assertEquals(result, expectedSection)

        assertNull(sectionMediator.getSectionByItems(expectedSection.sourceList[1], TestItem("test"))?.section)
    }


    @Test
    fun getSectionPositionByKey() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = sectionList[0].sourceList.size

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getSectionPosition(expectedSection.key)

        assertEquals(result, itemPosition)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getSectionPositionByKeyInvalid() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        val itemPosition = sectionList[0].sourceList.size + 1

        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getSectionPosition("asd")

        assertEquals(result, itemPosition)
    }

    @Test
    fun getVisibleItemsList() {
        val sectionList = SectionFactory.getSectionList()
        sectionMediator.addSections(sectionList, sectionStateCallback)

        val result = sectionMediator.getVisibleItemsList()
        val expectedList = ArrayList<ItemContainer>()
        sectionList.forEach {
            expectedList.addAll(it.sourceList)
        }
        assertEquals(expectedList, result)
    }

    @Test
    fun getSectionByItem() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        var result = sectionMediator.getSectionByItem(expectedSection.sourceList[2])

        assertEquals(result?.section, expectedSection)

        result = sectionMediator.getSectionByItem(mock())

        assertNull(result)
    }

    @Test
    fun sectionStateCallback() {
        val sectionList = SectionFactory.getSectionList()
        val expectedSection = sectionList[1]

        sectionMediator.addSections(sectionList, sectionStateCallback)

        sectionMediator.detachSectionStateCallback()

        sectionList.forEach {
            assertNull(it.sectionStateCallback)
        }

        sectionMediator.attachSectionStateCallback(sectionStateCallback)

        sectionList.forEach {
            assertEquals(it.sectionStateCallback, sectionStateCallback)
        }

    }

}