# 🧪 Tugas Praktikum 10: Dependency Injection & Automated Testing

Pada pertemuan 10 ini, aplikasi telah direfaktor untuk memenuhi standar arsitektur industri dengan menerapkan **Dependency Injection (Koin)** dan telah divalidasi ketahanannya menggunakan **Automated Testing** secara menyeluruh (Unit Test & UI Test).

## ✨ Pencapaian Kriteria Tugas

### 1. 💉 Dependency Injection (Koin) - (20%)
- Memisahkan konfigurasi Koin DI menjadi 2 modul utama yang terstruktur:
  - `dataModule`: Menyediakan instance untuk Network, Database, dan Repository.
  - `viewModelModule`: Menyediakan instance untuk ViewModel.
- Injeksi dependensi berjalan mulus ke dalam aplikasi tanpa error.

### 2. 🧩 Unit Testing & Flow Testing - (35%)
Menggunakan pustaka `kotlin.test`, `MockK`, dan `Turbine` untuk memastikan logika bisnis (Business Logic) berjalan tanpa celah secara terisolasi.
- **ViewModel Tests (20%):** Terdapat 4 *test cases* pada `NoteViewModelTest` yang memvalidasi operasi UI State dan eksekusi Repository menggunakan `UnconfinedTestDispatcher` dan `MockK`.
- **Repository Tests (20%):** Terdapat 5 *test cases* pada `NoteRepositoryTest` untuk memvalidasi operasi internal pada data secara aman.
- **Flow Testing (15%):** Divalidasi secara asinkron menggunakan pustaka `Turbine` untuk mengamati aliran data.
- **Domain Tests:** Validasi *business logic* terisolasi pada `NoteValidatorTest` untuk memastikan akurasi data.

### 3. 📱 UI Testing (Compose Test) - (15%)
Menggunakan `ui-test-junit4` dan mesin `AndroidJUnit4` untuk menguji antarmuka (Jetpack Compose).
- Terdapat 3 *test cases* terisolasi pada `NotesScreenTest` yang memvalidasi komponen UI krusial (eksistensi menu navigasi bawah: Catatan, Favorit, dan Profil).
- Seluruh tes berhasil lulus (*Passed*) dan tervalidasi menggunakan `assertIsDisplayed()`.

### 4. 🎯 [BONUS] Test Coverage > 80% - (+10%)
Proyek ini telah dikonfigurasi menggunakan mesin **JaCoCo** melalui Gradle Task untuk menghasilkan laporan cakupan kode (Code Coverage) berstandar industri dalam format HTML.
- Cakupan pengujian pada paket `domain` mencapai **100%** (Instruksi, Baris, dan Metode).

#### 📸 Bukti Test Coverage (JaCoCo Report)
![Test Coverage Report](<img width="767" height="17" alt="Screenshot 2026-06-16 114535" src="https://github.com/user-attachments/assets/4a853362-333e-4232-a7e0-56b24cb90d69" />)

---
*Dibuat untuk memenuhi Tugas Praktikum 10.*
