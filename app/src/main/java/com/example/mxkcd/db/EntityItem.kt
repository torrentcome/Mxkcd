package com.example.mxkcd.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mxkcd.dto.XkcdItem

@Entity
data class EntityItem (
    @PrimaryKey
    val num: Long = -1,
    val month: String = "",
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

fun EntityItem.asDto(): XkcdItem {
    return XkcdItem(
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