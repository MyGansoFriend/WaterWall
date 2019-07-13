package com.luckylittlesparrow.waterwall.example.simplelist

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

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