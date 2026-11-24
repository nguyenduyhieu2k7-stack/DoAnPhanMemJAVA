package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class KhuVucForm extends JFrame {
    private JTextField txtMaKV, txtTenKV, txtTimKiem;
    private JTable tblKhuVuc;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTotalCount;

    public KhuVucForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Khu Vực");
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
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC KHU VỰC");
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

        JLabel lblFormTitle = new JLabel("Thông Tin Khu Vực");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(20));

        panelLeft.add(createInputLabel("Mã Khu Vực:"));
        txtMaKV = new JTextField();
        styleTextField(txtMaKV);
        panelLeft.add(txtMaKV);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Tên Khu Vực:"));
        txtTenKV = new JTextField();
        styleTextField(txtTenKV);
        panelLeft.add(txtTenKV);
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

        tblKhuVuc = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Khu Vực", "Tên Khu Vực"}, 0);
        tblKhuVuc.setModel(model);
        tblKhuVuc.setRowHeight(30);
        tblKhuVuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblKhuVuc.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblKhuVuc.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        panelRight.add(new JScrollPane(tblKhuVuc), BorderLayout.CENTER);

        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số khu vực hiện tại: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);
        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // Sự kiện
        loadDataToTable("");

        tblKhuVuc.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblKhuVuc.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaKV.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenKV.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaKV.setEditable(false);
            }
        });

        btnThem.addActionListener(e -> addKhuVuc());
        btnSua.addActionListener(e -> updateKhuVuc());
        btnXoa.addActionListener(e -> deleteKhuVuc());
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
        String sql = "SELECT * FROM KhuVuc";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaKV LIKE ? OR TenKV LIKE ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("MaKV"), rs.getString("TenKV")});
                count++;
            }
            lblTotalCount.setText("Tổng số khu vực hiện tại: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addKhuVuc() {
        String ma = txtMaKV.getText().trim();
        String ten = txtTenKV.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO KhuVuc (MaKV, TenKV) VALUES (?, ?)")) {
            pstmt.setString(1, ma);
            pstmt.setString(2, ten);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKhuVuc() {
        String ma = txtMaKV.getText().trim();
        String ten = txtTenKV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE KhuVuc SET TenKV = ? WHERE MaKV = ?")) {
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

    private void deleteKhuVuc() {
        String ma = txtMaKV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM KhuVuc WHERE MaKV = ?")) {
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
        txtMaKV.setText("");
        txtTenKV.setText("");
        txtTimKiem.setText("");
        txtMaKV.setEditable(true);
        tblKhuVuc.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KhuVucForm().setVisible(true));
    }
}