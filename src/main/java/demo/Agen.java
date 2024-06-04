
package demo;

import java.util.*;
import java.sql.*;

public class Agen{
    private Scanner sc;
    private Connection connection;

    public Agen(Scanner sc, Connection connection){
        super();
        this.sc = sc;
        this.connection = connection;
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
                App.interfaceInput();
            }
            else{
                System.out.println("Maaf pilihan tidak tersedia.");
                interfaceLoginAgen();
            }
        }
        catch(InputMismatchException e){
            System.out.println("Harap memasukkan pilihan dengan benar");
            System.out.println();
            App.interfaceInput();
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
                    mengubahJenisUnit();
                }
                catch(SQLException e){
                    System.out.println("Maaf, gagal mengubah jenis Unit.\n");
                    mengubahJenisUnit();
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
                            pengelolaanUnit();
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

                case "5":
                    pengelolaanUnit();
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

    public void melihatUnit(){
        System.out.print("Masukkan tanggal untuk melihat unit yang tersedia(format : yyyy-mm-dd): ");
        String tanggal = sc.next();
        sc.nextLine();

        try{
            String query = "SELECT * FROM UnitPelanggan WHERE waktuSewa <= ? AND waktuSelesai >= ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tanggal);
            preparedStatement.setString(2, tanggal);

            ResultSet rs = preparedStatement.executeQuery();

            System.out.printf("%-10s%-10s%-10s%-10s%-10s%-10s%n", 
                    ("kodeUnit"),
                    ("noUnit"),
                    ("lantai"),
                    ("jenis"),
                    ("status"),
                    ("harga"));

            while(rs.next()){
                String kodeUnit = rs.getString("kodeUnit");
                String joinQuery = "SELECT * FROM UnitPelanggan JOIN Unit ON UnitPelanggan.kodeUnit = Unit.kodeUnit WHERE Unit.kodeUnit = ?";
                preparedStatement = connection.prepareStatement(joinQuery);
                preparedStatement.setString(1, kodeUnit);

                ResultSet joinset = preparedStatement.executeQuery();
                while(joinset.next()){
                    System.out.printf("%-10s%-10d%-10d%-10s%-10s%-10f%n", 
                    joinset.getString("kodeUnit"),
                    joinset.getInt("noUnit"),
                    joinset.getInt("lantai"),
                    joinset.getString("jenis"),
                    joinset.getString("statusKetersediaan"),
                    joinset.getDouble("harga"));

                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void melihatCheckInDanCheckOutPelanggan(){
        System.out.print("Masukkan tanggal: ");
        String tanggal = sc.next();
        sc.nextLine();

        try{
            // Cek In
            String queryCheckIn = "SELECT * FROM UnitPelanggan JOIN Pelanggan ON UnitPelanggan.idPelanggan = Pelanggan.idPelanggan WHERE waktuSewa = ?"; 
            PreparedStatement preparedStatement = connection.prepareStatement(queryCheckIn);
            preparedStatement.setString(1, tanggal);

            ResultSet rsCekIn = preparedStatement.executeQuery();
            System.out.println("--- CHECK IN ---");
            System.out.printf("%-15s%-10s%n",
            "idPelanggan",
            "Nama Pelanggan");
            while(rsCekIn.next()){
                System.out.printf("%-15d%-10s%n",
                rsCekIn.getInt("idPelanggan"),
                rsCekIn.getString("Nama"));
            }

            // Cek In
            String queryCheckOut = "SELECT * FROM UnitPelanggan JOIN Pelanggan ON UnitPelanggan.idPelanggan = Pelanggan.idPelanggan WHERE waktuSelesai  = ?"; 
            preparedStatement = connection.prepareStatement(queryCheckOut);
            preparedStatement.setString(1, tanggal);

            System.out.println("--- CHECK OUT ---");
            System.out.printf("%-15s%-10s%n",
            "idPelanggan",
            "Nama Pelanggan");
            ResultSet rsCekOut = preparedStatement.executeQuery();
            while(rsCekOut.next()){
                System.out.printf("%-15d%-10s%n",
                rsCekOut.getInt("idPelanggan"),
                rsCekOut.getString("Nama"));
            }
            System.out.println();
            interfaceUtamaAgen();
        }
        catch(SQLException e){
            System.out.println("Gagal mencari data check-in dan check-out");
        }
    }

    public void 
}
