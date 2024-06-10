package demo;

import java.sql.*;
import java.util.*;

public class Pelanggan{
    private Scanner sc;
    private Connection connection;
    private int idPelanggan;

    public Pelanggan(Scanner sc, Connection connection) {
        super();
        this.sc = sc;
        this.connection = connection;
    }
    public int getIdPelanggan() {
        return this.idPelanggan;
    }

    public void interfaceLoginPelanggan(){
        System.out.println("---- LOGIN / REGISTRASI PELANGGAN SewaY ----");
        System.out.println("1) Registrasi sebagai pelanggan baru.");
        System.out.println("2) Login sebagai pelanggan lama.");
        System.out.println("3) Logout.");

        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");
        String input = sc.next();
        System.out.println();

        switch (input) {
            case "1":
                registerPelanggan();
                break;
            case "2":
                loginPelanggan();
                break;
            case "3":
                App.interfaceInput();
            default:
                App.interfaceInput();
                break;
        }
    }

    
    public void registerPelanggan(){
        try{
            System.out.print("Masukkan NIK Pelanggan: ");
            String NIK = sc.next();
            sc.nextLine();
            System.out.print("Masukkan nama Pelanggan: ");
            String nama = sc.nextLine();
            System.out.print("Masukkan NoHP Pelanggan: ");
            String noHp = sc.next();
            sc.nextLine();
            System.out.print("Masukkan alamat Pelanggan: ");
            String alamat = sc.nextLine();
            

            String insert = "INSERT INTO Pelanggan(NIK, nama, NoHp, alamat) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, NIK);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, noHp);
            preparedStatement.setString(4, alamat);

            int OTP = App.generateOTP();
            System.out.println("OTP untuk registrasi pelanggan : "+OTP);

            while (true) {
                System.out.print("Masukkan kode OTP: ");
                int input = sc.nextInt();

                if(input == OTP){
                    preparedStatement.executeUpdate();
                    System.out.println("Registrasi Pelanggan berhasil.");
                    System.out.println();
                    interfaceLoginPelanggan();
                    break;
                }
                else{
                    System.out.println("Kode OTP salah, harap masukkan kembali.");
                    System.out.println();
                }
            }
        } catch (InputMismatchException e){
            System.out.println("Harap masukkan data dengan benar.");
        } catch(SQLException e){
            System.out.println("Gagal memasukkan data.");
        }
    }

    
    public void loginPelanggan(){
        try{
            System.out.print("Masukkan nomor Handphone: ");
            String noHP = sc.next();
            sc.nextLine();
            System.out.println();

            String search = String.format("SELECT * FROM Pelanggan WHERE NoHp = ?");
            
            PreparedStatement preparedStatement = connection.prepareStatement(search);
            preparedStatement.setString(1, noHP);

            ResultSet rs = preparedStatement.executeQuery();

            //jika noHp belum terdaftar
            if (!rs.isBeforeFirst()) { 
                System.out.println("Nomor handphone belum terdaftar, harap registrasi terlebih dahulu.");
                System.out.println();
                interfaceLoginPelanggan();
            } else { //jika noHp terdaftar

                int OTP = App.generateOTP();
                System.out.println("OTP untuk registrasi pelanggan : "+OTP);

                boolean login = false;
    
                while (!login) {
                    System.out.print("Masukkan kode OTP: ");
                    int input = sc.nextInt();
    
                    if(input == OTP){
                        System.out.println();
                        String name = null;
                        while(rs.next()){
                            name = rs.getString("Nama");
                            break;
                        }
                        this.idPelanggan = rs.getInt("idPelanggan");
                        System.out.printf("Login pelanggan sebagai %s berhasil!\n", name);
                        System.out.println();
                        interfaceUtamaPelanggan();
                        login = true;
                    }
                    else{
                        System.out.println("Kode OTP salah, harap masukkan kembali.");
                        System.out.println();
                    }
                }
            }

        }catch(SQLException e){
            System.out.println("Gagal melakukan proses login.");
            System.out.println();
            App.interfaceInput();
        }
    }

