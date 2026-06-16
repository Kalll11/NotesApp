package com.namakamu.notesapp.domain

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NoteValidatorTest {
    private val validator = NoteValidator()

    @Test
    fun `valid note returns true`() {
        val result = validator.isValid("Belanja", "Beli Sayur")
        assertTrue(result) // Memastikan hasilnya true
    }

    @Test
    fun `empty title returns false`() {
        val result = validator.isValid("", "Isi catatan")
        assertFalse(result) // Memastikan hasilnya false
    }

    @Test
    fun `title too long returns false`() {
        val longTitle = "a".repeat(100) // Membuat judul sepanjang 100 karakter
        val result = validator.isValid(longTitle, "Isi catatan")
        assertFalse(result) // Memastikan hasilnya false
    }
}