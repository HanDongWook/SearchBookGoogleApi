package com.google.book.ui.searchbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.book.base.BaseViewModel
import com.google.book.domain.GetBookListUseCase
import com.google.book.domain.entities.BookInfo
import com.google.book.utils.SingleLiveEvent
import kotlinx.coroutines.launch

internal class SearchBookViewModel(
    private val getBookListUseCase: GetBookListUseCase
): BaseViewModel() {

    sealed class Navigate {
        data class BookDetail(val bookId: String) : Navigate()
    }

    private val totalCountLiveData = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = totalCountLiveData

    private val bookListLiveData = MutableLiveData<List<BookInfo>>()
    val bookList: LiveData<List<BookInfo>> = bookListLiveData

    val navigate = SingleLiveEvent<Navigate>()
    val error = SingleLiveEvent<Exception>()
    val loading = MutableLiveData(false)

    fun fetch(query: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                val result = getBookListUseCase(query)
                totalCountLiveData.value = result.totalItems
                bookListLiveData.value = result.list
                Log.d("test", "SearchBookViewModel result.totalItems:${result.totalItems} result.list : ${result.list}")
            } catch (e: Exception) {
                error.value = e
            } finally {
                loading.value = false
            }
        }
    }

    fun onBookClick(bookId: String) {
        navigate.value = Navigate.BookDetail(bookId)
    }
}