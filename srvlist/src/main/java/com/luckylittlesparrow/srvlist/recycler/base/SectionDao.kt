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

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

internal interface SectionDao<H, I, F> {
    val section: Section<H, I, F>

    fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean

    fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean

    fun getItem(itemType: ItemContainer.ItemType, position: Int = -1): ItemContainer

    fun hasItems(item: ItemContainer, newItem: ItemContainer): Boolean

    fun hasItem(item: ItemContainer): Boolean

    fun getSectionList(): List<ItemContainer>

    fun sectionCurrentSize(): Int

    fun sectionOriginalSize(): Int

    fun hasHeader(): Boolean

    fun hasFooter(): Boolean

    fun isVisible(): Boolean

    fun state(): SectionState

    fun key(): String

    fun getFooterIndex(): Int

    fun filter(search: CharSequence): Pair<List<ItemContainer>, List<ItemContainer>>? = null

    fun getContentItem(position: Int): ItemContainer
}