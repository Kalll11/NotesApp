package com.namakamu.notesapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import org.koin.compose.koinInject
import com.namakamu.notesapp.platform.NetworkMonitor
import com.namakamu.notesapp.viewmodel.NoteViewModel
import com.namakamu.notesapp.db.Note

// ==========================================
// 1. DAFTAR CATATAN (TAB NOTES) + NETWORK BANNER
// ==========================================
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    networkMonitor: NetworkMonitor = koinInject(),
    onNavigateToDetail: (Int) -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isConnected by networkMonitor.observeConnectivity().collectAsState(initial = true)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        AnimatedVisibility(visible = !isConnected) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.error,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Offline Mode - Tidak Ada Koneksi Internet",
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Text("Catatan Saya", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari judul atau isi catatan...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (notes.isEmpty()) {
            if (searchQuery.isNotEmpty()) {
                Text("Catatan tidak ditemukan.", color = Color.Gray)
            } else {
                Text("Belum ada catatan. Klik tombol + untuk menambah.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notes) { note ->
                    NoteCard(note = note, viewModel = viewModel, onClick = { onNavigateToDetail(note.id.toInt()) })
                }
            }
        }
    }
}

// ==========================================
// 2. DAFTAR FAVORIT (TAB FAVORITES)
// ==========================================
@Composable
fun FavoritesScreen(
    viewModel: NoteViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val favoriteNotes = notes.filter { it.isFavorite }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catatan Favorit", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteNotes.isEmpty()) {
            Text("Belum ada catatan favorit.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favoriteNotes) { note ->
                    NoteCard(note = note, viewModel = viewModel, onClick = { onNavigateToDetail(note.id.toInt()) })
                }
            }
        }
    }
}

// ==========================================
// COMPONENT: KARTU CATATAN (DIPAKAI DI LIST & FAVORIT)
// ==========================================
@Composable
fun NoteCard(note: Note, viewModel: NoteViewModel, onClick: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(note.content, color = Color.Gray, maxLines = 1)
            }

            Row {
                IconButton(onClick = { viewModel.toggleFavorite(note.id.toInt()) }) {
                    Icon(
                        imageVector = if (note.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (note.isFavorite) Color.Red else Color.Gray
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Catatan",
                        tint = Color.Gray
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Catatan") },
            text = { Text("Apakah Anda yakin ingin menghapus catatan '${note.title}'? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteNote(note.id.toInt())
                        showDeleteDialog = false
                    }
                ) { Text("Hapus", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }
}

// ==========================================
// 3. TAMBAH CATATAN (FORM ADD)
// ==========================================
@Composable
fun AddNoteScreen(viewModel: NoteViewModel, onNavigateBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Text("Tambah Catatan", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi Catatan") }, modifier = Modifier.fillMaxWidth(), minLines = 5)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.addNote(title, content)
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Catatan")
        }
    }
}

// ==========================================
// 4. DETAIL CATATAN (BACA)
// ==========================================
@Composable
fun NoteDetailScreen(noteId: Int, viewModel: NoteViewModel, onNavigateBack: () -> Unit, onNavigateToEdit: (Int) -> Unit) {
    val note = viewModel.getNoteById(noteId)

    val isSummarizing by viewModel.isSummarizing
    val summaryState by viewModel.summaryState

    DisposableEffect(Unit) { onDispose { viewModel.clearSummary() } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (note != null) {
            // 👇 1. BUNGKUS TEKS DALAM COLUMN BARU YANG BISA DI-SCROLL 👇
            Column(
                modifier = Modifier
                    .weight(1f) // Memaksa area teks mengambil seluruh sisa ruang kosong
                    .verticalScroll(rememberScrollState()) // Menambahkan kemampuan scroll
            ) {
                Text(note.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(note.content, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // KOTAK RINGKASAN AI
                if (isSummarizing) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("AI sedang membaca catatan Anda...", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (summaryState != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("✨ Ringkasan AI", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (summaryState!!.isSuccess) {
                                Text(summaryState!!.summary, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            } else {
                                Text(summaryState!!.errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            } // 👈 Tutup area scroll di sini

            Spacer(modifier = Modifier.height(16.dp))

            // 👇 2. AREA TOMBOL BAWAH (TETAP STATIS TIDAK IKUT DI-SCROLL) 👇
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onNavigateBack, modifier = Modifier.weight(1f)) { Text("Kembali") }
                Button(onClick = { viewModel.generateSummary(note.content) }, modifier = Modifier.weight(1.5f)) {
                    Text("✨ Ringkas (AI)")
                }
                Button(onClick = { onNavigateToEdit(noteId) }, modifier = Modifier.weight(1f)) { Text("Edit") }
            }
        } else {
            Text("Catatan tidak ditemukan.")
            Button(onClick = onNavigateBack) { Text("Kembali") }
        }
    }
}

// ==========================================
// 5. EDIT CATATAN (FORM EDIT)
// ==========================================
@Composable
fun EditNoteScreen(noteId: Int, viewModel: NoteViewModel, onNavigateBack: () -> Unit) {
    val note = viewModel.getNoteById(noteId)

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Text("Edit Catatan", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi Catatan") }, modifier = Modifier.fillMaxWidth(), minLines = 5)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.updateNote(noteId, title, content)
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Perubahan")
        }
    }
}