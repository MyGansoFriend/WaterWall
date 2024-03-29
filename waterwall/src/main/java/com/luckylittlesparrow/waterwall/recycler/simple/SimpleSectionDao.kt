package com.luckylittlesparrow.waterwall.recycler.simple

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

import com.luckylittlesparrow.waterwall.recycler.base.SectionDao
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.StubItem
import com.luckylittlesparrow.waterwall.recycler.state.SectionState

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal open class SimpleSectionDao<H, I, F>(override val section: Section<H, I, F>) :
    SectionDao<H, I, F> {

    override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
        return when (oldItem.itemType) {
            ItemContainer.ItemType.ITEM -> section.getDiffUtilItemCallback().areContentsTheSame(
                oldItem,
                newItem
            )
            ItemContainer.ItemType.HEADER -> section.getDiffUtilItemCallback().areHeadersContentsTheSame(
                oldItem,
                newItem
            )
            ItemContainer.ItemType.FOOTER -> section.getDiffUtilItemCallback().areFootersContentsTheSame(
                oldItem,
                newItem
            )
        }
    }

    override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
        return when (oldItem.itemType) {
            ItemContainer.ItemType.ITEM -> section.getDiffUtilItemCallback().areItemsTheSame(
                oldItem,
                newItem
            )
            ItemContainer.ItemType.HEADER -> section.getDiffUtilItemCallback().areHeadersTheSame(
                oldItem,
                newItem
            )
            ItemContainer.ItemType.FOOTER -> section.getDiffUtilItemCallback().areFootersTheSame(
                oldItem,
                newItem
            )

        }
    }

    override fun getItem(itemType: ItemContainer.ItemType, position: Int): ItemContainer {
        return when (itemType) {
            ItemContainer.ItemType.HEADER -> section.sourceList.first()
            ItemContainer.ItemType.FOOTER -> section.sourceList.last()
            ItemContainer.ItemType.ITEM -> {
                check(position != -1); getContentItem(position)
            }
        }
    }

    override fun getVisibleItemsList(): List<ItemContainer> {
        if (!section.isVisible) return emptyList()
        return if (section.isExpanded && section.isNotEmpty()) {
            if (state() != SectionState.LOADED) {
                return section.sourceList.subList(0, 2)
            } else section.sourceList
        } else {
            listOf(section.sourceList.first())
        }
    }

    override fun getContentItem(position: Int): ItemContainer {
        return section.sourceList[position]
    }

    override fun hasItems(item: ItemContainer, newItem: ItemContainer): Boolean {
        return hasItem(item) && hasItem(newItem)
    }

    override fun hasItem(item: ItemContainer): Boolean {
        return section.sourceList.any { it === item }
    }

    override fun getSectionList() = section.sourceList

    override fun sectionCurrentSize(): Int = section.currentSize()

    override fun sectionOriginalSize(): Int = section.originalSize()

    override fun hasHeader(): Boolean = section.hasHeader

    override fun hasFooter(): Boolean = section.hasFooter

    override fun isVisible(): Boolean = section.isVisible

    override fun isEmpty(): Boolean = section.sourceList.first() is StubItem

    override fun state(): SectionState = section.state

    override fun key(): String? = section.sectionKey

    override fun getFooterIndex(): Int = section.sourceList.size - 1
}