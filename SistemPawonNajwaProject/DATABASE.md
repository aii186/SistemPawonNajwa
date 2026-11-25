# Database Pawon Najwa (Laragon)

Setup Laragon sudah dijalankan di laptop ini: MySQL Laragon aktif, database `pawon_najwa` memakai port 3306, dan sembilan menu awal sudah dimasukkan. Connector/J juga disimpan di `lib/mysql-connector-j-26.10.0.jar` dan terdaftar pada classpath NetBeans.

Untuk laptop lain, jalankan MySQL di Laragon lalu impor `database/pawon_najwa.sql`. Project menggunakan Connector/J yang sudah ada di `lib`.

Laragon standar memakai `root` dengan password kosong. Untuk pengaturan lain, tambahkan JVM options pada project (Project Properties → Run):

```text
-Dpawon.db.url=jdbc:mysql://localhost:3306/pawon_najwa?useSSL=false&serverTimezone=Asia/Jakarta
-Dpawon.db.user=root
-Dpawon.db.password=PASSWORD_MYSQL
```

Kasir dan pengguna membaca menu/stok dari database; kedua jendela memuat ulang menu setiap 2 detik. Perubahan menu, harga, dan stok dari kasir disimpan ke database. Database laptop ini sudah memiliki tabel akun, pembayaran, pesanan, serta detail pesanan; penyimpanan riwayat transaksi dari aplikasi Java masih memakai memori.
