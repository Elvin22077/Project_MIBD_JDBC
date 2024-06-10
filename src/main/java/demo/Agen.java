
package demo;

import java.util.*;
import java.sql.*;

public class Agen extends App{
    private Scanner sc;
    private Connection connection;

    public Agen(Scanner sc, Connection connection){
        super();
        this.sc = sc;
        this.connection = connection;
    }

    public void loginAgen(){
        System.out.print("Masukkan nomor Hp agen yang sudah terdaftar: ");
        String noHp = sc.next();
        sc.nextLine();

        try{
            String query = "SELECT NIK FROM Agen WHERE NoHp = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, noHp);

            ResultSet tempAgen = preparedStatement.executeQuery();

            if(!tempAgen.isBeforeFirst()){
                System.out.println("Maaf, nomor Hp belum terdaftar. ");
                System.out.println();
                super.interfaceInput();
            }
            else{
                boolean login = false;
                int OTP = super.generateOTP();
                System.out.println("OTP untuk login: "+OTP);
                while(!login){
                    System.out.println();
                    System.out.print("Masukkan OTP: ");
                    int inputOTP = sc.nextInt();
                    sc.nextLine();
                    if(OTP==inputOTP){
                        System.out.println("Berhasil login sebagai agen! Selamat Datang!");
                        login = true;
                        System.out.println();
                        interfaceLoginAgen();
                    }
                    else{
                        System.out.println("Maaf, kode OTP salah. Silakan coba lagi.");
                    }
                }
            }   
        }
        catch(SQLException e){
            System.out.println("Gagal login sebagai agen.");
            System.out.println();
            super.interfaceInput();
        }
    }

