package com.google.book.ui.bookdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.book.base.BaseViewModel
import com.google.book.domain.GetBookDetailByIdUseCase
import com.google.book.domain.entities.BookInfo
import com.google.book.utils.SingleLiveEvent
import kotlinx.coroutines.launch

internal class BookDetailViewModel(
    private val bookId: String,
    private val getBookDetailByIdUseCase: GetBookDetailByIdUseCase
): BaseViewModel() {

    private val bookInfoLiveData = MutableLiveData<BookInfo>()
    val bookInfo: LiveData<BookInfo> = bookInfoLiveData

    val error = SingleLiveEvent<Exception>()
    val loading = MutableLiveData(false)

    init {
        viewModelScope.launch {
            try {
                loading.value = true
                val result = getBookDetailByIdUseCase(bookId)
                bookInfoLiveData.value = result
            } catch (e: Exception) {
                error.value = e
            } finally {
                loading.value = false
            }
        }
    }
}