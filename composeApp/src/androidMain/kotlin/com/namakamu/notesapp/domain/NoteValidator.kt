package com.namakamu.notesapp.domain

// Kelas murni (business logic) untuk memvalidasi input catatan
class NoteValidator {
    fun isValid(title: String, content: String): Boolean {
        if (title.isBlank()) return false // Judul tidak boleh kosong
        if (title.length > 50) return false // Judul tidak boleh terlalu panjang
        return true // Jika lolos semua, berarti valid
    }
}