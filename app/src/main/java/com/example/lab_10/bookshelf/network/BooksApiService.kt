package com.example.lab_10.bookshelf.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class OpenLibraryResponse(@SerializedName("docs") val docs: List<BookDoc>?)
data class BookDoc(@SerializedName("cover_i") val coverId: Int?)

interface BooksApiService {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): OpenLibraryResponse
}

object BooksApi {
    private const val BASE_URL = "https://openlibrary.org/"

    val retrofitService: BooksApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(BooksApiService::class.java)
    }
}