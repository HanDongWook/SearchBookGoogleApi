package com.google.book.repository

import com.google.book.api.ApiProvider
import com.google.book.api.BookAPI
import com.google.book.api.model.BookInfoMapper
import com.google.book.api.model.BookMapper
import com.google.book.domain.entities.Book
import com.google.book.domain.entities.BookInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BookRepository(
    private val apiProvider: ApiProvider,
) {
    private val bookApi by lazy { apiProvider.create(BookAPI::class) }
    private val bookMapper = BookMapper()
    private val bookInfoMapper = BookInfoMapper()

    suspend fun getBookList(query: String, startIndex: Int, maxResults: Int): Book = withContext(Dispatchers.IO) {
        val response = bookApi.getBookList(query, startIndex, maxResults)
        return@withContext bookMapper.mapToDomainModel(response)
    }

    suspend fun getBook(bookId: String): BookInfo = withContext(Dispatchers.IO) {
        val response = bookApi.getBook(bookId)
        return@withContext bookInfoMapper.mapToDomainModel(response)
    }
}