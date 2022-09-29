package com.google.book.domain.entities

data class Book(
    val totalItems: Int,
    val list: List<BookInfo> = emptyList<BookInfo>()
)

data class BookInfo(
    val id: String?,
    val volumeInfo: VolumeInfo,
)

data class VolumeInfo(
    val title: String? = null,
    val subtitle: String? = null,
    val authors: List<String> = arrayListOf(),
    val publisher: String,
    val publishedDate: String? = null,
    val description: String,
    val pageCount: Int? = null,
    val printType: String? = null,
    val categories: List<String> = arrayListOf(),
    val averageRating: Double? = null,
    val ratingsCount: Int? = null,
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val imageLinks: ImageLinks,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
)

data class ImageLinks(
    var smallThumbnail: String?,
    var thumbnail: String?
)