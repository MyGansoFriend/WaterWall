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

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

internal abstract class BaseSectionMediator : SectionMediator {
    protected val sections: MutableMap<String, SectionDao<Nothing, Nothing, Nothing>> = LinkedHashMap()

    override fun getSectionList(): MutableMap<String, SectionDao<Nothing, Nothing, Nothing>> = sections

    @Suppress("UNCHECKED_CAST")
    override fun addSection(
        key: String,
        section: Section<*, *, *>,
        sectionStateCallback: SectionStateCallback
    ): String {
        section.key = key
        section.sectionStateCallback = sectionStateCallback
        sections[key] = SimpleSectionDao(section as Section<Nothing, Nothing, Nothing>)
        return key
    }

    override fun addSection(section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String {
        return addSection(
            UUID.randomUUID().toString(),
            section,
            sectionStateCallback
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback) {
        list.forEach {
            val key = UUID.randomUUID().toString()
            it.key = key
            it.sectionStateCallback = sectionStateCallback
            sections[key] = SimpleSectionDao(it as Section<Nothing, Nothing, Nothing>)
        }
    }

    override fun removeSection(section: Section<*, *, *>): Boolean {
        for (sectionDao in sections) {
            if (sectionDao.value.section == section) {
                section.sectionStateCallback = null
                sections.remove(sectionDao.key)
                return true
            }
        }
        return false
    }

    override fun removeSection(sectionKey: String): Boolean {
        val sectionDao = sections.remove(sectionKey)
        sectionDao?.section?.sectionStateCallback = null
        return sectionDao != null
    }

    override fun clearList() {
        for (sectionDao in sections) {
            sectionDao.value.section.sectionStateCallback = null
        }
        sections.clear()
    }

    override fun getAllItemsList(): List<ItemContainer> {
        val list = ArrayList<ItemContainer>()
        for (sectionDao in sections) {
            list.addAll(sectionDao.value.section.sourceList)
        }
        return list
    }

    override fun getSectionByKey(key: String): SectionDao<Nothing, Nothing, Nothing> {
        sections.forEach {
            if (it.value.section.key == key) return it.value
        }
        throw NoSuchElementException()
    }

    override fun containsSection(key: String): Boolean {
        sections.forEach {
            if (it.value.section.key == key) return true
        }
        return false
    }

    override fun containsSection(section: Section<*, *, *>): Boolean {
        try {
            section.key
        } catch (e: UninitializedPropertyAccessException) {
            return false
        }

        sections.forEach {
            if (it.value.section.key == section.key) return true
        }
        return false
    }

    override fun getVisibleItemCount(): Int {
        var count = 0

        sections.forEach {
            if (it.value.section.isVisible) count += it.value.sectionCurrentSize()
        }

        return count
    }

    override fun getSectionByItemPosition(position: Int): SectionDao<Nothing, Nothing, Nothing> {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            val sectionTotal = sectionDao.sectionCurrentSize()

            if (position >= currentPos && position <= currentPos + sectionTotal - 1) return sectionDao

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid position")
    }

    override fun getSectionPosition(section: Section<*, *, *>): Int {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            if (sectionDao.section === section) return currentPos + (if (section.hasHeader()) 1 else 0)

            val sectionTotal = sectionDao.sectionCurrentSize()

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid section")
    }

    override fun getSectionPosition(sectionKey: String): Int {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            if (sectionDao.section.key === sectionKey) return currentPos

            val sectionTotal = sectionDao.sectionCurrentSize()

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid section")
    }

    override fun getPositionInSection(position: Int): Int {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            val sectionTotal = sectionDao.sectionCurrentSize()

            if (position >= currentPos && position <= currentPos + sectionTotal - 1) {
                return position - currentPos
            }

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid position")
    }

    override fun getItemByPosition(position: Int): ItemContainer {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            val sectionTotal = sectionDao.sectionCurrentSize()

            if (position >= currentPos && position <= currentPos + sectionTotal - 1) {
                return sectionDao.getContentItem(position - currentPos)
            }

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid position")
    }

    override fun getSectionByItems(
        item: ItemContainer,
        newItem: ItemContainer
    ): SectionDao<Nothing, Nothing, Nothing>? {
        for (sectionDao in sections) {
            if (sectionDao.value.hasItems(item, newItem)) return sectionDao.value
        }
        return null
    }

    override fun getSectionByItem(
        item: ItemContainer
    ): SectionDao<Nothing, Nothing, Nothing>? {
        for (sectionDao in sections) {
            if (sectionDao.value.hasItem(item)) return sectionDao.value
        }
        return null
    }
}
