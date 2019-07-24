package com.luckylittlesparrow.waterwall.example.listsample

object ItemsFactory {
    fun getNames(): List<Item> {
        val cards = ArrayList<Item>()
        cards.add(
            Item(
                "Andrei"
            )
        )
        cards.add(
            Item(
                "Anton"
            )
        )
        cards.add(
            Item(
                "Dmitry"
            )
        )
        cards.add(
            Item(
                "Daniil"
            )
        )

        return cards
    }

    fun getSecondEvents(): List<Item> {
        val cards = ArrayList<Item>()
        cards.add(
            Item(
                "Anna"
            )
        )
        cards.add(
            Item(
                "Kiril"
            )
        )
        cards.add(
            Item(
                "Andrei"
            )
        )
        cards.add(
            Item(
                "Sonya"
            )
        )

        return cards
    }

    fun getNumbersList(): List<Item> {
        val cards = ArrayList<Item>()

        for (i in 1 until 35) {
            cards.add(Item(i.toString()))
        }

        return cards
    }
}