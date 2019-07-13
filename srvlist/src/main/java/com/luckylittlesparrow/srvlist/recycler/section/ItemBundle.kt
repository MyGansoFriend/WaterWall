package com.luckylittlesparrow.srvlist.recycler.section

import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSection

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

/**
 * Class container for new data submission to the Section
 *
 * @see Section<H,I,F>
 * @see FilterableSection<H,I,F>
 *
 * @author Andrei Gusev
 * @since  1.0
 */
data class ItemBundle(
    val headerItem: ItemContainer? = null,
    val contentItems: List<ItemContainer>? = null,
    val footerItem: ItemContainer? = null
) {
    /**
     * Check if container is empty, there is no purpose to submit empty container, nothing will happened
     *
     * @return [true] if container is empty, otherwise [false]
     */
    fun isEmpty() = headerItem == null && contentItems == null && footerItem == null
}