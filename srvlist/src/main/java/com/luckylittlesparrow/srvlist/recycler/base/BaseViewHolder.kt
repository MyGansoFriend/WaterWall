package com.luckylittlesparrow.srvlist.recycler.base

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