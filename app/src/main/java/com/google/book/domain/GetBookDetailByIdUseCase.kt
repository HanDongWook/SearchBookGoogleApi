package com.google.book.domain

import com.google.book.domain.entities.BookInfo
import com.google.book.repository.BookRepository

internal class GetBookDetailByIdUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): BookInfo {
        return bookRepository.getBook(bookId)
    }
}