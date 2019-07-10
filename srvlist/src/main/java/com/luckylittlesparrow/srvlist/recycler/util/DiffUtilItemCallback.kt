package com.luckylittlesparrow.srvlist.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

/**
 * Util class for comparing items
 */
abstract class DiffUtilItemCallback : DiffUtil.ItemCallback<ItemContainer>()