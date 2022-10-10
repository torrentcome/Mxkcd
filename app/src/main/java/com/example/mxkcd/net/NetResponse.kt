package com.example.mxkcd.net

import com.example.mxkcd.dto.DtoItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NetResponse (
    @Json(name = "num")
    val num: Long = -1,
    @Json(name = "month")
    val month: String = "",
    @Json(name = "link")
    val link: String = "",
    @Json(name = "year")
    val year: String = "",
    @Json(name = "news")
    val news: String = "",
    @Json(name = "safe_title")
    val safe_title: String = "",
    @Json(name = "transcript")
    val transcript: String = "",
    @Json(name = "alt")
    val alt: String = "",
    @Json(name = "img")
    val img: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "day")
    val day: String = ""
) {
    override fun toString(): String {
        return "NetItem(num=$num" +
                ", month='$month'" +
                ", link='$link'" +
                ", year='$year'" +
                ", news='$news'" +
                ", safe_title='$safe_title'" +
                ", transcript='$transcript'" +
                ", alt='$alt'" +
                ", img='$img'" +
                ", title='$title'" +
                ", day='$day')"
    }
}

fun NetResponse.asDtoModel(): DtoItem {
    return DtoItem(
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
