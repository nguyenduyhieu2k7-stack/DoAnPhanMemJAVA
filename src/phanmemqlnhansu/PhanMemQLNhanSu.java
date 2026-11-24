package phanmemqlnhansu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PhanMemQLNhanSu {

    public static void main(String[] args) {
        // Chuỗi kết nối đến SQL Server
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PhanMemQLNhanSu;encrypt=true;trustServerCertificate=true;";
        String user = "sa"; // Tài khoản SQL Server của bạn
        String password = "Hieu020908#"; // Thay chữ này bằng mật khẩu đăng nhập SQL Server thực tế

        try {
            // Nạp driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Tiến hành kết nối
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {
                System.out.println("Kết nối thành công đến database PhanMemQLNhanSu!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy Driver JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại! Vui lòng kiểm tra lại tài khoản, mật khẩu hoặc cổng kết nối.");
            e.printStackTrace();
        }
    }
}