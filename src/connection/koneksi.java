package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class koneksi {
    Connection connect;
    Statement statement;
    ResultSet result;
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=BarunoShoeStoreApp;user=lemillion;password=dey12399;encrypt=true;trustServerCertificate=true";
                Connection conn = DriverManager.getConnection(connectionString);
                koneksi = conn;
                System.out.println("Koneksi berhasil");
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return koneksi;
    }
}
