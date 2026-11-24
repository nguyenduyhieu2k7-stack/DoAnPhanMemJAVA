package phanmemqlnhansu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Khai báo chuỗi kết nối đến SQL Server
            // Đảm bảo tên database đúng là PhanMemQLNhanSu
            String url = "jdbc:sqlserver://localhost:1433;databaseName=PhanMemQLNhanSu;encrypt=true;trustServerCertificate=true;";
            String user = "sa"; // Tên tài khoản SQL Server của bạn
            String password = "Hieu020908#"; // Thay mật khẩu SQL Server của bạn vào đây
            
            // Nạp driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Tạo kết nối
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại! Kiểm tra lại tên đăng nhập, mật khẩu hoặc port.");
            e.printStackTrace();
        }
        return conn;
    }
    
    // Hàm main nhỏ để test kết nối trực tiếp
    public static void main(String[] args) {
        getConnection();
    }
}