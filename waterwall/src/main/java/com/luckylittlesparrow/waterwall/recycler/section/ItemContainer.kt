package com.luckylittlesparrow.waterwall.recycler.section

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

import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer.ItemType
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import java.util.*

/**
 * Base class for all items : [header, contentItem, footer]
 *
 * All models must extend this class in order to work with [SimpleSectionedAdapter]
 * or [FilterableSectionedAdapter]
 *
 * @param itemType type of item: [ItemType.HEADER],[ItemType.ITEM],[ItemType.FOOTER]
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class ItemContainer(val itemType: ItemType) {

    /**
     * Auto generated ITEM_CONTAINER_ID for item for [RecyclerView.Adapter.setHasStableIds]
     *
     * @see RecyclerView.Adapter.setHasStableIds(true)
     */
    val ITEM_CONTAINER_ID = UUID.randomUUID().mostSignificantBits

    enum class ItemType {
        HEADER, FOOTER, ITEM
    }

    fun isHeader() = itemType == ItemType.HEADER

    fun isNotHeader() = itemType != ItemType.HEADER

    fun isFooter() = itemType == ItemType.FOOTER

    fun isNotFooter() = itemType != ItemType.FOOTER

    fun isItem() = itemType == ItemType.ITEM

    fun isNotItem() = itemType != ItemType.ITEM

    fun isTheSameType(type: ItemType) = itemType == type

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ItemContainer) return false
        return if (other === this) true else this.ITEM_CONTAINER_ID == other.ITEM_CONTAINER_ID
    }

    override fun hashCode(): Int {
        return ITEM_CONTAINER_ID.toInt()
    }
}