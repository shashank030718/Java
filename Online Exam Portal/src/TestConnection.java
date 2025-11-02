import java.sql.*;



public class TestConnection {
    public static void main(String[] args) {
        if (DBConnection.getConnection() != null) {
            System.out.println("✅ Connected using DBConnection class!");
        } else {
            System.out.println("❌ Connection failed!");
        }
    }
}
