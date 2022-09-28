package com.google.book.api

import com.google.book.api.model.BookInfoResponse
import com.google.book.api.model.BookListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookAPI {
    @GET("volumes/")
    suspend fun getBookList(
        @Query("q") query: String
    ): BookListResponse

    @GET("volumes/{id}")
    suspend fun getBook(
        @Path("id") id: String
    ): BookInfoResponse
}