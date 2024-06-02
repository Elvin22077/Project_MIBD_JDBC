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

        // Buat koneksi antara project java dengan server SQL.
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement stmt = connection.createStatement();

        instanceAllEntity(stmt);
        interfaceInput(sc, connection);

        sc.close();
    }

    private static void interfaceInput(Scanner sc, Connection connection){
        System.out.println("----PILIH UNTUK LOGIN----");
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
        System.out.println("----LOGIN / REGISTRASI PELANGGAN----");
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
    
                while (true) {
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
                        break;
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
        System.out.println("----LOGIN AGEN----");
        System.out.println("1) Fitur-fitur Agen.");
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
        System.out.println("1) Mengelola data unit apartemen.");
        System.out.println("2) Mengelola jadwal ketersediaan.");
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
                System.out.println("Tolong masukkan input yang valid.");
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

                    System.out.println("Jenis unit sudah dirubah!");
                }
                catch(SQLException e){
                    System.out.println("Maaf, gagal mengubah jenis Unit.");
                    interfaceUtamaAgen(sc, connection);
                }
                break;
        
            default:
                interfaceUtamaAgen(sc, connection);
                break;
        }
    }

    private static void pengelolaanUnit(Connection connection, Scanner sc ){
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
                    
                    break;
                default:
                    break;
            }
        }
        catch(SQLException e){

        }
    }

    private static void interfaceUtamaPelanggan(Scanner sc, Connection connection){
        System.out.println("1) Mencari apartemen.");
        System.out.println("2) Memesan unit apartemen.");
        System.out.println("3) Check in dan check out sesuai tanggal pemesanan.");
        System.out.println("4) Memberikan rating dan komentar atas unit apartemen yang disewa setelah check out.");
        System.out.println("5) Exit.");
    }
}
