package com.luckylittlesparrow.srvlist.recycler.base

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

class SectionBinder {

    fun <T> onBindContentViewHolder(holder: BaseViewHolder<T>, state: SectionState, itemContainer: ItemContainer? = null) {
        when (state) {
            SectionState.LOADING -> onBindLoadingViewHolder(holder)
            SectionState.LOADED -> itemContainer?.let { onBindItemViewHolder(it, holder) }
            SectionState.FAILED -> onBindFailedViewHolder(holder)
            SectionState.EMPTY -> onBindEmptyViewHolder(holder)
        }
    }

    fun <T> onBindHeaderViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
        holder.bindItem(itemContainer as T)
    }

    fun <T> onBindFooterViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
        holder.bindItem(itemContainer as T)
    }

    fun <T> onBindItemViewHolder(itemContainer: ItemContainer, holder: BaseViewHolder<T>) {
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