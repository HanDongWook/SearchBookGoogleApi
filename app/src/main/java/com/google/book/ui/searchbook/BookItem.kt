package com.google.book.ui.searchbook

import com.google.book.domain.entities.BookInfo

enum class BookItemView(val type: Int) {
    Item(0), Loading(1)
}

sealed class BookItem {
    data class Item(val info: BookInfo) : BookItem()
    object Loading : BookItem()
}