package com.luckylittlesparrow.srvlist.recycler.section

import java.util.*

abstract class ItemContainer(val itemType: ItemType) {

    val ID= UUID.randomUUID().mostSignificantBits

    enum class ItemType {
        HEADER, FOOTER, ITEM
    }

    fun isHeader() = itemType == ItemType.HEADER

    fun isNotHeader() = itemType != ItemType.HEADER

    fun isFooter() = itemType == ItemType.FOOTER

    fun isNotFooter() = itemType != ItemType.FOOTER

    fun isItem() = itemType == ItemType.ITEM

    fun isNotItem() = itemType != ItemType.ITEM

    fun isTheSameType(type: ItemType) = itemType == type

}