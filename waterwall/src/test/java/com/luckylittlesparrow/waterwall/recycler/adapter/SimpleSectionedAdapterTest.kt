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

package com.luckylittlesparrow.waterwall.recycler.adapter


import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.base.*
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.StubSection
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionMediator
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionStateCallback
import com.luckylittlesparrow.waterwall.recycler.sticky.StickyHeaderDecoration
import com.luckylittlesparrow.waterwall.recycler.testdata.SectionFactory
import com.luckylittlesparrow.waterwall.recycler.testdata.TestFooter
import com.luckylittlesparrow.waterwall.recycler.testdata.TestItemsFactory
import com.luckylittlesparrow.waterwall.recycler.testdata.getField
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito.spy
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Field


@RunWith(RobolectricTestRunner::class)
class SimpleSectionedAdapterTest {
    private lateinit var adapter: SimpleSectionedAdapter
    private val recylcerView: RecyclerView = mock()
    private val sectionMediator: SectionMediator = mock()


    @Before
    fun setUp() {
        adapter = SimpleSectionedAdapter()
        adapter.sectionMediator = sectionMediator
    }

    @Test
    fun initTest() {
        adapter = SimpleSectionedAdapter()
        assertTrue(adapter.sectionMediator is SimpleSectionMediator)
    }

    @Test
    fun supportStickyHeader() {
        val binder = getField("sectionBinder").get(adapter) as SectionBinder

        adapter.supportStickyHeader = false

        assertFalse(adapter.supportStickyHeader)
        assertFalse(binder.isStickyHeader)
        assertNull(binder.clickListener)

        adapter.supportStickyHeader = true

        assertTrue(adapter.supportStickyHeader)
        assertTrue(binder.isStickyHeader)
        assertNotNull(binder.clickListener)
    }


    @Test
    fun dispatchUpdates() {
        val diff: DiffUtil.DiffResult = mock()
        adapter.dispatchUpdates(diff)
        verify(diff).dispatchUpdatesTo(adapter)
    }

    @Test
    fun onAttachedToRecyclerView() {
        var localRV = getField("recyclerView").get(adapter)
        val sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback
        assertNull(localRV)
        adapter.onAttachedToRecyclerView(recylcerView)

        localRV = getField("recyclerView").get(adapter)

        assertNotNull(localRV)
        verify(sectionMediator).attachSectionStateCallback(sectionStateCallback)
        verify(recylcerView, never()).addItemDecoration(StickyHeaderDecoration(adapter.stickyHeaderHelper))

        adapter.supportStickyHeader = true
        adapter.onAttachedToRecyclerView(recylcerView)
        verify(recylcerView).addItemDecoration(any<StickyHeaderDecoration>())

        reset(recylcerView)
        adapter.onAttachedToRecyclerView(recylcerView)
    }

    @Test
    fun onDetachedToRecyclerView() {
        adapter.onAttachedToRecyclerView(recylcerView)
        var localRV = getField("recyclerView").get(adapter)
        assertNotNull(localRV)

        adapter.onDetachedFromRecyclerView(recylcerView)
        localRV = getField("recyclerView").get(adapter)
        assertNull(localRV)

        verify(sectionMediator).detachSectionStateCallback()
    }

    @Test
    fun clickListener() {

        val item = TestItemsFactory.footer
        val view: View = mock()
        val viewHolder = spy(EmptyViewHolder<TestFooter>(view))
        adapter.currentStickyHeader = viewHolder

        viewHolder.bindItem(item)
        var result = adapter.getClickListener().onItemClick(item)
        assertTrue(result)

        adapter.onAttachedToRecyclerView(recylcerView)
        whenever(recylcerView.getChildAt(0)).thenReturn(view)
        whenever(recylcerView.getChildViewHolder(view)).thenReturn(viewHolder)

        result = adapter.getClickListener().onItemClick(item)
        assertFalse(result)
        verify(viewHolder).performClick()

        viewHolder.bindItem(mock())
        result = adapter.getClickListener().onItemClick(item)
        assertTrue(result)
    }

    @Test
    fun addExistingSection() {
        val section = SectionFactory.getSection()
        val sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback

        whenever(sectionMediator.containsSection(section)).thenReturn(true)
        assertEquals(adapter.addSection(section), section.key)
        verify(sectionMediator, never()).addSection(section, sectionStateCallback)
    }

