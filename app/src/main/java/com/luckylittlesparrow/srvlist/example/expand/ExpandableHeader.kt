package com.luckylittlesparrow.srvlist.example.expand

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

data class ExpandableHeader(val title: String) : ItemContainer(ItemType.HEADER)