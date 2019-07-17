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

package com.luckylittlesparrow.waterwall.recycler.base

import android.view.View
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.testdata.TestHeader
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItem
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import java.lang.ref.WeakReference


class BaseViewHolderTest {
    private val view: View = mock()
    private val captor = ArgumentCaptor.forClass(View.OnClickListener::class.java)

    @Before
    fun setUp() {
        doNothing().whenever(view).setOnClickListener(captor.capture())
    }

    @Test
    fun testNoErrors() {
        val viewHolder = object : BaseViewHolder<TestHeader>(view) {}
        viewHolder.bindEmptyView()
        viewHolder.bindFailedView()
        viewHolder.bindLoadingView()
        viewHolder.onClickAction()
        viewHolder.performClick()
    }

    @Test
    fun testBindItem() {
        val viewHolder = object : BaseViewHolder<TestHeader>(view) {}
        assertNull(viewHolder.item)
        val item = TestItemsFactory.header
        viewHolder.bindItem(item)
        assertEquals(viewHolder.item, item)

        viewHolder.item = null
        assertNull(viewHolder.item)

        viewHolder.bindStickyItem(item)
        assertEquals(viewHolder.item, item)
    }

    @Test
    fun testClickOnHeader() {

        var result = false
        val itemClickListener = { item: ItemContainer ->
            result = true
        }
        val viewHolder = object : BaseViewHolder<TestHeader>(view, itemClickListener) {}
        viewHolder.bindItem(TestItemsFactory.header)

        captor.value.onClick(view)
        assertTrue(result)
    }

    @Test
    fun testClickSticky() {
        var result = false
        val adapterClick = object : OnItemClickListener {
            override fun onItemClick(item: ItemContainer): Boolean {
                return true
            }
        }

        val itemClickListener = { item: ItemContainer ->
            result = true
        }
        val viewHolder = object : BaseViewHolder<TestItem>(view, itemClickListener) {}
        viewHolder.clickListener = WeakReference(adapterClick)
        viewHolder.bindItem(TestItemsFactory.getNames()[0])

        captor.value.onClick(view)
        assertTrue(result)

        result = false
        viewHolder.isStickyHeaderSupported = true
        captor.value.onClick(view)
        assertTrue(result)
    }
}