package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnection {

    public static Connection initializeConnection() throws SQLException {
        String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://localhost:1433";
        //String dbURL = "jdbc:sqlserver://MEOWWW\\DAT:1305";
        //String dbURL = "jdbc:sqlserver://26.149.246.221:1433";
        //String dbURL = "jdbc:sqlserver://HARRY\\HARRY1:1433";
        String dbName = "CVHub";
        String dbUsername = "sa";
        //String dbPassword = "123";
        String dbPassword = "1";

        // Chuỗi kết nối với các tham số cấu hình chính xác
        String connectionURL = dbURL + ";databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;";  
        // Chuỗi của Đạt
        //String connectionURL = dbURL + ";databaseName=" + dbName + ";trustServerCertificate=true;"; 
        Connection conn = null;

		try {
			// Nạp driver
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
			System.out.println("Connect successfully!");
		} catch (ClassNotFoundException e) {
			System.out.println("Lỗi không tìm thấy Driver!");
			e.printStackTrace(); // In ra chi tiết lỗi để tiện debug
		} catch (SQLException e) {
			System.out.println("Lỗi kết nối CSDL!");
			if (conn == null)
				System.out.println("conn = null");
			e.printStackTrace(); // In ra chi tiết lỗi SQL để tiện debug
		}
		return conn;

	}

}
