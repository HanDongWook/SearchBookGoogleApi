package com.google.book

import android.app.Application
import com.google.book.di.appModule
import org.koin.core.context.startKoin

internal class BookApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule) }
    }
}