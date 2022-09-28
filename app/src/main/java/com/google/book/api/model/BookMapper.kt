package com.google.book.api.model

import com.google.book.domain.entities.Book
import com.google.book.domain.entities.BookInfo
import com.google.book.domain.entities.ImageLinks
import com.google.book.domain.entities.VolumeInfo
import com.google.book.domain.util.DomainMapper

class BookMapper : DomainMapper<BookListResponse, Book> {
    private val bookInfoMapper = BookInfoMapper()

    override fun mapToDomainModel(entity: BookListResponse): Book {
        return Book(
            entity.totalItems,
            (entity.items ?: emptyList<BookInfoResponse>()).map {
                bookInfoMapper.mapToDomainModel(it)
            }
        )
    }
}

class BookInfoMapper : DomainMapper<BookInfoResponse, BookInfo> {
    private val volumeInfoMapper = VolumeInfoMapper()

    override fun mapToDomainModel(entity: BookInfoResponse): BookInfo {
        return BookInfo(
            entity.id,
            volumeInfoMapper.mapToDomainModel(entity.volumeInfo!!)
        )
    }
}

class VolumeInfoMapper : DomainMapper<VolumeInfoResponse, VolumeInfo> {
    private val imageLinksMapper = ImageLinksMapper()
    override fun mapToDomainModel(entity: VolumeInfoResponse): VolumeInfo {
        return VolumeInfo(
            entity.title,
            entity.subtitle,
            entity.authors,
            entity.publisher!!,
            entity.publishedDate,
            entity.description!!,
            entity.pageCount,
            entity.printType,
            entity.categories,
            entity.averageRating,
            entity.ratingsCount,
            entity.maturityRating,
            entity.allowAnonLogging,
            entity.contentVersion,
            imageLinksMapper.mapToDomainModel(entity.imageLinks!!),
            entity.language,
            entity.previewLink,
            entity.infoLink,
            entity.canonicalVolumeLink,
        )
    }
}

class ImageLinksMapper : DomainMapper<ImageLinksResponse, ImageLinks> {
    override fun mapToDomainModel(entity: ImageLinksResponse): ImageLinks {
        return ImageLinks(
            entity.smallThumbnail!!,
            entity.thumbnail!!
        )
    }
}