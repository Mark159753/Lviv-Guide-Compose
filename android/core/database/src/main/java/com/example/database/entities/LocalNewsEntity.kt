package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_news")
data class LocalNewsEntity(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val guid: String,
    val image: String,
    val fulltext:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
