package demo;

import java.sql.*;
import java.util.*;

public abstract class Perbaikan {
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
        interfaceInput();

        sc.close();
    }

    private static void interfaceInput(){
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
                interfaceLoginAgen();
                break;
            case "2":
                interfaceLoginPelanggan();
                break;
            case "3":
                System.out.println("Bye bye.");
                return;
            default:
                System.out.println("Maaf, pilihan tidak tersedia.");
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
            System.out.println("Gagal membuat tabel.");
        }
    }

    private static int generateOTP(){
        return (int) (Math.random()*99999) + 10000;
    }

    
}
