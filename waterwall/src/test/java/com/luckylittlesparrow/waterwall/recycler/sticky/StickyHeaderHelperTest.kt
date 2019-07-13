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

package com.luckylittlesparrow.waterwall.recycler.sticky

import android.view.View
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.SectionDao
import com.luckylittlesparrow.waterwall.recycler.base.SectionMediator
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.waterwall.recycler.testdata.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StickyHeaderHelperTest {

    private val mediator: SectionMediator = mock()
    private val stickyHeaderHelper = StickyHeaderHelper(mediator)
    private lateinit var list: ArrayList<ItemContainer>
    private val view: View = mock()
    private val viewHolder = EmptyViewHolder<TestHeader>(view)

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

        override fun getHeaderViewHolder(view: View): BaseViewHolder<TestHeader> {
            return viewHolder
        }
    }

    @Before
    fun setUp() {
        list = ArrayList()
        list.add(TestItemsFactory.header)
        list.addAll(TestItemsFactory.getNames2())
        list.add(TestItemsFactory.footer)

        whenever(mediator.getVisibleItemsList()).thenReturn(list)
        whenever(mediator.getSectionByItem(any())).thenReturn(SimpleSectionDao(section) as SectionDao<Nothing, Nothing, Nothing>)
    }

    @Test
    fun stateChanged() {
        assertFalse(stickyHeaderHelper.getField("isStateChanged").get(stickyHeaderHelper) as Boolean)
        stickyHeaderHelper.stateChanged()
        assertTrue(stickyHeaderHelper.getField("isStateChanged").get(stickyHeaderHelper) as Boolean)
    }

    @Test
    fun resetItemsList() {
        stickyHeaderHelper.isHeader(0)

        assertEquals(
            stickyHeaderHelper.getField("items").get(stickyHeaderHelper) as List<ItemContainer>,
            list
        )

        stickyHeaderHelper.resetItemsList()
        assertNull(stickyHeaderHelper.getField("items").get(stickyHeaderHelper))
    }

    @Test
    fun isHeader() {
        assertTrue(stickyHeaderHelper.isHeader(0))
        assertFalse(stickyHeaderHelper.isHeader(1))
    }

    @Test
    fun getHeaderLayout() {
        assertEquals(stickyHeaderHelper.getHeaderLayout(0), headerResourceId)
        assertEquals(stickyHeaderHelper.getHeaderLayout(1), headerResourceId)
    }

    @Test
    fun getHeaderLayout_null() {
        whenever(mediator.getSectionByItem(any())).thenReturn(null)

        assertEquals(stickyHeaderHelper.getHeaderLayout(0), 0)
        assertEquals(stickyHeaderHelper.getHeaderLayout(1), 0)
    }


    @Test
    fun bindHeaderData() {
        assertEquals(stickyHeaderHelper.bindHeaderData(view, 0), viewHolder to list.first())
    }

    @Test
    fun getHeaderPositionForItem() {
        assertEquals(stickyHeaderHelper.getHeaderPositionForItem(2), 0)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getHeaderPositionForItem_Invalid() {
        stickyHeaderHelper.getHeaderPositionForItem(-1)
    }

    @Test
    fun getHeaderPositionForItem_Empty() {
        whenever(mediator.getVisibleItemsList()).thenReturn(listOf())
        assertEquals(stickyHeaderHelper.getHeaderPositionForItem(0), -1)
    }
}