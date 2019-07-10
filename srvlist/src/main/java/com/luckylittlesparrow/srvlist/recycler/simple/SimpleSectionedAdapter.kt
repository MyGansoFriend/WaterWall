package com.luckylittlesparrow.srvlist.recycler.simple

import com.luckylittlesparrow.srvlist.recycler.base.BaseListAdapter

class SimpleSectionedAdapter : BaseListAdapter() {

    init {
        sectionMediator = SimpleSectionMediator()
        //initUtil()
    }
}