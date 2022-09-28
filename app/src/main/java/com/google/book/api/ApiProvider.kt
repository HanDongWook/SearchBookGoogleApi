package com.google.book.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass


class ApiProvider {
    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }

    fun <T : Any> create(kClass: KClass<T>): T = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(kClass.java)
}