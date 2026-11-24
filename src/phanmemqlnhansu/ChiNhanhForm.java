package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public final class ChiNhanhForm extends JFrame {
    private JTextField txtMaCN, txtTenCN, txtTimKiem;
    private JTable tblChiNhanh;
    private DefaultTableModel model;
    private final JButton btnThem;
    private final JButton btnSua;
    private final JButton btnXoa;
    private final JButton btnLamMoi;
    private final JButton btnTimKiem;
    private final JLabel lblTotalCount;

    public ChiNhanhForm() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Chi Nhánh");
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
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC CHI NHÁNH");
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

        JLabel lblFormTitle = new JLabel("Thông Tin Chi Nhánh");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(20));

        panelLeft.add(createInputLabel("Mã Chi Nhánh:"));
        txtMaCN = new JTextField();
        styleTextField(txtMaCN);
        panelLeft.add(txtMaCN);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Tên Chi Nhánh:"));
        txtTenCN = new JTextField();
        styleTextField(txtTenCN);
        panelLeft.add(txtTenCN);
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

        tblChiNhanh = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Chi Nhánh", "Tên Chi Nhánh"}, 0);
        tblChiNhanh.setModel(model);
        tblChiNhanh.setRowHeight(30);
        tblChiNhanh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChiNhanh.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblChiNhanh.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        panelRight.add(new JScrollPane(tblChiNhanh), BorderLayout.CENTER);

        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số chi nhánh hiện tại: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);
        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // Sự kiện
        loadDataToTable("");

        tblChiNhanh.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblChiNhanh.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaCN.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenCN.setText(model.getValueAt(selectedRow, 1).toString());
                txtMaCN.setEditable(false);
            }
        });

        btnThem.addActionListener(e -> addChiNhanh());
        btnSua.addActionListener(e -> updateChiNhanh());
        btnXoa.addActionListener(e -> deleteChiNhanh());
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

    /**
     *
     * @param keyword
     */
    public void loadDataToTable(String keyword) {
        model.setRowCount(0);
        int count = 0;
        String sql = "SELECT * FROM ChiNhanh";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaCN LIKE ? OR TenCN LIKE ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("MaCN"), rs.getString("TenCN")});
                count++;
            }
            lblTotalCount.setText("Tổng số chi nhánh hiện tại: " + count);
        } catch (SQLException e) {
        }
    }

    private void addChiNhanh() {
        String ma = txtMaCN.getText().trim();
        String ten = txtTenCN.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ChiNhanh (MaCN, TenCN) VALUES (?, ?)")) {
            pstmt.setString(1, ma);
            pstmt.setString(2, ten);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm chi nhánh thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChiNhanh() {
        String ma = txtMaCN.getText().trim();
        String ten = txtTenCN.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE ChiNhanh SET TenCN = ? WHERE MaCN = ?")) {
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

    private void deleteChiNhanh() {
        String ma = txtMaCN.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ChiNhanh WHERE MaCN = ?")) {
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
        txtMaCN.setText("");
        txtTenCN.setText("");
        txtTimKiem.setText("");
        txtMaCN.setEditable(true);
        tblChiNhanh.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChiNhanhForm().setVisible(true));
    }
}