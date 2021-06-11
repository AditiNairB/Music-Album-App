package com.example.musicapp

import java.io.Serializable

data class AlbumListItem(val artistName: String, val name: String, val artworkUrl100: String, val url: String, var contentAdvisoryRating: String?, var copyright: String?):Serializable

data class FeedResponse(val feed: ResultsResponse)

data class ResultsResponse (val results: ArrayList<AlbumListItem>)
