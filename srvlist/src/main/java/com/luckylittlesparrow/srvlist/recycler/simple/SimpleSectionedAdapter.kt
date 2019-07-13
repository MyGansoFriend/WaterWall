package com.luckylittlesparrow.srvlist.recycler.simple

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

import com.luckylittlesparrow.srvlist.recycler.base.BaseListAdapter
import com.luckylittlesparrow.srvlist.recycler.base.SectionItemDecoration
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.sticky.StickyHeaderDecoration

/**
 * Default version of adapter, should be used in all cases except filter,
 * for usage with filter see [FilterableSectionedAdapter]
 *
 * Supported functionality:
 *           States
 *           Decorations
 *           Expandable sections
 *           Sticky headers
 *           Show more, show less
 *
 * @see FilterableSectionedAdapter
 * @see Section<H,I,F>
 * @see SectionItemDecoration
 * @see StickyHeaderDecoration
 * @see supportStickyHeader
 *
 * @author Andrei Gusev
 * @since  1.0
 */
class SimpleSectionedAdapter : BaseListAdapter() {

    init {
        sectionMediator = SimpleSectionMediator()
    }
}