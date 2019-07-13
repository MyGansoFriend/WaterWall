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

package com.luckylittlesparrow.waterwall.recycler.mediator

import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionMediator
import com.luckylittlesparrow.waterwall.recycler.state.SectionStateCallback
import com.luckylittlesparrow.waterwall.recycler.testdata.SectionFactory
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterableSectionMediatorTest {

    private val sectionMediator = FilterableSectionMediator()
    private val sectionStateCallback: SectionStateCallback = mock()

    @Test
    fun addSectionWithKey() {
        val key = "key"
        val section = SectionFactory.getFilterableSection()
        val result = sectionMediator.addSection(key, section, sectionStateCallback)

        assertEquals(key, result)
        assertEquals(section, sectionMediator.getSectionByKey(key).section)
        assertEquals(sectionStateCallback, sectionMediator.getSectionByKey(key).section.sectionStateCallback)
    }

    @Test
    fun addSections() {
        val sectionList = SectionFactory.getFilterableSectionList()
        sectionMediator.addSections(sectionList, sectionStateCallback)

        assertEquals(sectionList.size, sectionMediator.getSectionList().size)

        val resultList = sectionMediator.getSectionList()

        for (i in 0 until sectionList.size) {
            assertTrue(resultList.containsKey(sectionList[i].key))
        }
    }
}