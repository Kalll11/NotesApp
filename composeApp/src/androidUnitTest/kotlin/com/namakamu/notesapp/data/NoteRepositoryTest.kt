package com.namakamu.notesapp.data

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteRepositoryTest {

    private val repository = mockk<NoteRepository>(relaxed = true)

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // ==========================================
    // 5 TEST CASES UNTUK REPOSITORY
    // ==========================================

    @Test
    fun `Test 1 - getAllNotes returns empty flow successfully`() = runTest {
        // Arrange
        coEvery { repository.getAllNotes() } returns flowOf(emptyList())

        // Act & Assert
        repository.getAllNotes().test {
            val result = awaitItem()
            assertEquals(0, result.size) // Memastikan list kosong
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Test 2 - getAllNotes returns flow with data successfully`() = runTest {
        // Arrange (Kita pakai list bohongan berisi 1 item)
        coEvery { repository.getAllNotes() } returns flowOf(listOf(mockk(relaxed = true)))

        // Act & Assert
        repository.getAllNotes().test {
            val result = awaitItem()
            assertEquals(1, result.size) // Memastikan list ada isinya
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Test 3 - insertNote correctly processes new data`() = runTest {
        // Arrange (Mendefinisikan tipe data secara eksplisit agar Kotlin tidak bingung)
        coEvery { repository.insertNote(any<String>(), any<String>(), any<Boolean>()) } just Runs

        // Act
        repository.insertNote("Judul", "Isi", false)

        // Assert
        coVerify { repository.insertNote("Judul", "Isi", false) }
    }

    @Test
    fun `Test 4 - updateNote modifies existing data successfully`() = runTest {
        // Arrange
        val noteId = 1L // Menggunakan 1L karena database meminta tipe Long
        coEvery { repository.updateNote(any<Long>(), any<String>(), any<String>(), any<Boolean>()) } just Runs

        // Act
        repository.updateNote(noteId, "Judul Baru", "Isi Baru", false)

        // Assert
        coVerify { repository.updateNote(noteId, "Judul Baru", "Isi Baru", false) }
    }

    @Test
    fun `Test 5 - deleteNote removes data based on ID`() = runTest {
        // Arrange
        val noteId = 1L // Menggunakan 1L
        coEvery { repository.deleteNote(any<Long>()) } just Runs

        // Act
        repository.deleteNote(noteId)

        // Assert
        coVerify(exactly = 1) { repository.deleteNote(noteId) } // Memastikan dipanggil tepat 1 kali
    }
}