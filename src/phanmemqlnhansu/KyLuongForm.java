package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class KyLuongForm extends JFrame {
    private JTextField txtMaKy, txtTenKy, txtThang, txtNam, txtTimKiem;
    private JTable tblKyLuong;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public KyLuongForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Kỳ Lương");
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

        JLabel lblTitle = new JLabel("QUẢN LÝ KỲ LƯƠNG NHÂN SỰ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Phân hệ quản lý tài chính & kỳ tính lương", JLabel.RIGHT);
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

        JLabel lblFormTitle = new JLabel("Thông Tin Kỳ Lương");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Mã Kỳ Lương (MaKyLuong):"));
        txtMaKy = new JTextField();
        styleTextField(txtMaKy);
        panelLeft.add(txtMaKy);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Tên Kỳ Lương:"));
        txtTenKy = new JTextField();
        styleTextField(txtTenKy);
        panelLeft.add(txtTenKy);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Tháng:"));
        txtThang = new JTextField();
        styleTextField(txtThang);
        panelLeft.add(txtThang);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Năm:"));
        txtNam = new JTextField();
        styleTextField(txtNam);
        panelLeft.add(txtNam);
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
        
        panelSearch.add(new JLabel("Tìm kiếm (Mã / Tên kỳ): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);

        panelRight.add(panelSearch, BorderLayout.NORTH);

        // Bảng dữ liệu JTable
        tblKyLuong = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Kỳ Lương", "Tên Kỳ Lương", "Tháng", "Năm"}, 0);
        tblKyLuong.setModel(model);
        tblKyLuong.setRowHeight(30);
        tblKyLuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblKyLuong.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblKyLuong.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblKyLuong);
        panelRight.add(scrollPane, BorderLayout.CENTER);

        // Thống kê tổng số lượng phía dưới bảng
        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số kỳ lương: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);

        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. SỰ KIỆN (EVENTS) =================
        loadDataToTable("");

        // Click chọn dòng trên bảng đưa dữ liệu lên form
        tblKyLuong.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblKyLuong.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaKy.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenKy.setText(model.getValueAt(selectedRow, 1).toString());
                txtThang.setText(model.getValueAt(selectedRow, 2).toString());
                txtNam.setText(model.getValueAt(selectedRow, 3).toString());
                txtMaKy.setEditable(false); // Khóa không cho sửa mã khi đang cập nhật
            }
        });

        btnThem.addActionListener(e -> addKyLuong());
        btnSua.addActionListener(e -> updateKyLuong());
        btnXoa.addActionListener(e -> deleteKyLuong());
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
        // Tên bảng trong CSDL tương ứng: KyLuong
        String sql = "SELECT * FROM KyLuong";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaKyLuong LIKE ? OR TenKyLuong LIKE ?";
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
                    rs.getString("MaKyLuong"),
                    rs.getString("TenKyLuong"),
                    rs.getString("Thang"),
                    rs.getString("Nam")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số kỳ lương: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addKyLuong() {
        String maKy = txtMaKy.getText().trim();
        String tenKy = txtTenKy.getText().trim();
        String thang = txtThang.getText().trim();
        String nam = txtNam.getText().trim();

        if (maKy.isEmpty() || tenKy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Kỳ và Tên Kỳ Lương!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO KyLuong (MaKyLuong, TenKyLuong, Thang, Nam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKy);
            pstmt.setString(2, tenKy);
            pstmt.setString(3, thang);
            pstmt.setString(4, nam);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Thêm kỳ lương thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi (Có thể trùng Mã Kỳ): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKyLuong() {
        String maKy = txtMaKy.getText().trim();
        String tenKy = txtTenKy.getText().trim();
        String thang = txtThang.getText().trim();
        String nam = txtNam.getText().trim();

        if (maKy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ lương cần sửa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE KyLuong SET TenKyLuong = ?, Thang = ?, Nam = ? WHERE MaKyLuong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenKy);
            pstmt.setString(2, thang);
            pstmt.setString(3, nam);
            pstmt.setString(4, maKy);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteKyLuong() {
        String maKy = txtMaKy.getText().trim();
        if (maKy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ lương cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa kỳ lương này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM KyLuong WHERE MaKyLuong = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maKy);
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
        txtMaKy.setText("");
        txtTenKy.setText("");
        txtThang.setText("");
        txtNam.setText("");
        txtTimKiem.setText("");
        txtMaKy.setEditable(true);
        tblKyLuong.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KyLuongForm().setVisible(true));
    }
}