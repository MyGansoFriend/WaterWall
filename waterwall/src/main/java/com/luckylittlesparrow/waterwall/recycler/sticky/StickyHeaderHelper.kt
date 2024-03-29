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

import androidx.annotation.LayoutRes
import com.luckylittlesparrow.waterwall.recycler.base.Binder
import com.luckylittlesparrow.waterwall.recycler.base.SectionMediator
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal class StickyHeaderHelper(
    private val mediator: SectionMediator
) {

    fun isHeader(position: Int): Boolean {
        if (position > mediator.items.size) return false
        return mediator.items[position].isHeader()
    }

    @LayoutRes
    fun getHeaderLayout(position: Int): Int {
        return mediator.getSectionByItem(mediator.items[position])?.section?.headerResourceId ?: 0
    }

    fun getBinder(position: Int): Pair<Binder, ItemContainer> {
        return mediator.getSectionByItem(mediator.items[position])?.section?.stickyHeaderBinder!! to mediator.items[position]
    }

    fun getHeaderPositionForItem(position: Int): Int {
        if (position > mediator.items.size) return -1
        if (mediator.items.isEmpty()) return -1

        for (i in position downTo 0) {
            if (mediator.items[i].isHeader()) return i
        }

        return -1
    }
}