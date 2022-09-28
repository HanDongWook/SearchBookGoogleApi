package com.google.book.di


import com.google.book.api.ApiProvider
import com.google.book.api.model.BookMapper
import com.google.book.domain.GetBookDetailByIdUseCase
import com.google.book.domain.GetBookListUseCase
import com.google.book.repository.BookRepository
import com.google.book.ui.bookdetail.BookDetailViewModel
import com.google.book.ui.searchbook.SearchBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    //Data Layer
    single { BookRepository(get(), get()) }
    single { ApiProvider() }
    single { BookMapper() }

    //Domain Layer
    single { GetBookDetailByIdUseCase(get()) }
    single { GetBookListUseCase(get()) }

    //View Layer
    viewModel { SearchBookViewModel(get()) }
    viewModel { (id: String) -> BookDetailViewModel(id, get()) }
}
