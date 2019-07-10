package com.luckylittlesparrow.srvlist.recycler.section

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
import androidx.annotation.LayoutRes
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.state.State
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

/**
 *
 */
abstract class Section<H, I, F>(
    headerItem: ItemContainer? = null,
    contentItems: List<ItemContainer>? = null,
    footerItem: ItemContainer? = null,
    sectionKey: String? = null,
    val headerClickListener: (ItemContainer) -> Unit = {},
    val itemClickListener: (ItemContainer) -> Unit = {},
    val footerClickListener: (ItemContainer) -> Unit = {}
) : State() {

    internal lateinit var key: String

    internal val sourceList = ArrayList<ItemContainer>()

    @LayoutRes
    internal var itemResourceId: Int? = null
        private set
    @LayoutRes
    internal var headerResourceId: Int? = null
        private set
    @LayoutRes
    internal var footerResourceId: Int? = null
        private set
    @LayoutRes
    internal var loadingResourceId: Int? = null
    @LayoutRes
    internal var failedResourceId: Int? = null
    @LayoutRes
    internal var emptyResourceId: Int? = null

    internal var isVisible = true

    var hasHeader: Boolean = false
        private set

    var hasFooter: Boolean = false
        private set

    var isExpanded: Boolean = true
        private set

    var supportStates: Boolean = false
        private set

    var supportExpansion: Boolean = false
        private set

    var supportShowMore: Boolean = false
        private set

    var collapsedItemCount: Int = 4
        private set

    var isShowMoreClicked: Boolean = false
        private set

    val onExpandClickListener: () -> Unit = {
        check(supportExpansion) { "Section must support expand functionality" }
        isExpanded = !isExpanded
        sectionStateCallback?.onSectionExpandChange(key, isExpanded)
    }

    val onShowMoreClickListener: () -> Unit = {
        check(supportShowMore) { "Section must support \"show more\" functionality" }
        isShowMoreClicked = !isShowMoreClicked
        sectionStateCallback?.onSectionShowMoreChange(key, collapsedItemCount, isShowMoreClicked)
    }

    init {
        setup()
        initItems(sectionKey, headerItem, contentItems, footerItem)
    }

    private fun setup() {
        itemResourceId = getSectionParams().itemResourceId
        headerResourceId = getSectionParams().headerResourceId
        footerResourceId = getSectionParams().footerResourceId
        loadingResourceId = getSectionParams().loadingResourceId
        failedResourceId = getSectionParams().failedResourceId
        emptyResourceId = getSectionParams().emptyResourceId

        if (loadingResourceId != null || failedResourceId != null || emptyResourceId != null) {
            supportStates = true
        }

        headerResourceId?.let { hasHeader = true }
        footerResourceId?.let { hasFooter = true }

        supportShowMore = getSectionParams().supportShowMoreFunction
        collapsedItemCount = getSectionParams().collapsedItemCount

        supportExpansion = getSectionParams().supportExpansionFunction
        isExpanded = getSectionParams().isExpandedByDefault
    }

    private fun initItems(
        sectionKey: String?,
        headerItem: ItemContainer?,
        contentItems: List<ItemContainer>?,
        footerItem: ItemContainer?
    ) {
        sectionKey?.let { key = it }

        headerItem?.let { sourceList.add(it) }

        contentItems?.let {
            check(hasHeader && headerItem != null || !hasHeader && headerItem == null) { "Forgot to provide header item or resource" }
            sourceList.addAll(contentItems)
        }

        footerItem?.let {
            check(contentItems != null || headerItem != null) { "Forgot to provide content items or header" }
            sourceList.add(footerItem)
        }
    }

    abstract fun getSectionParams(): SectionParams

    abstract fun getDiffUtilItemCallback(): DiffUtilItemCallback

    abstract fun getItemViewHolder(view: View): BaseViewHolder<I>

    open fun getHeaderViewHolder(view: View): BaseViewHolder<H> {
        check(hasHeader) { "Forgot to override HeaderViewHolder getter" }
        return EmptyViewHolder(view)
    }

    open fun getFooterViewHolder(view: View): BaseViewHolder<F> {
        check(hasFooter) { "Forgot to override FooterViewHolder getter" }
        return EmptyViewHolder(view)
    }

    open fun getLoadingViewHolder(view: View): BaseViewHolder<Nothing> {
        check(loadingResourceId != null) { "Forgot to override LoadingViewHolder getter" }
        return EmptyViewHolder(view)
    }

    open fun getEmptyViewHolder(view: View): BaseViewHolder<Nothing> {
        check(loadingResourceId != null) { "Forgot to override EmptyViewHolder getter" }
        return EmptyViewHolder(view)
    }

    open fun getFailedViewHolder(view: View): BaseViewHolder<Nothing> {
        check(loadingResourceId != null) { "Forgot to override FailedViewHolder getter" }
        return EmptyViewHolder(view)
    }

    override fun emptyStateRequirements(): Boolean = emptyResourceId != null

    override fun loadedStateRequirements(): Boolean = itemResourceId != null

    override fun loadingStateRequirements(): Boolean = loadingResourceId != null

    override fun failedStateRequirements(): Boolean = failedResourceId != null

    override fun provideId(): String = key

    open fun addItems(itemBundle: ItemBundle) {
        check(
            !hasHeader()
                    || sourceList.isNotEmpty()
                    && sourceList.first().isHeader()
                    || hasHeader() && itemBundle.headerItem != null
        ) { "Forgot to provide header item" }

        var isNewContent = true
        var itemsCount = 0
        val isEmpty = sourceList.isEmpty()

        itemBundle.headerItem?.let {
            if (!isEmpty && sourceList.first().isHeader()) {
                sourceList[0] = it
                isNewContent = false
            } else {
                sourceList.add(0, it)
                itemsCount++
            }
        }

        itemBundle.contentItems?.let {
            //itemsCount += sourceList.size
            itemsCount += it.size
            if (!isEmpty && hasFooter() && sourceList.last().isFooter()) addItemsWithFooter(it)
            else {
                sourceList.addAll(it)
            }
        }

        itemBundle.footerItem?.let {
            if (!isEmpty && sourceList.last().isFooter()) {
                sourceList[getFooterIndex()] = it
                isNewContent = false
            } else {
                sourceList.add(getFooterIndex() + 1, it)
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
    }

    /**
     * Replace previous items with the new items
     */
    open fun updateItems(itemBundle: ItemBundle) {
        val previousList = ArrayList<ItemContainer>()
        previousList.addAll(sourceList)
        sourceList.clear()

        check(
            !hasHeader() || hasHeader() && itemBundle.headerItem != null
        ) { "Forgot to provide header item" }

        var itemsCount = 0


        itemBundle.headerItem?.let {
            sourceList.add(0, it)
            itemsCount++
        }

        itemBundle.contentItems?.let {
            itemsCount += it.size
            sourceList.addAll(it)
        }

        itemBundle.footerItem?.let {
            sourceList.add(getFooterIndex() + 1, it)
            itemsCount++
        }

        if (state == SectionState.LOADED) {
            sectionStateCallback?.onSectionContentUpdated(previousList, sourceList, provideId())
        }
    }

    internal open fun currentSize(): Int {
        return if (isExpanded) getExpandedSectionSize() else getCollapsedSectionSize()
    }

    internal open fun originalSize(): Int {
        return sourceList.size
    }

    internal fun hasFooter(): Boolean {
        return footerResourceId != null
    }

    internal fun hasHeader(): Boolean {
        return headerResourceId != null
    }

    protected fun getFooterIndex(): Int = sourceList.size - 1

    private fun getExpandedSectionSize(): Int {
        if (state != SectionState.LOADED) return getCollapsedSectionSize() + 1
        return if (supportShowMore && !isShowMoreClicked) collapsedItemCount else sourceList.size
    }

    private fun getCollapsedSectionSize(): Int {
        var size = 0

        if (hasHeader) size++
        if (hasFooter) size++
        return size
    }

    private fun addItemsWithFooter(list: List<ItemContainer>) {
        val footerItem = sourceList.removeAt(getFooterIndex())
        sourceList.addAll(list)
        sourceList.add(footerItem)
    }
}