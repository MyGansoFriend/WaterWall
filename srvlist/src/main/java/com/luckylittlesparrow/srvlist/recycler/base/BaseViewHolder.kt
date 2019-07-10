package com.luckylittlesparrow.srvlist.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @param view view item
 * @param itemClickedListener on item click listener
 */
abstract class BaseViewHolder<T>(
    view: View,
    itemClickedListener: (T) -> Unit = {}
) : RecyclerView.ViewHolder(view) {

    /**
     * Bind the data to the ViewHolder for Loaded state
     *
     * @param item content to bind
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

    var item: T? = null

    init {
        view.setOnClickListener {
            item?.let { item -> itemClickedListener(item) }
        }
    }
}