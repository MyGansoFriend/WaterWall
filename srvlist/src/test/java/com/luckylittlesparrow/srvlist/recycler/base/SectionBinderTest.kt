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

package com.luckylittlesparrow.srvlist.recycler.base

import android.view.View
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.testdata.TestHeader
import com.luckylittlesparrow.srvlist.recycler.testdata.TestItemsFactory
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

class SectionBinderTest {

    private val sectionBinder = SectionBinder()
    private val view: View = mock()
    private val item: ItemContainer = mock()
    private val clickListener = WeakReference(mock<OnItemClickListener>())
    private lateinit var viewHolder: BaseViewHolder<TestHeader>
    @Before
    fun setUp() {
        viewHolder = spy(object : BaseViewHolder<TestHeader>(view) {})
    }

    @After
    fun tearDown() {
        reset(viewHolder)
    }

    @Test
    fun isStickyHeader() {
        assertFalse(sectionBinder.isStickyHeader)
        sectionBinder.isStickyHeader = true
        assertTrue(sectionBinder.isStickyHeader)
    }

    @Test
    fun clickListener() {
        assertNull(sectionBinder.clickListener)
        sectionBinder.clickListener = clickListener
        assertEquals(sectionBinder.clickListener, clickListener)
    }


    @Test
    fun onBindLoadingContentViewHolder() {
        sectionBinder.onBindContentViewHolder(viewHolder, SectionState.LOADING, null)
        verify(viewHolder).bindLoadingView()
    }

    @Test
    fun onBindLoadedContentViewHolder() {
        val header = TestItemsFactory.header
        sectionBinder.onBindContentViewHolder(viewHolder, SectionState.LOADED, header)
        verify(viewHolder).bindItem(header)
    }

    @Test
    fun onBindLoadedNullContentViewHolder() {
        val header = TestItemsFactory.header
        sectionBinder.onBindContentViewHolder(viewHolder, SectionState.LOADED, null)
        verify(viewHolder, never()).bindItem(header)
    }

    @Test
    fun onBindFailedContentViewHolder() {
        sectionBinder.onBindContentViewHolder(viewHolder, SectionState.FAILED, item)
        verify(viewHolder).bindFailedView()
    }

    @Test
    fun onBindEmptyContentViewHolder() {
        sectionBinder.onBindContentViewHolder(viewHolder, SectionState.EMPTY, item)
        verify(viewHolder).bindEmptyView()
    }

    @Test
    fun onBindHeaderViewHolder() {
        assertFalse(viewHolder.isStickyHeader)
        assertNull(viewHolder.clickListener)
        sectionBinder.isStickyHeader = true
        sectionBinder.clickListener = clickListener


        sectionBinder.onBindHeaderViewHolder(item, viewHolder)
        assertEquals(viewHolder.item, item)
        assertTrue(viewHolder.isStickyHeader)
        assertEquals(viewHolder.clickListener, clickListener)
    }

    @Test
    fun onBindFooterViewHolder() {
        assertFalse(viewHolder.isStickyHeader)
        assertNull(viewHolder.clickListener)
        sectionBinder.isStickyHeader = true
        sectionBinder.clickListener = clickListener


        sectionBinder.onBindFooterViewHolder(item, viewHolder)
        assertEquals(viewHolder.item, item)
        assertTrue(viewHolder.isStickyHeader)
        assertEquals(viewHolder.clickListener, clickListener)
    }

    @Test
    fun onBindItemViewHolder() {
        assertFalse(viewHolder.isStickyHeader)
        assertNull(viewHolder.clickListener)
        sectionBinder.isStickyHeader = true
        sectionBinder.clickListener = clickListener


        sectionBinder.onBindItemViewHolder(item, viewHolder)
        assertEquals(viewHolder.item, item)
        assertTrue(viewHolder.isStickyHeader)
        assertEquals(viewHolder.clickListener, clickListener)
    }
}