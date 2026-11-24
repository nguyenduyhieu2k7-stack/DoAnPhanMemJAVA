package phanmemqlnhansu;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class UiHelper {
    public static final Color PRIMARY_COLOR = new Color(79, 70, 229);
    public static final Color BG_APP_COLOR = new Color(241, 245, 249);
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129);
    public static final Color WARNING_COLOR = new Color(245, 158, 11);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);
    public static final Color NEUTRAL_COLOR = new Color(100, 116, 139);

    public static void applyNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored) {
        }
    }

    public static JPanel createHeaderBanner(String title, String subtitle) {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(PRIMARY_COLOR);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel lblSubtitle = new JLabel(subtitle, JLabel.RIGHT);
            lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblSubtitle.setForeground(new Color(224, 231, 255));
            panelHeader.add(lblSubtitle, BorderLayout.EAST);
        }
        return panelHeader;
    }

    public static JLabel createInputLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(new Color(30, 41, 59));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static void styleTextField(JTextField tf, int height) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        tf.setPreferredSize(new Dimension(0, height));
    }

    public static void styleComboBox(JComboBox<?> cb, int height) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        cb.setPreferredSize(new Dimension(0, height));
    }

    public static JButton createStyledButton(String text, Color bgColor, int padV, int padH) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(padV, padH, padV, padH));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void styleTable(JTable table, int rowHeight) {
        table.setRowHeight(rowHeight);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(224, 231, 255));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
    }

    public static class ComboItem {
        private final String code;
        private final String label;

        public ComboItem(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComboItem)) return false;
            ComboItem that = (ComboItem) o;
            return Objects.equals(code, that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code);
        }
    }

    public static void loadNhanVienCombo(JComboBox<ComboItem> cb, boolean includeEmpty) {
        cb.removeAllItems();
        if (includeEmpty) {
            cb.addItem(new ComboItem("", "-- Không liên kết --"));
        }
        String sql = "SELECT MaNV, HoTen FROM NhanVien ORDER BY MaNV";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String ma = rs.getString("MaNV");
                String ten = rs.getString("HoTen");
                cb.addItem(new ComboItem(ma, ma + " - " + ten));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPhongBanCombo(JComboBox<ComboItem> cb, boolean includeEmpty) {
        cb.removeAllItems();
        if (includeEmpty) {
            cb.addItem(new ComboItem("", "-- Chọn phòng ban --"));
        }
        String sql = "SELECT * FROM PhongBan";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            int colCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                String ma = rs.getString(1);
                String ten = colCount >= 2 && rs.getString(2) != null ? rs.getString(2) : ma;
                cb.addItem(new ComboItem(ma, ma + " - " + ten));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectComboByCode(JComboBox<ComboItem> cb, String code) {
        if (code == null) code = "";
        for (int i = 0; i < cb.getItemCount(); i++) {
            ComboItem item = cb.getItemAt(i);
            if (code.equals(item.getCode())) {
                cb.setSelectedIndex(i);
                return;
            }
        }
        if (cb.getItemCount() > 0) {
            cb.setSelectedIndex(0);
        }
    }
}