package com.luckylittlesparrow.waterwall.recycler.base

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

import android.view.View

/**
 * An empty ViewHolder without data. Should be used in case, if you have only view to show, otherwise
 * extend [BaseViewHolder]
 *
 * @see BaseViewHolder<T>
 *
 * @param itemView view item
 *
 * @author Andrei Gusev
 * @since  1.0
 */
open class EmptyViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView)