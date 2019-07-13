package com.luckylittlesparrow.srvlist.recycler.filterable

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

import com.luckylittlesparrow.srvlist.recycler.base.BaseSectionMediator
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import java.util.*

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal class FilterableSectionMediator : BaseSectionMediator() {

    @Suppress("UNCHECKED_CAST")
    override fun addSection(
        key: String,
        section: Section<*, *, *>,
        sectionStateCallback: SectionStateCallback
    ): String {
        section.key = key
        section.sectionStateCallback = sectionStateCallback
        sections[key] = FilterableSectionDao(section as FilterableSection<Nothing, Nothing, Nothing>)
        return key
    }

    @Suppress("UNCHECKED_CAST")
    override fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback) {
        list.forEach {
            val key = UUID.randomUUID().toString()
            it.key = key
            it.sectionStateCallback = sectionStateCallback
            sections[key] = FilterableSectionDao(it as FilterableSection<Nothing, Nothing, Nothing>)
        }
    }
}