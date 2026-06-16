package com.namakamu.notesapp.viewmodel

import app.cash.turbine.test
import com.namakamu.notesapp.ai.AIRepository
import com.namakamu.notesapp.data.NoteRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NoteViewModelTest {

    // 1. Setup MockK untuk mengisolasi ViewModel dari database & jaringan asli [cite: 227, 228, 549]
    private val mockNoteRepo = mockk<NoteRepository>(relaxed = true)
    private val mockAIRepo = mockk<AIRepository>(relaxed = true)

    private lateinit var viewModel: NoteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        // Mengganti dispatcher utama dengan dispatcher khusus pengujian (TestDispatcher)
        Dispatchers.setMain(testDispatcher)

        // Memasukkan (inject) repo bohongan/mock ke dalam ViewModel [cite: 236]
        viewModel = NoteViewModel(mockNoteRepo, mockAIRepo)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // Membersihkan dispatcher setelah test selesai [cite: 364]
    }

    // ==========================================
    // BAGIAN 1: PENGUJIAN MOCKK (Interaksi Repository) [cite: 612]
    // ==========================================

    @Test
    fun `addNote calls repository to insert data`() = runTest {
        // Act: Menjalankan fungsi tambah catatan
        viewModel.addNote("Tugas Baru", "Belajar Testing")

        // Assert: Memastikan fungsi penyimpan di Repository benar-benar terpanggil [cite: 242, 334]
        // 👇 PENTING: Ubah 'insertNote' jika di repository Anda namanya berbeda (misal: addNote)
        coVerify { mockNoteRepo.insertNote(any(), any()) }
    }

    @Test
    fun `deleteNote calls repository delete function`() = runTest {
        // Act
        viewModel.deleteNote(1)

        // Assert [cite: 563]
        // 👇 PENTING: Ubah 'deleteNote' jika di repository Anda namanya berbeda
        coVerify { mockNoteRepo.deleteNote(1) }
    }

    // ==========================================
    // BAGIAN 2: PENGUJIAN TURBINE (Aliran Data / Flow) [cite: 613]
    // ==========================================

    @Test
    fun `updateSearchQuery correctly updates the state flow`() = runTest {
        // .test { ... } adalah fungsi dari Turbine [cite: 319]
        viewModel.searchQuery.test {
            // Mengecek nilai awal (harus kosong) [cite: 321]
            assertEquals("", awaitItem())

            // Mengubah query pencarian
            viewModel.updateSearchQuery("Ujian")

            // Mengecek apakah state flow memancarkan nilai yang baru diubah [cite: 324]
            assertEquals("Ujian", awaitItem())

            cancelAndIgnoreRemainingEvents() // Mengakhiri observasi [cite: 327]
        }
    }

    @Test
    fun `notes flow emits valid initial data state`() = runTest {
        viewModel.notes.test {
            // Memastikan aliran data catatan tidak null/error saat pertama kali dimuat
            val items = awaitItem()
            assertNotNull(items)
            cancelAndIgnoreRemainingEvents()
        }
    }
}