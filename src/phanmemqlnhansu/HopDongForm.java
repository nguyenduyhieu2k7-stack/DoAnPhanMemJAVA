package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class HopDongForm extends JFrame {
    private JTextField txtMaHD, txtMaNV, txtLoaiHD, txtNgayBD, txtNgayKT, txtTimKiem;
    private JTable tblHopDong;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public HopDongForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Hợp Đồng Lao Động");
        setSize(1100, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color primaryColor = new Color(79, 70, 229); // Xanh Indigo
        Color bgAppColor = new Color(241, 245, 249);   // Nền sáng
        getContentPane().setBackground(bgAppColor);

        // ================= 1. HEADER BANNER =================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(primaryColor);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ HỢP ĐỒNG LAO ĐỘNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Phân hệ pháp lý & nhân sự", JLabel.RIGHT);
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

        JLabel lblFormTitle = new JLabel("Thông Tin Hợp Đồng");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        // Nhập các trường thông tin hợp đồng
        panelLeft.add(createInputLabel("Mã Hợp Đồng:"));
        txtMaHD = new JTextField();
        styleTextField(txtMaHD);
        panelLeft.add(txtMaHD);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Nhân Viên (MaNV):"));
        txtMaNV = new JTextField();
        styleTextField(txtMaNV);
        panelLeft.add(txtMaNV);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Loại Hợp Đồng:"));
        txtLoaiHD = new JTextField();
        styleTextField(txtLoaiHD);
        panelLeft.add(txtLoaiHD);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Ngày Bắt Đầu (YYYY-MM-DD):"));
        txtNgayBD = new JTextField();
        styleTextField(txtNgayBD);
        panelLeft.add(txtNgayBD);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Ngày Kết Thúc (YYYY-MM-DD):"));
        txtNgayKT = new JTextField();
        styleTextField(txtNgayKT);
        panelLeft.add(txtNgayKT);
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
        
        panelSearch.add(new JLabel("Tìm kiếm (Mã HĐ / Mã NV): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);

        panelRight.add(panelSearch, BorderLayout.NORTH);

        // Bảng dữ liệu JTable
        tblHopDong = new JTable();
        model = new DefaultTableModel(new String[]{"Mã HĐ", "Mã NV", "Loại Hợp Đồng", "Ngày Bắt Đầu", "Ngày Kết Thúc"}, 0);
        tblHopDong.setModel(model);
        tblHopDong.setRowHeight(30);
        tblHopDong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblHopDong.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblHopDong.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblHopDong);
        panelRight.add(scrollPane, BorderLayout.CENTER);

        // Thống kê tổng số lượng phía dưới bảng
        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số hợp đồng hiện tại: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);

        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. SỰ KIỆN (EVENTS) =================
        loadDataToTable("");

        // Click chọn dòng trên bảng đưa dữ liệu lên form
        tblHopDong.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblHopDong.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaHD.setText(model.getValueAt(selectedRow, 0).toString());
                txtMaNV.setText(model.getValueAt(selectedRow, 1).toString());
                txtLoaiHD.setText(model.getValueAt(selectedRow, 2).toString());
                txtNgayBD.setText(model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : "");
                txtNgayKT.setText(model.getValueAt(selectedRow, 4) != null ? model.getValueAt(selectedRow, 4).toString() : "");
                txtMaHD.setEditable(false); // Khóa mã hợp đồng khi sửa
            }
        });

        btnThem.addActionListener(e -> addHopDong());
        btnSua.addActionListener(e -> updateHopDong());
        btnXoa.addActionListener(e -> deleteHopDong());
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
        // Tên bảng trong CSDL tương ứng: HopDongLaoDong
        String sql = "SELECT * FROM HopDongLaoDong";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaHD LIKE ? OR MaNV LIKE ?";
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
                    rs.getString("MaHD"), // Lưu ý: Điều chỉnh tên cột trong CSDL nếu khác (ví dụ: MaHopDong)
                    rs.getString("MaNV"),
                    rs.getString("LoaiHopDong"),
                    rs.getString("NgayBatDau"),
                    rs.getString("NgayKetThuc")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số hợp đồng hiện tại: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addHopDong() {
        String maHD = txtMaHD.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String loaiHD = txtLoaiHD.getText().trim();
        String ngayBD = txtNgayBD.getText().trim();
        String ngayKT = txtNgayKT.getText().trim();

        if (maHD.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã HĐ và Mã NV!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO HopDongLaoDong (MaHD, MaNV, LoaiHopDong, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            pstmt.setString(2, maNV);
            pstmt.setString(3, loaiHD);
            pstmt.setString(4, ngayBD.isEmpty() ? null : ngayBD);
            pstmt.setString(5, ngayKT.isEmpty() ? null : ngayKT);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi (Kiểm tra lại MaNV có tồn tại không): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHopDong() {
        String maHD = txtMaHD.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String loaiHD = txtLoaiHD.getText().trim();
        String ngayBD = txtNgayBD.getText().trim();
        String ngayKT = txtNgayKT.getText().trim();

        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần sửa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE HopDongLaoDong SET MaNV = ?, LoaiHopDong = ?, NgayBatDau = ?, NgayKetThuc = ? WHERE MaHD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, loaiHD);
            pstmt.setString(3, ngayBD.isEmpty() ? null : ngayBD);
            pstmt.setString(4, ngayKT.isEmpty() ? null : ngayKT);
            pstmt.setString(5, maHD);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cập nhật hợp đồng thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHopDong() {
        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hợp đồng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM HopDongLaoDong WHERE MaHD = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maHD);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
                loadDataToTable("");
                resetForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtLoaiHD.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        txtTimKiem.setText("");
        txtMaHD.setEditable(true);
        tblHopDong.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HopDongForm().setVisible(true));
    }
}