    public void interfaceUtamaPelanggan() {
        System.out.println("---- UTAMA PELANGGAN SewaY ----");
        System.out.println("1) Mencari apartemen.");
        System.out.println("2) Memesan unit apartemen.");
        System.out.println("3) Checkout dan Memberikan rating dan komentar");
        System.out.println("4) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");
        
        String input = sc.next();
        sc.nextLine();
    
        switch (input) {
            case "1":
                mencariApartemen();
                break;
            case "2":
                memesanUnitApartemen();
                break;
            case "3":
                memberikanRatingDanKomentar();
                break;
            case "4":
                interfaceLoginPelanggan();
                return;
            default:
                System.out.println("Pilihan tidak tersedia.");
                interfaceUtamaPelanggan();
                break;
        }
        interfaceUtamaPelanggan();
    }
    
    public void mencariApartemen() {
        System.out.println("---- MENCARI APARTEMEN SewaY ----");
        System.out.println("1) Mencari berdasarkan tanggal ketersediaan.");
        System.out.println("2) Mencari berdasarkan harga.");
        System.out.println("3) Mencari berdasarkan rating.");
        System.out.println("4) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");
        String input = sc.next();
        sc.nextLine();
    
        switch (input) {
            case "1":
                mencariApartemenBerdasarkanTanggalKetersediaan();
                break;
            case "2":
                mencariApartemenBerdasarkanHarga();
                break;
            case "3":
                mencariApartemenBerdasarkanRating();
                break;
            case "4":
                interfaceUtamaPelanggan();
                break;
            default:
                System.out.println("Pilihan tidak tersedia.");
                mencariApartemen();
                break;
        }
        mencariApartemen();
    }
    
