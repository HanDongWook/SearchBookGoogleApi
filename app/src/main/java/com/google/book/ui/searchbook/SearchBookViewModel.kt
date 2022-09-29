package com.google.book.ui.searchbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.book.base.BaseViewModel
import com.google.book.domain.GetBookListUseCase
import com.google.book.utils.SingleLiveEvent
import kotlinx.coroutines.launch

internal class SearchBookViewModel(
    private val getBookListUseCase: GetBookListUseCase
) : BaseViewModel() {
    companion object {
        const val MAX_RESULTS = 10
    }

    sealed class Navigate {
        data class BookDetail(val bookId: String) : Navigate()
    }

    private val totalCountLiveData = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = totalCountLiveData

    private val list = mutableListOf<BookItem>()
    private val bookListLiveData = MutableLiveData<List<BookItem>>()
    val bookList: LiveData<List<BookItem>> = bookListLiveData

    private val queryLiveData = MutableLiveData("")

    private val startIndexLiveData = MutableLiveData<Int>()

    val navigate = SingleLiveEvent<Navigate>()
    val error = SingleLiveEvent<Exception>()
    val loading = MutableLiveData(false)

    fun fetch(query: String) {
        Log.d("Test", "fetch call $query")
        viewModelScope.launch {
            saveParam(query, 0)
            list.clear()

            if (query.isBlank()) {
                totalCountLiveData.value = 0
                bookListLiveData.value = list
            } else {
                try {
                    loading.value = true

                    val result = getBookListUseCase(query, 0, MAX_RESULTS)


                    if (result.list.isNotEmpty()) {
                        val bookList = result.list.map { BookItem.Item(it) }
                        list.addAll(bookList)
                    }

                    bookListLiveData.value = list
                    totalCountLiveData.value = result.totalItems
                } catch (e: Exception) {
                    error.value = e
                } finally {
                    loading.value = false
                }
            }
        }
    }

    private fun saveParam(query: String, startIdx: Int) {
        queryLiveData.value = query
        startIndexLiveData.value = startIdx
    }

    fun getResultCount(): Int {
        return totalCountLiveData.value!!
    }

    fun loadMore() {
        val currentIdx = startIndexLiveData.value!! + MAX_RESULTS
        if (currentIdx < (totalCountLiveData.value!! - 1)) {
            viewModelScope.launch {
                try {
                    loading.value = true
                    saveParam(queryLiveData.value!!, currentIdx)

                    val result = getBookListUseCase(queryLiveData.value!!, currentIdx, MAX_RESULTS)

                    val bookList = result.list.map { BookItem.Item(it) }
                    list.addAll(bookList)

                    bookListLiveData.value = list
                } catch (e: Exception) {
                    error.value = e
                } finally {
                    loading.value = false
                }
            }
        }
    }

    fun onBookClick(bookId: String) {
        navigate.value = Navigate.BookDetail(bookId)
    }
}