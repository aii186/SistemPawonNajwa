package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** MySQL connection and menu persistence for the Laragon database. */
public final class Database {
    private static final String URL = System.getProperty("pawon.db.url", "jdbc:mysql://localhost:3306/pawon_najwa?useSSL=false&serverTimezone=Asia/Jakarta");
    private static final String USER = System.getProperty("pawon.db.user", "root");
    private static final String PASSWORD = System.getProperty("pawon.db.password", "");

    private Database() { }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Menu> loadMenu() throws SQLException {
        List<Menu> menus = new ArrayList<>();
        try (Connection c = connect(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id_menu,kategori,nama_menu,harga,stok FROM menu WHERE tersedia=1 ORDER BY id_menu")) {
            while (rs.next()) {
                String nama = rs.getString("nama_menu");
                int harga = rs.getInt("harga"), stok = rs.getInt("stok");
                Menu menu = "Makanan".equals(rs.getString("kategori")) ? new Makan(nama, harga, stok) : new Minum(nama, harga, stok);
                menu.setDatabaseId(rs.getInt("id_menu"));
                menus.add(menu);
            }
        }
        return menus;
    }

    public static void saveMenu(List<Menu> menus) throws SQLException {
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (Statement s = c.createStatement()) { s.executeUpdate("UPDATE menu SET tersedia=0"); }
            try (PreparedStatement update = c.prepareStatement("UPDATE menu SET nama_menu=?,harga=?,stok=?,tersedia=1 WHERE id_menu=?");
                 PreparedStatement insert = c.prepareStatement("INSERT INTO menu(kategori,nama_menu,harga,stok,tersedia) VALUES(?,?,?,?,1)", Statement.RETURN_GENERATED_KEYS)) {
                for (Menu m : menus) {
                    if (m.getDatabaseId() > 0) {
                        update.setString(1, m.getNama()); update.setInt(2, m.getHarga()); update.setInt(3, m.getStok()); update.setInt(4, m.getDatabaseId());
                        update.executeUpdate();
                    } else {
                        insert.setString(1, m instanceof Makan ? "Makanan" : "Minuman");
                        insert.setString(2, m.getNama()); insert.setInt(3, m.getHarga()); insert.setInt(4, m.getStok());
                        insert.executeUpdate();
                        try (ResultSet keys = insert.getGeneratedKeys()) { if (keys.next()) m.setDatabaseId(keys.getInt(1)); }
                    }
                }
            }
            c.commit();
        } catch (SQLException ex) { throw ex; }
    }

    public static void initialize() throws SQLException {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS menu (id_menu INT AUTO_INCREMENT PRIMARY KEY, nama_menu VARCHAR(120) NOT NULL, kategori ENUM('Makanan','Minuman') NOT NULL, harga INT NOT NULL, tersedia TINYINT(1) DEFAULT 1, stok INT NOT NULL DEFAULT 0)");
            try (ResultSet columns = c.getMetaData().getColumns(null, null, "menu", "stok")) {
                if (!columns.next()) s.executeUpdate("ALTER TABLE menu ADD COLUMN stok INT NOT NULL DEFAULT 0");
            }
            s.executeUpdate("CREATE TABLE IF NOT EXISTS pawon_app_orders (id_order VARCHAR(30) PRIMARY KEY, pelanggan VARCHAR(120) NOT NULL, nomor_meja VARCHAR(30) NOT NULL, ringkasan TEXT NOT NULL, total INT NOT NULL, metode VARCHAR(30) NOT NULL, status VARCHAR(40) NOT NULL, dibuat TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS pawon_app_order_items (id_detail INT AUTO_INCREMENT PRIMARY KEY, id_order VARCHAR(30) NOT NULL, nama_menu VARCHAR(120) NOT NULL, kategori VARCHAR(20) NOT NULL, harga INT NOT NULL, jumlah INT NOT NULL, FOREIGN KEY (id_order) REFERENCES pawon_app_orders(id_order) ON DELETE CASCADE)");
        }
    }

    public static void saveOrder(TransaksiPesanan order) throws SQLException {
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement p = c.prepareStatement("INSERT INTO pawon_app_orders(id_order,pelanggan,nomor_meja,ringkasan,total,metode,status) VALUES(?,?,?,?,?,?,?)")) {
                p.setString(1, order.getIdTransaksi()); p.setString(2, order.getNamaPelanggan()); p.setString(3, order.getNomorMeja());
                p.setString(4, order.getRingkasanItem()); p.setInt(5, order.getTotal()); p.setString(6, order.getMetodeBayar()); p.setString(7, order.getStatus()); p.executeUpdate();
            }
            try (PreparedStatement p = c.prepareStatement("INSERT INTO pawon_app_order_items(id_order,nama_menu,kategori,harga,jumlah) VALUES(?,?,?,?,?)")) {
                for (ItemPesanan item : order.getItems()) {
                    p.setString(1, order.getIdTransaksi()); p.setString(2, item.getMenu().getNama());
                    p.setString(3, item.getMenu() instanceof Makan ? "Makanan" : "Minuman"); p.setInt(4, item.getMenu().getHarga()); p.setInt(5, item.getJumlah()); p.addBatch();
                }
                p.executeBatch();
            }
            c.commit();
        }
    }

    public static List<TransaksiPesanan> loadOrders() throws SQLException {
        List<TransaksiPesanan> orders = new ArrayList<>();
        try (Connection c = connect(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM pawon_app_orders ORDER BY dibuat DESC, id_order DESC")) {
            while (rs.next()) {
                TransaksiPesanan order = new TransaksiPesanan(rs.getString("id_order"), rs.getString("pelanggan"), rs.getString("nomor_meja"), rs.getString("ringkasan"), rs.getInt("total"), rs.getString("metode"));
                order.setStatus(rs.getString("status"));
                List<ItemPesanan> items = new ArrayList<>();
                try (PreparedStatement p = c.prepareStatement("SELECT * FROM pawon_app_order_items WHERE id_order=?")) {
                    p.setString(1, order.getIdTransaksi());
                    try (ResultSet ir = p.executeQuery()) { while (ir.next()) {
                        Menu m = "Makanan".equals(ir.getString("kategori")) ? new Makan(ir.getString("nama_menu"), ir.getInt("harga"), 0) : new Minum(ir.getString("nama_menu"), ir.getInt("harga"), 0);
                        items.add(new ItemPesanan(m, ir.getInt("jumlah")));
                    }}
                }
                order.setItems(items); orders.add(order);
            }
        }
        return orders;
    }

    public static void updateOrderStatus(String id, String status) throws SQLException {
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement("UPDATE pawon_app_orders SET status=? WHERE id_order=?")) { p.setString(1, status); p.setString(2, id); p.executeUpdate(); }
    }
}
