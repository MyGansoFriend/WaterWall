package com.luckylittlesparrow.waterwall.recycler.testdata

import android.view.View
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

open class FilterTestSection(
    headerItem: TestHeader? = null,
    contentItems: List<TestItem>? = null,
    footerItem: TestFooter? = null,
    headerClickListener: (ItemContainer) -> Unit = {},
    itemClickListener: (ItemContainer) -> Unit = {},
    footerClickListener: (ItemContainer) -> Unit = {}
) : FilterableSection<TestHeader, TestItem, TestFooter>(
    headerItem, contentItems, footerItem,
    itemClickListener = itemClickListener,
    headerClickListener = headerClickListener,
    footerClickListener = footerClickListener
) {

    override fun itemFilter(search: String, item: ItemContainer): Boolean {
        return (item as TestItem).name.contains(search, true)
    }

    override fun headerFilter(search: String, item: ItemContainer): Boolean {
        return true
    }
    override fun getSectionParams(): SectionParams {
        return SectionParams.builder().build()
    }

    override fun getDiffUtilItemCallback(): DiffUtilItemCallback {
        return object : DiffUtilItemCallback() {
            override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
                return false
            }
        }
    }

    override fun getItemViewHolder(view: View): BaseViewHolder<TestItem> {
        return EmptyViewHolder(view)
    }

}