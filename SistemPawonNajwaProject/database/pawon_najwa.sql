CREATE DATABASE IF NOT EXISTS pawon_najwa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pawon_najwa;

-- Menu is shared by the customer and cashier screens.
CREATE TABLE IF NOT EXISTS menu (
  id_menu INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nama_menu VARCHAR(120) NOT NULL,
  kategori ENUM('Makanan','Minuman') NOT NULL,
  harga INT NOT NULL,
  tersedia TINYINT(1) DEFAULT 1,
  stok INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS admin (
  id_admin INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30) NOT NULL UNIQUE,
  password VARCHAR(64) NOT NULL,
  nama VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS pembayaran (
  id_pembayaran INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nama_metode VARCHAR(30) NOT NULL
);
CREATE TABLE IF NOT EXISTS pesanan (
  id_pesanan INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nama_pelanggan VARCHAR(50) NOT NULL DEFAULT 'Pelanggan',
  no_meja VARCHAR(10),
  id_pembayaran INT,
  tanggal_waktu DATETIME DEFAULT CURRENT_TIMESTAMP,
  total_harga INT DEFAULT 0,
  jumlah_bayar INT DEFAULT 0,
  kembalian INT DEFAULT 0,
  status_transaksi VARCHAR(20) DEFAULT 'Menunggu',
  id_admin INT,
  FOREIGN KEY (id_pembayaran) REFERENCES pembayaran(id_pembayaran),
  FOREIGN KEY (id_admin) REFERENCES admin(id_admin)
);
CREATE TABLE IF NOT EXISTS detail_pesanan (
  id_detail INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_pesanan INT NOT NULL,
  id_menu INT NOT NULL,
  jumlah_pesanan INT NOT NULL,
  subtotal INT NOT NULL,
  FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan),
  FOREIGN KEY (id_menu) REFERENCES menu(id_menu)
);
