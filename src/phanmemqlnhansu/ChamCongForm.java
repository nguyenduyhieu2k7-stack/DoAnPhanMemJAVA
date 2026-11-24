package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ChamCongForm extends JFrame {
    private JTextField txtMaCC, txtMaNV, txtSoNgayCong, txtThangNam, txtTimKiem;
    private JTable tblChamCong;
    private DefaultTableModel model;
    private final JButton btnThem;
    private final JButton btnSua;
    private final JButton btnXoa;
    private final JButton btnLamMoi;
    private final JButton btnTimKiem;
    private final JLabel lblTotalCount;

    public ChamCongForm() {
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

        setTitle("Hệ Thống Quản Lý Nhân Sự - Quản Lý Chấm Công");
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
        JLabel lblTitle = new JLabel("QUẢN LÝ CHẤM CÔNG NHÂN VIÊN");
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

        JLabel lblFormTitle = new JLabel("Thông Tin Chấm Công");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(new Color(15, 23, 42));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeft.add(lblFormTitle);
        panelLeft.add(Box.createVerticalStrut(15));

        panelLeft.add(createInputLabel("Mã Chấm Công:"));
        txtMaCC = new JTextField();
        styleTextField(txtMaCC);
        panelLeft.add(txtMaCC);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Mã Nhân Viên (MaNV):"));
        txtMaNV = new JTextField();
        styleTextField(txtMaNV);
        panelLeft.add(txtMaNV);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Số Ngày Công:"));
        txtSoNgayCong = new JTextField();
        styleTextField(txtSoNgayCong);
        panelLeft.add(txtSoNgayCong);
        panelLeft.add(Box.createVerticalStrut(10));

        panelLeft.add(createInputLabel("Tháng / Năm (MM/YYYY):"));
        txtThangNam = new JTextField();
        styleTextField(txtThangNam);
        panelLeft.add(txtThangNam);
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
        panelSearch.add(new JLabel("Tìm kiếm (Mã CC / MaNV): "));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTimKiem);
        panelRight.add(panelSearch, BorderLayout.NORTH);

        tblChamCong = new JTable();
        model = new DefaultTableModel(new String[]{"Mã Chấm Công", "Mã NV", "Số Ngày Công", "Tháng/Năm"}, 0);
        tblChamCong.setModel(model);
        tblChamCong.setRowHeight(30);
        tblChamCong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChamCong.setSelectionBackground(new Color(224, 231, 255));
        
        JTableHeader header = tblChamCong.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        panelRight.add(new JScrollPane(tblChamCong), BorderLayout.CENTER);

        JPanel panelFooterTable = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFooterTable.setBackground(bgAppColor);
        lblTotalCount = new JLabel("Tổng số bản ghi chấm công: 0");
        lblTotalCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCount.setForeground(new Color(51, 65, 85));
        panelFooterTable.add(lblTotalCount);
        panelRight.add(panelFooterTable, BorderLayout.SOUTH);

        panelMain.add(panelRight, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // Sự kiện
        loadDataToTable("");

        tblChamCong.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblChamCong.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaCC.setText(model.getValueAt(selectedRow, 0).toString());
                txtMaNV.setText(model.getValueAt(selectedRow, 1).toString());
                txtSoNgayCong.setText(model.getValueAt(selectedRow, 2).toString());
                txtThangNam.setText(model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : "");
                txtMaCC.setEditable(false);
            }
        });

        btnThem.addActionListener(e -> addChamCong());
        btnSua.addActionListener(e -> updateChamCong());
        btnXoa.addActionListener(e -> deleteChamCong());
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
        // Khớp với bảng ChamCong trong CSDL
        String sql = "SELECT * FROM ChamCong";
        if (!keyword.isEmpty()) {
            sql += " WHERE MaCC LIKE ? OR MaNV LIKE ?";
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
                    rs.getString("MaCC"), // Điều chỉnh tên cột nếu trong CSDL của bạn khác
                    rs.getString("MaNV"),
                    rs.getString("SoNgayCong"),
                    rs.getString("ThangNam")
                });
                count++;
            }
            lblTotalCount.setText("Tổng số bản ghi chấm công: " + count);
        } catch (SQLException e) {
        }
    }

    private void addChamCong() {
        String maCC = txtMaCC.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String soNC = txtSoNgayCong.getText().trim();
        String thangNam = txtThangNam.getText().trim();

        if (maCC.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Chấm Công và Mã NV!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO ChamCong (MaCC, MaNV, SoNgayCong, ThangNam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maCC);
            pstmt.setString(2, maNV);
            pstmt.setString(3, soNC);
            pstmt.setString(4, thangNam);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm chấm công thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChamCong() {
        String maCC = txtMaCC.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String soNC = txtSoNgayCong.getText().trim();
        String thangNam = txtThangNam.getText().trim();

        if (maCC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE ChamCong SET MaNV = ?, SoNgayCong = ?, ThangNam = ? WHERE MaCC = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, soNC);
            pstmt.setString(3, thangNam);
            pstmt.setString(4, maCC);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable("");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteChamCong() {
        String maCC = txtMaCC.getText().trim();
        if (maCC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ChamCong WHERE MaCC = ?")) {
                pstmt.setString(1, maCC);
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
        txtMaCC.setText("");
        txtMaNV.setText("");
        txtSoNgayCong.setText("");
        txtThangNam.setText("");
        txtTimKiem.setText("");
        txtMaCC.setEditable(true);
        tblChamCong.clearSelection();
        loadDataToTable("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChamCongForm().setVisible(true));
    }
}