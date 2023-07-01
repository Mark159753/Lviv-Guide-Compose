package com.example.data.model

import com.example.core.network.model.local_news.NewsItem
import com.example.database.entities.LocalNewsEntity

data class LocalNewsModel(
    val id:Int,
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val guid: String,
    val image: String,
    val fulltext:String
)

fun NewsItem.toEntity():LocalNewsEntity = LocalNewsEntity(
    title = title,
    link = link,
    description = description,
    pubDate = pubDate,
    guid = guid,
    image = image,
    fulltext = fulltext
)

fun LocalNewsEntity.toExternal(): LocalNewsModel = LocalNewsModel(
    id = id,
    title = title,
    link = link,
    description = description,
    pubDate = pubDate,
    guid = guid,
    image = image,
    fulltext = fulltext
)