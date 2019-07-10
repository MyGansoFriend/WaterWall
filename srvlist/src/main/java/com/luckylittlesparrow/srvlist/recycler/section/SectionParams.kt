package com.luckylittlesparrow.srvlist.recycler.section

import androidx.annotation.LayoutRes

/**
 * Class used as constructor parameters of [Section].
 */
class SectionParams private constructor(builder: Builder) {
    @LayoutRes
    val itemResourceId: Int?
    @LayoutRes
    val headerResourceId: Int?
    @LayoutRes
    val footerResourceId: Int?
    @LayoutRes
    val loadingResourceId: Int?
    @LayoutRes
    val failedResourceId: Int?
    @LayoutRes
    val emptyResourceId: Int?

    val supportFilterHeaderFunction: Boolean

    val supportExpansionFunction: Boolean

    val supportShowMoreFunction: Boolean

    val isExpandedByDefault: Boolean

    val collapsedItemCount: Int

    init {
        itemResourceId = builder.itemResourceId
        headerResourceId = builder.headerResourceId
        footerResourceId = builder.footerResourceId
        loadingResourceId = builder.loadingResourceId
        failedResourceId = builder.failedResourceId
        emptyResourceId = builder.emptyResourceId
        supportFilterHeaderFunction = builder.supportFilterHeaderFunction
        supportExpansionFunction = builder.supportExpansionFunction
        isExpandedByDefault = builder.isExpandedByDefault
        supportShowMoreFunction = builder.supportShowMoreFunction
        collapsedItemCount = builder.collapsedItemCount
    }

    class Builder {
        @LayoutRes
        var itemResourceId: Int? = null
            private set
        @LayoutRes
        var headerResourceId: Int? = null
            private set
        @LayoutRes
        var footerResourceId: Int? = null
            private set
        @LayoutRes
        var loadingResourceId: Int? = null
            private set
        @LayoutRes
        var failedResourceId: Int? = null
            private set
        @LayoutRes
        var emptyResourceId: Int? = null
            private set

        var supportFilterHeaderFunction: Boolean = false
            private set

        var supportExpansionFunction: Boolean = false
            private set

        var supportShowMoreFunction: Boolean = false
            private set

        var isExpandedByDefault: Boolean = true
            private set

        var collapsedItemCount: Int = 4
            private set


        /**
         * Set layout resource for Section's items.
         *
         * @param itemResourceId layout resource for Section's items
         * @return this builder
         */
        fun itemResourceId(@LayoutRes itemResourceId: Int): Builder {
            this.itemResourceId = itemResourceId

            return this
        }

        /**
         * Set layout resource for Section's header.
         *
         * @param headerResourceId layout resource for Section's header
         * @return this builder
         */
        fun headerResourceId(@LayoutRes headerResourceId: Int): Builder {
            this.headerResourceId = headerResourceId

            return this
        }

        /**
         * Set layout resource for Section's footer.
         *
         * @param footerResourceId layout resource for Section's footer
         * @return this builder
         */
        fun footerResourceId(@LayoutRes footerResourceId: Int): Builder {
            this.footerResourceId = footerResourceId
            return this
        }

        /**
         * Set layout resource for Section's loading state.
         *
         * @param loadingResourceId layout resource for Section's loading state
         * @return this builder
         */
        fun loadingResourceId(@LayoutRes loadingResourceId: Int): Builder {
            this.loadingResourceId = loadingResourceId
            return this
        }

        /**
         * Set layout resource for Section's failed state.
         *
         * @param failedResourceId layout resource for Section's failed state
         * @return this builder
         */
        fun failedResourceId(@LayoutRes failedResourceId: Int): Builder {
            this.failedResourceId = failedResourceId
            return this
        }

        /**
         * Set layout resource for Section's empty state.
         *
         * @param emptyResourceId layout resource for Section's empty state
         * @return this builder
         */
        fun emptyResourceId(@LayoutRes emptyResourceId: Int): Builder {
            this.emptyResourceId = emptyResourceId
            return this
        }


        /**
         * Support header filter
         *
         * @param supportFilterHeader
         * @return this builder
         */
        fun supportFilterHeader(supportFilterHeader: Boolean): Builder {
            this.supportFilterHeaderFunction = supportFilterHeader
            return this
        }

        /**
         * Support expansion function
         *
         * @param supportExpansion
         * @return this builder
         */
        fun supportExpansion(supportExpansion: Boolean): Builder {
            this.supportExpansionFunction = supportExpansion
            return this
        }

        /**
         * Should Section be opened by default
         *
         * @param isExpandedByDefault
         * @return this builder
         */
        fun isExpandedByDefault(isExpandedByDefault: Boolean): Builder {
            this.isExpandedByDefault = isExpandedByDefault
            return this
        }

        /**
         * Support show more function
         *
         * @param supportShowMoreFunction
         * @return this builder
         */
        fun supportShowMoreFunction(supportShowMoreFunction: Boolean): Builder {
            this.supportShowMoreFunction = supportShowMoreFunction
            return this
        }

        /**
         * In case of [supportShowMoreFunction = true], set amount of items to be showed
         * in collapsed mode
         *
         * @see supportShowMoreFunction
         *
         * @param collapsedItemCount
         * @return this builder
         */
        fun collapsedItemCount(collapsedItemCount: Int): Builder {
            this.collapsedItemCount = collapsedItemCount
            return this
        }

        /**
         * Build an instance of SectionParameters.
         *
         * @return an instance of SectionParameters
         */
        fun build(): SectionParams {
            return SectionParams(this)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }
}