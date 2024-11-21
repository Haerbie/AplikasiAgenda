// Agenda.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private int id;
    private String nama;
    private String deskripsi;
    private String kategori;
    private Date tanggal;
    private String jam;
    private boolean status; // Tambahkan field status

    // Konstruktor
    public Agenda(int id, String nama, String deskripsi, String kategori, Date tanggal, String jam, boolean status) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.jam = jam;
        this.status = status;
    }

    public Agenda(String nama, String deskripsi, String kategori, Date tanggal, String jam, boolean status) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.jam = jam;
        this.status = status;
    }

    // Koneksi ke database SQLite
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:agenda.db";
            conn = DriverManager.getConnection(url);

            // Membuat tabel jika belum ada
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS agenda (\n"
                    + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "	nama TEXT NOT NULL,\n"
                    + "	deskripsi TEXT,\n"
                    + "	kategori TEXT,\n"
                    + "	tanggal DATE,\n"
                    + "	jam TEXT,\n"
                    + "	status INTEGER DEFAULT 0\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sqlCreateTable);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Metode untuk menyimpan agenda ke database
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

    // Metode untuk mengupdate agenda di database
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

    // Metode untuk menghapus agenda dari database
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

    // Metode untuk mendapatkan semua agenda
    public static List<Agenda> getAllAgenda() {
        List<Agenda> listAgenda = new ArrayList<>();
        String sql = "SELECT * FROM agenda";
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Agenda agenda = new Agenda(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("deskripsi"),
                        rs.getString("kategori"),
                        rs.getDate("tanggal"),
                        rs.getString("jam"),
                        rs.getInt("status") == 1 // Ambil status
                );
                listAgenda.add(agenda);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listAgenda;
    }

    // Metode untuk mencari agenda berdasarkan keyword
    public static List<Agenda> cariAgenda(String keyword) {
        List<Agenda> listAgenda = new ArrayList<>();
        String sql = "SELECT * FROM agenda WHERE nama LIKE ? OR deskripsi LIKE ?";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Agenda agenda = new Agenda(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("deskripsi"),
                        rs.getString("kategori"),
                        rs.getDate("tanggal"),
                        rs.getString("jam"),
                        rs.getInt("status") == 1
                );
                listAgenda.add(agenda);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listAgenda;
    }
    
    // Metode untuk mengupdate status agenda di database
    public void updateStatus() {
        String sql = "UPDATE agenda SET status = ? WHERE id = ?";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.status ? 1 : 0);
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Getter dan Setter
    // Tambahkan getter dan setter untuk status
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public boolean isStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
