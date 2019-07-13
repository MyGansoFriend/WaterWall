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

import android.view.View
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

/**
 * Section for usage in case, when it's necessary to show state for all adapter (e.g match parent layout),
 * not for each section separately.
 * <p> StubSection will be automaticly removed from adapter, when real sections will be added. </p>
 *
 * @see SimpleSectionedAdapter
 * @see FilterableSectionedAdapter
 *
 * @author Andrei Gusev
 * @since  1.0
 */
class StubSection(
    emptyLayoutId: Int? = null,
    failedLayoutId: Int? = null,
    loadingLayoutId: Int? = null
) : FilterableSection<Void, Void, Void>(contentItems = listOf(StubItem())) {

    override fun itemFilter(search: String, item: ItemContainer): Boolean = true

    init {
        emptyResourceId = emptyLayoutId
        loadingResourceId = loadingLayoutId
        failedResourceId = failedLayoutId
    }

    override fun getSectionParams(): SectionParams = SectionParams.builder().build()

    override fun getDiffUtilItemCallback(): DiffUtilItemCallback = object : DiffUtilItemCallback() {
        override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = true

        override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = true
    }

    override fun getItemViewHolder(view: View): BaseViewHolder<Void> =
        EmptyViewHolder(view)

    override fun addMoreItems(itemBundle: ItemBundle) {
    }
}