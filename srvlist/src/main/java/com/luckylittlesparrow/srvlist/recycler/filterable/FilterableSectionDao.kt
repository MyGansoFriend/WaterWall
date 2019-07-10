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

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.simple.SimpleSectionDao

internal class FilterableSectionDao<H, I, F>(section: FilterableSection<H, I, F>) : SimpleSectionDao<H, I, F>(section),
    Filterable {
    private var lastSearchString = ""

    override fun filter(search: CharSequence): Pair<List<ItemContainer>, List<ItemContainer>>? {
        val searchString = search.toString().toLowerCase()
        val filterableSection = section as FilterableSection

        if (lastSearchString.isNotEmpty() && searchString.contains(lastSearchString) && filterableSection.filteredList.isEmpty()) {
            return null
        }

        var isEmpty = true
        var startIndex = 0

        if (filterableSection.filteredList.isEmpty()) {
            if (lastSearchString.isEmpty())
                filterableSection.baseList = ArrayList(filterableSection.sourceList)
            else filterableSection.baseList = ArrayList()
        } else {
            filterableSection.baseList = ArrayList(filterableSection.filteredList)
            filterableSection.filteredList.clear()
        }

        lastSearchString = searchString

        if (filterableSection.hasHeader()) {
            if (filterableSection.supportFilterHeader && filterableSection.headerFilter(
                    searchString,
                    filterableSection.sourceList.first()
                )
            ) {
                filterableSection.filteredList.addAll(filterableSection.sourceList)

                if (!filterableSection.isVisible) {
                    filterableSection.isVisible = true
                    return Pair(ArrayList(), filterableSection.filteredList)
                }

                return Pair(
                    filterableSection.baseList,
                    filterableSection.filteredList
                )
            }

            filterableSection.filteredList.add(filterableSection.sourceList.first())
            startIndex = 1
        }

        for (i in startIndex until filterableSection.sourceList.size) {
            if (i == getFooterIndex() && filterableSection.hasFooter()) {
                filterableSection.filteredList.add(filterableSection.sourceList.last())
                break
            }

            if (filterableSection.itemFilter(searchString, filterableSection.sourceList[i])) {
                filterableSection.filteredList.add(filterableSection.sourceList[i])
                isEmpty = false
            }
        }

        filterableSection.isVisible = if (isEmpty) {
            filterableSection.filteredList.clear()
            false
        } else true

        return filterableSection.baseList to filterableSection.filteredList
    }

    override fun sectionCurrentSize(): Int {
        return section.currentSize()
    }

    override fun getContentItem(position: Int): ItemContainer {
        section as FilterableSection<H, I, F>
        return if (section.filteredList.isEmpty()) section.baseList[position] else section.filteredList[position]
    }
}