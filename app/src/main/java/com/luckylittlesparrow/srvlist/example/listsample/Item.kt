package com.luckylittlesparrow.srvlist.example.listsample

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

data class Item(
    val body: String
) : ItemContainer(ItemType.ITEM)