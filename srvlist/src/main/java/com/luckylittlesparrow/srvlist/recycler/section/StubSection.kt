package com.luckylittlesparrow.srvlist.recycler.section

import android.view.View
import com.luckylittlesparrow.srvlist.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSection
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

/**
 * Section for usage in case, when it's necessary to show state for all adapter (e.g match parent layout),
 * not for each section separately
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

    override fun addItems(itemBundle: ItemBundle) {
    }

}