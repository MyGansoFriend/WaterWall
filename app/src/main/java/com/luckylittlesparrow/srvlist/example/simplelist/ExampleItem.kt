package com.luckylittlesparrow.srvlist.example.simplelist

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

data class ExampleItem(
    val type: ExampleType,
    val body: String
) : ItemContainer(ItemType.ITEM) {
    enum class ExampleType {
        FILTER,
        STATES,
        EXPAND,
        STUB,
        DEFAULT
    }
}