package com.luckylittlesparrow.waterwall.recycler.util

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

import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.waterwall.recycler.base.SectionMediator
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import java.util.concurrent.CopyOnWriteArrayList

internal class DiffListUtilBySections(private val sectionMediator: SectionMediator) : DiffUtil.Callback() {

    private lateinit var oldList: List<ItemContainer>

    private lateinit var newList: List<ItemContainer>

    var isTheSameSection = false


    fun submitLists(oldList: List<ItemContainer>, newList: List<ItemContainer>) {
        this.newList = CopyOnWriteArrayList(newList)
        this.oldList = CopyOnWriteArrayList(oldList)
    }


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList.size < oldItemPosition || newList.size < newItemPosition) return false

        val oldElement = oldList[oldItemPosition]
        val newElement = newList[newItemPosition]

        if (!oldElement.isTheSameType(newElement.itemType)) return false

        val section =
            if (isTheSameSection) sectionMediator.getSectionByItem(newElement)
            else sectionMediator.getSectionByItems(oldElement, newElement)

        return section?.areItemsTheSame(
            oldElement,
            newElement
        ) ?: false
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList.size < oldItemPosition || newList.size < newItemPosition) return false

        val oldElement = oldList[oldItemPosition]
        val newElement = newList[newItemPosition]

        if (!oldElement.isTheSameType(newElement.itemType)) return false

        val section =
            if (isTheSameSection) sectionMediator.getSectionByItem(newElement)
            else sectionMediator.getSectionByItems(oldElement, newElement)
        return section?.areContentsTheSame(
            oldElement,
            newElement
        ) ?: false
    }
}