package com.luckylittlesparrow.srvlist.recycler.testdata

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer


object TestItemsFactory {

    val header = TestHeader("header")
    val footer = TestFooter("footer")

    fun getFullItems() {

    }

    fun getNames(): ArrayList<TestItem> {
        val cards = ArrayList<TestItem>()
        cards.add(
            TestItem(
                "Andrei"
            )
        )
        cards.add(
            TestItem(
                "Anton"
            )
        )
        cards.add(
            TestItem(
                "Dmitry"
            )
        )
        cards.add(
            TestItem(
                "Daniil"
            )
        )

        return cards
    }

    fun getNames2(): ArrayList<ItemContainer> {
        val cards = ArrayList<ItemContainer>()
        cards.add(
            TestItem(
                "Andrei"
            )
        )
        cards.add(
            TestItem(
                "Anton"
            )
        )
        cards.add(
            TestItem(
                "Daniil"
            )
        )
        cards.add(
            TestItem(
                "Dmitry"
            )
        )

        return cards
    }

    fun getNumbersList(): List<TestItem> {
        val cards = ArrayList<TestItem>()

        for (i in 1 until 15) {
            cards.add(TestItem(i.toString()))
        }

        return cards
    }
}