package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class TaiKhoanForm extends JFrame {
    private JTextField txtUsername, txtMaNV, txtTimKiem;
    private JPasswordField txtPassword;
    private JTable tblTaiKhoan;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public TaiKhoanForm() {
        // Áp dụng giao diện Nimbus hiện đại
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Tài Khoản");
        setSize(1100, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color primaryColor = new Color(79, 70, 229); // Xanh Indigo chủ đạo
        Color bgAppColor = new Color(241, 245, 249);   // Nền sáng
        getContentPane().setBackground(bgAppColor);

        // ================= 1. HEADER BANNER =================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(primaryColor);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Phân hệ quản lý bảo mật & tài khoản", JLabel.RIGHT);
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSubtitle.setForeground(new Color(224, 231, 255));
        panelHeader.add(lblSubtitle, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // ================= 2. MAIN CONTAINER =================
        JPanel panelMain = new JPanel(new BorderLayout(15, 0));
        panelMain.setBackground(bgAppColor);
        panelMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- CỘT TRÁI: FORM NHẬP LIỆU & NÚT CHỨC NĂNG ---
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBackground(Color.WHITE);
        panelLeft.setPreferredSize(new Dimension(360, 0));
        panelLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Thông Tin Tài Khoản");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Tên Đăng Nhập (Username):"));
        txtUsername = new JTextField();
        styleTextField(txtUsername);
        panelLeft.add(txtUsername);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mật Khẩu (PasswordHash):"));
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        panelLeft.add(txtPassword);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Nhân Viên liên kết (MaNV):"));
        txtMaNV = new JTextField();
        styleTextField(txtMaNV);
        panelLeft.add(txtMaNV);
        panelLeft.add(Box.createVerticalStrut(20));

        // Các nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        btnThem = createStyledButton("Thêm Mới", new Color(16, 185, 129));   // Xanh lá
        btnSua = createStyledButton("Cập Nhật", new Color(245, 158, 11));   // Cam vàng
        btnXoa = createStyledButton("Xóa Bỏ", new Color(239, 68, 68));      // Đỏ
        btnLamMoi = createStyledButton("Làm Mới", new Color(100, 116, 139)); // Xám

        panelButtons.add(btnThem);
        panelButtons.add(btnSua);
        panelButtons.add(btnXoa);
        panelButtons.add(btnLamMoi);

        panelLeft.add(panelButtons);
        panelMain.add(panelLeft, BorderLayout.WEST);

        // --- CỘT PHẢI: BẢNG DỮ LIỆU & TÌM KIẾM ---
        JPanel panelRight = new JPanel(new BorderLayout(0, 10));
        panelRight.setBackground(bgAppColor);

        // Thanh tìm kiếm phía trên bảng
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelSearch.setBackground(bgAppColor);

        txtTimKiem = new JTextField(18);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 35));
        
        btnTimKiem = createStyledButton("Tìm Kiếm", new Color(79, 70, 229));
        
        panelSearch.add(new JLabel("Tìm kiếm (Username / MaNV): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);

        panelRight.add(panelSearch, BorderLayout.NORTH);

        // Bảng dữ liệu JTable
        tblTaiKhoan = new JTable();
        model = new DefaultTableModel(new String[]{"Username", "PasswordHash", "MaNV", "TrangThai", "NgayTao"}, 0);
        tblTaiKhoan.setModel(model);
        tblTaiKhoan.setRowHeight(30);
        tblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblTaiKhoan.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblTaiKhoan.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        panelRight.add(scrollPane, BorderLayout.CENTER);

        // Thống kê tổng số lượng phía dưới bảng
        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số tài khoản: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);

        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. SỰ KIỆN (EVENTS) =================
        loadDataToTable("");

        // Click chọn dòng trên bảng đưa dữ liệu lên form
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblTaiKhoan.getSelectedRow();
            if (selectedRow >= 0) {
                txtUsername.setText(model.getValueAt(selectedRow, 0).toString());
                txtPassword.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaNV.setText(model.getValueAt(selectedRow, 2) != null ? model.getValueAt(selectedRow, 2).toString() : "");
                txtUsername.setEditable(false); // Khóa không cho sửa tên đăng nhập khi đang cập nhật
            }
        });

        btnThem.addActionListener(e -> addTaiKhoan());
        btnSua.addActionListener(e -> updateTaiKhoan());
        btnXoa.addActionListener(e -> deleteTaiKhoan());
        btnLamMoi.addActionListener(e -> resetForm());
        
        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
    }

    // --- CÁC HÀM TIỆN ÍCH GIAO DIỆN ---
    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(30, 41, 59));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tf.setPreferredSize(new Dimension(0, 32));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU CSDL ---
    public void loadDataToTable(String keyword) {
        model.setRowCount(0);
        int count = 0;
        // Tên bảng trong CSDL tương ứng: TaiKhoan
        String sql = "SELECT * FROM TaiKhoan";
        if (!keyword.isEmpty()) {
            sql += " WHERE Username LIKE ? OR MaNV LIKE ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (!keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("Username"),
                    rs.getString("PasswordHash"),
                    rs.getString("MaNV"),
                    rs.getBoolean("TrangThai"),
                    rs.getString("NgayTao")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số tài khoản: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTaiKhoan() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String maNV = txtMaNV.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên đăng nhập và Mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO TaiKhoan (Username, PasswordHash, MaNV, TrangThai, NgayTao) VALUES (?, ?, ?, 1, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, maNV.isEmpty() ? null : maNV);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi (Có thể trùng Username hoặc sai MaNV): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTaiKhoan() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String maNV = txtMaNV.getText().trim();

        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE TaiKhoan SET PasswordHash = ?, MaNV = ? WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pass);
            pstmt.setString(2, maNV.isEmpty() ? null : maNV);
            pstmt.setString(3, user);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTaiKhoan() {
        String user = txtUsername.getText().trim();
        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM TaiKhoan WHERE Username = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable("");
                resetForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtMaNV.setText("");
        txtTimKiem.setText("");
        txtUsername.setEditable(true);
        tblTaiKhoan.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaiKhoanForm().setVisible(true));
    }
}