package com.luckylittlesparrow.waterwall.example.sectionedlist

import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer

data class EventHeader(val title: String) : ItemContainer(ItemType.HEADER)