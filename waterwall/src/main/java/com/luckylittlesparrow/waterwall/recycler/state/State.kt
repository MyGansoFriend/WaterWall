package com.luckylittlesparrow.waterwall.recycler.state

import com.luckylittlesparrow.waterwall.recycler.section.Section

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
 * Base state class, that [Section] extend to support [SectionState]
 *
 * @see Section
 * @see SectionState
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class State {

    /**
     * Change state of the section, for every state resource must be provided in order to work
     *
     * @see SectionState
     */
    var state = SectionState.LOADED
        set(state) {
            when (state) {
                SectionState.LOADING -> require(loadingStateRequirements())
                { "Resource ITEM_CONTAINER_ID for 'loading state' should be provided" }

                SectionState.FAILED -> require(failedStateRequirements())
                { "Resource ITEM_CONTAINER_ID for 'failed state' should be provided" }

                SectionState.EMPTY -> require(emptyStateRequirements())
                { "Resource ITEM_CONTAINER_ID for 'empty state' should be provided" }

                SectionState.LOADED -> require(loadedStateRequirements())
                { "Resource ITEM_CONTAINER_ID for 'loaded state' should be provided" }
            }
            val oldState = field
            field = state


            if (field != oldState) {
                if (field == SectionState.LOADED && isEmpty()) return
                sectionStateCallback?.onSectionStateChanged(provideId(), field, oldState)
            }
        }

    abstract fun isEmpty(): Boolean

    internal var sectionStateCallback: SectionStateCallback? = null

    internal abstract fun loadingStateRequirements(): Boolean

    internal abstract fun failedStateRequirements(): Boolean

    internal abstract fun emptyStateRequirements(): Boolean

    internal abstract fun loadedStateRequirements(): Boolean

    internal abstract fun provideId(): String
}

