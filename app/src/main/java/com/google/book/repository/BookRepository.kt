package com.google.book.repository

import com.google.book.api.ApiProvider
import com.google.book.api.BookAPI
import com.google.book.api.model.BookMapper
import com.google.book.domain.entities.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BookRepository(
    private val apiProvider: ApiProvider,
    private val bookMapper: BookMapper
) {
    private val bookApi by lazy { apiProvider.create(BookAPI::class) }

    suspend fun getBookList(query: String): Book = withContext(Dispatchers.IO) {
        val response = bookApi.getBookList(query)
        return@withContext bookMapper.mapToDomainModel(response)
    }
}