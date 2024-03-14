package com.example.e_fir.data.modal

data class PageNode(
    var title: String,
    var pageIcon: Int,
    var description: String,
    var subNodes: List<SubPageNode>
)
