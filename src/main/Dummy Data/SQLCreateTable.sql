---Create Table Agen
CREATE TABLE Agen (
idAgen INT PRIMARY KEY IDENTITY(1,1) NOT NULL, 
NIK VARCHAR(16) NOT NULL, 
Nama VARCHAR(30) NOT NULL, 
NoHp VARCHAR(13) NOT NULL, 
alamat VARCHAR(50) NOT NULL,
idKelurahan INT
FOREIGN KEY (idKelurahan) REFERENCES Kelurahan(idKelurahan)
)

--Create Table Pelanggan
CREATE TABLE Pelanggan (
idPelanggan INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
NIK VARCHAR(16) NOT NULL, 
Nama VARCHAR(30) NOT NULL,
NoHp VARCHAR(13) NOT NULL, 
alamat VARCHAR(50) NOT NULL
idKelurahan INT
FOREIGN KEY (idKelurahan) REFERENCES Kelurahan(idKelurahan)
)

--Create Table Tower
CREATE TABLE Tower (
idTower VARCHAR(1) PRIMARY KEY NOT NULL
)

--Create Table Unit
CREATE TABLE Unit (
kodeUnit VARCHAR(6) PRIMARY KEY NOT NULL, 
noUnit INT NOT NULL, 
lantai INT NOT NULL, 
jenis VARCHAR(6) NOT NULL, 
statusKetersediaan VARCHAR(6) NOT NULL, 
harga MONEY NOT NULL, 
idTower VARCHAR(1) NOT NULL, 
FOREIGN KEY (idTower) REFERENCES Tower(idTower)
)

--Create Table Kelurahan
CREATE TABLE Kelurahan (
idKelurahan INT PRIMARY KEY NOT NULL,
namaKelurahan VARCHAR(30) NOT NULL
)

--Create Table Kecamatan
CREATE TABLE Kecamatan(
	idKecamatan INT PRIMARY KEY NOT NULL,
	namaKecamatan VARCHAR(30)
)

--Create Table Review
CREATE TABLE Review (
idPelanggan INT NOT NULL, 
kodeUnit VARCHAR(6) FOREIGN KEY REFERENCES Unit(kodeUnit) NOT NULL,
rating FLOAT NOT NULL,
komentar VARCHAR(500) NOT NULL,
FOREIGN KEY (idPelanggan) REFERENCES Pelanggan(idPelanggan),
)

--Create Table Mengelola
CREATE TABLE Mengelola (
idMengelola INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
kodeUnit VARCHAR(6) NOT NULL, 
idAgen INT NOT NULL,
FOREIGN KEY (kodeUnit) REFERENCES Unit(kodeUnit), 
FOREIGN KEY (idAgen) REFERENCES Agen(idAgen)
)

-- Create Table Transaksi
CREATE TABLE Transaksi (
    idTransaksi INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
    kodeUnit VARCHAR(6) NOT NULL, 
    idPelanggan INT NOT NULL, 
    idAgen INT NOT NULL,
    waktuSewa DATE NOT NULL, 
    waktuSelesai DATE NOT NULL, 
    harga MONEY NOT NULL, 
    FOREIGN KEY (kodeUnit) REFERENCES Unit(kodeUnit), 
    FOREIGN KEY (idPelanggan) REFERENCES Pelanggan(idPelanggan),
    FOREIGN KEY (idAgen) REFERENCES Agen(idAgen)
);