package demo;

import java.sql.*;
import java.util.*;

public class Perbaikan {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Buat connection URL, username, and password
        String url = "jdbc:sqlserver://MSI\\SQLEXPRESS01\\instanceName:50089;databaseName=Project_MIBD_JDBC";
        String username = "i22077"; 
        String password = "i22077";

        //debug untuk device alex
        /* String url = "jdbc:sqlserver://LAPTOP-2NQQ286G\\SQLEXPRESS;encrypt=true;trustServerCertificate=true;databaseName=Demo";
        String username = "sa";
        String password = "alex123"; */

        // Buat koneksi antara project java dengan server SQL.
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement stmt = connection.createStatement();

        instanceAllEntity(stmt);
        interfaceInput(sc, connection);

        sc.close();
    }

    private static void interfaceInput(Scanner sc, Connection connection){
        System.out.println("---- LOGIN SewaY ----");
        System.out.println("1) Login sebagai Agen.");
        System.out.println("2) Login sebagai Pelanggan.");
        System.out.println("3) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");

        String input = sc.next();
        System.out.println();

        switch (input) {
            case "1":
                //masuk ke interface Login Agen
                interfaceLoginAgen(sc, connection);
                break;
            case "2":
                interfaceLoginPelanggan(sc, connection);
                break;
            default:
                break;
        }
    }

    private static void interfaceLoginPelanggan(Scanner sc, Connection connection){
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
                registerPelanggan(connection, sc);
                break;
            case "2":
                loginPelanggan(connection, sc);
                break;
            default:
                interfaceInput(sc, connection);
                break;
        }
    }

    private static void instanceAllEntity(Statement stmt){
        try{
            String createTableAgen = "CREATE TABLE Agen ("
            + "idAgen INT PRIMARY KEY IDENTITY(1,1) NOT NULL, "
            + "NIK VARCHAR(20) NOT NULL, "
            + "Nama VARCHAR(30) NOT NULL, "
            + "NoHp VARCHAR(13) NOT NULL, "
            + "alamat VARCHAR(50) NOT NULL "
            + ")";
            stmt.executeUpdate(createTableAgen);

            String createTablePelanggan = "CREATE TABLE Pelanggan ("
            + "idPelanggan INT PRIMARY KEY IDENTITY(1,1) NOT NULL, "
            + "NIK VARCHAR(20) NOT NULL, "
            + "Nama VARCHAR(30) NOT NULL, "
            + "NoHp VARCHAR(13) NOT NULL, "
            + "alamat VARCHAR(50) NOT NULL "
            + ")";
            stmt.executeUpdate(createTablePelanggan);

            String createTableTower = "CREATE TABLE Tower ("
            + "idTower INT PRIMARY KEY NOT NULL, "
            + "nama VARCHAR(1) NOT NULL"
            + ")";
            stmt.executeUpdate(createTableTower);

            String createTableUnit = "CREATE TABLE Unit ("
                + "kodeUnit VARCHAR(6) PRIMARY KEY NOT NULL, "
                + "noUnit INT NOT NULL, "
                + "lantai INT NOT NULL, "
                + "jenis VARCHAR(6) NOT NULL, "
                + "statusKetersediaan VARCHAR(6) NOT NULL, "
                + "harga FLOAT NOT NULL"
                + ")";
            stmt.executeUpdate(createTableUnit);

            String createTableKecamatan = "CREATE TABLE Kecamatan ("
                + "idKecamatan INT PRIMARY KEY NOT NULL, "
                + "namaKecamatan VARCHAR(30) NOT NULL"
                + ")";
            stmt.executeUpdate(createTableKecamatan);

            String createTableKelurahan = "CREATE TABLE Kelurahan ("
                + "idKelurahan INT PRIMARY KEY NOT NULL, "
                + "namaKelurahan VARCHAR(30) NOT NULL"
                + ")";
            stmt.executeUpdate(createTableKelurahan);

            String createTableReview = "CREATE TABLE Review ("
                + "kodeUnit VARCHAR(6) FOREIGN KEY REFERENCES Unit(kodeUnit) NOT NULL, "
                + "rating FLOAT NOT NULL, "
                + "komentar VARCHAR(500) NOT NULL"
                + ")";
            stmt.executeUpdate(createTableReview);

            String createTablePelangganKelurahan = "CREATE TABLE PelangganKelurahan ("
                + "idPelanggan INT NOT NULL, "
                + "idKelurahan INT NOT NULL, "
                + "FOREIGN KEY (idPelanggan) REFERENCES Pelanggan(idPelanggan), "
                + "FOREIGN KEY (idKelurahan) REFERENCES Kelurahan(idKelurahan)"
                + ")";
            stmt.executeUpdate(createTablePelangganKelurahan);

            String createTableUnitPelanggan = "CREATE TABLE UnitPelanggan ("
                + "kodeUnit VARCHAR(6) NOT NULL, "
                + "idPelanggan INT NOT NULL, "
                + "tarif FLOAT NOT NULL, "
                + "waktuSewa DATE NOT NULL, "
                + "waktuSelesai DATE NOT NULL, "
                + "FOREIGN KEY (kodeUnit) REFERENCES Unit(kodeUnit), "
                + "FOREIGN KEY (idPelanggan) REFERENCES Pelanggan(idPelanggan)"
                + ")";
            stmt.executeUpdate(createTableUnitPelanggan);
        }
        catch(SQLException e){
        }
    }

    private static int generateOTP(){
        return (int) (Math.random()*99999) + 10000;
    }

    private static void registerPelanggan(Connection connection, Scanner sc){
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

            int OTP = generateOTP();
            System.out.println("OTP untuk registrasi pelanggan : "+OTP);

            while (true) {
                System.out.print("Masukkan kode OTP: ");
                int input = sc.nextInt();

                if(input == OTP){
                    preparedStatement.executeUpdate();
                    System.out.println("Registrasi Pelanggan berhasil.");
                    System.out.println();
                    interfaceLoginPelanggan(sc, connection);
                }
                else{
                    System.out.println("Kode OTP salah, harap masukkan kembali.");
                    System.out.println();
                }
            }
        }
        catch(InputMismatchException e){
            System.out.println("Harap masukkan data dengan benar.");

        }
        catch(SQLException e){
            System.out.println("Gagal memasukkan data.");
        }
    }

    private static void loginPelanggan(Connection connection, Scanner sc){

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
                interfaceLoginPelanggan(sc, connection);
            } else { //jika noHp terdaftar

                int OTP = generateOTP();
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
                        interfaceUtamaPelanggan(sc, connection);
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
            interfaceInput(sc, connection);
        }
    }

    private static void interfaceLoginAgen(Scanner sc, Connection connection){
        System.out.println("---- LOGIN AGEN ----");
        System.out.println("1) Kelola Unit Apartemen.");
        System.out.println("2) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan (angkanya saja): ");
        try{
            String input = sc.next();
            sc.nextLine();
            System.out.println();
            if(input.equals("1")){
                interfaceUtamaAgen(sc, connection);
            }
            else if(input.equals("2")){
                interfaceInput(sc, connection);
            }
            else{
                System.out.println("Maaf pilihan tidak tersedia.");
                interfaceLoginAgen(sc, connection);
            }
        }
        catch(InputMismatchException e){
            System.out.println("Harap memasukkan pilihan dengan benar");
            System.out.println();
            interfaceInput(sc, connection);
        }
    }

    private static void interfaceUtamaAgen(Scanner sc, Connection connection){
        System.out.println("---- Selamat Datang Agen SewaY ----");
        System.out.println("1) Kelola Data Unit.");
        System.out.println("2) Kelola Ketersediaan Unit.");
        System.out.println("3) Melihat unit yang telah dipesan pada waktu tertentu.");
        System.out.println("4) Melihat daftar check in dan check out pada tanggal tertentu.");
        System.out.println("5) Melihat laporan utilitas penyewaan unit-unit yang dikelola pada rentang waktu tertentu.");
        System.out.println("6) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");

        String input = sc.next();
        sc.nextLine();

        switch (input) {
            case "1":
                // mengelola unit apartemen
                mengubahJenisUnit(connection, sc);
                break;

            case "2":
                pengelolaanUnit(connection, sc);
                break;

            case "3":

                break;

            case "4":

                break;

            case "5":

                break;

            case "6":
                interfaceLoginAgen(sc, connection);
                break;
        
            default:
                System.out.println("Mohon masukkan input yang valid.");
                break;
        }
    }

    private static void mengubahJenisUnit(Connection connection, Scanner sc){
        System.out.println("1) Ubah jenis unit.");
        System.out.println("2) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");
        String input = sc.next();
        sc.nextLine();

        switch (input) {
            case "1":
                System.out.println();
                System.out.print("Masukkan kode unit yang ingin diubah jenisnya: ");
                String kodeUnit = sc.next();
                sc.nextLine();

                try{
                    System.out.print("Masukkan jenis unit baru: ");
                    String newType = sc.next();
                    sc.nextLine();

                    String update = "UPDATE Unit SET jenis = ? WHERE kodeUnit = ?";
            
                    PreparedStatement preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setString(1, newType);
                    preparedStatement.setString(2, kodeUnit);

                    preparedStatement.executeUpdate();

                    System.out.println("Jenis unit sudah dirubah!\n");
                    mengubahJenisUnit(connection, sc);
                }
                catch(SQLException e){
                    System.out.println("Maaf, gagal mengubah jenis Unit.\n");
                    mengubahJenisUnit(connection, sc);
                }
                break;
        
            default:
                interfaceUtamaAgen(sc, connection);
                break;
        }
    }

    private static void pengelolaanUnit(Connection connection, Scanner sc ){
        System.out.println("---- PENGELOLAAN UNIT SewaY ----");
        System.out.println("1) Melihat status unit.");
        System.out.println("2) Mengubah tanggal ketersediaan unit.");
        System.out.println("3) Mengubah status ketersediaan unit.");
        System.out.println("4) Mengubah tarif sewa unit.");
        System.out.println("5) Exit.");
        System.out.println();

        System.out.print("Masukkan pilihan(angkanya saja): ");
        String input = sc.next();
        sc.nextLine();
        System.out.println();

        try{
            switch (input) {
                case "1":
                    System.out.println("---- Status Unit ----");
                    String query = "SELECT "
                    + "Unit.kodeUnit, "
                    + "Unit.statusKetersediaan, "
                    + "Unit.harga, "
                    + "UnitPelanggan.waktuSewa, "
                    + "UnitPelanggan.waktuSelesai FROM Unit JOIN UnitPelanggan ON Unit.kodeUnit = UnitPelanggan.kodeUnit";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    ResultSet rs = preparedStatement.executeQuery();
                    System.out.printf("%-15s%-25s%-20s%-20s%-20s%n", 
                        "Kode Unit",
                        "Status Ketersediaan",
                        "Harga per malam",
                        "Waktu Mulai Sewa",
                        "Waktu Selesai Sewa"
                    );
                    while(rs.next()){
                        // kodeUnit, statusKetersediaan, harga, waktuSewa, waktuSelesai
                        System.out.printf("%-15s%-25s%-20s%-20s%-20s%n", 
                        rs.getString("kodeUnit"),
                        rs.getString("statusKetersediaan"),
                        rs.getFloat("harga"),
                        rs.getDate("waktuSewa"),
                        rs.getDate("waktuSelesai"));
                    }
                    break;
                
                case "2":
                    System.out.println("---- Ubah Tanggal Ketersediaan Unit ----");
                    System.out.println("1) Mengubah tanggal mulai sewa unit.");
                    System.out.println("2) Mengubah tanggal selesai sewa unit.");
                    System.out.println("3) Exit.");
                    System.out.println();

                    System.out.print("Masukkan pilihan(angkanya saja): ");
                    String input2 = sc.next();
                    sc.nextLine();
                    System.out.println();
                    switch (input2) {
                        case "1":
                            System.out.print("Masukkan kode Unit yang ingin diubah waktu mulai sewa: ");
                            String kodeUnit = sc.next();
                            sc.nextLine();
                            System.out.println();
                            System.out.print("Masukkan tanggal mulai sewa yang baru(format : yyyy-mm-dd): ");
                            String newDate = sc.next();
                            sc.nextLine();

                            query = "UPDATE UnitPelanggan SET waktuSewa = ? WHERE kodeUnit = ?";
                            preparedStatement = connection.prepareStatement(query);
                            
                            preparedStatement.setString(1, newDate);
                            preparedStatement.setString(2, kodeUnit);

                            preparedStatement.executeUpdate();
                            System.out.println("Berhasil mengubah tanggal akhir sewa dari unit.\n");
                            pengelolaanUnit(connection, sc);
                            break;

                        case "2": 
                            System.out.print("Masukkan kode Unit yang ingin diubah waktu akhir sewa: ");
                            kodeUnit = sc.next();
                            sc.nextLine();
                            System.out.println();
                            System.out.print("Masukkan tanggal akhir sewa yang baru(format : yyyy-mm-dd): ");
                            newDate = sc.next();
                            sc.nextLine();

                            query = "UPDATE UnitPelanggan SET waktuSelesai = ? WHERE kodeUnit = ?";
                            preparedStatement = connection.prepareStatement(query);
                            
                            preparedStatement.setString(1, newDate);
                            preparedStatement.setString(2, kodeUnit);

                            preparedStatement.executeUpdate();

                            System.out.println("Berhasil mengubah tanggal akhir sewa dari unit.\n");
                            break;
                    
                        default:
                            System.out.println("Pilihan tersebut tidak tersedia.");
                            System.out.println();
                            pengelolaanUnit(connection, sc);
                            break;
                    }
                    break;

                case "3":
                    System.out.println("---- Ubah Status Ketersediaan Unit ----");
                    System.out.print("Masukkan kode unit yang akan diubah status ketersediaannya: ");
                    String kodeUnit = sc.next();
                    sc.nextLine();
                    System.out.println();

                    System.out.print("Masukkan status ketersediaan yang baru: ");
                    String statusKetersediaan = sc.next();
                    sc.nextLine();
                    System.out.println();

                    try{
                        query = "UPDATE Unit SET statusKetersediaan = ? WHERE kodeUnit = ?";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, statusKetersediaan);
                        preparedStatement.setString(2, kodeUnit);

                        preparedStatement.executeUpdate();
                        System.out.println("Berhasil mengubah status ketersediaan unit.");
                        System.out.println();
                        pengelolaanUnit(connection, sc);
                    }
                    catch(SQLException e){
                        System.out.println("Gagal mengubah status ketersediaan unit.");
                        System.out.println();
                        pengelolaanUnit(connection, sc);
                    }
                    break;

                case "5":
                    pengelolaanUnit(connection, sc);
                    break;

                default:
                    System.out.println("Pilihan tersebut tidak tersedia.");
                    System.out.println();
                    pengelolaanUnit(connection, sc);
                    break;
            }
        }
        catch(SQLException e){
            System.out.println("Terdapat error dalam mengelola Unit.");
        }
    }

    private static void interfaceUtamaPelanggan(Scanner sc, Connection connection) {
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
                mencariApartemen(connection, sc);
                break;
            case "2":
                memesanUnitApartemen(connection, sc);
                break;
            case "3":
                checkInOut(connection, sc);
                break;
            case "4":
                memberikanRatingDanKomentar(connection, sc);
                break;
            case "5":
                interfaceLoginPelanggan(sc, connection);
                break;
            default:
                System.out.println("Pilihan tidak tersedia.");
                interfaceUtamaPelanggan(sc, connection);
                break;
        }
    }
    
    private static void mencariApartemen(Connection connection, Scanner sc) {
        System.out.println("---- MENCARI APARTEMEN SewaY ----");
        System.out.println("1) Mencari berdasarkan lokasi.");
        System.out.println("2) Mencari berdasarkan harga.");
        System.out.println("3) Mencari berdasarkan kategori.");
        System.out.println("4) Exit.");
        System.out.println();
        System.out.print("Masukkan pilihan(angkanya saja): ");
        String input = sc.next();
        sc.nextLine();
    
        switch (input) {
            case "1":
                mencariApartemenBerdasarkanLokasi(connection, sc);
                break;
            case "2":
                mencariApartemenBerdasarkanHarga(connection, sc);
                break;
            case "3":
                mencariApartemenBerdasarkanKategori(connection, sc);
                break;
            case "4":
                interfaceUtamaPelanggan(sc, connection);
                break;
            default:
                System.out.println("Pilihan tidak tersedia.");
                mencariApartemen(connection, sc);
                break;
        }
    }
    
    private static void mencariApartemenBerdasarkanLokasi(Connection connection, Scanner sc) {
        try {
            System.out.print("Masukkan lokasi yang ingin dicari (e.g. Jakarta, Bandung, etc.): ");
            String lokasi = sc.nextLine();
    
            String query = "SELECT * FROM Unit JOIN Kelurahan ON Unit.kodeUnit = Kelurahan.kodeUnit WHERE Kelurahan.namaKelurahan LIKE?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + lokasi + "%");
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.println("Hasil pencarian:");
            while (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Nama Kelurahan: " + rs.getString("namaKelurahan"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void mencariApartemenBerdasarkanHarga(Connection connection, Scanner sc) {
        try {
            System.out.print("Masukkan harga minimal yang ingin dicari: ");
            float hargaMin = sc.nextFloat();
            sc.nextLine();
    
            System.out.print("Masukkan harga maksimal yang ingin dicari: ");
            float hargaMax = sc.nextFloat();
            sc.nextLine();
    
            String query = "SELECT * FROM Unit WHERE harga BETWEEN? AND?";
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
    
    private static void mencariApartemenBerdasarkanKategori(Connection connection, Scanner sc) {
        try {
            System.out.print("Masukkan kategori apartemen yang ingin dicari (e.g. studio, 1BR, 2BR, etc.): ");
            String kategori = sc.nextLine();
    
            String query = "SELECT * FROM Unit WHERE jenis LIKE?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + kategori + "%");
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.println("Hasil pencarian:");
            while (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void memesanUnitApartemen(Connection connection, Scanner sc) {
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
    
    private static void checkInOut(Connection connection, Scanner sc) {
        System.out.print("Masukkan kode unit yang ingin dicek in/out: ");
        String kodeUnit = sc.next();
        sc.nextLine();
    
        try {
            String query = "SELECT * FROM Unit WHERE kodeUnit =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kodeUnit);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            if (rs.next()) {
                System.out.println("Kode Unit: " + rs.getString("kodeUnit"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Harga: " + rs.getFloat("harga"));
                System.out.println("Status Ketersediaan: " + rs.getString("statusKetersediaan"));
                System.out.println();
    
                System.out.print("Masukkan tanggal check in (format: yyyy-mm-dd): ");
                String tglCheckIn = sc.next();
                sc.nextLine();
    
                System.out.print("Masukkan tanggal check out (format: yyyy-mm-dd): ");
                String tglCheckOut = sc.next();
                sc.nextLine();
    
                query = "INSERT INTO CheckInOut (kodeUnit, idPelanggan, tglCheckIn, tglCheckOut) VALUES (?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, kodeUnit);
                preparedStatement.setInt(2, 1); // contoh idPelanggan =1
                preparedStatement.setString(3, tglCheckIn);
                preparedStatement.setString(4, tglCheckOut);
    
                preparedStatement.executeUpdate();
    
                System.out.println("Check in/check out berhasil.");
            } else {
                System.out.println("Unit apartemen tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void memberikanRatingDanKomentar(Connection connection, Scanner sc) {
        System.out.println("Rating");
    }
}