    @Test
    fun addStubSection() {
        val stub = StubSection()
        val section = SectionFactory.getSection()
        val sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback

        whenever(sectionMediator.containsSection(section)).thenReturn(false)
        adapter.addSection(stub)
        verify(sectionMediator).addSection(stub, sectionStateCallback)
        assertNotNull(getField("stubSection").get(adapter))

        adapter.addSection(section)
        assertNull(getField("stubSection").get(adapter))
        verify(sectionMediator).clearList()
    }

    @Test
    fun removeSection() {
        val section = SectionFactory.getSection()
        adapter.addSection(section)
        whenever(sectionMediator.removeSection(section.key)).thenReturn(true)
        adapter.removeSection(section.key)
        verify(sectionMediator).removeSection(section.key)
    }

    @Test
    fun removeStubSection() {
        val section = SectionFactory.getSection()
        assertFalse(adapter.removeSection(StubSection()))
        verify(sectionMediator, never()).removeSection(section)
    }

    @Test
    fun clearList() {
        val section = SectionFactory.getSection()
        adapter.addSection(section)
        adapter.clearList()
        verify(sectionMediator).clearList()
    }

//    @Test
//    fun onCreateViewHolder() {
//        val sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback
//        val section = SectionFactory.getSection() as Section<Nothing, Nothing, Nothing>
//        val map = LinkedHashMap<String, SectionDao<Nothing, Nothing, Nothing>>()
//        map[section.key] = SimpleSectionDao(section)
//
//        whenever(sectionMediator.getSectionList()).thenReturn(map)
//        whenever(sectionMediator.addSection(section, sectionStateCallback)).thenReturn(section.key)
//
//
//        adapter.addSection(section)
//        val holder = adapter.onCreateViewHolder(mock(), BaseListAdapter.VIEW_TYPE_HEADER)
//        assertNotNull(holder)
//    }

    @Test
    fun getItemId() {
        val position = 2
        val item = TestItemsFactory.footer
        whenever(sectionMediator.getItemByPosition(position)).thenReturn(item)
        assertEquals(adapter.getItemId(position), item.ITEM_CONTAINER_ID)
        verify(sectionMediator).getItemByPosition(position)
    }

    @Test
    fun getItemCount() {
        val count = 2
        whenever(sectionMediator.getVisibleItemCount()).thenReturn(count)
        assertEquals(adapter.itemCount, count)
        verify(sectionMediator).getVisibleItemCount()
    }

    @Ignore
    @Test
    fun onSectionContentUpdated() {
        assertFalse(adapter.stickyHeaderHelper.isStateChanged)
        val sectionStateCallback = getField("sectionStateCallback").get(adapter) as SectionStateCallback

        val oldList = TestItemsFactory.getNumbersList()
        val newList = TestItemsFactory.getNames()

        sectionStateCallback.onSectionContentUpdated(
            oldList,
            newList,
            "key"
        )

        assertTrue(adapter.stickyHeaderHelper.isStateChanged)
        assertEquals(adapter.diffListUtil.getField("oldList").get(adapter.diffListUtil), oldList)
        assertEquals(adapter.diffListUtil.getField("newList").get(adapter.diffListUtil), newList)
    }


    @Ignore
    @Test
    fun onSectionShowMoreChange() {


        val spyAdapter = spy(adapter)
        assertFalse(spyAdapter.stickyHeaderHelper.isStateChanged)
        val sectionStateCallback = getField("sectionStateCallback").get(spyAdapter) as SectionStateCallback


        val startPosition = 10
        val section = SectionFactory.getSection() as Section<Nothing, Nothing, Nothing>

        val sectionDao = SimpleSectionDao(section)

        whenever(sectionMediator.getSectionByKey(section.key)).thenReturn(sectionDao)
        whenever(sectionMediator.getSectionPosition(section)).thenReturn(startPosition)
       // doNothing().whenever(spyAdapter).notifyItemRangeInsertedInternal(section.key, startPosition)


        sectionStateCallback.onSectionShowMoreChange(
            section.key,
            startPosition,
            true
        )


        assertTrue(spyAdapter.stickyHeaderHelper.isStateChanged)
        verify(spyAdapter).notifyItemRangeInserted(
            startPosition + startPosition,
            sectionDao.sectionCurrentSize() - 1 - startPosition
        )
    }


    private fun getField(fieldName: String): Field {
        val field = BaseListAdapter::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field
    }
}