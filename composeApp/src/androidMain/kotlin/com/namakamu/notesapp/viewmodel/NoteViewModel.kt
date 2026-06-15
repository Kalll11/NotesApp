package com.namakamu.notesapp.viewmodel

import com.namakamu.notesapp.ai.AIRepository
import com.namakamu.notesapp.ai.SummaryResult
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namakamu.notesapp.data.NoteRepository
import com.namakamu.notesapp.db.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class Content(val notes: List<Note>) : UiState()
}

// 👉 PERUBAHAN 1: Menambahkan 'private val aiRepository: AIRepository' di sini
class NoteViewModel(
    private val repository: NoteRepository,
    private val aiRepository: AIRepository
) : ViewModel() {

    // Untuk fitur Search (Syarat Tugas No. 3)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 1. Ambil data LANGSUNG dari SQLite (Syarat Tugas No. 5 - Offline First)
    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<Note>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllNotes()
            else repository.searchNotes(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // State tambahan jika Anda ingin mengimplementasikan UI Loading/Empty di Screen
    val uiState: StateFlow<UiState> = notes.map {
        if (it.isEmpty()) UiState.Empty else UiState.Content(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    // Fungsi untuk memperbarui teks pencarian dari SearchBar di UI
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // ====================================================================
    // FUNGSI-FUNGSI LAMA ANDA YANG DIPERTAHANKAN (TAPI KINI TERSAMBUNG KE DB)
    // ====================================================================

    // 1. Tambah Catatan (Simpan permanen)
    fun addNote(title: String, content: String) {
        if (title.isNotBlank() || content.isNotBlank()) {
            viewModelScope.launch {
                repository.insertNote(title, content, false)
            }
        }
    }

    // 2. Edit Catatan (Update ke DB dengan konversi Int ke Long otomatis)
    fun updateNote(id: Int, newTitle: String, newContent: String) {
        viewModelScope.launch {
            val oldNote = getNoteById(id)
            if (oldNote != null) {
                repository.updateNote(id.toLong(), newTitle, newContent, oldNote.isFavorite)
            }
        }
    }

    // 3. Toggle Favorit (Bintang)
    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            val oldNote = getNoteById(id)
            if (oldNote != null) {
                repository.updateNote(oldNote.id, oldNote.title, oldNote.content, !oldNote.isFavorite)
            }
        }
    }

    // 4. Ambil 1 Catatan berdasarkan ID
    fun getNoteById(id: Int): Note? {
        // Kita bandingkan id (Long) dari database dengan id (Int) dari UI Anda
        return notes.value.find { it.id == id.toLong() }
    }

    // 5. Hapus Catatan (Syarat Tugas CRUD - Delete)
    fun deleteNote(id: Int) {
        viewModelScope.launch {
            // Memanggil fungsi delete dari repository (konversi Int ke Long)
            repository.deleteNote(id.toLong())
        }
    }

    // ====================================================================
    // 👉 PERUBAHAN 2: KODE UNTUK FITUR AI GENERATE SUMMARY
    // ====================================================================

    // State untuk UI Ringkasan
    private val _summaryState = mutableStateOf<SummaryResult?>(null)
    val summaryState: State<SummaryResult?> = _summaryState

    private val _isSummarizing = mutableStateOf(false)
    val isSummarizing: State<Boolean> = _isSummarizing

    // Fungsi untuk memanggil AI
    fun generateSummary(noteContent: String) {
        viewModelScope.launch {
            _isSummarizing.value = true
            _summaryState.value = null // Reset ringkasan lama

            val result = aiRepository.summarizeNote(noteContent)
            _summaryState.value = result

            _isSummarizing.value = false
        }
    }

    fun clearSummary() {
        _summaryState.value = null
    }
}