package phanmemqlnhansu;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AuthForm extends JFrame {
    private final JTextField txtUserLog;
    private final JPasswordField txtPassLog;
    private final JButton btnLogin;

    private final JTextField txtUserReg;
    private final JTextField txtMaNVReg;
    private final JPasswordField txtPassReg;
    private final JPasswordField txtPassConfirmReg;
    private final JButton btnRegister;

    public AuthForm() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        setTitle("Hệ Thống Quản Lý Nhân Sự - Đăng Nhập & Đăng Ký");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color primaryColor = new Color(79, 70, 229);
        getContentPane().setBackground(Color.WHITE);

        // Header phía trên
        JPanel panelTop = new JPanel();
        panelTop.setBackground(primaryColor);
        panelTop.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        panelTop.add(lblTitle);
        add(panelTop, BorderLayout.NORTH);

        // JTabbedPane chia thành 2 tab: Đăng Nhập và Đăng Ký
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ================= TAB 1: ĐĂNG NHẬP =================
        JPanel panelLogin = new JPanel(new GridLayout(5, 1, 5, 5));
        panelLogin.setBackground(Color.WHITE);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelLogin.add(new JLabel("Tên đăng nhập (Username):"));
        txtUserLog = new JTextField();
        txtUserLog.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelLogin.add(txtUserLog);

        panelLogin.add(new JLabel("Mật khẩu:"));
        txtPassLog = new JPasswordField();
        txtPassLog.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelLogin.add(txtPassLog);

        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        
        panelLogin.add(new JLabel(""));
        panelLogin.add(btnLogin);

        tabbedPane.addTab("Đăng Nhập", panelLogin);

        // ================= TAB 2: ĐĂNG KÝ TÀI KHOẢN =================
        JPanel panelRegister = new JPanel(new GridLayout(9, 1, 3, 3));
        panelRegister.setBackground(Color.WHITE);
        panelRegister.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panelRegister.add(new JLabel("Tên đăng nhập mới (Username):"));
        txtUserReg = new JTextField();
        txtUserReg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelRegister.add(txtUserReg);

        panelRegister.add(new JLabel("Mã Nhân Viên liên kết (MaNV):"));
        txtMaNVReg = new JTextField();
        txtMaNVReg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelRegister.add(txtMaNVReg);

        panelRegister.add(new JLabel("Mật khẩu:"));
        txtPassReg = new JPasswordField();
        txtPassReg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelRegister.add(txtPassReg);

        panelRegister.add(new JLabel("Xác nhận mật khẩu:"));
        txtPassConfirmReg = new JPasswordField();
        txtPassConfirmReg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelRegister.add(txtPassConfirmReg);

        btnRegister = new JButton("Đăng Ký Tài Khoản");
        btnRegister.setBackground(new Color(16, 185, 129));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        panelRegister.add(new JLabel(""));
        panelRegister.add(btnRegister);

        tabbedPane.addTab("Đăng Ký", panelRegister);

        add(tabbedPane, BorderLayout.CENTER);

        // Xử lý sự kiện
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> handleRegister());
    }

    // Xử lý đăng nhập
    private void handleLogin() {
        String user = txtUserLog.getText().trim();
        String pass = new String(txtPassLog.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Khớp với cột Username và PasswordHash trong CSDL
        String sql = "SELECT * FROM TaiKhoan WHERE Username = ? AND PasswordHash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                this.dispose(); 
                // CHUYỂN TRỰC TIẾP SANG TRANG MENU TỔNG (DASHBOARD)
                new MainDashboardForm().setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xử lý đăng ký tài khoản mới lưu vào bảng TaiKhoan
    private void handleRegister() {
        String user = txtUserReg.getText().trim();
        String maNV = txtMaNVReg.getText().trim();
        String pass = new String(txtPassReg.getPassword()).trim();
        String confirmPass = new String(txtPassConfirmReg.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ Tên đăng nhập và Mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String checkSql = "SELECT * FROM TaiKhoan WHERE Username = ?";
        String insertSql = "INSERT INTO TaiKhoan (Username, PasswordHash, MaNV, TrangThai, NgayTao) VALUES (?, ?, ?, 1, GETDATE())";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Kiểm tra xem Username đã tồn tại chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, user);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập (Username) này đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 2. Thêm tài khoản mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, user);
                insertStmt.setString(2, pass);
                
                if (maNV.isEmpty()) {
                    insertStmt.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    insertStmt.setString(3, maNV);
                }
                
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công! Bạn có thể chuyển sang tab Đăng Nhập.");
                
                // Reset form đăng ký
                txtUserReg.setText("");
                txtMaNVReg.setText("");
                txtPassReg.setText("");
                txtPassConfirmReg.setText("");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng ký CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthForm().setVisible(true));
    }
}