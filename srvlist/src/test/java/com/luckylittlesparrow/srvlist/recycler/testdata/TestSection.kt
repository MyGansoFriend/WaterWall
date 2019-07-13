package com.luckylittlesparrow.srvlist.recycler.testdata

import android.view.View
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.base.EmptyViewHolder
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

open class TestSection(
    headerItem: TestHeader? = null,
    contentItems: List<TestItem>? = null,
    footerItem: TestFooter? = null
) : Section<TestHeader, TestItem, TestFooter>(headerItem, contentItems, footerItem,
    itemClickListener = { item: ItemContainer -> },
    headerClickListener = { item: ItemContainer -> },
    footerClickListener = { item: ItemContainer -> }) {
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