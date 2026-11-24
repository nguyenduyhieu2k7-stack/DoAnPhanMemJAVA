package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ChucVuForm extends JFrame {
    private JTextField txtMaCV, txtTenCV, txtTimKiem;
    private JTable tblChucVu;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public ChucVuForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Chức Vụ");
        setSize(1000, 580);
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

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC CHỨC VỤ");
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
        panelLeft.setPreferredSize(new Dimension(340, 0));
        panelLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Thông Tin Chức Vụ");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(20));

        panelLeft.add(createInputLabel("Mã Chức Vụ:"));
        txtMaCV = new JTextField();
        styleTextField(txtMaCV);
        panelLeft.add(txtMaCV);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Tên Chức Vụ:"));
        txtTenCV = new JTextField();
        styleTextField(txtTenCV);
        panelLeft.add(txtTenCV);
        panelLeft.add(Box.createVerticalStrut(30));

        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

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
        
        panelSearch.add(new JLabel("Tìm kiếm (Mã/Tên): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);
        panelRight.add(panelSearch, BorderLayout.NORTH);

        tblChucVu = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Chức Vụ", "Tên Chức Vụ"}, 0);
        tblChucVu.setModel(model);
        tblChucVu.setRowHeight(30);
        tblChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChucVu.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblChucVu.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        panelRight.add(new JScrollPane(tblChucVu), BorderLayout.CENTER);

        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số chức vụ hiện tại: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);
        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // Sự kiện
        loadDataToTable("");

        tblChucVu.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblChucVu.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaCV.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenCV.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaCV.setEditable(false);
            }
        });

        btnThem.addActionListener(e -> addChucVu());
        btnSua.addActionListener(e -> updateChucVu());
        btnXoa.addActionListener(e -> deleteChucVu());
        btnLamMoi.addActionListener(e -> resetForm());
        
        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
    }

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

    public void loadDataToTable(String keyword) {
        model.setRowCount(0);
        int count = 0;
        String sql = "SELECT * FROM ChucVu";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaCV LIKE ? OR TenCV LIKE ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("MaCV"), rs.getString("TenCV")});
                count++;
            }
            lblTotalCount.setText("Tổng số chức vụ hiện tại: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addChucVu() {
        String ma = txtMaCV.getText().trim();
        String ten = txtTenCV.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ChucVu (MaCV, TenCV) VALUES (?, ?)")) {
            pstmt.setString(1, ma);
            pstmt.setString(2, ten);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm chức vụ thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChucVu() {
        String ma = txtMaCV.getText().trim();
        String ten = txtTenCV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE ChucVu SET TenCV = ? WHERE MaCV = ?")) {
            pstmt.setString(1, ten);
            pstmt.setString(2, ma);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteChucVu() {
        String ma = txtMaCV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ChucVu WHERE MaCV = ?")) {
                pstmt.setString(1, ma);
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
        txtMaCV.setText("");
        txtTenCV.setText("");
        txtTimKiem.setText("");
        txtMaCV.setEditable(true);
        tblChucVu.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChucVuForm().setVisible(true));
    }
}