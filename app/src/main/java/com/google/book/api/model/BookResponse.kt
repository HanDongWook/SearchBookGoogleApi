package com.google.book.api.model

import com.google.gson.annotations.SerializedName


data class BookListResponse(
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("items") val items: List<BookInfoResponse>? = emptyList<BookInfoResponse>()
)

data class BookInfoResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoResponse? = VolumeInfoResponse(),
)

data class VolumeInfoResponse(
    @SerializedName("title") var title: String? = null,
    @SerializedName("subtitle") var subtitle: String? = null,
    @SerializedName("authors") var authors: List<String>? = listOf("Author unknown"),
    @SerializedName("publisher") var publisher: String? = "Publisher details not available",
    @SerializedName("publishedDate") var publishedDate: String? = null,
    @SerializedName("description") var description: String? = "No description found",
    @SerializedName("pageCount") var pageCount: Int? = null,
    @SerializedName("printType") var printType: String? = null,
    @SerializedName("categories") var categories: List<String> = arrayListOf(),
    @SerializedName("averageRating") var averageRating: Double? = null,
    @SerializedName("ratingsCount") var ratingsCount: Int? = null,
    @SerializedName("maturityRating") var maturityRating: String? = null,
    @SerializedName("allowAnonLogging") var allowAnonLogging: Boolean? = null,
    @SerializedName("contentVersion") var contentVersion: String? = null,
    @SerializedName("imageLinks") var imageLinks: ImageLinksResponse? = ImageLinksResponse(),
    @SerializedName("language") var language: String? = null,
    @SerializedName("previewLink") var previewLink: String? = null,
    @SerializedName("infoLink") var infoLink: String? = null,
    @SerializedName("canonicalVolumeLink") var canonicalVolumeLink: String? = null
)

data class ImageLinksResponse(
    @SerializedName("smallThumbnail") var smallThumbnail: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null
)