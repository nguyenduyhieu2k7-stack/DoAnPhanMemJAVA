package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class LichSuLuanChuyenForm extends JFrame {
    private JTextField txtMaLC, txtMaNV, txtPhongBanCu, txtPhongBanMoi, txtNgayChuyen, txtTimKiem;
    private JTable tblLuanChuyen;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public LichSuLuanChuyenForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Lịch Sử Luân Chuyển");
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

        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH SỬ LUÂN CHUYỂN CÔNG TÁC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Phân hệ quản lý nhân sự", JLabel.RIGHT);
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

        JLabel lblFormTitle = new JLabel("Thông Tin Luân Chuyển");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Mã Luân Chuyển (MaLC):"));
        txtMaLC = new JTextField();
        styleTextField(txtMaLC);
        panelLeft.add(txtMaLC);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Nhân Viên (MaNV):"));
        txtMaNV = new JTextField();
        styleTextField(txtMaNV);
        panelLeft.add(txtMaNV);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Phòng Ban Cũ:"));
        txtPhongBanCu = new JTextField();
        styleTextField(txtPhongBanCu);
        panelLeft.add(txtPhongBanCu);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Phòng Ban Mới:"));
        txtPhongBanMoi = new JTextField();
        styleTextField(txtPhongBanMoi);
        panelLeft.add(txtPhongBanMoi);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Ngày Chuyển (YYYY-MM-DD):"));
        txtNgayChuyen = new JTextField();
        styleTextField(txtNgayChuyen);
        panelLeft.add(txtNgayChuyen);
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
        
        panelSearch.add(new JLabel("Tìm kiếm (Mã LC / MaNV): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);

        panelRight.add(panelSearch, BorderLayout.NORTH);

        // Bảng dữ liệu JTable
        tblLuanChuyen = new JTable();
        model = new DefaultTableModel(new String[]{"Mã LC", "Mã NV", "Phòng Ban Cũ", "Phòng Ban Mới", "Ngày Chuyển"}, 0);
        tblLuanChuyen.setModel(model);
        tblLuanChuyen.setRowHeight(30);
        tblLuanChuyen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblLuanChuyen.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblLuanChuyen.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblLuanChuyen);
        panelRight.add(scrollPane, BorderLayout.CENTER);

        // Thống kê tổng số lượng phía dưới bảng
        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số bản ghi luân chuyển: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);

        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. SỰ KIỆN (EVENTS) =================
        loadDataToTable("");

        // Click chọn dòng trên bảng đưa dữ liệu lên form
        tblLuanChuyen.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblLuanChuyen.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaLC.setText(model.getValueAt(selectedRow, 0).toString());
                txtMaNV.setText(model.getValueAt(selectedRow, 1).toString());
                txtPhongBanCu.setText(model.getValueAt(selectedRow, 2) != null ? model.getValueAt(selectedRow, 2).toString() : "");
                txtPhongBanMoi.setText(model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : "");
                txtNgayChuyen.setText(model.getValueAt(selectedRow, 4) != null ? model.getValueAt(selectedRow, 4).toString() : "");
                txtMaLC.setEditable(false); // Khóa không cho sửa mã khi đang cập nhật
            }
        });

        btnThem.addActionListener(e -> addLuanChuyen());
        btnSua.addActionListener(e -> updateLuanChuyen());
        btnXoa.addActionListener(e -> deleteLuanChuyen());
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
        // Tên bảng trong CSDL tương ứng: LichSuLuanChuyen
        String sql = "SELECT * FROM LichSuLuanChuyen";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaLC LIKE ? OR MaNV LIKE ?";
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
                    rs.getString("MaLC"),
                    rs.getString("MaNV"),
                    rs.getString("PhongBanCu"),
                    rs.getString("PhongBanMoi"),
                    rs.getString("NgayChuyen")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số bản ghi luân chuyển: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addLuanChuyen() {
        String maLC = txtMaLC.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String pbCu = txtPhongBanCu.getText().trim();
        String pbMoi = txtPhongBanMoi.getText().trim();
        String ngayChuyen = txtNgayChuyen.getText().trim();

        if (maLC.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã LC và Mã NV!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO LichSuLuanChuyen (MaLC, MaNV, PhongBanCu, PhongBanMoi, NgayChuyen) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLC);
            pstmt.setString(2, maNV);
            pstmt.setString(3, pbCu);
            pstmt.setString(4, pbMoi);
            pstmt.setString(5, ngayChuyen.isEmpty() ? null : ngayChuyen);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Thêm lịch sử luân chuyển thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi (Kiểm tra lại MaNV): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLuanChuyen() {
        String maLC = txtMaLC.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String pbCu = txtPhongBanCu.getText().trim();
        String pbMoi = txtPhongBanMoi.getText().trim();
        String ngayChuyen = txtNgayChuyen.getText().trim();

        if (maLC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần sửa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE LichSuLuanChuyen SET MaNV = ?, PhongBanCu = ?, PhongBanMoi = ?, NgayChuyen = ? WHERE MaLC = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, pbCu);
            pstmt.setString(3, pbMoi);
            pstmt.setString(4, ngayChuyen.isEmpty() ? null : ngayChuyen);
            pstmt.setString(5, maLC);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLuanChuyen() {
        String maLC = txtMaLC.getText().trim();
        if (maLC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bản ghi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM LichSuLuanChuyen WHERE MaLC = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maLC);
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
        txtMaLC.setText("");
        txtMaNV.setText("");
        txtPhongBanCu.setText("");
        txtPhongBanMoi.setText("");
        txtNgayChuyen.setText("");
        txtTimKiem.setText("");
        txtMaLC.setEditable(true);
        tblLuanChuyen.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LichSuLuanChuyenForm().setVisible(true));
    }
}