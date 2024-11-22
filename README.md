# Aplikasi Agenda Pribadi
Aplikasi "Agenda Pribadi" adalah sebuah aplikasi berbasis Java yang digunakan untuk mengelola agenda sehari-hari. Aplikasi ini menggunakan SQLite sebagai database untuk menyimpan data agenda dan dilengkapi dengan antarmuka pengguna berbasis JFrame untuk interaksi yang mudah.
## Pembuat
- Nama: Muhammad Irwan Habibie  
- NPM: 2210010461  



## Fitur Utama
- **Menambahkan, mengedit, dan menghapus data agenda.**
- **Menampilkan data agenda dalam tabel dengan status yang dapat diubah.**
- **Mendukung pencarian data agenda berdasarkan nama atau deskripsi.**
- **Impor dan ekspor data agenda dalam format CSV.**
- **Mengelola kategori, tanggal, waktu, dan status agenda.**

---

## Struktur Proyek dan Fitur

### 1. Database Connection
**File**: `Agenda.java`  
Fitur ini menghubungkan aplikasi ke database SQLite, membuat tabel `agenda` jika belum ada.
```java
public static Connection connect() {
    Connection conn = null;
    try {
        String url = "jdbc:sqlite:agenda.db";
        conn = DriverManager.getConnection(url);

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS agenda (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nama TEXT NOT NULL,\n"
                + " deskripsi TEXT,\n"
                + " kategori TEXT,\n"
                + " tanggal DATE,\n"
                + " jam TEXT,\n"
                + " status INTEGER DEFAULT 0\n"
                + ");";
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreateTable);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return conn;
}
```
---
```
### 2. Menambahkan Agenda
**File**: `Agenda.java`  
Fitur ini menyimpan data agenda ke dalam database.

public void simpanAgenda() {
    String sql = "INSERT INTO agenda(nama, deskripsi, kategori, tanggal, jam, status) VALUES(?,?,?,?,?,?)";
    try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, this.nama);
        pstmt.setString(2, this.deskripsi);
        pstmt.setString(3, this.kategori);
        pstmt.setDate(4, this.tanggal);
        pstmt.setString(5, this.jam);
        pstmt.setInt(6, this.status ? 1 : 0);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
```
**File**: `AgendaPribadiFrame.java`  
Event pada tombol "Tambah" untuk menyimpan agenda.
```
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    String nama = teksNama.getText();
    String deskripsi = teksDeskripsi.getText();
    String kategori = comboKategori.getSelectedItem().toString();
    java.util.Date tanggalUtil = jDateChooser1.getDate();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String jam = sdf.format(jSpinner1.getValue());

    if (tanggalUtil != null) {
        java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());
        Agenda agenda = new Agenda(nama, deskripsi, kategori, tanggal, jam, false);
        agenda.simpanAgenda();
        muatData();
        kosongkanField();
        JOptionPane.showMessageDialog(this, "Agenda berhasil ditambahkan");
    } else {
        JOptionPane.showMessageDialog(this, "Pilih tanggal terlebih dahulu");
    }
}
```
---

### 3. Mengedit Agenda
**File**: `Agenda.java`  
Fitur ini memperbarui data agenda yang sudah ada di database.
```
public void updateAgenda() {
    String sql = "UPDATE agenda SET nama = ?, deskripsi = ?, kategori = ?, tanggal = ?, jam = ?, status = ? WHERE id = ?";
    try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, this.nama);
        pstmt.setString(2, this.deskripsi);
        pstmt.setString(3, this.kategori);
        pstmt.setDate(4, this.tanggal);
        pstmt.setString(5, this.jam);
        pstmt.setInt(6, this.status ? 1 : 0);
        pstmt.setInt(7, this.id);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
```
**File**: `AgendaPribadiFrame.java`  
Event pada tombol "Edit" untuk memperbarui agenda.
```
private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    int selectedRow = tabelAgenda.getSelectedRow();
    if (selectedRow >= 0) {
        int id = Integer.parseInt(tabelAgenda.getValueAt(selectedRow, 0).toString());
        String nama = teksNama.getText();
        String deskripsi = teksDeskripsi.getText();
        String kategori = comboKategori.getSelectedItem().toString();
        java.util.Date tanggalUtil = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String jam = sdf.format(jSpinner1.getValue());
        Boolean status = (Boolean) tabelAgenda.getValueAt(selectedRow, 6);

        if (tanggalUtil != null) {
            java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());
            Agenda agenda = new Agenda(id, nama, deskripsi, kategori, tanggal, jam, status);
            agenda.updateAgenda();
            muatData();
            kosongkanField();
            JOptionPane.showMessageDialog(this, "Agenda berhasil diupdate");
        } else {
            JOptionPane.showMessageDialog(this, "Pilih tanggal terlebih dahulu");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih agenda yang akan diubah");
    }
}
```
---

### 4. Menghapus Agenda
**File**: `Agenda.java`  
Fitur ini menghapus data agenda dari database.
```
public void hapusAgenda() {
    String sql = "DELETE FROM agenda WHERE id = ?";
    try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, this.id);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
```
**File**: `AgendaPribadiFrame.java`  
Event pada tombol "Hapus" untuk menghapus agenda.
```
private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    int selectedRow = tabelAgenda.getSelectedRow();
    if (selectedRow >= 0) {
        int id = Integer.parseInt(tabelAgenda.getValueAt(selectedRow, 0).toString());
        Agenda agenda = new Agenda(id, null, null, null, null, null, false);
        agenda.hapusAgenda();
        muatData();
        kosongkanField();
        JOptionPane.showMessageDialog(this, "Agenda berhasil dihapus");
    } else {
        JOptionPane.showMessageDialog(this, "Pilih agenda yang akan dihapus");
    }
}
```
---

## Teknologi yang Digunakan
- **Bahasa Pemrograman**: Java
- **Database**: SQLite
- **Library Eksternal**:
  - `sqlite-jdbc`: Untuk koneksi SQLite.
  - `com.toedter.calendar.JDateChooser`: Untuk memilih tanggal.

---

