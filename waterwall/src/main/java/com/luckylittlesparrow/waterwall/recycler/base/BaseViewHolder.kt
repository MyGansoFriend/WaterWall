package com.luckylittlesparrow.waterwall.recycler.base

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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import java.lang.ref.WeakReference

/**
 * BaseViewHolder with core functionality, all ViewHolders must extend it to be used in
 * [FilterableSectionedAdapter] and [SimpleSectionedAdapter],
 * in case if item binding is not needed, use [EmptyViewHolder]
 *
 * @see EmptyViewHolder
 * @see FilterableSectionedAdapter
 * @see SimpleSectionedAdapter
 *
 * @param view view item
 * @param itemClickedListener on item click listener
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class BaseViewHolder<T>(
    view: View,
    val itemClickedListener: (T) -> Unit = {}
) : RecyclerView.ViewHolder(view) {

    var item: T? = null

    /**
     * Bind an [item] to the ViewHolder for Loaded state
     *
     * @param item content to bind
     *
     * @see SectionState.LOADED
     */
    open fun bindItem(item: T) {
        this.item = item
    }

    /**
     * Bind loading view to the ViewHolder for Loading state
     */
    open fun bindLoadingView() {
    }

    /**
     * Bind empty view to the ViewHolder for Empty state
     */
    open fun bindEmptyView() {
    }

    /**
     * Bind failed view to the ViewHolder for Failed state
     */
    open fun bindFailedView() {
    }

    /**
     * call on click action
     */
    open fun onClickAction() {
    }

    /**
     * Bind the data to the ViewHolder for sticky item
     *
     * @param item content to bind
     *
     * @see BaseListAdapter.supportStickyHeader
     */
    @Suppress("UNCHECKED_CAST")
    internal fun bindStickyItem(item: ItemContainer) {
        bindItem(item as T)
    }

    internal fun performClick() {
        item?.let { item -> itemClickedListener(item) }
    }

    internal fun isItemNotNull(): Boolean = item != null

    internal fun checkForStickyHeader(): Boolean = clickListener?.get()?.onItemClick(item as ItemContainer) == true

    internal fun isHeader(): Boolean = (item as ItemContainer).isHeader()

    internal var clickListener: WeakReference<OnItemClickListener>? = null
}