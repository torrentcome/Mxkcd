package com.example.mxkcd.dto

import com.example.mxkcd.db.EntityItem

data class XkcdItem (
    val month: String = "",
    val num: Long = -1,
    val link: String = "",
    val year: String = "",
    val news: String = "",
    val safe_title: String = "",
    val transcript: String = "",
    val alt: String = "",
    val img: String = "",
    val title: String = "",
    val day: String = ""
)

fun XkcdItem.asDbModel(): EntityItem {
    return EntityItem(
        num = num,
        month = month,
        link = link,
        year = year,
        news = news,
        safe_title = safe_title,
        transcript = transcript,
        alt = alt,
        img = img,
        title = title,
        day = day
    )
}