    public void interfaceLoginAgen(){
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
                interfaceUtamaAgen();
            }
            else if(input.equals("2")){
                super.interfaceInput();
            }
            else{
                System.out.println("Maaf pilihan tidak tersedia.");
                interfaceLoginAgen();
            }
        }
        catch(InputMismatchException e){
            System.out.println("Harap memasukkan pilihan dengan benar");
            System.out.println();
            super.interfaceInput();
        }
    }

    public void interfaceUtamaAgen(){
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
                mengubahJenisUnit();
                break;

            case "2":
                pengelolaanUnit();
                break;

            case "3":
                melihatUnit();
                break;

            case "4":
                melihatCheckInDanCheckOutPelanggan();
                break;

            case "5":
                melihatLaporanUtilitas();
                break;

            case "6":
                interfaceLoginAgen();
                break;
        
            default:
                System.out.println("Mohon masukkan input yang valid.");
                break;
        }
    }

    public void mengubahJenisUnit(){
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
                    interfaceUtamaAgen();
                }
                catch(SQLException e){
                    System.out.println("Maaf, gagal mengubah jenis Unit.\n");
                    interfaceUtamaAgen();
                }
                break;
        
            default:
                interfaceUtamaAgen();
                break;
        }
    }

    public void pengelolaanUnit(){
        System.out.println("---- PENGELOLAAN UNIT SewaY ----");
        System.out.println("1) Melihat status unit.");
        System.out.println("2) Mengubah tanggal ketersediaan unit.");
        System.out.println("3) Mengubah status ketersediaan unit.");
        System.out.println("4) Mengubah tarif sewa unit.");
        System.out.println("5) Exit.");
        System.out.println();
    
        System.out.print("Masukkan pilihan (angkanya saja): ");
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
                        + "Transaksi.waktuSewa, "
                        + "Transaksi.waktuSelesai "
                        + "FROM Unit JOIN Transaksi ON Unit.kodeUnit = Transaksi.kodeUnit";
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
                    System.out.println();
                    pengelolaanUnit();
                    break;
                    
                case "2":
                    System.out.println("---- Ubah Tanggal Ketersediaan Unit ----");
                    System.out.println("1) Mengubah tanggal mulai sewa unit.");
                    System.out.println("2) Mengubah tanggal selesai sewa unit.");
                    System.out.println("3) Exit.");
                    System.out.println();
    
                    System.out.print("Masukkan pilihan (angkanya saja): ");
                    String input2 = sc.next();
                    sc.nextLine();
                    System.out.println();
                    switch (input2) {
                        case "1":
                            System.out.print("Masukkan kode Unit yang ingin diubah waktu mulai sewa: ");
                            String kodeUnit = sc.next();
                            sc.nextLine();
                            System.out.println();
                            System.out.print("Masukkan tanggal mulai sewa yang baru (format: yyyy-mm-dd): ");
                            String newDate = sc.next();
                            sc.nextLine();
    
                            query = "UPDATE Transaksi SET waktuSewa = ? WHERE kodeUnit = ?";
                            preparedStatement = connection.prepareStatement(query);
                            
                            preparedStatement.setString(1, newDate);
                            preparedStatement.setString(2, kodeUnit);
    
                            preparedStatement.executeUpdate();
                            System.out.println("Berhasil mengubah tanggal mulai sewa dari unit.\n");
                            pengelolaanUnit();
                            break;
    
                        case "2": 
                            System.out.print("Masukkan kode Unit yang ingin diubah waktu selesai sewa: ");
                            kodeUnit = sc.next();
                            sc.nextLine();
                            System.out.println();
                            System.out.print("Masukkan tanggal selesai sewa yang baru (format: yyyy-mm-dd): ");
                            newDate = sc.next();
                            sc.nextLine();
    
                            query = "UPDATE Transaksi SET waktuSelesai = ? WHERE kodeUnit = ?";
                            preparedStatement = connection.prepareStatement(query);
                            
                            preparedStatement.setString(1, newDate);
                            preparedStatement.setString(2, kodeUnit);
    
                            preparedStatement.executeUpdate();
    
                            System.out.println("Berhasil mengubah tanggal selesai sewa dari unit.\n");
                            System.out.println();
                            pengelolaanUnit();
                            break;
                    
                        default:
                            System.out.println("Pilihan tersebut tidak tersedia.");
                            System.out.println();
                            pengelolaanUnit();
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
                        pengelolaanUnit();
                    }
                    catch(SQLException e){
                        System.out.println("Gagal mengubah status ketersediaan unit.");
                        System.out.println();
                        pengelolaanUnit();
                    }
                    break;
    
                case "4":
                    gantiTarifUnit();
                    break;
    
                case "5":
                    interfaceUtamaAgen();
                    break;
    
                default:
                    System.out.println("Pilihan tersebut tidak tersedia.");
                    System.out.println();
                    pengelolaanUnit();
                    break;
            }
        }
        catch(SQLException e){
            System.out.println("Terdapat error dalam mengelola Unit.");
        }
    }
    

    public void melihatUnit() {
        System.out.print("Masukkan tanggal untuk melihat unit yang tersedia (format: yyyy-mm-dd): ");
        String tanggal = sc.next();
        sc.nextLine();
    
        try {
            String query = "SELECT Unit.kodeUnit, Unit.noUnit, Unit.lantai, Unit.jenis, Unit.statusKetersediaan, Unit.harga " +
                           "FROM Unit " +
                           "LEFT JOIN Transaksi ON Unit.kodeUnit = Transaksi.kodeUnit " +
                           "AND Transaksi.waktuSewa <= ? " +
                           "AND Transaksi.waktuSelesai >= ? " +
                           "WHERE Transaksi.kodeUnit IS NULL";
    
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tanggal);
            preparedStatement.setString(2, tanggal);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.printf("%-10s%-10s%-10s%-10s%-20s%-10s%n",
                    "kodeUnit",
                    "noUnit",
                    "lantai",
                    "jenis",
                    "statusKetersediaan",
                    "harga");
    
            while (rs.next()) {
                System.out.printf("%-10s%-10d%-10d%-10s%-20s%-10f%n",
                        rs.getString("kodeUnit"),
                        rs.getInt("noUnit"),
                        rs.getInt("lantai"),
                        rs.getString("jenis"),
                        rs.getString("statusKetersediaan"),
                        rs.getDouble("harga"));
            }
            System.out.println();
<<<<<<< HEAD
            pengelolaanUnit();
        } catch (SQLException e) {
=======
            interfaceUtamaAgen();
        }
        catch(SQLException e){
>>>>>>> a54899da91258cc7c349305c81debb5e7524131f
            e.printStackTrace();
        }
    }
    

    public void melihatCheckInDanCheckOutPelanggan() {
        System.out.print("Masukkan tanggal (format: yyyy-mm-dd): ");
        String tanggal = sc.next();
        sc.nextLine();
    
        try {
            // Check In
            String queryCheckIn = "SELECT * FROM Transaksi JOIN Pelanggan ON Transaksi.idPelanggan = Pelanggan.idPelanggan WHERE waktuSewa = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryCheckIn);
            preparedStatement.setString(1, tanggal);
    
            ResultSet rsCheckIn = preparedStatement.executeQuery();
            System.out.println("--- CHECK IN ---");
            System.out.printf("%-15s%-10s%n",
                    "idPelanggan",
                    "Nama Pelanggan");
            while (rsCheckIn.next()) {
                System.out.printf("%-15d%-10s%n",
                        rsCheckIn.getInt("idPelanggan"),
                        rsCheckIn.getString("Nama"));
            }
    
            // Check Out
            String queryCheckOut = "SELECT * FROM Transaksi JOIN Pelanggan ON Transaksi.idPelanggan = Pelanggan.idPelanggan WHERE waktuSelesai = ?";
            preparedStatement = connection.prepareStatement(queryCheckOut);
            preparedStatement.setString(1, tanggal);
    
            System.out.println("--- CHECK OUT ---");
            System.out.printf("%-15s%-10s%n",
                    "idPelanggan",
                    "Nama Pelanggan");
            ResultSet rsCheckOut = preparedStatement.executeQuery();
            while (rsCheckOut.next()) {
                System.out.printf("%-15d%-10s%n",
                        rsCheckOut.getInt("idPelanggan"),
                        rsCheckOut.getString("Nama"));
            }
            System.out.println();
<<<<<<< HEAD
            pengelolaanUnit();
        } catch (SQLException e) {
=======
            interfaceUtamaAgen();
        }
        catch(SQLException e){
>>>>>>> a54899da91258cc7c349305c81debb5e7524131f
            System.out.println("Gagal mencari data check-in dan check-out");
            System.out.println();
            interfaceUtamaAgen();
        }
    }
    

    public void gantiTarifUnit(){
        System.out.print("Masukkan kode unit yang akan diubah tarifnya: ");
        String kodeUnit = sc.next();
        sc.nextLine();
        System.out.print("Masukka harga yang baru: ");
        double harga = sc.nextDouble();

        try{
            String query = "UPDATE Unit SET harga = ? WHERE kodeUnit = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, harga);
            preparedStatement.setString(2, kodeUnit);

            preparedStatement.executeUpdate();

            System.out.println("Harga unit berhasil diubah!");
            System.out.println();
            interfaceUtamaAgen();
        }
        catch(SQLException e){
            System.out.println("Gagal mengganti harga unit.");
            System.out.println();
            interfaceUtamaAgen();
        }
    }

    public void melihatLaporanUtilitas(){
        System.out.println("Masukkan rentang tanggal (format: yyyy-mm-dd): ");
        System.out.print("Mulai dari: ");
        String tanggal1 = sc.next();
        sc.nextLine();
        System.out.print("Hingga: ");
        String tanggal2 = sc.next();
        sc.nextLine();
    
        try{
            String query = "SELECT Transaksi.kodeUnit, count(Transaksi.kodeUnit) AS 'countUnit', sum(harga) AS 'countHargaUnit' "
                         + "FROM Transaksi "
                         + "WHERE Transaksi.waktuSewa BETWEEN ? AND ? "
                         + "GROUP BY Transaksi.kodeUnit";
    
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tanggal1);
            preparedStatement.setString(2, tanggal2);
    
            ResultSet rs = preparedStatement.executeQuery();
    
            System.out.printf("%-20s%-30s%-30s%n",
                              "Kode Unit",
                              "Banyak Unit yang disewa",
                              "Total harga sewa unit");
            while(rs.next()){
                System.out.printf("%-20s%-30d%-30f%n", 
                                  rs.getString("kodeUnit"), 
                                  rs.getInt("countUnit"), 
                                  rs.getDouble("countHargaUnit"));
            }
            System.out.println();
            interfaceUtamaAgen();
    
        } catch(SQLException e){
            System.out.println("Gagal mencari Utilitas");
            System.out.println();
            interfaceUtamaAgen();
        }
    }    
}
