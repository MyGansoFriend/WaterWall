package com.luckylittlesparrow.srvlist.recycler.state

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

abstract class State {

    internal var sectionStateCallback: SectionStateCallback? = null

    var state = SectionState.LOADED
        set(state) {
            when (state) {
                SectionState.LOADING -> require(loadingStateRequirements())
                { "Resource id for 'loading state' should be provided" }

                SectionState.FAILED -> require(failedStateRequirements())
                { "Resource id for 'failed state' should be provided" }

                SectionState.EMPTY -> require(emptyStateRequirements())
                { "Resource id for 'empty state' should be provided" }

                SectionState.LOADED -> require(loadedStateRequirements())
                { "Resource id for 'loaded state' should be provided" }
            }
            val oldState = field
            field = state

            sectionStateCallback?.onSectionStateChanged(provideId(), field, oldState)
        }

    protected abstract fun loadingStateRequirements(): Boolean

    protected abstract fun failedStateRequirements(): Boolean

    protected abstract fun emptyStateRequirements(): Boolean

    protected abstract fun loadedStateRequirements(): Boolean

    protected abstract fun provideId(): String
}

