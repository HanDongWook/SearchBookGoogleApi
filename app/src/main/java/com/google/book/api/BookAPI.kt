package com.google.book.api

import com.google.book.api.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookAPI {
    @GET(" ")
    suspend fun getBookList(
        @Query("q") query: String
    ): BookResponse
}