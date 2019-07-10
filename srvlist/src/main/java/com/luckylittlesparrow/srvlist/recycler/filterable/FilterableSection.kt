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

import com.luckylittlesparrow.srvlist.recycler.section.ItemBundle
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.StubItem
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

/**
 * Doesn't support "show more", "expand" functions
 */
abstract class FilterableSection<H, I, F>(
    headerItem: ItemContainer? = null,
    contentItems: List<ItemContainer>? = null,
    footerItem: ItemContainer? = null,
    sectionKey: String? = null,
    headerClickListener: (ItemContainer) -> Unit = {},
    itemClickListener: (ItemContainer) -> Unit = {},
    footerClickListener: (ItemContainer) -> Unit = {}
) : Section<H, I, F>(
    headerItem,
    contentItems,
    footerItem,
    sectionKey,
    headerClickListener,
    itemClickListener,
    footerClickListener
) {

    internal var baseList = ArrayList<ItemContainer>()

    internal val filteredList = ArrayList<ItemContainer>()

    internal var supportFilterHeader = false

    init {
        initResources()
        initItems(headerItem, contentItems, footerItem)
    }

    private fun initResources() {
        supportFilterHeader = getSectionParams().supportFilterHeaderFunction
        check(!getSectionParams().supportExpansionFunction && !getSectionParams().supportShowMoreFunction)
        { "FilterableSection doesn't support \"show more\", \"expand\" functions" }
    }

    private fun initItems(headerItem: ItemContainer?, contentItems: List<ItemContainer>?, footerItem: ItemContainer?) {
        headerItem?.let { baseList.add(it) }

        contentItems?.let {
            check(hasHeader() && headerItem != null || !hasHeader() && headerItem == null)
            baseList.addAll(contentItems)
        }

        footerItem?.let {
            check(contentItems != null)
            baseList.add(footerItem)
        }
    }

    override fun addMoreItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        sourceList.remove(stateItem)

        check(
            !hasHeader()
                    || sourceList.isNotEmpty() && sourceList.first().isHeader()
                    || hasHeader() && itemBundle.headerItem != null
        ) { "submit header item or resource" }

        var isNewContent = true
        var itemsCount = 0

        itemBundle.headerItem?.let {
            if (sourceList.isNotEmpty() && sourceList.first().isHeader()) {
                sourceList[0] = it
                baseList[0] = it
                isNewContent = false
            } else {
                baseList.add(0, it)
                sourceList.add(0, it)
                itemsCount++
            }
        }

        itemBundle.contentItems?.let {
            itemsCount += it.size
            if (hasFooter() && sourceList.isNotEmpty() && sourceList.last().isFooter()) addItemsWithFooter(it) else {
                sourceList.addAll(it)
                baseList.addAll(it)
            }
        }

        itemBundle.footerItem?.let {
            if (sourceList.isNotEmpty() && sourceList.last().isFooter()) {
                sourceList[getFooterIndex()] = it
                baseList[getFooterIndex()] = it
                isNewContent = false
            } else {
                val footerIndex = getFooterIndex() + 1
                sourceList.add(footerIndex, it)
                baseList.add(footerIndex, it)
                itemsCount++
            }
        }

        if (state == SectionState.LOADED) {
            if (isNewContent && itemsCount > 0)
                sectionStateCallback?.onSectionContentAdded(provideId(), itemsCount)
            else {
                if (!isNewContent) sectionStateCallback?.onSectionContentChanged(provideId())
            }
        }

        if (sourceList.isEmpty()) sourceList.add(stateItem)
    }

    /**
     * Replace previous items with the new items
     */
    override fun replaceItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        val previousList = ArrayList<ItemContainer>()
        previousList.addAll(sourceList)
        sourceList.clear()
        baseList.clear()

        check(
            !hasHeader() || hasHeader() && itemBundle.headerItem != null
        ) { "Forgot to provide header item" }

        var itemsCount = 0


        itemBundle.headerItem?.let {
            sourceList.add(0, it)
            baseList.add(0, it)
            itemsCount++
        }

        itemBundle.contentItems?.let {
            itemsCount += it.size
            sourceList.addAll(it)
            baseList.addAll(it)
        }

        itemBundle.footerItem?.let {
            val footerIndex = getFooterIndex() + 1
            sourceList.add(footerIndex, it)
            baseList.add(footerIndex, it)
            itemsCount++
        }

        if (state == SectionState.LOADED) {
            sectionStateCallback?.onSectionContentUpdated(previousList, sourceList, provideId())
        }

        if (sourceList.isEmpty()) sourceList.add(stateItem)
    }

    abstract fun itemFilter(search: String, item: ItemContainer): Boolean

    open fun headerFilter(search: String, item: ItemContainer): Boolean = false

    override fun currentSize(): Int {
        if (sourceList.first() is StubItem)
            return if (state == SectionState.LOADED) 0 else 1

        val baseSize = super.currentSize()
        return if (baseSize == sourceList.size) {
            if (filteredList.isEmpty()) baseList.size else filteredList.size
        } else baseSize
    }

    private fun addItemsWithFooter(list: List<ItemContainer>) {
        val footerIndex = getFooterIndex()
        val footerItem = sourceList.removeAt(footerIndex)
        sourceList.addAll(list)
        sourceList.add(footerItem)

        val footerBaseItem = baseList.removeAt(footerIndex)
        baseList.addAll(list)
        baseList.add(footerBaseItem)
    }

}