package com.luckylittlesparrow.waterwall.example.expand

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

data class ExpandableHeader(val title: String) : ItemContainer(ItemType.HEADER)