package com.luckylittlesparrow.srvlist.recycler.state

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

