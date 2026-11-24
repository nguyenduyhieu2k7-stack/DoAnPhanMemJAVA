package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class PhongBanForm extends JFrame {
    private JTextField txtMaPB, txtTenPB, txtTimKiem;
    private JTable tblPhongBan;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public PhongBanForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Phòng Ban");
        setSize(1000, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Bảng màu đồng bộ (Indigo & Slate)
        Color primaryColor = new Color(79, 70, 229); // Xanh Indigo
        Color bgAppColor = new Color(241, 245, 249);   // Nền sáng
        getContentPane().setBackground(bgAppColor);

        // ================= 1. HEADER BANNER =================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(primaryColor);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC PHÒNG BAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Phân hệ quản lý cơ cấu tổ chức", JLabel.RIGHT);
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
        panelLeft.setPreferredSize(new Dimension(340, 0));
        panelLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Thông Tin Phòng Ban");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(20));

        // Nhập Mã Phòng Ban
        panelLeft.add(createInputLabel("Mã Phòng Ban:"));
        txtMaPB = new JTextField();
        styleTextField(txtMaPB);
        panelLeft.add(txtMaPB);
        panelLeft.add(Box.createVerticalStrut(15));

        // Nhập Tên Phòng Ban
        panelLeft.add(createInputLabel("Tên Phòng Ban:"));
        txtTenPB = new JTextField();
        styleTextField(txtTenPB);
        panelLeft.add(txtTenPB);
        panelLeft.add(Box.createVerticalStrut(30));

        // Các nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

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
        
        panelSearch.add(new JLabel("Tìm kiếm (Mã/Tên): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);

        panelRight.add(panelSearch, BorderLayout.NORTH);

        // Bảng dữ liệu JTable (Khớp với bảng PhongBan trong CSDL)
        tblPhongBan = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Phòng Ban", "Tên Phòng Ban"}, 0);
        tblPhongBan.setModel(model);
        tblPhongBan.setRowHeight(30);
        tblPhongBan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblPhongBan.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblPhongBan.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblPhongBan);
        panelRight.add(scrollPane, BorderLayout.CENTER);

        // Thống kê tổng số lượng phía dưới bảng
        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số phòng ban hiện tại: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);

        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. SỰ KIỆN (EVENTS) =================
        loadDataToTable("");

        // Click chọn dòng trên bảng đưa dữ liệu lên form
        tblPhongBan.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblPhongBan.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaPB.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenPB.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaPB.setEditable(false); // Khóa không cho sửa mã khi đang cập nhật
            }
        });

        btnThem.addActionListener(e -> addPhongBan());
        btnSua.addActionListener(e -> updatePhongBan());
        btnXoa.addActionListener(e -> deletePhongBan());
        btnLamMoi.addActionListener(e -> resetForm());
        
        // Sự kiện tìm kiếm nhanh
        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
    }

    // --- CÁC HÀM TIỆN ÍCH GIAO DIỆN ---
    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(30, 41, 59));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        tf.setPreferredSize(new Dimension(0, 38));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU CSDL ---
    public void loadDataToTable(String keyword) {
        model.setRowCount(0);
        int count = 0;
        // Lưu ý: Tên bảng trong CSDL của bạn là PhongBan (hoặc dbo.PhongBan)
        String sql = "SELECT * FROM PhongBan";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaPB LIKE ? OR TenPB LIKE ?";
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
                    rs.getString("MaPB"), // Đảm bảo tên cột trong CSDL khớp với lệnh này (ví dụ MaPB hoặc MaPhongBan)
                    rs.getString("TenPB")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số phòng ban hiện tại: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPhongBan() {
        String ma = txtMaPB.getText().trim();
        String ten = txtTenPB.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã PB và Tên Phòng Ban!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO PhongBan (MaPB, TenPB) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ma);
            pstmt.setString(2, ten);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Thêm phòng ban thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi (Có thể bị trùng mã PB): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePhongBan() {
        String ma = txtMaPB.getText().trim();
        String ten = txtTenPB.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng ban cần sửa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE PhongBan SET TenPB = ? WHERE MaPB = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ten);
            pstmt.setString(2, ma);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePhongBan() {
        String ma = txtMaPB.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng ban cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng ban này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM PhongBan WHERE MaPB = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ma);
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
        txtMaPB.setText("");
        txtTenPB.setText("");
        txtTimKiem.setText("");
        txtMaPB.setEditable(true);
        tblPhongBan.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhongBanForm().setVisible(true));
    }
}