package com.google.book.domain.util

interface DomainMapper <T, DomainModel> {
    fun mapToDomainModel(entity: T): DomainModel
}