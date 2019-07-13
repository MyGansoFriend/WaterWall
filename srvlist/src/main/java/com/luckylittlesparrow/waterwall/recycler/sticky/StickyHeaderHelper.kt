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
import androidx.annotation.LayoutRes
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.SectionMediator
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal class StickyHeaderHelper(
    private val mediator: SectionMediator
) {

    var isStateChanged = false
        private set

    fun stateChanged() {
        isStateChanged = true
    }

    private var items: List<ItemContainer>? = null
        get() {
            if (field == null || isStateChanged) {
                field = mediator.getVisibleItemsList()
                isStateChanged = false
            }
            return field
        }

    fun resetItemsList() {
        items = null
    }

    fun isHeader(position: Int): Boolean {
        return items!![position].isHeader()
    }

    @LayoutRes
    fun getHeaderLayout(position: Int): Int {
        return mediator.getSectionByItem(items!![position])?.section?.headerResourceId ?: 0
    }

    fun bindHeaderData(view: View, position: Int): Pair<BaseViewHolder<*>, ItemContainer> {
        return mediator.getSectionByItem(items!![position])?.section?.getHeaderViewHolder(view)!! to items!![position]
    }

    fun getHeaderPositionForItem(position: Int): Int {
        if (items!!.isEmpty()) return -1

        for (i in position downTo 0) {
            if (items!![i].isHeader()) return i
        }

        throw IndexOutOfBoundsException("Invalid position")
    }
}