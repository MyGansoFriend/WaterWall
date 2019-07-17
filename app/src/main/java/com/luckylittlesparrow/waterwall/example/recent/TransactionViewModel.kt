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

package com.luckylittlesparrow.waterwall.example.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.recycler.section.ItemBundle
import kotlin.random.Random

class TransactionViewModel {

    private val _transactions = MutableLiveData<List<ItemBundle>>()

    internal val transactions: LiveData<List<ItemBundle>> = _transactions

    fun onStart() {
        _transactions.postValue(getTransactions())
    }

    private fun getTransactions(): List<ItemBundle> {
        val list = ArrayList<ItemBundle>()

        list.add(ItemBundle(TransactionHeader("TODAY"), getItems(3)))

        list.add(ItemBundle(TransactionHeader("YESTERDAY"), getItems(10)))

        list.add(ItemBundle(TransactionHeader("19.02.10"), getItems(15)))

        list.add(ItemBundle(TransactionHeader("20.02.10"), getItems(20)))

        return list
    }

    private fun getItems(n: Int): List<TransactionItem> {
        val list = ArrayList<TransactionItem>()
        for (i in 0 until n) {
            list.add(
                TransactionItem(
                    "$i", Random(100).toString(),
                    R.drawable.ic_error_black_24dp,
                    R.drawable.ic_done_black_24dp
                )
            )
        }
        return list
    }
}