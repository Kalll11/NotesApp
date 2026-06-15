package com.namakamu.notesapp.di // Sesuaikan jika package Anda berbeda

import com.namakamu.notesapp.ai.AIRepository
import com.namakamu.notesapp.data.NoteRepository
import com.namakamu.notesapp.platform.DeviceInfo
import com.namakamu.notesapp.platform.NetworkMonitor
import com.namakamu.notesapp.viewmodel.NoteViewModel
import com.namakamu.notesapp.viewmodel.ProfileViewModel

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 1. Dependensi bawaan (Dibiarkan seperti aslinya)
    single { DeviceInfo() }
    single { NetworkMonitor() }
    single { NoteRepository(androidContext()) } // Tetap butuh androidContext()

    // 2. Ktor HttpClient untuk AI (Cukup panggil kosong)
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    // 3. Repository AI
    single { AIRepository(get()) }

    // 4. ViewModel (Perhatikan NoteViewModel kini butuh DUA get() )
    viewModel { NoteViewModel(get(), get()) }
    viewModel { ProfileViewModel() }
}