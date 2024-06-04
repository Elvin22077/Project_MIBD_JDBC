package demo;

import java.sql.*;
import java.util.*;

public class Pelanggan{
    private Scanner sc;
    private Connection connection;

    public Pelanggan(Scanner sc, Connection connection) {
        super();
        this.sc = sc;
        this.connection = connection;
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
        System.out.println("3) Check in dan check out sesuai tanggal pemesanan.");
        System.out.println("4) Memberikan rating dan komentar atas unit apartemen yang disewa setelah check out.");
        System.out.println("5) Exit.");
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
                checkInOut();
                break;
            case "4":
                memberikanRatingDanKomentar();
                break;
            case "5":
                interfaceLoginPelanggan();
                break;
            default:
                System.out.println("Pilihan tidak tersedia.");
                interfaceUtamaPelanggan();
                break;
        }
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
                           "FROM UnitPelanggan UP " +
                           "JOIN Unit U ON UP.kodeUnit = U.kodeUnit " +
                           "WHERE UP.waktuSewa >= ? AND UP.waktuSelesai <= ? " +
                           "AND U.statusKetersediaan = 'kosong'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
    
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
    
                System.out.print("Masukkan tanggal mulai sewa (format: yyyy-mm-dd): ");
                String tglMulai = sc.next();
                sc.nextLine();
    
                System.out.print("Masukkan tanggal selesai sewa (format: yyyy-mm-dd): ");
                String tglSelesai = sc.next();
                sc.nextLine();
    
                query = "INSERT INTO UnitPelanggan (kodeUnit, idPelanggan, tarif, waktuSewa, waktuSelesai) VALUES (?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, kodeUnit);
                preparedStatement.setInt(2, 1); // replace with actual idPelanggan
                preparedStatement.setFloat(3, rs.getFloat("harga"));
                preparedStatement.setString(4, tglMulai);
                preparedStatement.setString(5, tglSelesai);
    
                preparedStatement.executeUpdate();
    
                System.out.println("Unit apartemen berhasil dipesan.");
            } else {
                System.out.println("Unit apartemen tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void checkInOut() {
        System.out.print("Masukkan kode unit yang ingin dicek in/out: ");
        String kodeUnit = sc.next();
        sc.nextLine();
    
        try {
            // Cari data pemesanan berdasarkan kodeUnit
            String query = "SELECT * FROM UnitPelanggan WHERE kodeUnit = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kodeUnit);
            ResultSet rs = preparedStatement.executeQuery();
    
            if (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("ID Pelanggan: " + rs.getInt("idPelanggan"));
                System.out.println("Tanggal Sewa: " + rs.getDate("waktuSewa"));
                System.out.println("Tanggal Selesai: " + rs.getDate("waktuSelesai"));
                System.out.println();
    
                System.out.print("Masukkan tanggal check in (format: yyyy-mm-dd): ");
                String tglCheckIn = sc.next();
                sc.nextLine();
    
                System.out.print("Masukkan tanggal check out (format: yyyy-mm-dd): ");
                String tglCheckOut = sc.next();
                sc.nextLine();
    
                // Validasi tanggal check in dan check out
                java.sql.Date waktuSewa = rs.getDate("waktuSewa");
                java.sql.Date waktuSelesai = rs.getDate("waktuSelesai");
                java.sql.Date checkInDate = java.sql.Date.valueOf(tglCheckIn);
                java.sql.Date checkOutDate = java.sql.Date.valueOf(tglCheckOut);
    
                if (checkInDate.before(waktuSewa) || checkOutDate.after(waktuSelesai)) {
                    System.out.println("Tanggal check in/check out tidak valid dengan tanggal pemesanan.");
                } else {
                    System.out.println("Check in dan check out berhasil.");
                }
            } else {
                System.out.println("Unit apartemen tidak ditemukan atau belum dipesan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void memberikanRatingDanKomentar() {
        System.out.print("Masukkan kode unit yang ingin diberi rating dan komentar: ");
        String kodeUnit = sc.next();
        sc.nextLine();
    
        try {
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
}
