/*
 *  Copyright 2019 Gusev Andrei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.luckylittlesparrow.waterwall.recycler.util

import com.luckylittlesparrow.waterwall.recycler.base.SectionDao
import com.luckylittlesparrow.waterwall.recycler.base.SectionMediator
import com.luckylittlesparrow.waterwall.recycler.testdata.TestFooter
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DiffListUtilByItemsTest {
    private val sectionMediator: SectionMediator = mock()
    private val section: SectionDao<Nothing, Nothing, Nothing> = mock()
    private val diffListUtil = DiffListUtilByItems(sectionMediator)

    private val oldList = TestItemsFactory.getNames2()
    private val newList = TestItemsFactory.getNumbersList()

    @Before
    fun setUp() {
        oldList.add(TestFooter("footer"))
        diffListUtil.submitLists(oldList, newList)
    }

    @Test
    fun areItemsTheSame() {
        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(section)
        whenever(section.areItemsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areItemsTheSame(0, 0)
        assertTrue(result)
    }

    @Test
    fun areItemsTheSame_SectionNull() {
        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(null)
        whenever(section.areItemsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areItemsTheSame(0, 0)
        assertFalse(result)
    }

    @Test
    fun areItemsTheSame_SameSection() {

        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(section)
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
        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(section)
        whenever(section.areContentsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areContentsTheSame(0, 0)
        assertTrue(result)
    }

    @Test
    fun areContentsTheSame_SectionNull() {
        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(null)
        whenever(section.areContentsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areContentsTheSame(0, 0)
        assertFalse(result)
    }

    @Test
    fun areContentsTheSameDifferentType() {
        val result = diffListUtil.areContentsTheSame(oldList.lastIndex, 0)
        assertFalse(result)
    }

    @Test
    fun areContentsTheSame_SameSection() {

        whenever(sectionMediator.getSectionByItem(newList[0])).thenReturn(section)
        whenever(section.areContentsTheSame(oldList[0], newList[0])).thenReturn(true)

        val result = diffListUtil.areContentsTheSame(0, 0)
        assertTrue(result)
    }
}