package com.namakamu.notesapp.ai

import android.R.attr.apiKey
import com.namakamu.notesapp.platform.ApiConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AIRepository(private val client: HttpClient) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=MASUKKAN_API_KEY_MILIK_ANDA_DISINI"

    suspend fun summarizeNote(noteContent: String): SummaryResult {
        return try {
            // PROMPT ENGINEERING: Spesifik dan jelas
            val engineeredPrompt = """
                Bertindaklah sebagai asisten cerdas. Tolong buatkan ringkasan yang terstruktur dari catatan berikut. 
                Gunakan poin-poin (bullet points) jika perlu, dan pastikan ringkasannya tidak lebih dari 3 paragraf pendek.
                
                Teks Catatan:
                $noteContent
            """.trimIndent()

            val requestBody = GeminiRequest(listOf(Content(listOf(Part(engineeredPrompt)))))

            // HTTP Request ke Gemini API
            val response: GeminiResponse = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                parameter("key", ApiConfig.geminiApiKey)
                setBody(requestBody)
            }.body()

            val summaryText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            // 2. Cek hasilnya
            if (summaryText != null) {
                // Jika berhasil dan teksnya ada
                return SummaryResult(summary = summaryText, isSuccess = true)
            } else {
                // Jika gagal (teks kosong), kita ambil pesan error dari Google (jika ada)
                val errorMessage = response.error?.message ?: "Google menolak permintaan, tapi tidak memberikan alasan."
                return SummaryResult(summary = "", isSuccess = false, errorMessage = errorMessage)
            }

        } catch (e: Exception) {
            e.printStackTrace()

            return SummaryResult(
                isSuccess = false,
                summary = "",
                errorMessage = "Gagal memuat ringkasan. Cek koneksi internet Anda."
            )
        }
    }
}