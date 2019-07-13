package com.luckylittlesparrow.srvlist.recycler.section

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

import androidx.annotation.LayoutRes
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSection

/**
 * Class used as constructor parameters of [Section].
 *
 * @see Section<H,I,F>
 * @see FilterableSection<H,I,F>
 *
 * @author Andrei Gusev
 * @since  1.0
 */
class SectionParams private constructor(builder: Builder) {

    /**
     * Layout resource for content items
     */
    @LayoutRes
    val itemResourceId: Int?

    /**
     * Layout resource for header item
     */
    @LayoutRes
    val headerResourceId: Int?

    /**
     * Layout resource for footer item
     */
    @LayoutRes
    val footerResourceId: Int?

    /**
     * Layout resource for loading item
     */
    @LayoutRes
    val loadingResourceId: Int?

    /**
     * Layout resource for failed item
     */
    @LayoutRes
    val failedResourceId: Int?

    /**
     * Layout resource for empty item
     */
    @LayoutRes
    val emptyResourceId: Int?

    /**
     * Support header filter in [FilterableSection]
     *
     * @see FilterableSection
     */
    val supportFilterHeaderFunction: Boolean

    /**
     * Support expand/collapse function
     */
    val supportExpandFunction: Boolean

    /**
     * Support showMore/showLess function
     */
    val supportShowMoreFunction: Boolean

    /**
     * Is Section expanded by default
     */
    val isExpandedByDefault: Boolean

    /**
     * Minimum collapsed items to be shown if section support showMore/showLess functionality.
     */
    val collapsedItemCount: Int

    init {
        itemResourceId = builder.itemResourceId
        headerResourceId = builder.headerResourceId
        footerResourceId = builder.footerResourceId
        loadingResourceId = builder.loadingResourceId
        failedResourceId = builder.failedResourceId
        emptyResourceId = builder.emptyResourceId
        supportFilterHeaderFunction = builder.supportFilterHeaderFunction
        supportExpandFunction = builder.supportExpandFunction
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

        var supportExpandFunction: Boolean = false
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
         * Support expand/collapse function
         *
         * @param supportExpandFunction
         * @return this builder
         */
        fun supportExpandFunction(supportExpandFunction: Boolean): Builder {
            this.supportExpandFunction = supportExpandFunction
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
         * Support showMore/showLess function
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