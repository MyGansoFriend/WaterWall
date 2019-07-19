package com.luckylittlesparrow.waterwall.recycler.util

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

import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

/**
 * Base util class for comparing items, ensure that you implements comparison correctly,
 * otherwise items in Recycler View will be updated
 *
 * @see DiffUtil.ItemCallback
 * @see Object.equals
 *
 * @author Andrei Gusev
 * @since  1.0
 */
abstract class DiffUtilItemCallback : DiffUtil.ItemCallback<ItemContainer>() {

    /**
     * Called to check whether two content objects represent the same item.
     *
     *
     * For example, if your content items have unique ids, this method should check their id equality.
     *
     *
     * Note: `null` items in the list are assumed to be the same as another `null`
     * item and are assumed to not be the same as a non-`null` item. This callback will
     * not be invoked for either of those cases.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two content items represent the same object or false if they are different.
     *
     */
    override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem

    /**
     * Called to check whether two content items have the same data.
     *
     *
     * This information is used to detect if the contents of an item have changed.
     *
     * This method to check equality instead of [Object.equals] so that you can
     * change its behavior depending on your UI.
     *
     * This method is called only if [.areItemsTheSame] returns `true` for
     * these items.
     *
     *
     * Note: Two `null` items are assumed to represent the same contents. This callback
     * will not be invoked for this case.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the content items are the same or false if they are different.
     *
     */
    override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem

    /**
     * Called to check whether two header objects represent the same item.
     *
     *
     * For example, if your header items have unique ids, this method should check their id equality.
     *
     *
     * Note: `null` items in the list are assumed to be the same as another `null`
     * item and are assumed to not be the same as a non-`null` item. This callback will
     * not be invoked for either of those cases.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two header items represent the same object or false if they are different.
     *
     */
    open fun areHeadersTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem

    /**
     * Called to check whether two header items have the same data.
     *
     *
     * This information is used to detect if the contents of an item have changed.
     *
     * This method to check equality instead of [Object.equals] so that you can
     * change its behavior depending on your UI.
     *
     * This method is called only if [.areItemsTheSame] returns `true` for
     * these items.
     *
     *
     * Note: Two `null` items are assumed to represent the same contents. This callback
     * will not be invoked for this case.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the header items are the same or false if they are different.
     *
     */
    open fun areHeadersContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem

    /**
     * Called to check whether two footer objects represent the same footer item.
     *
     *
     * For example, if your footer items have unique ids, this method should check their id equality.
     *
     *
     * Note: `null` items in the list are assumed to be the same as another `null`
     * item and are assumed to not be the same as a non-`null` item. This callback will
     * not be invoked for either of those cases.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two footer items represent the same object or false if they are different.
     *
     */
    open fun areFootersTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem

    /**
     * Called to check whether two footer items have the same data.
     *
     *
     * This information is used to detect if the contents of an item have changed.
     *
     *
     * This method to check equality instead of [Object.equals] so that you can
     * change its behavior depending on your UI.
     *
     * This method is called only if [.areItemsTheSame] returns `true` for
     * these items.
     *
     *
     * Note: Two `null` items are assumed to represent the same contents. This callback
     * will not be invoked for this case.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the footer items are the same or false if they are different.
     *
     */
    open fun areFootersContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean = oldItem == newItem
}