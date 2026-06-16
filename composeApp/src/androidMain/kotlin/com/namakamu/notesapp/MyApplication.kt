package com.namakamu.notesapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.namakamu.notesapp.di.allModules
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(allModules)
        }
    }
}