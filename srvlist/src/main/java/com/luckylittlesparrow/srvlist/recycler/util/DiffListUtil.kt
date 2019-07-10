package com.luckylittlesparrow.srvlist.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.srvlist.recycler.base.SectionMediator
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

internal class DiffListUtil(private val sectionMediator: SectionMediator) : DiffUtil.Callback() {
    private lateinit var oldList: List<ItemContainer>
    private lateinit var newList: List<ItemContainer>
    var isTheSameSection = false


    fun submitLists(oldList: List<ItemContainer>, newList: List<ItemContainer>) {
        this.newList = newList
        this.oldList = oldList
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldElement = oldList[oldItemPosition]
        val newElement = newList[newItemPosition]

        if (!oldElement.isTheSameType(newElement.itemType)) return false

        val section =
            if (isTheSameSection) sectionMediator.getSectionByItem(newElement)
            else sectionMediator.getSectionByItems(oldElement, newElement)

        return section?.areItemsTheSame(
            oldElement,
            newElement
        ) ?: false
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldElement = oldList[oldItemPosition]
        val newElement = newList[newItemPosition]

        if (!oldElement.isTheSameType(newElement.itemType)) return false

        val section =
            if (isTheSameSection) sectionMediator.getSectionByItem(newElement)
            else sectionMediator.getSectionByItems(oldElement, newElement)
        return section?.areContentsTheSame(
            oldElement,
            newElement
        ) ?: false
    }
}