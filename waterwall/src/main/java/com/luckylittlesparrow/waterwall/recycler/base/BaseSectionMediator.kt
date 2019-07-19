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

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionDao
import com.luckylittlesparrow.waterwall.recycler.state.SectionStateCallback
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal abstract class BaseSectionMediator : SectionMediator {
    protected val sections: MutableMap<String, SectionDao<Nothing, Nothing, Nothing>> = LinkedHashMap()

    override fun stateChanged() {
        visibleItemCount = -1
    }

    private var visibleItemCount = -1

    override fun attachSectionStateCallback(sectionStateCallback: SectionStateCallback) {
        sections.forEach {
            it.value.section.sectionStateCallback = sectionStateCallback
        }
    }

    override fun detachSectionStateCallback() {
        sections.forEach {
            it.value.section.sectionStateCallback = null
        }
    }

    override fun getSectionList(): MutableMap<String, SectionDao<Nothing, Nothing, Nothing>> = sections

    @Suppress("UNCHECKED_CAST")
    override fun addSection(
        key: String,
        section: Section<*, *, *>,
        sectionStateCallback: SectionStateCallback
    ): String {
        stateChanged()
        section.sectionKey = key
        section.sectionStateCallback = sectionStateCallback
        sections[key] = SimpleSectionDao(section as Section<Nothing, Nothing, Nothing>)
        return key
    }

    override fun addSection(section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String {
        return addSection(
            section.sectionKey ?: UUID.randomUUID().toString(),
            section,
            sectionStateCallback
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback) {
        stateChanged()
        list.forEach {
            val key = it.sectionKey ?: UUID.randomUUID().toString()
            it.sectionKey = key
            it.sectionStateCallback = sectionStateCallback
            sections[key] = SimpleSectionDao(it as Section<Nothing, Nothing, Nothing>)
        }
    }

    override fun removeSection(section: Section<*, *, *>): Boolean {
        for (sectionDao in sections) {
            if (sectionDao.value.section == section) {
                section.sectionStateCallback = null
                sections.remove(sectionDao.key)
                stateChanged()
                return true
            }
        }
        return false
    }

    override fun removeSection(sectionKey: String): Boolean {
        stateChanged()
        val sectionDao = sections.remove(sectionKey)
        sectionDao?.section?.sectionStateCallback = null
        return sectionDao != null
    }

    override fun clearList() {
        stateChanged()
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

    override fun getVisibleItemsList(): List<ItemContainer> {
        val list = ArrayList<ItemContainer>()
        for (sectionDao in sections) {
            list.addAll(sectionDao.value.getVisibleItemsList())
        }
        return list
    }

    override fun getSectionByKey(key: String): SectionDao<Nothing, Nothing, Nothing> {
        return sections[key] ?: throw NoSuchElementException()
    }

    override fun containsSection(key: String): Boolean {
        return sections.containsKey(key)
    }

    override fun containsSection(section: Section<*, *, *>): Boolean {
        if (section.sectionKey == null) return false

        sections.forEach {
            if (it.value.section.sectionKey == section.sectionKey) return true
        }
        return false
    }

    override fun getVisibleItemCount(): Int {
        if (visibleItemCount != -1) return visibleItemCount
        var count = 0

        sections.forEach {
            if (it.value.section.isVisible) count += it.value.sectionCurrentSize()
        }

        visibleItemCount = count
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

            if (sectionDao.section === section) return currentPos + (if (section.hasHeader) 1 else 0)

            val sectionTotal = sectionDao.sectionCurrentSize()

            currentPos += sectionTotal
        }

        throw IndexOutOfBoundsException("Invalid section")
    }

    override fun getSectionPosition(sectionKey: String): Int {
        var currentPos = 0

        for ((_, sectionDao) in sections) {

            if (!sectionDao.section.isVisible) continue

            if (sectionDao.section.sectionKey === sectionKey) return currentPos

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
                if (sectionDao.isEmpty()) return sectionDao.section.sourceList.first()
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
