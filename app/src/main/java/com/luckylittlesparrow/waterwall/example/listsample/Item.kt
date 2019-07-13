package com.luckylittlesparrow.waterwall.example.listsample

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

data class Item(
    val body: String
) : ItemContainer(ItemType.ITEM)