package com.example.core.network.local_news

import com.example.core.network.model.local_news.LocalNewsResponse
import com.example.core.network.model.local_news.NewsItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class LocalNewsParser {

    operator fun invoke(xmlString: String): LocalNewsResponse {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlString))

        var eventType = parser.eventType
        var currentItem: NewsItem? = null
        val items = mutableListOf<NewsItem>()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "item" -> currentItem = NewsItem()
                        "title" -> currentItem?.title = parseText(parser)
                        "link" -> currentItem?.link = parseText(parser)
                        "description" -> currentItem?.description = parseText(parser)
                        "pubDate" -> currentItem?.pubDate = parseText(parser)
                        "guid" -> currentItem?.guid = parseText(parser)
                        "enclosure" -> currentItem?.image = parser.getAttributeValue(null, "url")
                        "fulltext" -> currentItem?.fulltext = parseText(parser)
                    }
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "item" -> currentItem?.let { items.add(it) }
                    }
                }
            }
            eventType = parser.next()
        }

        return LocalNewsResponse(items)
    }

    private fun parseText(parser: XmlPullParser): String {
        return if (parser.next() == XmlPullParser.TEXT) {
            parser.text
        } else {
            ""
        }
    }
}