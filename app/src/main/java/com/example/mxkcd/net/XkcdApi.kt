package com.example.mxkcd.net

import retrofit2.http.GET
import retrofit2.http.Path

interface XkcdApi {
    @GET("/{id}/info.0.json")
    suspend fun getXkcdItem(@Path("id") id: Int): NetItem
}