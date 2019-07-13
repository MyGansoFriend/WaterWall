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

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import java.lang.ref.WeakReference

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal class SectionBinder {

    internal var isStickyHeader = false

    internal var clickListener: WeakReference<OnItemClickListener>? = null

    fun <T> onBindContentViewHolder(
        holder: BaseViewHolder<T>,
        state: SectionState,
        itemContainer: ItemContainer? = null
    ) {
        when (state) {
            SectionState.LOADING -> onBindLoadingViewHolder(holder)
            SectionState.LOADED -> itemContainer?.let { onBindItemViewHolder(it, holder) }
            SectionState.FAILED -> onBindFailedViewHolder(holder)
            SectionState.EMPTY -> onBindEmptyViewHolder(holder)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> onBindHeaderViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
        holder.isStickyHeader = isStickyHeader
        holder.clickListener = clickListener
        holder.bindItem(itemContainer as T)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> onBindFooterViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
        holder.isStickyHeader = isStickyHeader
        holder.clickListener = clickListener
        holder.bindItem(itemContainer as T)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> onBindItemViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
        holder.isStickyHeader = isStickyHeader
        holder.clickListener = clickListener
        holder.bindItem(itemContainer as T)
    }

    private fun <T> onBindLoadingViewHolder(holder: BaseViewHolder<T>) {
        holder.bindLoadingView()
    }

    private fun <T> onBindFailedViewHolder(holder: BaseViewHolder<T>) {
        holder.bindFailedView()
    }

    private fun <T> onBindEmptyViewHolder(holder: BaseViewHolder<T>) {
        holder.bindEmptyView()
    }
}