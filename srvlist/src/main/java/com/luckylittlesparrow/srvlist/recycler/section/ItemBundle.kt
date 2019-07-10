package com.luckylittlesparrow.srvlist.recycler.section

/**
 * Container for new data submission to the Section
 */
data class ItemBundle(
    val headerItem: ItemContainer? = null,
    val contentItems: List<ItemContainer>? = null,
    val footerItem: ItemContainer? = null
)