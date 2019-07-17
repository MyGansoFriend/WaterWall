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

package com.luckylittlesparrow.waterwall.example.recent

import android.view.View
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback


class TransactionSection(
    header: ItemContainer?,
    events: List<ItemContainer>?
) :
    Section<TransactionHeader, TransactionItem, Void>(headerItem = header, contentItems = events) {

    override fun getItemViewHolder(view: View): BaseViewHolder<TransactionItem> {
        return TransactionViewHolder(view)
    }

    override fun getHeaderViewHolder(view: View): BaseViewHolder<TransactionHeader> {
        return TransactionHeaderViewHolder(view)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .itemResourceId(R.layout.transactiom_view_item)
            .headerResourceId(R.layout.transaction_header_view_item)
            .build()
    }

    override fun getDiffUtilItemCallback(): DiffUtilItemCallback {
        return object : DiffUtilItemCallback() {
            override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
                return (oldItem as TransactionItem) == (newItem as TransactionItem)
            }

            override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer) =
                oldItem.let {
                    oldItem as TransactionItem
                    newItem as TransactionItem

                    return@let newItem.value == oldItem.value && newItem.message == oldItem.message

                }
        }
    }
}