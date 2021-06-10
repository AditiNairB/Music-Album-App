package com.example.musicapp

import retrofit2.http.GET

const val BASE_URL= "https://rss.itunes.apple.com"

interface RetrofitInterface {
    @GET("/api/v1/us/apple-music/top-albums/all/25/explicit.json")
    suspend fun getAlbumDetails(): FeedResponse
}