package com.google.book.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass


class ApiProvider {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    fun <T : Any> create(kClass: KClass<T>): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(kClass.java)
}