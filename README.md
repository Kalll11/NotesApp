# 📝 NotesApp - Integrasi AI

Aplikasi pencatatan (NotesApp) berbasis Android yang telah diintegrasikan dengan teknologi *Generative AI* untuk membantu pengguna merangkum catatan panjang secara instan. Proyek ini dikembangkan untuk memenuhi Tugas Praktikum Pengembangan Aplikasi Mobile.

## ✨ Fitur AI yang Diimplementasikan: **Content Summarization**
Aplikasi ini menggunakan **Google Gemini API (Model: `gemini-2.5-flash`)** untuk memproses dan menghasilkan ringkasan cerdas dari isi catatan pengguna. Fitur ini dapat diakses langsung melalui tombol "✨ Ringkas (AI)" pada halaman Detail Catatan.

### 🎯 Pemenuhan Kriteria Rubrik Penilaian:

* **1. AI Integration & Architecture**
  Integrasi API dilakukan dengan arsitektur yang rapi menggunakan `Ktor HttpClient` untuk lapisan jaringan dan `Koin` untuk *Dependency Injection* (DI).
* **2. Prompt Engineering**
  Sistem *prompt* dirancang secara spesifik (well-designed) untuk memastikan keluaran AI selalu relevan, ringkas, dan terstruktur. *(Prompt diinstruksikan untuk bertindak sebagai asisten cerdas dan membatasi ringkasan maksimum 3 paragraf).*
* **3. Proper Error Handling**
  Aplikasi memiliki penanganan *error* yang tangguh (graceful degradation):
  - Menangkap *error* jaringan (No Internet).
  - Menangani JSON *Serialization Exception* jika API mengembalikan respons tak terduga.
  - Memberikan pesan UI yang jelas jika kuota API habis (Rate Limit) atau *API Key* tidak valid, sehingga aplikasi **tidak mengalami force close**.
* **4. UI/UX yang Responsif**
  - Terdapat *Loading State* (`CircularProgressIndicator`) saat sistem sedang menunggu balasan dari AI.
  - UI menggunakan `verticalScroll` sehingga pengguna tetap nyaman membaca ringkasan teks yang panjang.

---

## 🚀 Cara Menjalankan Aplikasi

Agar fitur AI dapat berjalan di perangkat/emulator Anda, Anda **WAJIB** memasukkan API Key Gemini milik Anda sendiri. Aplikasi ini sengaja tidak menyertakan API Key di dalam *repository* publik demi keamanan kredensial.

**Langkah-langkah setup:**
1. Dapatkan *API Key* gratis dari [Google AI Studio](https://aistudio.google.com/app/apikey).
2. *Clone repository* ini ke komputer Anda dan buka menggunakan Android Studio.
3. Di dalam Android Studio, buka file `local.properties` (berada di folder paling luar/root proyek).
4. Tambahkan baris kode berikut di bagian paling bawah:
   ```properties
   GEMINI_API_KEY=TulisApiKeyAndaDisiniTanpaTandaKutip
5. Lakukan Sync Project with Gradle Files.
6. Build dan Run aplikasi ke Emulator atau HP fisik Anda.

---

## 🛠️ Teknologi yang Digunakan

1. Kotlin
2. Jetpack Compose (UI)
3. Ktor (Network/HTTP Client)
4. Kotlinx Serialization (JSON Parsing)
5. Koin (Dependency Injection)