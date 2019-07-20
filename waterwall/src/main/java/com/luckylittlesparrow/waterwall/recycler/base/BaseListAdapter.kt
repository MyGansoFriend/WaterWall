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

import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.StubSection
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import com.luckylittlesparrow.waterwall.recycler.state.SectionStateCallback
import com.luckylittlesparrow.waterwall.recycler.sticky.StickyHeaderDecoration
import com.luckylittlesparrow.waterwall.recycler.sticky.StickyHeaderHelper
import com.luckylittlesparrow.waterwall.recycler.util.*
import java.util.*
import java.util.concurrent.Executors

/**
 * BaseAdapter with core functionality for sectioned view types
 *
 * For filter purpose use [FilterableSectionedAdapter] otherwise [SimpleSectionedAdapter]
 *
 * Supported functionality:
 *           States
 *           Decorations
 *           Expandable sections
 *           Sticky headers
 *           Show more, show less
 *
 * @see FilterableSectionedAdapter
 * @see SimpleSectionedAdapter
 * @see Section<H,I,F>
 * @see SectionItemDecoration
 * @see StickyHeaderDecoration
 * @see supportStickyHeader
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class BaseListAdapter : RecyclerView.Adapter<BaseViewHolder<Nothing>>() {

    var maxRecycledContentItemViewsPool = 30

    var maxRecycledHeaderItemViewsPool = 0

    var maxRecycledFooterItemViewsPool = 0

    var supportFixedSize = true

    var onFailedToRecycleView = true

    var sameSectionType = true

    var supportStableIds = true

    private val sectionBinder = SectionBinder()

    private val sectionViewTypeNumbers: MutableMap<String, Int> = LinkedHashMap()

    private var viewTypeCount = 0

    private val ioExecutor = Executors.newSingleThreadExecutor()

    private val mainUIExecutor = MainThreadExecutor()

    private var stubSection: StubSection? = null

    protected var recyclerView: RecyclerView? = null

    internal lateinit var sectionMediator: SectionMediator

    /**
     * Support sticky header functionality, section must have header support
     *
     * This method must be called before RecyclerView#setAdapter(RecyclerView.Adapter) [recyclerView]setAdapter]
     * @see RecyclerView#setAdapter(RecyclerView.Adapter)
     */
    var supportStickyHeader = false
        set(value) {
//            field = value
//            sectionBinder.isStickyHeaderSupported = value
//            sectionBinder.clickListener = if (value) WeakReference(clickListener) else null
        }


    /**
     * Use default adapter settings, also recommended to use
     * [recyclerView.setHasFixedSize(true)] in case of const size
     *
     * @see RecyclerView.setHasFixedSize(true)
     * @see RecyclerView.Adapter.setHasStableIds(true)
     */
    fun setDefaultOptimizationSettings() {
        setHasStableIds(supportStableIds)
    }

    /**
     * Add a section with sectionKey to the adapter
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see Section<H, I, F>
     *
     * @param key     unique sectionKey of the section
     * @param section section to add
     */
    open fun addSection(key: String, section: Section<*, *, *>) {
        check(section !is StubSection) { "Stub section not allowed to be added with sectionKey" }
        if (!sectionMediator.containsSection(key)) {
            check(!supportStickyHeader || supportStickyHeader && section.hasHeader) {
                "$section must have header for \"Sticky header\" support"
            }
            addSectionToEnd(section, key)
        }
    }

    /**
     * Add a section to the adapter, sectionKey for section will be generated and returned,
     * if it null in section
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see Section<H, I, F>
     *
     * @param section section to add
     * @return Generated sectionKey
     */
    open fun addSection(section: Section<*, *, *>): String {
        checkForStubSection(section)
        return if (!sectionMediator.containsSection(section)) {
            check(!supportStickyHeader || supportStickyHeader && section.hasHeader) {
                "$section must have header for \"Sticky header\" support"
            }
            addSectionToEnd(section)
        } else section.sectionKey!!
    }

    /**
     * Add more sections to the adapter
     *
     * In case of adding a duplicate section, nothing will happen
     *
     * @see Section<H, I, F>
     *
     * @param list list of sections to add
     */
    open fun addMoreSections(list: List<Section<*, *, *>>) {
        if (list.isEmpty()) return
        checkForStubSection()
        list.forEach {
            check(!supportStickyHeader || supportStickyHeader && it.hasHeader) {
                "$it must have header for \"Sticky header\" support"
            }
            if (sectionMediator.containsSection(it)) return
        }
        addSectionsToEnd(list)
    }

    /**
     * Submit list of sections to the adapter, adapter will remove old sections and display new ones
     * in case, if only part of sections were changed, it's better to update them instead of full list
     *
     * @see Section<H, I, F>
     *
     * @param list list of sections
     */
    open fun submitSections(list: List<Section<*, *, *>>) {
        if (list.isEmpty()) return
        checkForStubSection()
        list.forEach {
            check(!supportStickyHeader || supportStickyHeader && it.hasHeader) {
                "$it must have header for \"Sticky header\" support"
            }
        }
        submitSectionsInternal(list)
    }

    /**
     * Remove section from the adapter, [true] if success [false] otherwise
     *
     * Stub section will be removed automatically, if new sections are provided
     *
     * @see Section<H, I, F>
     *
     * @param section section to remove
     */
    open fun removeSection(section: Section<*, *, *>): Boolean {
        return if (section !is StubSection) removeSectionInternal(section) else false
    }

    /**
     * Remove section from the adapter by sectionKey, [true] if success [false] otherwise
     *
     * Stub section will be removed automatically, if new sections are provided
     *
     * @see Section<H, I, F>
     *
     * @param key sectionKey of section to remove
     */
    open fun removeSection(key: String): Boolean {
        return removeSectionInternal(sectionKey = key)
    }

    /**
     * Remove all section inside adapter and return it to initial state
     */
    fun clearList() {
        if (viewTypeCount > 0)//
            sectionMediator.clearList()
        sectionViewTypeNumbers.clear()
        viewTypeCount = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Nothing> {
        var viewHolder: BaseViewHolder<Nothing>? = null

        for ((key, value) in sectionViewTypeNumbers) {
            if (viewType >= value && viewType < value + VIEW_TYPE_ITEM) {

                val section = sectionMediator.getSectionList()[key]!!.section
                val sectionViewType = viewType - value

                viewHolder = getViewHolderByType(sectionViewType, parent, section)
                break
            }
        }

        viewHolder?.itemView?.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                if (viewHolder.isItemNotNull()) {
                    if (viewHolder.isHeader()) viewHolder.performClick()
                    else if (supportStickyHeader) {
                        if (viewHolder.checkForStickyHeader()) viewHolder.performClick()
                    } else viewHolder.performClick()
                }
            }
        }

        return viewHolder!!
    }

    override fun getItemId(position: Int): Long {
        // return stickyHeaderHelper.getItemByPosition(position).ITEM_CONTAINER_ID
        return sectionMediator.getItemByPosition(position).ITEM_CONTAINER_ID
    }

    override fun getItemCount(): Int {
        return sectionMediator.getVisibleItemCount()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Nothing>, position: Int) {
        var currentPos = 0

        for ((_, sectionDao) in sectionMediator.getSectionList()) {

            if (!sectionDao.isVisible()) continue

            val sectionTotal = sectionDao.sectionCurrentSize()

            if (position >= currentPos && position <= currentPos + sectionTotal - 1) {
                val boundSection = sectionMediator.getSectionByItemPosition(position)
                when {
                    sectionDao.hasHeader() && position == currentPos -> {
                        sectionBinder.onBindHeaderViewHolder(
                            boundSection.getItem(ItemContainer.ItemType.HEADER),
                            holder
                        )
                        return
                    }
                    sectionDao.hasFooter() && position == currentPos + sectionTotal - 1 -> {
                        sectionBinder.onBindFooterViewHolder(
                            boundSection.getItem(ItemContainer.ItemType.FOOTER),
                            holder
                        )
                        return
                    }
                    else -> {
                        val state = boundSection.state()
                        if (state != SectionState.LOADED)
                            sectionBinder.onBindContentViewHolder(holder, state)
                        else
                            sectionBinder.onBindContentViewHolder(
                                holder,
                                state,
                                boundSection.getItem(
                                    ItemContainer.ItemType.ITEM,
                                    sectionMediator.getPositionInSection(position)
                                )
                            )
                        return
                    }
                }
            }
            currentPos += sectionTotal
        }
    }

    override fun getItemViewType(position: Int): Int {
        var currentPos = 0

        for ((key, sectionDao) in sectionMediator.getSectionList()) {

            if (!sectionDao.isVisible()) continue

            val sectionTotal = sectionDao.sectionCurrentSize()

            if (position >= currentPos && position <= currentPos + sectionTotal - 1) {

                val viewType = sectionViewTypeNumbers[key]!!

                return when {
                    sectionDao.hasHeader() && position == currentPos -> viewType
                    sectionDao.hasFooter() && position == currentPos + sectionTotal - 1 -> viewType + 1
                    else -> when (sectionDao.state()) {
                        SectionState.LOADED -> viewType + VIEW_TYPE_ITEM_LOADED
                        SectionState.LOADING -> viewType + VIEW_TYPE_LOADING
                        SectionState.FAILED -> viewType + VIEW_TYPE_FAILED
                        SectionState.EMPTY -> viewType + VIEW_TYPE_EMPTY
                    }
                }
            }
            currentPos += sectionTotal
        }
        throw IndexOutOfBoundsException("Invalid position")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView

        sectionMediator.attachSectionStateCallback(sectionStateCallback)

        if (supportStickyHeader) {
            recyclerView.addItemDecoration(
                StickyHeaderDecoration(
                    stickyHeaderHelper
                )
            )
        }

        recyclerView.recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ITEM, maxRecycledContentItemViewsPool)

        if (maxRecycledHeaderItemViewsPool > 0)
            recyclerView.recycledViewPool.setMaxRecycledViews(
                VIEW_TYPE_HEADER,
                maxRecycledHeaderItemViewsPool
            )

        if (maxRecycledFooterItemViewsPool > 0)
            recyclerView.recycledViewPool.setMaxRecycledViews(
                VIEW_TYPE_FOOTER,
                maxRecycledFooterItemViewsPool
            )
        recyclerView.setHasFixedSize(supportFixedSize)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null

        sectionMediator.detachSectionStateCallback()
    }

    override fun onFailedToRecycleView(holder: BaseViewHolder<Nothing>): Boolean {
        return onFailedToRecycleView
    }

    internal var currentStickyHeader: BaseViewHolder<*>? = null

    internal val stickyHeaderHelper: StickyHeaderHelper by lazyFast {
        StickyHeaderHelper(sectionMediator)
    }

    private val clickListener: OnItemClickListener by lazyFast {
        object : OnItemClickListener {
            override fun onItemClick(item: ItemContainer): Boolean {
                var result = true
                recyclerView?.let {
                    val child = recyclerView!!.getChildAt(0)

                    val itemOnZeroPosition = (recyclerView!!.getChildViewHolder(child) as BaseViewHolder<*>).item

                    result = item != itemOnZeroPosition
                    if (!result) currentStickyHeader?.performClick()
                }

                return result
            }
        }
    }

    private val sectionStateCallback = object : SectionStateCallback {
        override fun onSectionContentUpdated(
            previousList: List<ItemContainer>,
            newList: List<ItemContainer>,
            sectionKey: String
        ) {
            calculateDiff(previousList, newList, true)
            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }

        override fun onSectionShowMoreChange(sectionKey: String, collapsedItemCount: Int, isShowMore: Boolean) {
            if (isShowMore) notifyItemRangeInsertedInternal(sectionKey, collapsedItemCount)
            else notifyItemRangeRemovedInSection(sectionKey)

            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }

        override fun onSectionExpandChange(sectionKey: String, isExpanded: Boolean) {
            if (isExpanded) notifyItemRangeInsertedInternal(sectionKey)
            else notifyItemRangeRemovedInSection(sectionKey)

            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }

        override fun onSectionContentChanged(sectionKey: String) {
            notifyItemRangeChangedInSection(sectionKey)
            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }

        override fun onSectionContentAdded(sectionKey: String, addItemsCount: Int) {
            notifyItemRangeInsertedInSection(sectionKey, addItemsCount)
            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }

        override fun onSectionStateChanged(sectionKey: String, newState: SectionState, oldState: SectionState) {
            if (newState != SectionState.LOADED) {
                val section = sectionMediator.getSectionByKey(sectionKey)
                if (section.isEmpty()) notifyItemChanged(0)
                else {
                    val list = section.getSectionList()
                    calculateDiff(list, list.subList(0, 1))
                }
            } else {
                notifyDataSetChanged()
            }
            stickyHeaderHelper.stateChanged()
            sectionMediator.stateChanged()
        }
    }

    internal fun dispatchUpdates(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    private fun checkForStubSection(section: Section<*, *, *>? = null) {
        if (section is StubSection) stubSection = section
        else if (stubSection != null) {
            stubSection = null
            clearList()
            notifyItemRemoved(0)
        }
    }

    private fun addSectionToEnd(section: Section<*, *, *>, sectionKey: String? = null): String {
        stickyHeaderHelper.stateChanged()
        val oldList = sectionMediator.getAllItemsList()

        var key = sectionKey
        if (key == null) key = sectionMediator.addSection(section, sectionStateCallback)
        else sectionMediator.addSection(key, section, sectionStateCallback)

        sectionViewTypeNumbers[key] = viewTypeCount
        if (!sameSectionType) viewTypeCount += VIEW_TYPE_ITEM


        val newList = sectionMediator.getAllItemsList()

        if (oldList.size != newList.size)
            notifyItemRangeInserted(oldList.size + 1, section.sourceList.size)
        else calculateDiff(oldList, newList)

        return key
    }

    private fun removeSectionInternal(section: Section<*, *, *>? = null, sectionKey: String? = null): Boolean {
        check(section != null && sectionKey == null || sectionKey != null && section == null)
        stickyHeaderHelper.stateChanged()

        val oldList = sectionMediator.getAllItemsList()
        val result =
            if (sectionKey == null) sectionMediator.removeSection(section!!) else sectionMediator.removeSection(
                sectionKey
            )
        ioExecutor.execute {
            val newList = sectionMediator.getAllItemsList()

            if (result) {
                val key = sectionKey ?: section!!.sectionKey
                sectionViewTypeNumbers.remove(key)
                if (!sameSectionType) viewTypeCount -= VIEW_TYPE_ITEM
            }
            calculateDiff(oldList, newList)
        }
        return result
    }

    private fun addSectionsToEnd(list: List<Section<*, *, *>>) {
        stickyHeaderHelper.stateChanged()
        var newElementsSize = 0
        val oldList = sectionMediator.getAllItemsList()

        sectionMediator.addSections(list, sectionStateCallback)
        list.forEach {
            newElementsSize += it.sourceList.size
            sectionViewTypeNumbers[it.sectionKey!!] = viewTypeCount
            if (!sameSectionType) viewTypeCount += VIEW_TYPE_ITEM
        }

        notifyItemRangeInserted(oldList.size + 1, newElementsSize)
    }

    private fun submitSectionsInternal(list: List<Section<*, *, *>>) {
        stickyHeaderHelper.stateChanged()
        val oldList = sectionMediator.getAllItemsList()
        if (oldList.isNotEmpty()) clearList()
        sectionMediator.addSections(list, sectionStateCallback)

        list.forEach {
            sectionViewTypeNumbers[it.sectionKey!!] = viewTypeCount
            if (!sameSectionType) viewTypeCount += VIEW_TYPE_ITEM
        }
        val newList = sectionMediator.getAllItemsList()
        if (oldList.isEmpty()) super.notifyItemRangeInserted(0, newList.size) else {

            ioExecutor.execute {

                val diffListUtilByItems = DiffListUtilByItems(sectionMediator)
                diffListUtilByItems.submitLists(oldList, newList)

                val result = DiffUtil.calculateDiff(diffListUtilByItems)

                mainUIExecutor.execute {
                    result.dispatchUpdatesTo(this)
                }
            }
        }
    }

    private fun calculateDiff(
        oldList: List<ItemContainer>,
        newList: List<ItemContainer>,
        isTheSameSection: Boolean = false
    ) {
        ioExecutor.execute {
            val diffListUtilBySections = DiffListUtilBySections(sectionMediator)
            diffListUtilBySections.submitLists(oldList, newList)
            diffListUtilBySections.isTheSameSection = isTheSameSection && oldList.size == newList.size

            val result = DiffUtil.calculateDiff(diffListUtilBySections)

            mainUIExecutor.execute {
                result.dispatchUpdatesTo(this)
                if (isTheSameSection) recyclerView?.scrollToPosition(0)
            }
        }
    }

    private fun getViewHolder(
        parent: ViewGroup,
        resourceId: Int?,
        getItemFunction: (view: View) -> BaseViewHolder<Nothing>
    ): BaseViewHolder<Nothing> {
        require(resourceId != null) { "Missing item resource ITEM_CONTAINER_ID" }
        return getItemFunction(parent.inflate(resourceId))
    }

    private fun getViewHolderByType(
        sectionViewType: Int,
        parent: ViewGroup,
        section: Section<Nothing, Nothing, Nothing>?
    ): BaseViewHolder<Nothing> {
        return when (sectionViewType) {
            VIEW_TYPE_HEADER ->
                getViewHolder(
                    parent,
                    section!!.headerResourceId,
                    section::getHeaderViewHolder
                )
            VIEW_TYPE_FOOTER ->
                getViewHolder(
                    parent,
                    section!!.footerResourceId,
                    section::getFooterViewHolder
                )

            VIEW_TYPE_ITEM_LOADED ->
                getViewHolder(
                    parent,
                    section!!.itemResourceId,
                    section::getItemViewHolder
                )

            VIEW_TYPE_LOADING ->
                getViewHolder(
                    parent,
                    section!!.loadingResourceId,
                    section::getLoadingViewHolder
                )

            VIEW_TYPE_FAILED ->
                getViewHolder(
                    parent,
                    section!!.failedResourceId,
                    section::getFailedViewHolder
                )

            VIEW_TYPE_EMPTY ->
                getViewHolder(
                    parent,
                    section!!.emptyResourceId,
                    section::getEmptyViewHolder
                )

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    private fun notifyItemRangeInsertedInSection(sectionKey: String, addItemsCount: Int) {
        val sectionPosition = sectionMediator.getSectionPosition(sectionKey)
        notifyItemRangeInserted(sectionPosition, addItemsCount)
    }

    private fun notifyItemRangeInsertedInternal(key: String, startPosition: Int = 0) {
        val sectionDao = sectionMediator.getSectionByKey(key)
        super.notifyItemRangeInserted(
            startPosition + sectionMediator.getSectionPosition(sectionDao.section),
            sectionDao.sectionCurrentSize() - 1 - startPosition
        )
    }

    private fun notifyItemRangeChangedInSection(key: String) {
        val sectionDao = sectionMediator.getSectionByKey(key)
        super.notifyItemRangeChanged(
            sectionMediator.getSectionPosition(sectionDao.section),
            sectionDao.sectionCurrentSize(),
            null
        )
    }

    private fun notifyItemRangeRemovedInSection(key: String) {
        val sectionDao = sectionMediator.getSectionByKey(key)
        super.notifyItemRangeRemoved(
            sectionMediator.getSectionPosition(sectionDao.section),
            sectionDao.sectionOriginalSize() - 1
        )
    }

    companion object {
        internal const val VIEW_TYPE_HEADER = 0
        internal const val VIEW_TYPE_FOOTER = 1
        internal const val VIEW_TYPE_ITEM_LOADED = 2
        internal const val VIEW_TYPE_LOADING = 3
        internal const val VIEW_TYPE_FAILED = 4
        internal const val VIEW_TYPE_EMPTY = 5
        internal const val VIEW_TYPE_ITEM = 6
    }

    @VisibleForTesting
    internal fun getClickListener() = clickListener
}