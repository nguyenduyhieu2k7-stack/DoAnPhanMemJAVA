package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class LichLamViecForm extends JFrame {
    private JTextField txtMaLich, txtMaNV, txtMaCa, txtNgayLam, txtTimKiem;
    private JTable tblLichLamViec;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public LichLamViecForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Lịch Làm Việc");
        setSize(1100, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color primaryColor = new Color(79, 70, 229);
        Color bgAppColor = new Color(241, 245, 249);
        getContentPane().setBackground(bgAppColor);

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(primaryColor);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH LÀM VIỆC NHÂN SỰ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // Main Container
        JPanel panelMain = new JPanel(new BorderLayout(15, 0));
        panelMain.setBackground(bgAppColor);
        panelMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel Trái (Form Nhập)
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBackground(Color.WHITE);
        panelLeft.setPreferredSize(new Dimension(360, 0));
        panelLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Thông Tin Lịch Làm");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Mã Lịch Làm:"));
        txtMaLich = new JTextField();
        styleTextField(txtMaLich);
        panelLeft.add(txtMaLich);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Nhân Viên (MaNV):"));
        txtMaNV = new JTextField();
        styleTextField(txtMaNV);
        panelLeft.add(txtMaNV);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Ca Làm (MaCa):"));
        txtMaCa = new JTextField();
        styleTextField(txtMaCa);
        panelLeft.add(txtMaCa);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Ngày Làm (YYYY-MM-DD):"));
        txtNgayLam = new JTextField();
        styleTextField(txtNgayLam);
        panelLeft.add(txtNgayLam);
        panelLeft.add(Box.createVerticalStrut(20));

        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        btnThem = createStyledButton("Thêm Mới", new Color(16, 185, 129));
        btnSua = createStyledButton("Cập Nhật", new Color(245, 158, 11));
        btnXoa = createStyledButton("Xóa Bỏ", new Color(239, 68, 68));
        btnLamMoi = createStyledButton("Làm Mới", new Color(100, 116, 139));

        panelButtons.add(btnThem);
        panelButtons.add(btnSua);
        panelButtons.add(btnXoa);
        panelButtons.add(btnLamMoi);
        panelLeft.add(panelButtons);

        panelMain.add(panelLeft, BorderLayout.WEST);

        // Panel Phải (Bảng & Tìm kiếm)
        JPanel panelRight = new JPanel(new BorderLayout(0, 10));
        panelRight.setBackground(bgAppColor);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelSearch.setBackground(bgAppColor);
        txtTimKiem = new JTextField(18);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 35));
        btnTimKiem = createStyledButton("Tìm Kiếm", new Color(79, 70, 229));
        panelSearch.add(new JLabel("Tìm kiếm (Mã Lịch / MaNV): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);
        panelRight.add(panelSearch, BorderLayout.NORTH);

        tblLichLamViec = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Lịch Làm", "Mã NV", "Mã Ca", "Ngày Làm"}, 0);
        tblLichLamViec.setModel(model);
        tblLichLamViec.setRowHeight(30);
        tblLichLamViec.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblLichLamViec.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblLichLamViec.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        panelRight.add(new JScrollPane(tblLichLamViec), BorderLayout.CENTER);

        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số lịch làm việc: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);
        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // Sự kiện
        loadDataToTable("");

        tblLichLamViec.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblLichLamViec.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaLich.setText(model.getValueAt(selectedRow, 0).toString());
                txtMaNV.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaCa.setText(model.getValueAt(selectedRow, 2).toString());
                txtNgayLam.setText(model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : "");
                txtMaLich.setEditable(false);
            }
        });

        btnThem.addActionListener(e -> addLichLamViec());
        btnSua.addActionListener(e -> updateLichLamViec());
        btnXoa.addActionListener(e -> deleteLichLamViec());
        btnLamMoi.addActionListener(e -> resetForm());
        
        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
    }

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

    public void loadDataToTable(String keyword) {
        model.setRowCount(0);
        int count = 0;
        String sql = "SELECT * FROM LichLamViec";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaLich LIKE ? OR MaNV LIKE ?";
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
                    rs.getString("MaLich"), // Điều chỉnh tên cột nếu CSDL của bạn khác
                    rs.getString("MaNV"),
                    rs.getString("MaCa"),
                    rs.getString("NgayLam")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số lịch làm việc: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addLichLamViec() {
        String maLich = txtMaLich.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String maCa = txtMaCa.getText().trim();
        String ngayLam = txtNgayLam.getText().trim();

        if (maLich.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Lịch và Mã NV!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO LichLamViec (MaLich, MaNV, MaCa, NgayLam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLich);
            pstmt.setString(2, maNV);
            pstmt.setString(3, maCa);
            pstmt.setString(4, ngayLam.isEmpty() ? null : ngayLam);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm lịch làm việc thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLichLamViec() {
        String maLich = txtMaLich.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String maCa = txtMaCa.getText().trim();
        String ngayLam = txtNgayLam.getText().trim();

        if (maLich.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE LichLamViec SET MaNV = ?, MaCa = ?, NgayLam = ? WHERE MaLich = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, maCa);
            pstmt.setString(3, ngayLam.isEmpty() ? null : ngayLam);
            pstmt.setString(4, maLich);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLichLamViec() {
        String maLich = txtMaLich.getText().trim();
        if (maLich.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM LichLamViec WHERE MaLich = ?")) {
                pstmt.setString(1, maLich);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable("");
                resetForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        txtMaLich.setText("");
        txtMaNV.setText("");
        txtMaCa.setText("");
        txtNgayLam.setText("");
        txtTimKiem.setText("");
        txtMaLich.setEditable(true);
        tblLichLamViec.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LichLamViecForm().setVisible(true));
    }
}