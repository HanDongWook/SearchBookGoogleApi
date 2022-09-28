package com.google.book.domain

import com.google.book.domain.entities.Book
import com.google.book.repository.BookRepository

internal class GetBookListUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(query: String): Book {
        return bookRepository.getBookList(query)
    }
}