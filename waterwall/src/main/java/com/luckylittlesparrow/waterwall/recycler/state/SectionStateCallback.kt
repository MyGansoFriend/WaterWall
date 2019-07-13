package com.luckylittlesparrow.waterwall.recycler.state

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

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal interface SectionStateCallback {
    fun onSectionStateChanged(sectionKey: String, newState: SectionState, oldState: SectionState)

    fun onSectionContentAdded(sectionKey: String, addItemsCount: Int)

    fun onSectionContentChanged(sectionKey: String)

    fun onSectionExpandChange(sectionKey: String, isExpanded: Boolean)

    fun onSectionShowMoreChange(sectionKey: String, collapsedItemCount: Int, isShowMore: Boolean)

    fun onSectionContentUpdated(previousList: List<ItemContainer>, newList: List<ItemContainer>, sectionKey: String)
}