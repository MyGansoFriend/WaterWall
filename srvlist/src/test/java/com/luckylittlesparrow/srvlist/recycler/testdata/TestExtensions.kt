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

package com.luckylittlesparrow.srvlist.recycler.testdata

import java.lang.reflect.Field

fun Any.getField(fieldName: String): Field {
    val field = this::class.java.getDeclaredField(fieldName)
    field.isAccessible = true
    return field
}


const val itemResourceId = 1
const val headerResourceId = 2
const val footerResourceId = 3
const val failedResourceId = 4
const val loadingResourceId = 5
const val emptyResourceId = 6