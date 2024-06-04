CREATE TABLE Kecamatan(
	idKecamatan INT PRIMARY KEY NOT NULL,
	namaKecamatan VARCHAR(30)
)

INSERT INTO Kecamatan VALUES (1, 'Bandung Kidul')
INSERT INTO Kecamatan VALUES (2, 'Bandung Wetan')
INSERT INTO Kecamatan VALUES (3, 'Cidadap')
INSERT INTO Kecamatan VALUES (4, 'BuahBatu')
INSERT INTO Kecamatan VALUES (5, 'Batununggal')
INSERT INTO Kecamatan VALUES (6, 'Lengkong')
INSERT INTO Kecamatan VALUES (7, 'Gedebage')
INSERT INTO Kecamatan VALUES (8, 'Andir')
INSERT INTO Kecamatan VALUES (9, 'Ujungberung')
INSERT INTO Kecamatan VALUES (10,'Antapani')

--------------------------------------------------
CREATE TABLE Kelurahan(
	idKelurahan INT PRIMARY KEY NOT NULL,
	namaKelurahan VARCHAR(30)
)

INSERT INTO Kelurahan VALUES (1,'Kujangsari')
INSERT INTO Kelurahan VALUES (2,'Mengger')
INSERT INTO Kelurahan VALUES (3,'Cibaduyut')
INSERT INTO Kelurahan VALUES (4,'Pasirendah')
INSERT INTO Kelurahan VALUES (5,'Ciroyom')
INSERT INTO Kelurahan VALUES (6,'Jamika')
INSERT INTO Kelurahan VALUES (7,'Gegerkalong')
INSERT INTO Kelurahan VALUES (8,'Cijagra')
INSERT INTO Kelurahan VALUES (9,'Hegarmanah')
INSERT INTO Kelurahan VALUES (10,'Kebongedang')
INSERT INTO Kelurahan VALUES (11,'Cihapit')
INSERT INTO Kelurahan VALUES (12,'Ledeng')
INSERT INTO Kelurahan VALUES (13,'Cijawura')
INSERT INTO Kelurahan VALUES (14,'Rancabolang')
INSERT INTO Kelurahan VALUES (15,'Antapani Wetan')

----------------------------------------------
CREATE TABLE Tower(
	idTower VARCHAR(1) PRIMARY KEY NOT NULL
)

INSERT INTO Tower VALUES ('A')
INSERT INTO Tower VALUES ('B')
INSERT INTO Tower VALUES ('C')
INSERT INTO Tower VALUES ('D')

--------------------------------------------
CREATE TABLE Agen (
	idAgen INT PRIMARY KEY NOT NULL,
	NIK VARCHAR(16) NOT NULL,
	Nama VARCHAR(30) NOT NULL,
	NoHp VARCHAR(13) NOT NULL,
	alamat VARCHAR(50) NOT NULL
)
DROP TABLE Agen

BULK INSERT Agen
FROM 'C:\Users\Zakki\Downloads\Dummy Data Agen.csv'
WITH (
    FIELDTERMINATOR = ',',
    ROWTERMINATOR = '\n'
);

-----------------------------------------
CREATE TABLE Unit (
	kodeUnit VARCHAR(6) PRIMARY KEY NOT NULL,
	noUnit VARCHAR(3) NOT NULL,
	lantai VARCHAR(2) NOT NULL,
	Tower VARCHAR(1),
	statusKetersediaan VARCHAR(6) NOT NULL,
	harga MONEY NOT NULL,
	jenis VARCHAR(6)
)

DROP TABLE Unit

-------------------------------------
DROP TABLE Pelanggan

CREATE TABLE Pelanggan(
	idPelanggan INT PRIMARY KEY,
	NIK VARCHAR(20),
	Nama VARCHAR(20),
	NoHp VARCHAR(20),
	alamat VARCHAR(50)
)

SELECT * FROM Pelanggan

BULK INSERT Pelanggan
FROM 'C:\Users\Zakki\Downloads\Pelanggan_1717480121814.csv'
WITH (
    FIELDTERMINATOR = ',',
    ROWTERMINATOR = '\n'
);