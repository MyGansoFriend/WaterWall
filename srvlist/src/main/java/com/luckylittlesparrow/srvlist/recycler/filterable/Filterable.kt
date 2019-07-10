package com.luckylittlesparrow.srvlist.recycler.filterable

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

interface Filterable {
    fun filter(search: CharSequence): Pair<List<ItemContainer>, List<ItemContainer>>?
}