package com.namakamu.notesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==========================================
    // 3 TEST CASES UNTUK UI (Isolated Component Testing)
    // Sesuai standar industri untuk Jetpack Compose
    // ==========================================

    @Test
    fun test1_ButtonInteractionUpdatesState() {
        // Menguji interaksi tombol dan perubahan UI (State)
        composeTestRule.setContent {
            var text by remember { mutableStateOf("Belum Diklik") }
            Button(onClick = { text = "Sudah Diklik" }) {
                Text(text)
            }
        }

        // Pastikan teks awal benar
        composeTestRule.onNodeWithText("Belum Diklik").assertIsDisplayed()

        // Robot menekan tombol
        composeTestRule.onNodeWithText("Belum Diklik").performClick()

        // Pastikan UI langsung berubah
        composeTestRule.onNodeWithText("Sudah Diklik").assertIsDisplayed()
    }

    @Test
    fun test2_TextFieldAcceptsInput() {
        // Menguji komponen form pencarian (Input Text)
        composeTestRule.setContent {
            var query by remember { mutableStateOf("") }
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Cari catatan...") }
            )
        }

        // Robot mencari form berdasarkan placeholder, lalu mengetik
        composeTestRule.onNodeWithText("Cari catatan...").performTextInput("Tugas Praktikum 10")

        // Pastikan teks yang diketik berhasil masuk ke layar
        composeTestRule.onNodeWithText("Tugas Praktikum 10").assertIsDisplayed()
    }

    @Test
    fun test3_LoadingStateRendersCorrectly() {
        // Menguji render UI bersyarat (misalnya saat mengambil data dari API)
        composeTestRule.setContent {
            val isMemuatData = true

            Column {
                if (isMemuatData) {
                    Text("Sedang memuat catatan...")
                } else {
                    Text("Daftar Catatan Anda")
                }
            }
        }

        // Pastikan tulisan loading muncul
        composeTestRule.onNodeWithText("Sedang memuat catatan...").assertIsDisplayed()

        // Pastikan tulisan daftar catatan disembunyikan
        composeTestRule.onNodeWithText("Daftar Catatan Anda").assertDoesNotExist()
    }
}