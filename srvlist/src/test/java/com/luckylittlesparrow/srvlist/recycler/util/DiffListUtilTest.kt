package com.luckylittlesparrow.srvlist.recycler.util

import com.luckylittlesparrow.srvlist.recycler.base.SectionDao
import com.luckylittlesparrow.srvlist.recycler.base.SectionMediator
import com.luckylittlesparrow.srvlist.recycler.testdata.TestFooter
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DiffListUtilTest {
    private val sectionMediator: SectionMediator = mock()
    private val section: SectionDao<Nothing, Nothing, Nothing> = mock()
    private val diffListUtil = DiffListUtil(sectionMediator)

    private val oldList = TestItemsFactory.getNames2()
    private val newList = TestItemsFactory.getNumbersList()

    @Before
    fun setUp() {
        oldList.add(TestFooter("footer"))
        diffListUtil.submitLists(oldList, newList)
    }

    @Test
    fun areItemsTheSame() {
        whenever(sectionMediator.getSectionByItems(oldList[0], newList[0])).thenReturn(section)
        whenever(section.areItemsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areItemsTheSame(0, 0)
        assertTrue(result)
    }

    @Test
    fun areItemsTheSameDifferentType() {

        val result = diffListUtil.areItemsTheSame(oldList.lastIndex, 0)
        assertFalse(result)
    }

    @Test
    fun getListSize() {
        assertEquals(diffListUtil.oldListSize, oldList.size)
        assertEquals(diffListUtil.newListSize, newList.size)
    }


    @Test
    fun areContentsTheSame() {
        whenever(sectionMediator.getSectionByItems(oldList[0], newList[0])).thenReturn(section)
        whenever(section.areContentsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areContentsTheSame(0, 0)
        assertTrue(result)
    }

    @Test
    fun areContentsTheSameDifferentType() {

        val result = diffListUtil.areContentsTheSame(oldList.lastIndex, 0)
        assertFalse(result)
    }
}