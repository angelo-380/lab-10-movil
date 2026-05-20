package com.example.lab_10.bookshelf.data

import com.example.lab_10.bookshelf.network.BooksApiService

interface BooksRepository {
    suspend fun getBookThumbnails(query: String): List<String>
}

class NetworkBooksRepository(private val booksApiService: BooksApiService) : BooksRepository {
    override suspend fun getBookThumbnails(query: String): List<String> {
        val response = booksApiService.searchBooks(query)
        val coverIds = response.docs?.mapNotNull { it.coverId }?.take(10) ?: emptyList()

        val thumbnails = mutableListOf<String>()
        for (id in coverIds) {
            thumbnails.add("https://covers.openlibrary.org/b/id/$id-L.jpg")
        }

        return thumbnails
    }
}