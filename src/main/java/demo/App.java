/*
author : 
Syalom Elvin Pasau - 6182201077
Imanuel Alexander Here - 6182201013
*/

package demo;

import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        // Register the JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Buat connection URL, username, and password
        String url = "jdbc:sqlserver://MSI\\SQLEXPRESS01\\instanceName:50089;databaseName=Project_MIBD_JDBC";
        String username = "i22077"; 
        String password = "i22077";
        //supanika
        // Buat koneksi antara project java dengan server SQL.
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement stmt = connection.createStatement();
        
        // baca input dari pengguna
        int input; // variabel input akan digunakan untuk membaca pilihan dari pengguna
        while(true){ // program berjalan menggunakan looping untuk meminta input sebanyak yang diinginkan pengguna
            interfaceInput(); // method ini untuk menampilkan pilihan yang dapat dipilih 
            input = sc.nextInt(); // baca pilihan input dari pengguna
            if(input==5) { // jika pengguna memilih pilihan 5, artinya program berhenti
                System.out.print("Bye bye");
                break;
            }
            else if(input==3){ // jika pengguna memilih pilihan 3, artinya program akan menambahkan record pada tabel pengguna
                addRekordPengguna(sc, connection);
            }
            else if(input==4){ // jika pengguna memilih pilihan 4, artinya program akan menambahkan record pada tabel unit
                addRekordUnit(sc, connection);
            }
            else{ // jika pengguna memasukkan angka di luar rentang 1 hingga 5, maka program akan 'menolak' input pengguna
                System.out.println("Maaf, pilihan anda tidak tersedia.");
            }
            System.out.println();
        }
    }
    // method ini digunakan untuk menampilkan pilihan bagi pengguna
    public static void interfaceInput(){
        System.out.println("1. Buat tabel Pengguna");
        System.out.println("2. Buat tabel Unit");
        System.out.println("3. Tambah rekord Pengguna");
        System.out.println("4. Tambah rekord Unit");
        System.out.println("5. Exit");
        System.out.print("Masukkan input > ");
    }
    
    // method ini digunakan untuk membuat tabel pengguna pada SQL
    private static void createTabelPengguna(Statement stmt){
        try{ 
            // syntax dari Java untuk membuat tabel di SQL
            String tabelPengguna = "CREATE TABLE Pengguna ( "
                    + "NIK VARCHAR(25) NOT NULL, " // NIK sebagai PK untuk pengguna 
                    + "nama VARCHAR(50) NOT NULL, " // nama sebagai atribut dari pengguna dengan tipe data varchar dan panjang nama maksimal 50 karakter
                    + "alamat VARCHAR(50) NOT NULL, " // alamat sebagai atribut dari pengguna dengan tipe data varchar panjang alamat maksimal 50 karakter
                    + "noHp VARCHAR(20) NOT NULL"  // noHp sebagai atribut dari pengguna dengan tipe data varchar dan panjang noHp maksimal 20 karakter
                    + ")";
            // program akan meng-execute syntax Java ke dalam SQL
            stmt.executeUpdate(tabelPengguna);
            System.out.println("Tabel Pengguna berhasil dibuat");
        }
        catch(SQLException e){ // method ini dapat mengeluarkan error, yaitu error yang disebabkan karena tabel sudah ada sebelumnya.
            System.out.println("Tabel Pengguna sudah ada");
        }
    }
    
    // method ini digunakan untuk membuat tabel unit pada SQL
    private static void createTabelUnit(Statement stmt){
        try{
            // syntax dari Java untuk membuat tabel di SQL
            String tabelUnit = "CREATE TABLE Unit ( "
                    + "noUnit INT NOT NULL, " // atribut noUnit sebagai PK dari tabel unit dan bertipe data integer
                    + "lantai INT NOT NULL, " // atribut lantai dari tabel unit bertipe data integer untuk menyimpan informasi pada lantai berapa unit berada
                    + "statusKetersediaan VARCHAR(6) NOT NULL, " // -> atribut bertipe data varchar untuk menyimpan informasi unit penuh/kosong
                    + "harga INT NOT NULL" // atribut bertipe data integer untuk menyimpan harga dari unit
                    + ")";
            // program akan meng-execute syntax Java ke dalam SQL
            stmt.executeUpdate(tabelUnit);
            System.out.println("Tabel Unit berhasil dibuat");
        }
        catch(SQLException e){ // method ini dapat mengeluarkan error, yaitu error yang disebabkan karena tabel sudah ada sebelumnya.
            System.out.println("Tabel Unit sudah ada");
        }
    }
    
    // method ini digunakan untuk menambahkan record dari unit ke dalam tabel unit
    public static void addRekordUnit(Scanner scanner, Connection connection){
        // interface untuk pengguna memasukkan informasi dari unit
        System.out.print("Enter noUnit: ");
        int noUnit = scanner.nextInt();
        System.out.print("Enter lantai: ");
        int lantai = scanner.nextInt();
        System.out.print("Enter statusKetersediaan: ");
        String statusKetersediaan = scanner.next();
        System.out.print("Enter harga: ");
        int harga = scanner.nextInt();
        
        String insertQuery = "INSERT INTO Unit (noUnit, lantai, statusKetersediaan, harga) VALUES (?, ?, ?, ?)";

        // Prepare and execute insert query
        try{
            // syntax untuk memasukkan informasi yang sudah dimasukkan pengguna dari Java ke SQL
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, noUnit);
            preparedStatement.setInt(2, lantai);
            preparedStatement.setString(3, statusKetersediaan);
            preparedStatement.setInt(4, harga);
            
            // variabel untuk menyimpan berapa banyak baris yang affected dari hasil eksekusi insert query
            int rowsAffected = preparedStatement.executeUpdate();
            int totalRowsAffected = 0;
            
            if (rowsAffected > 0) { // jika ada baris yang terkena 'efek' dari eksekusi insert query, artinya ada baris baru yang ditambahkan.
                totalRowsAffected += rowsAffected;
                System.out.println("Data has been successfully added. Total number of rows added: " + totalRowsAffected);
            } else { // namun jika tidak ada baris yang terkena 'efek' dari eksekusi insert query, artinya tidak ada baris baru yang ditambahkan.
                System.out.println("Failed to insert data.");
            }
        }
        catch(SQLException e){ // method ini dapat mengeluarkan error, yaitu error yang terjadi apabila pengguna memasukkan record ke dalam tabel unit yang belum dibuat.
            
        }
    }
    
    // method ini digunakan untuk menambahkan record pengguna ke dalam tabel pengguna
    public static void addRekordPengguna(Scanner scanner, Connection connection){
        // interface bagi pengguna untuk memasukkan informasi mengenai pengguna
        System.out.print("Enter NIK: ");
        String NIK = scanner.next();
        scanner.nextLine();
        System.out.print("Enter nama: ");
        String nama = scanner.nextLine();
        System.out.print("Enter Alamat: ");
        String alamat = scanner.nextLine();
        System.out.print("Enter noHP: ");
        String noHP = scanner.next();
        scanner.nextLine();

        // Prepare and execute insert query
        try{
            // Syntax Java untuk memasukkan record baru ke dalam tabel SQL
            String insertQuery = "INSERT INTO Pengguna (NIK, nama, alamat, noHP) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, NIK);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, alamat);
            preparedStatement.setString(4, noHP);

            // variabel untuk menyimpan berapa banyak baris yang affected dari hasil eksekusi insert query
            int rowsAffected = preparedStatement.executeUpdate(); 
            int totalRowsAffected = 0;

            if (rowsAffected > 0) { // jika ada baris yang terkena 'efek' dari eksekusi insert query, artinya ada baris baru yang ditambahkan.
                totalRowsAffected += rowsAffected;
                System.out.println("Data has been successfully added. Total number of rows added: " + totalRowsAffected);
            } else { // namun jika tidak ada baris yang terkena 'efek' dari eksekusi insert query, artinya tidak ada baris baru yang ditambahkan.
                System.out.println("Failed to insert data."); 
            }
        }
        catch(SQLException e){ // method ini dapat mengeluarkan error, yaitu error yang terjadi apabila pengguna memasukkan record ke dalam tabel pengguna yang belum dibuat.
            System.out.println("Tabel Pengguna belum ada. Harap membuat tabel terlebih dahulu");
        }
    }
}
