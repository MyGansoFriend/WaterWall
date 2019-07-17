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
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.state.SectionStateCallback

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal interface SectionMediator {

    fun stateChanged()

    fun attachSectionStateCallback(sectionStateCallback: SectionStateCallback)

    fun detachSectionStateCallback()

    fun addSection(key: String, section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String

    fun addSection(section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String

    fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback)

    fun removeSection(sectionKey: String): Boolean

    fun removeSection(section: Section<*, *, *>): Boolean

    fun getSectionList(): MutableMap<String, SectionDao<Nothing, Nothing, Nothing>>

    fun getAllItemsList(): List<ItemContainer>

    fun getSectionByKey(key: String): SectionDao<Nothing, Nothing, Nothing>

    fun getSectionByItemPosition(position: Int): SectionDao<Nothing, Nothing, Nothing>

    fun getVisibleItemCount(): Int

    fun getSectionPosition(section: Section<*, *, *>): Int

    fun getSectionPosition(sectionKey: String): Int

    fun getPositionInSection(position: Int): Int

    fun getItemByPosition(position: Int): ItemContainer

    fun getSectionByItems(item: ItemContainer, newItem: ItemContainer): SectionDao<Nothing, Nothing, Nothing>?

    fun getSectionByItem(item: ItemContainer): SectionDao<Nothing, Nothing, Nothing>?

    fun containsSection(key: String): Boolean

    fun containsSection(section: Section<*, *, *>): Boolean

    fun clearList()

    fun getVisibleItemsList(): List<ItemContainer>
}