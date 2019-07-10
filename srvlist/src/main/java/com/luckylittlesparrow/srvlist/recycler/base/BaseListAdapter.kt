package com.luckylittlesparrow.srvlist.recycler.base

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.StubSection
import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import com.luckylittlesparrow.srvlist.recycler.util.*
import java.util.*
import java.util.concurrent.Executors

abstract class BaseListAdapter : RecyclerView.Adapter<BaseViewHolder<Nothing>>() {

    companion object {
        private const val DEFAULT_CACHE_SIZE = 30

        internal const val VIEW_TYPE_HEADER = 0
        internal const val VIEW_TYPE_FOOTER = 1
        internal const val VIEW_TYPE_ITEM_LOADED = 2
        internal const val VIEW_TYPE_LOADING = 3
        internal const val VIEW_TYPE_FAILED = 4
        internal const val VIEW_TYPE_EMPTY = 5
        internal const val VIEW_TYPE_ITEM = 6
    }

    init {
        setHasStableIds(true)
    }

    var defaultSettings = true

    internal lateinit var sectionMediator: SectionMediator

    internal val diffListUtil: DiffListUtil by lazyFast {
        DiffListUtil(sectionMediator)
    }

    private val sectionBinder = SectionBinder()

    private val sectionViewTypeNumbers: MutableMap<String, Int> = LinkedHashMap()

    private var viewTypeCount = 0

    private val ioExecutor = Executors.newSingleThreadExecutor()

    private val mainUIExecutor = MainThreadExecutor()

    private var stubSection: StubSection? = null

    protected var recyclerView: RecyclerView? = null

    private val sectionStateCallback = object : SectionStateCallback {
        override fun onSectionContentUpdated(
            previousList: List<ItemContainer>,
            newList: List<ItemContainer>,
            sectionKey: String
        ) {
            calculateDiff(previousList, newList, true)
        }

        override fun onSectionShowMoreChange(sectionKey: String, collapsedItemCount: Int, isShowMore: Boolean) {
            if (isShowMore) notifyItemRangeInsertedInternal(sectionKey, collapsedItemCount)
            else notifyItemRangeRemovedInSection(sectionKey)
        }

        override fun onSectionExpandChange(sectionKey: String, isExpanded: Boolean) {
            if (isExpanded) notifyItemRangeInsertedInternal(sectionKey)
            else notifyItemRangeRemovedInSection(sectionKey)
        }

        override fun onSectionContentChanged(sectionKey: String) {
            notifyItemRangeChangedInSection(sectionKey)
        }

        override fun onSectionContentAdded(sectionKey: String, addItemsCount: Int) {
            notifyItemRangeInsertedInSection(sectionKey, addItemsCount)
        }

        override fun onSectionStateChanged(sectionKey: String, newState: SectionState, oldState: SectionState) {
            if (oldState != SectionState.LOADED)
                notifyItemRangeChangedInSection(sectionKey)
            else {
                val section = sectionMediator.getSectionByKey(sectionKey)
                val list = section.getSectionList()
                calculateDiff(list, list.subList(0, 1))
            }
        }
    }

    /**
     * also recommended to use  recyclerView.setHasFixedSize(true) in case of const size
     */
    private fun setDefaultOptimizationSettings() {
        recyclerView?.setItemViewCacheSize(DEFAULT_CACHE_SIZE)
    }

    /**
     * Add a section
     *
     * @param key     unique key of the section
     * @param section section to be added
     */
    open fun addSection(key: String, section: Section<*, *, *>) {
        check(section !is StubSection) { "Stub section not allowed to be added with key" }
        if (sectionMediator.containsSection(key)) throw DuplicateSectionException(key)
        addSectionToEnd(section, key)
    }

    open fun addSection(section: Section<*, *, *>): String {
        checkForStubSection(section)
        if (sectionMediator.containsSection(section)) throw DuplicateSectionException(section.key)
        return addSectionToEnd(section)
    }

    open fun addSections(list: List<Section<*, *, *>>) {
        checkForStubSection()
        list.forEach {
            if (sectionMediator.containsSection(it)) throw DuplicateSectionException(it.key)
        }
        addSectionsToEnd(list)
    }

    open fun removeSection(section: Section<*, *, *>): Boolean {
        return if (section !is StubSection) removeSectionInternal(section) else false
    }

    open fun removeSection(key: String): Boolean {
        return removeSectionInternal(sectionKey = key)
    }

    fun clearList() {
        if (viewTypeCount > 0)
            sectionMediator.clearList()
        sectionViewTypeNumbers.clear()
        viewTypeCount = 0
    }

    private fun checkForStubSection(section: Section<*, *, *>? = null) {
        if (section is StubSection) stubSection = section
        else if (stubSection != null) {
            stubSection = null
            clearList()
            notifyItemRemoved(0)
        }
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

        return viewHolder!!
    }

    override fun getItemId(position: Int): Long {
        return sectionMediator.getItemByPosition(position).ID
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
        if (defaultSettings) setDefaultOptimizationSettings()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    protected fun dispatchUpdates(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    private fun addSectionToEnd(section: Section<*, *, *>, sectionKey: String? = null): String {
        val oldList = sectionMediator.getAllItemsList()

        var key = sectionKey
        if (key == null) key = sectionMediator.addSection(section, sectionStateCallback)
        else sectionMediator.addSection(key, section, sectionStateCallback)

        sectionViewTypeNumbers[key] = viewTypeCount
        viewTypeCount += VIEW_TYPE_ITEM


        val newList = sectionMediator.getAllItemsList()

        if (oldList.size != newList.size)
            notifyItemRangeInserted(oldList.size + 1, section.sourceList.size)
        else calculateDiff(oldList, newList)

        return key
    }

    private fun removeSectionInternal(section: Section<*, *, *>? = null, sectionKey: String? = null): Boolean {
        check(section != null && sectionKey == null || sectionKey != null && section == null)

        val oldList = sectionMediator.getAllItemsList()
        val result =
            if (sectionKey == null) sectionMediator.removeSection(section!!) else sectionMediator.removeSection(
                sectionKey
            )
        ioExecutor.execute {
            val newList = sectionMediator.getAllItemsList()

            if (result) {
                val key = sectionKey ?: section!!.key
                sectionViewTypeNumbers.remove(key)
                viewTypeCount -= VIEW_TYPE_ITEM
            }
            calculateDiff(oldList, newList)
        }
        return result
    }

    private fun addSectionsToEnd(list: List<Section<*, *, *>>) {
        var newElementsSize = 0
        val oldList = sectionMediator.getAllItemsList()

        sectionMediator.addSections(list, sectionStateCallback)
        list.forEach {
            newElementsSize += it.sourceList.size
            sectionViewTypeNumbers[it.key] = viewTypeCount
            viewTypeCount += VIEW_TYPE_ITEM
        }

        notifyItemRangeInserted(oldList.size + 1, newElementsSize)
    }

    private fun calculateDiff(
        oldList: List<ItemContainer>,
        newList: List<ItemContainer>,
        isTheSameSection: Boolean = false
    ) {
        ioExecutor.execute {
            diffListUtil.submitLists(oldList, newList)
            diffListUtil.isTheSameSection = isTheSameSection && oldList.size == newList.size

            val result = DiffUtil.calculateDiff(diffListUtil)

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
        require(resourceId != null) { "Missing item resource id" }
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
}