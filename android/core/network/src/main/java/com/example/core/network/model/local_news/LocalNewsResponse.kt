package com.example.core.network.model.local_news

data class LocalNewsResponse(
    val items: List<NewsItem>
)

data class NewsItem(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var pubDate: String = "",
    var guid: String = "",
    var image: String = "",
    var fulltext:String = ""
)