    public void mencariApartemenBerdasarkanTanggalKetersediaan() {
        try {
            System.out.print("Masukkan tanggal awal sewa (YYYY-MM-DD): ");
            String startDate = sc.nextLine();
    
            System.out.print("Masukkan tanggal akhir sewa (YYYY-MM-DD): ");
            String endDate = sc.nextLine();
    
            String query = "SELECT U.kodeUnit, U.harga, U.statusKetersediaan " +
                           "FROM Unit U " +
                           "WHERE U.statusKetersediaan = 'kosong' " +
                           "AND U.kodeUnit NOT IN (" +
                           "    SELECT T.kodeUnit " +
                           "    FROM Transaksi T " +
                           "    WHERE (T.waktuSewa <= ? AND T.waktuSelesai >= ?) " +
                           "    OR (T.waktuSewa <= ? AND T.waktuSelesai >= ?) " +
                           "    OR (T.waktuSewa >= ? AND T.waktuSelesai <= ?)" +
                           ")";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, endDate); // Check if any transaction ends after startDate
            preparedStatement.setString(2, startDate); // Check if any transaction starts before endDate
            preparedStatement.setString(3, endDate); // Check if any transaction ends after startDate
            preparedStatement.setString(4, startDate); // Check if any transaction starts before endDate
            preparedStatement.setString(5, startDate); // Check if any transaction starts and ends within the range
            preparedStatement.setString(6, endDate); // Check if any transaction starts and ends within the range
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.println("Hasil pencarian:");
            while (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    public void mencariApartemenBerdasarkanHarga() {
        try {
            System.out.print("Masukkan harga minimal yang ingin dicari: ");
            float hargaMin = sc.nextFloat();
            sc.nextLine();
    
            System.out.print("Masukkan harga maksimal yang ingin dicari: ");
            float hargaMax = sc.nextFloat();
            sc.nextLine();
    
            String query = "SELECT * FROM Unit WHERE harga BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, hargaMin);
            preparedStatement.setFloat(2, hargaMax);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.println("Hasil pencarian:");
            while (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void mencariApartemenBerdasarkanRating() {
        try {
            System.out.print("Masukkan minimal rating yang ingin dicari: ");
            float minRating = sc.nextFloat();
            sc.nextLine();
    
            String query = "SELECT * FROM Review WHERE rating >= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, minRating);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.println("Hasil pencarian berdasarkan rating:");
            while (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Rating: " + rs.getFloat("rating"));
                System.out.println("Komentar: " + rs.getString("komentar"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void memesanUnitApartemen() {
        System.out.print("Masukkan kode unit yang ingin dipesan: ");
        String kodeUnit = sc.next();
        sc.nextLine();
    
        try {
            // Check unit details and availability
            String query = "SELECT * FROM Unit WHERE kodeUnit = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kodeUnit);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            if (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
    
                if (rs.getString("statusKetersediaan").equals("KOSONG")) {
                    // Get the price of the unit
                    float hargaUnit = rs.getFloat("harga");
    
                    // Find agent ID managing the unit
                    int idAgen;
                    query = "SELECT idAgen FROM Mengelola WHERE kodeUnit = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, kodeUnit);
                    rs = preparedStatement.executeQuery();
    
                    if (rs.next()) {
                        idAgen = rs.getInt("idAgen");
                    } else {
                        throw new SQLException("Agen untuk unit ini tidak ditemukan.");
                    }
    
                    System.out.print("Masukkan tanggal mulai sewa (format: yyyy-mm-dd): ");
                    String tglMulai = sc.next();
                    sc.nextLine();
    
                    System.out.print("Masukkan tanggal selesai sewa (format: yyyy-mm-dd): ");
                    String tglSelesai = sc.next();
                    sc.nextLine();

                    query = "INSERT INTO Transaksi (kodeUnit, idPelanggan, idAgen, waktuSewa, waktuSelesai, harga) VALUES (?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, kodeUnit);
                    preparedStatement.setInt(2, this.getIdPelanggan());
                    preparedStatement.setInt(3, idAgen);
                    preparedStatement.setString(4, tglMulai);
                    preparedStatement.setString(5, tglSelesai);
                    preparedStatement.setFloat(6, hargaUnit);
    
                    preparedStatement.executeUpdate();
                    updateUnitAvailability(kodeUnit);
    
                    System.out.println("Unit apartemen berhasil dipesan.");
                } else {
                    System.out.println("Unit apartemen tidak tersedia.");
                }
            } else {
                System.out.println("Unit apartemen tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateUnitAvailability(String kodeUnit) {
        try {
            String updateQuery = "UPDATE Unit SET statusKetersediaan = 'PENUH' WHERE kodeUnit = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, kodeUnit);
            int rowsAffected = updateStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Unit availability updated successfully.");
            } else {
                System.out.println("Unit with kodeUnit '" + kodeUnit + "' not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating unit availability: " + e.getMessage());
        }
    }
    
    public void memberikanRatingDanKomentar() {
        System.out.print("Masukkan kode unit yang ingin diberi rating dan komentar: ");
        String kodeUnit = sc.next();
        sc.nextLine();
    
        try {
            updateUnitAvailabilityToEmpty(kodeUnit);
            String query = "SELECT * FROM Unit WHERE kodeUnit = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kodeUnit);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            if (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
    
                System.out.print("Masukkan rating (antara 1.0 - 5.0): ");
                float rating = sc.nextFloat();
                sc.nextLine();
    
                System.out.print("Masukkan komentar: ");
                String komentar = sc.nextLine();
    
                query = "INSERT INTO Review (kodeUnit, rating, komentar) VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, kodeUnit);
                preparedStatement.setFloat(2, rating);
                preparedStatement.setString(3, komentar);
    
                preparedStatement.executeUpdate();
    
                System.out.println("Rating dan komentar berhasil ditambahkan.");
            } else {
                System.out.println("Unit apartemen tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateUnitAvailabilityToEmpty(String kodeUnit) {
        try {
            String updateQuery = "UPDATE Unit SET statusKetersediaan = 'KOSONG' WHERE kodeUnit = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, kodeUnit);
            int rowsAffected = updateStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Unit availability updated to 'KOSONG' successfully.");
            } else {
                System.out.println("Unit with kodeUnit '" + kodeUnit + "' not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating unit availability: " + e.getMessage());
        }
    }
    
}
