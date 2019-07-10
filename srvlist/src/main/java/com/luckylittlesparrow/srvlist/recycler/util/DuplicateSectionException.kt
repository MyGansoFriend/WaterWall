package com.luckylittlesparrow.srvlist.recycler.util

class DuplicateSectionException(key: String) : Exception("Section with key:$key is already provided in Adapter")