package com.luckylittlesparrow.waterwall.recycler.filterable

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

import com.luckylittlesparrow.waterwall.recycler.section.ItemBundle
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.StubItem
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.util.lazyFast

/**
 * Section with filter support with configured data to be used in [FilterableSectionedAdapter].
 *
 * Supported functionality:
 *           States
 *           Decorations
 *           Sticky headers
 *
 * @param H the type of header element in this section
 * @param I the type of content element in this section
 * @param F the type of footer element in this section
 *
 * @param headerItem data for header
 * @param contentItems data for items
 * @param footerItem data for footer
 * @param footerItem data for footer
 * @param headerClickListener click listener on header item
 * @param itemClickListener click listener on content item
 * @param footerClickListener click listener on footer item
 *
 * @see FilterableSectionedAdapter
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class FilterableSection<H, I, F>(
    headerItem: ItemContainer? = null,
    contentItems: List<ItemContainer>? = null,
    footerItem: ItemContainer? = null,
    key: String? = null
) : Section<H, I, F>(
    headerItem,
    contentItems,
    footerItem,
    key
) {

    internal var baseList = ArrayList<ItemContainer>()

    internal val filteredList by lazyFast { ArrayList<ItemContainer>() }

    internal var supportFilterHeader = false

    init {
        initResources()
        initItems(headerItem, contentItems, footerItem)
    }

    /**
     * Add more items to the section, if bundle contains header and footer,
     * and they were provided at section initialization, they will be replaced by new ones.
     *
     * @see ItemBundle
     *
     * @param itemBundle bundle with items to add
     */
    override fun addMoreItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        sourceList.remove(stateItem)

        check(
            !hasHeader
                    || sourceList.isNotEmpty() && sourceList.first().isHeader()
                    || hasHeader && itemBundle.headerItem != null
        ) { FORGOT_HEADER_RESOURCE }

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
            if (hasFooter && sourceList.isNotEmpty() && sourceList.last().isFooter()) addItemsWithFooter(it) else {
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

        if (state == SectionState.LOADED && isVisible) {
            if (isNewContent && itemsCount > 0)
                sectionStateCallback?.onSectionContentAdded(provideId(), itemsCount)
            else {
                if (!isNewContent) sectionStateCallback?.onSectionContentChanged(provideId())
            }
        }

        if (sourceList.isEmpty()) sourceList.add(stateItem)
    }

    /**
     * Add items or replace previous items with the new items.
     *
     * @see ItemBundle
     *
     * @param itemBundle bundle with items to add
     */
    override fun submitItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        val previousList = ArrayList<ItemContainer>()
        previousList.addAll(sourceList)
        sourceList.clear()
        baseList.clear()

        check(!hasHeader || hasHeader && itemBundle.headerItem != null) { FORGOT_HEADER }

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
    }

    /**
     * Callback for comparing item with requested string, it's guaranteed, that type of item is
     * equal to content item type [I], and have save cast.
     *
     * @param search filter string
     * @param item item to compare
     *
     * @return result of comparing
     */
    abstract fun itemFilter(search: String, item: ItemContainer): Boolean

    /**
     * Callback for comparing header with requested string in case of [supportFilterHeader],
     * it's guaranteed, that type of item is equal to header item type [H], and have save cast
     *
     * @see supportFilterHeader
     *
     * @param search filter string
     * @param item item to compare
     *
     * @return result of comparing
     */
    open fun headerFilter(search: String, item: ItemContainer): Boolean = false

    /**
     * Removes all of the elements from this section. The Section will
     * be empty after this call returns.
     */
    override fun clearSection() {
        baseList.clear()
        filteredList.clear()
        super.clearSection()
    }

    override fun currentSize(): Int {
        if (sourceList.first() is StubItem)
            return if (state == SectionState.LOADED) 0 else 1

        val baseSize = super.currentSize()
        return if (baseSize == sourceList.size) {
            if (filteredList.isEmpty()) baseList.size else filteredList.size
        } else baseSize
    }

    private fun initResources() {
        supportFilterHeader = getSectionParams().supportFilterHeaderFunction
        check(!getSectionParams().supportExpandFunction && !getSectionParams().supportShowMoreFunction) { SECTION_SUPPORT }
    }

    private fun initItems(headerItem: ItemContainer?, contentItems: List<ItemContainer>?, footerItem: ItemContainer?) {
        headerItem?.let { baseList.add(it) }

        contentItems?.let {
            check(hasHeader && headerItem != null || !hasHeader && headerItem == null) { HEADER_ITEM_BEFORE_ITEMS }
            baseList.addAll(contentItems)
        }

        footerItem?.let {
            check(contentItems != null) { ITEMS_BEFORE_FOOTER }
            baseList.add(footerItem)
        }
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

    companion object {
        private const val FORGOT_HEADER = "Forgot to provide header item"
        private const val FORGOT_HEADER_RESOURCE = "Submit header item or resource"
        private const val HEADER_ITEM_BEFORE_ITEMS = "Submit header item before contentItems"
        private const val ITEMS_BEFORE_FOOTER = "It's required to submit contentItems before footerItem"
        private const val SECTION_SUPPORT = "FilterableSection doesn't support \"show more\", \"expand\" functions"
    }
}