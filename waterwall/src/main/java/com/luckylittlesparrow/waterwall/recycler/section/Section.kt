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
import androidx.annotation.LayoutRes
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.Binder
import com.luckylittlesparrow.waterwall.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.state.State
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback
import com.luckylittlesparrow.waterwall.recycler.util.lazyFast

/**
 * Base Section aka Container with configured data to be used in [SimpleSectionedAdapter].
 *
 * Supported functionality:
 *           States
 *           Decorations
 *           Expandable sections
 *           Sticky headers
 *           Show more, show less
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
 * @see SimpleSectionedAdapter
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class Section<H, I, F>(
    headerItem: ItemContainer? = null,
    contentItems: List<ItemContainer>? = null,
    footerItem: ItemContainer? = null,
    key: String? = null
) : State() {

    internal var sectionKey: String? = null

    internal val sourceList = ArrayList<ItemContainer>()

    internal val stateItem = StubItem()

    internal var isSwitched = false

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

    var isVisible = true
        set(value) {
            field = value
            //sectionStateCallback?.onSectionVisibilityChange(value, provideId(), currentSize())
        }
    /**
     * Check if section has header.
     */
    var hasHeader: Boolean = false
        private set

    /**
     * Check if section has footer.
     */
    var hasFooter: Boolean = false
        private set

    /**
     * Check if section expanded.
     */
    var isExpanded: Boolean = true
        internal set

    /**
     * Check if section support [SectionState].
     *
     * @see SectionState
     */
    var supportStates: Boolean = false
        private set

    /**
     * Check if section support expand/collapse functionality.
     */
    var supportExpandFunction: Boolean = false
        private set

    /**
     * Check if section support showMore/showLess functionality.
     */
    var supportShowMore: Boolean = false
        private set

    /**
     * Minimum collapsed items to be shown if section support showMore/showLess functionality.
     *
     * @see supportShowMore
     */
    var collapsedItemCount: Int = 4
        private set

    /**
     * Indicates if section showMore function called.
     */
    var isShowMoreClicked: Boolean = false
        private set


    /**
     * Click listener to call, when section need to showMore/showLess.
     *
     * Usage: In your section implementation put this listener to viewHolder
     *
     *     override fun getHeaderViewHolder(view: View): BaseViewHolder<ExpandableHeader> {
     *             return ShowMoreHeaderViewHolder(view, onShowMoreClickListener, headerClickListener)
     *      }
     *
     * and invoke it on the view listener ->
     *
     *  showMoreItem.setOnClickListener {
     *      onShowMoreClickListener.invoke()
     *      isExpanded = !isExpanded
     *    }
     * @see supportShowMore
     *
     */
    val onShowMoreClickListener: () -> Unit? by lazyFast {
        return@lazyFast {
            check(supportShowMore) { SECTION_SUPPORT_SHOW_MORE }
            isShowMoreClicked = !isShowMoreClicked
            sectionStateCallback?.onSectionShowMoreChange(provideId(), collapsedItemCount, isShowMoreClicked)
        }
    }

    init {
        setup()
        initItems(headerItem, contentItems, footerItem, key)
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

        supportExpandFunction = getSectionParams().supportExpandFunction
        isExpanded = getSectionParams().isExpandedByDefault
    }

    private fun initItems(
        headerItem: ItemContainer?,
        contentItems: List<ItemContainer>?,
        footerItem: ItemContainer?,
        key: String?
    ) {
        key?.let { sectionKey = key }
        headerItem?.let { sourceList.add(it) }

        contentItems?.let {
            check(hasHeader && headerItem != null || !hasHeader && headerItem == null) { HEADER_ITEM_BEFORE_ITEMS }
            sourceList.addAll(contentItems)
        }

        footerItem?.let {
            check(contentItems != null || headerItem != null) { ITEMS_BEFORE_FOOTER }
            sourceList.add(footerItem)
        }

        if (sourceList.isEmpty()) sourceList.add(stateItem)
    }

    /**
     * Get mutable copy of all items stored in section.
     *
     * @return mutable list with items
     */
    fun getAllItems() = sourceList.toMutableList()

    /**
     * Check if section is Empty
     *
     * @return [true] if section is empty, [false] otherwise
     */
    fun isEmpty() = sourceList.first() is StubItem


    override fun shouldUpdate(): Boolean {
        return when {
            !isVisible -> false
            state == SectionState.LOADED && isEmpty() -> false
            !isExpanded -> false
            else -> true
        }
    }

    /**
     * Check if section is not empty.
     *
     * @return [true] if section is not empty, [false] otherwise
     */
    fun isNotEmpty() = sourceList.first() !is StubItem

    /**
     * Get sectionKey of the section
     *
     * @return sectionKey of the section, [null] if section doesn't added to the adapter
     */
    fun getKey() = sectionKey

    /**
     * Provide parameters to the section to initialize functionality.
     *
     * @see SectionParams
     *
     * @return section params
     */
    abstract fun getSectionParams(): SectionParams

    /**
     * Provide Callback for calculating the diff between two non-null content items in a list.
     *
     * @see DiffUtilItemCallback
     *
     * @return diffUtilItemCallback
     */
    abstract fun getDiffUtilItemCallback(): DiffUtilItemCallback

    /**
     * Provide ViewHolder for content items.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    abstract fun getItemViewHolder(view: View): BaseViewHolder<I>

    /**
     * Provide ViewHolder for header item.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    open fun getHeaderViewHolder(view: View): BaseViewHolder<H> {
        check(hasHeader) { FORGOT_OVERRIDE_HEADER_VH }
        return EmptyViewHolder(view)
    }

    /**
     * Provide ViewHolder for footer item.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    open fun getFooterViewHolder(view: View): BaseViewHolder<F> {
        check(hasFooter) { FORGOT_OVERRIDE_FOOTER_VH }
        return EmptyViewHolder(view)
    }

    /**
     * Provide ViewHolder for loading item.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    open fun getLoadingViewHolder(view: View): BaseViewHolder<Nothing> {
        check(loadingResourceId != null) { FORGOT_OVERRIDE_LOADING_VH }
        return EmptyViewHolder(view)
    }

    /**
     * Provide ViewHolder for empty item.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    open fun getEmptyViewHolder(view: View): BaseViewHolder<Nothing> {
        check(emptyResourceId != null) { FORGOT_OVERRIDE_EMPTY_VH }
        return EmptyViewHolder(view)
    }

    /**
     * Provide ViewHolder for failed item.
     *
     * @see BaseViewHolder<T>
     *
     * @return ViewHolder
     */
    open fun getFailedViewHolder(view: View): BaseViewHolder<Nothing> {
        check(failedResourceId != null) { FORGOT_OVERRIDE_FAILED_VH }
        return EmptyViewHolder(view)
    }

    override fun emptyStateRequirements(): Boolean = emptyResourceId != null

    override fun loadedStateRequirements(): Boolean = itemResourceId != null

    override fun loadingStateRequirements(): Boolean = loadingResourceId != null

    override fun failedStateRequirements(): Boolean = failedResourceId != null

    override fun provideId(): String = sectionKey ?: ""

    /**
     * Add more items to the section, if bundle contains header and footer,
     * and they were provided at section initialization, they will be replaced by new ones
     *
     * @see ItemBundle
     *
     * @param itemBundle bundle with items to add
     */
    open fun addMoreItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        check(
            !hasHeader
                    || sourceList.isNotEmpty()
                    && sourceList.first().isHeader()
                    || hasHeader && itemBundle.headerItem != null
        ) { FORGOT_HEADER }

        sourceList.remove(stateItem)

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
            itemsCount += it.size
            if (!isEmpty && hasFooter && sourceList.last().isFooter()) addItemsWithFooter(it)
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

        if (state == SectionState.LOADED && isVisible) {
            if (isNewContent && itemsCount > 0)
                sectionStateCallback?.onSectionContentAdded(provideId(), itemsCount)
            else {
                if (!isNewContent) sectionStateCallback?.onSectionContentChanged(provideId())
            }
        }
    }

    /**
     * Add items or replace previous items with the new items and change state to the Loaded
     *
     * @see ItemBundle
     *
     * @param itemBundle bundle with items to add
     */
    open fun submitItemsWithLoadedState(itemBundle: ItemBundle) {
        submitItems(itemBundle)
        state = SectionState.LOADED
    }

    /**
     * Add items or replace previous items with the new items
     *
     * @see ItemBundle
     *
     * @param itemBundle bundle with items to add
     */
    open fun submitItems(itemBundle: ItemBundle) {
        if (itemBundle.isEmpty()) return
        val previousList = ArrayList<ItemContainer>()
        previousList.addAll(sourceList)
        sourceList.clear()

        check(!hasHeader || hasHeader && itemBundle.headerItem != null) { FORGOT_HEADER }

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

    }

    /**
     * Removes all of the elements from this section. The Section will
     * be empty after this call returns.
     */
    open fun clearSection() {
        if (isEmpty()) return
        val previousList = ArrayList(sourceList)
        sourceList.clear()
        sourceList.add(StubItem())
        sectionStateCallback?.onSectionContentUpdated(previousList, sourceList, provideId())
    }

    open fun provideStickyHeaderBinder(): Binder {
        throw (IllegalStateException(SECTION_SUPPORT_STIKY))
    }

    internal val stickyHeaderBinder: Binder by lazyFast {
        provideStickyHeaderBinder()
    }

    internal open fun currentSize(): Int {
        if (sourceList.first() is StubItem)
            return if (state == SectionState.LOADED) 0 else 1

        return if (isExpanded) getExpandedSectionSize() else getCollapsedSectionSize()
    }

    internal open fun originalSize(): Int {
        return sourceList.size
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

    companion object {
        private const val FORGOT_HEADER = "Forgot to provide header item"
        private const val SECTION_SUPPORT_STIKY =
            "Section must override getStickyHeaderBinder method in order to support functionality"
        private const val SECTION_SUPPORT_SHOW_MORE = "Section must support \"show more\" functionality"
        private const val HEADER_ITEM_BEFORE_ITEMS = "Submit header item before contentItems"
        private const val ITEMS_BEFORE_FOOTER = "It's required to submit contentItems before footerItem"
        private const val FORGOT_OVERRIDE_HEADER_VH = "Forgot to override HeaderViewHolder getter"
        private const val FORGOT_OVERRIDE_FOOTER_VH = "Forgot to override FooterViewHolder getter"
        private const val FORGOT_OVERRIDE_LOADING_VH = "Forgot to override LoadingViewHolder getter"
        private const val FORGOT_OVERRIDE_EMPTY_VH = "Forgot to override EmptyViewHolder getter"
        private const val FORGOT_OVERRIDE_FAILED_VH = "Forgot to override FailedViewHolder getter"
    }
}