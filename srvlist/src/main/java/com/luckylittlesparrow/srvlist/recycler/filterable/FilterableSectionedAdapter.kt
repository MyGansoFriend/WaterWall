package com.luckylittlesparrow.srvlist.recycler.filterable

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

import android.annotation.SuppressLint
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.srvlist.recycler.base.BaseListAdapter
import com.luckylittlesparrow.srvlist.recycler.base.SectionItemDecoration
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.sticky.StickyHeaderDecoration

/**
 * Adapter version with filter support, more complex than [SimpleSectionedAdapter],
 * if filter not needed, use [SimpleSectionedAdapter] instead
 *
 * Supported functionality:
 *           States
 *           Decorations
 *           Sticky headers
 *
 * @see FilterableSection.supportFilterHeader
 * @see FilterableSection.itemFilter
 * @see FilterableSection.headerFilter
 *
 * @see SimpleSectionedAdapter
 * @see FilterableSection<H,I,F>
 * @see SectionItemDecoration
 * @see StickyHeaderDecoration
 * @see supportStickyHeader
 *
 * @author Andrei Gusev
 * @since  1.0
 */
class FilterableSectionedAdapter : BaseListAdapter(), Filterable {

    init {
        sectionMediator = FilterableSectionMediator()
    }

    /**
     * Add a section with key to the adapter
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see FilterableSection<H, I, F>
     *
     * @param key     unique key of the section
     * @param section section to add
     */
    override fun addSection(key: String, section: Section<*, *, *>) {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        super.addSection(key, section)
    }

    /**
     * Add a section to the adapter, key for section will be generated and returned
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see FilterableSection<H, I, F>
     *
     * @param section section to add
     * @return Generated key
     */
    override fun addSection(section: Section<*, *, *>): String {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        return super.addSection(section)
    }

    /**
     * Remove section from the adapter by key, [true] if success [false] otherwise
     *
     * Stub section will be removed automatically, if new sections are provided
     *
     * @see FilterableSection<H, I, F>
     *
     * @param key key of section to remove
     */
    override fun removeSection(section: Section<*, *, *>): Boolean {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        return super.removeSection(section)
    }

    /**
     * Add a list of sections to the adapter, key for section will be generated and returned
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see Section<H, I, F>
     *
     * @param list list of sections to add
     */
    override fun addSections(list: List<Section<*, *, *>>) {
        if (list.isEmpty()) return
        list.forEach {
            check(it is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        }
        super.addSections(list)
    }

    /**
     * Filter existing sections and update RecyclerView with the result,
     * section must be in [SectionState.LOADED] to be filtered, otherwise it will be skipped
     *
     * @see FilterableSection.supportFilterHeader
     * @see FilterableSection.itemFilter
     * @see FilterableSection.headerFilter
     */
    fun filter(constraint: CharSequence) {
        filter.filter(constraint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                stickyHeaderHelper.stateChanged()
                if (results.values == null) notifyDataSetChanged()
                else dispatchUpdates((results.values as DiffUtil.DiffResult))
                recyclerView?.scrollToPosition(0)
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                recyclerView?.stopScroll()
                val filterResult = FilterResults()

                val resultLists = ArrayList<Pair<List<ItemContainer>, List<ItemContainer>>>()

                for (section in sectionMediator.getSectionList()) {
                    if (section.value.state() == SectionState.LOADED) {
                        val result = section.value.filter(constraint)
                        result?.let { resultLists.add(result) }
                    }
                }

                if (resultLists.isEmpty()) return filterResult else {

                    val oldList = ArrayList<ItemContainer>()
                    val newList = ArrayList<ItemContainer>()

                    for (pair in resultLists) {
                        oldList.addAll(pair.first)
                        newList.addAll(pair.second)
                    }

                    diffListUtil.submitLists(oldList, newList)
                    filterResult.values = DiffUtil.calculateDiff(diffListUtil)
                }

                return filterResult
            }
        }
    }
}