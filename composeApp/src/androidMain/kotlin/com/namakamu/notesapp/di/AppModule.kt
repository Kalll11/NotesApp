package com.namakamu.notesapp.di

import com.namakamu.notesapp.ai.AIRepository
import com.namakamu.notesapp.data.NoteRepository
import com.namakamu.notesapp.platform.NetworkMonitor
import com.namakamu.notesapp.viewmodel.NoteViewModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// 1. Modul khusus Data (Repository, Database, Network) [cite: 518]
val dataModule = module {
    single { NetworkMonitor() }

    // Sesuaikan get() atau androidContext() bergantung pada implementasi Room Anda sebelumnya
    single { NoteRepository(get()) }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single { AIRepository(get()) }
}

// 2. Modul khusus ViewModel [cite: 524]
val viewModelModule = module {
    viewModel { NoteViewModel(get(), get()) }
}

// 3. Gabungkan modul [cite: 528]
val allModules = listOf(dataModule, viewModelModule)