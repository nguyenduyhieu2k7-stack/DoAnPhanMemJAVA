package phanmemqlnhansu;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainDashboardForm extends JFrame {
    private String currentUsername;
    private boolean isAdmin = false;

    // Constructor mặc định
    public MainDashboardForm() {
        this("admin"); // Mặc định là admin nếu gọi không tham số
    }

    // Constructor nhận diện tên tài khoản đăng nhập để kiểm tra quyền
    public MainDashboardForm(String username) {
        this.currentUsername = username;
        checkAdminRole(); // Kiểm tra xem user này có phải Admin hay không

        // Áp dụng giao diện Nimbus hiện đại
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        setTitle("Hệ Thống Quản Lý Nhân Sự - Cổng Thông Tin Trực Tuyến (" + (isAdmin ? "Quyền: ADMIN" : "Quyền: NHÂN VIÊN") + ")");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color headerBg = new Color(15, 23, 42);   // Slate 900
        Color bgAppColor = new Color(248, 250, 252); // Nền sáng
        getContentPane().setBackground(bgAppColor);

        // ================= 1. HEADER & NAVIGATION BAR =================
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setBackground(headerBg);
        panelNav.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Logo bên trái
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);
        JLabel lblLogoIcon = new JLabel("🌐");
        lblLogoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JLabel lblBrand = new JLabel("HR PORTAL - " + (isAdmin ? "ADMINISTRATOR" : "EMPLOYEE PORTAL"));
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBrand.setForeground(Color.WHITE);
        brandPanel.add(lblLogoIcon);
        brandPanel.add(lblBrand);
        panelNav.add(brandPanel, BorderLayout.WEST);

        // Menu Links ở giữa
        JPanel menuLinksPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        menuLinksPanel.setOpaque(false);
        
        JButton btnMenuHome = createWebNavLink("Trang Chủ");
        JButton btnMenuNV = createWebNavLink("Nhân Sự");
        JButton btnMenuLuong = createWebNavLink("Lương & Công");
        JButton btnMenuHeThong = createWebNavLink("Hệ Thống");
        
        menuLinksPanel.add(btnMenuHome);
        menuLinksPanel.add(btnMenuNV);
        menuLinksPanel.add(btnMenuLuong);
        menuLinksPanel.add(btnMenuHeThong);
        panelNav.add(menuLinksPanel, BorderLayout.CENTER);

        // Nút Admin/User (có Dropdown Menu) và Đăng xuất bên phải
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        
        JButton btnAdminInfo = new JButton(isAdmin ? "👤 Admin ▾" : "👤 " + currentUsername + " ▾");
        btnAdminInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdminInfo.setForeground(Color.WHITE);
        btnAdminInfo.setBackground(new Color(51, 65, 85));
        btnAdminInfo.setFocusPainted(false);
        btnAdminInfo.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnAdminInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- TẠO MENU THẢ XUỐNG (DROPDOWN MENU) ---
        JPopupMenu adminMenu = new JPopupMenu();
        JMenuItem itemTaiKhoan = new JMenuItem("🔐 Quản lý Tài khoản hệ thống");
        JMenuItem itemVaiTro = new JMenuItem("🛡️ Quản lý Vai trò & Phân quyền");
        JMenuItem itemInfo = new JMenuItem("ℹ️ Thông tin phiên đăng nhập");

        // Phân quyền cho menu thả xuống của Admin
        itemTaiKhoan.setEnabled(isAdmin);
        itemVaiTro.setEnabled(isAdmin);

        itemTaiKhoan.addActionListener(e -> new TaiKhoanForm().setVisible(true));
        itemVaiTro.addActionListener(e -> new VaiTroForm().setVisible(true));
        itemInfo.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Tài khoản: " + currentUsername + "\nLoại tài khoản: " + (isAdmin ? "Quản trị viên (Admin)" : "Nhân viên thường") + "\nPhiên bản: 2026 - Đồ án Java & SQL Server", 
            "Thông tin phiên đăng nhập", JOptionPane.INFORMATION_MESSAGE));

        adminMenu.add(itemTaiKhoan);
        adminMenu.add(itemVaiTro);
        adminMenu.addSeparator();
        adminMenu.add(itemInfo);

        btnAdminInfo.addActionListener(e -> adminMenu.show(btnAdminInfo, 0, btnAdminInfo.getHeight()));

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(239, 68, 68));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(e -> {
            this.dispose();
            new AuthForm().setVisible(true);
        });

        actionPanel.add(btnAdminInfo);
        actionPanel.add(btnLogout);
        panelNav.add(actionPanel, BorderLayout.EAST);

        add(panelNav, BorderLayout.NORTH);

        // Sự kiện click menu header
        btnMenuHome.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bạn đang ở trang chủ Dashboard tổng!"));
        btnMenuNV.addActionListener(e -> new NhanVienForm().setVisible(true));
        btnMenuLuong.addActionListener(e -> {
            if (!isAdmin) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập phân hệ lương thưởng!", "Hạn chế quyền", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new BangLuongForm().setVisible(true);
        });
        btnMenuHeThong.addActionListener(e -> {
            if (!isAdmin) {
                JOptionPane.showMessageDialog(this, "Chức năng này chỉ dành cho Admin hệ thống!", "Hạn chế quyền", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new TaiKhoanForm().setVisible(true);
        });

        // ================= 2. MAIN WORKSPACE (DASHBOARD CARDS) =================
        JPanel panelMain = new JPanel(new BorderLayout(0, 15));
        panelMain.setBackground(bgAppColor);
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblSectionTitle = new JLabel("📌 DANH MỤC PHÂN HỆ QUẢN LÝ TRỰC TUYẾN (" + (isAdmin ? "Toàn quyền" : "Quyền hạn giới hạn") + ")");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSectionTitle.setForeground(new Color(30, 41, 59));
        panelMain.add(lblSectionTitle, BorderLayout.NORTH);

        JPanel panelGrid = new JPanel(new GridLayout(3, 5, 15, 15));
        panelGrid.setBackground(bgAppColor);

        // Khởi tạo các thẻ Card chức năng
        JButton btnNhanVien = createWebCardButton("👥 Quản Lý Nhân Viên", "Hồ sơ nhân sự");
        JButton btnPhongBan = createWebCardButton("🏢 Quản Lý Phòng Ban", "Cơ cấu tổ chức");
        JButton btnChucVu = createWebCardButton("⭐ Quản Lý Chức Vụ", "Danh mục chức vụ");
        JButton btnHopDong = createWebCardButton("📄 Quản Lý Hợp Đồng", "Hợp đồng lao động");
        JButton btnChamCong = createWebCardButton("⏱️ Quản Lý Chấm Công", "Theo dõi ngày công");
        JButton btnBangLuong = createModulCardButton("💰 Quản Lý Bảng Lương", "Tính toán tài chính");
        JButton btnChiNhanh = createWebCardButton("🌐 Quản Lý Chi Nhánh", "Hệ thống cơ sở");
        JButton btnKhuVuc = createWebCardButton("📍 Quản Lý Khu Vực", "Khu vực hoạt động");
        JButton btnCaLamViec = createWebCardButton("⏰ Quản Lý Ca Làm Việc", "Phân ca làm việc");
        JButton btnLichLam = createWebCardButton("🗓️ Lịch Làm Việc", "Xếp lịch tuần, tháng");
        JButton btnLuânChuyen = createModuleHighlightButton("🔄 Lịch Sử Luân Chuyển", "Chuyển công tác");
        JButton btnQuyPhep = createWebCardButton("🌴 Quản Lý Quỹ Phép", "Số phép năm");
        JButton btnKyLuong = createWebCardButton("📊 Quản Lý Kỳ Lương", "Kỳ tính lương");
        JButton btnTaiKhoan = createModuleHighlightButton("🔐 Quản Lý Tài Khoản", "Bảo mật hệ thống");
        JButton btnVaiTro = createWebCardButton("🛡️ Quản Lý Vai Trò", "Phân quyền người dùng");

        // Gắn sự kiện mở form tương ứng (Có kiểm tra quyền nếu là tài khoản thường)
        btnNhanVien.addActionListener(e -> new NhanVienForm().setVisible(true));
        btnPhongBan.addActionListener(e -> new PhongBanForm().setVisible(true));
        btnChucVu.addActionListener(e -> new ChucVuForm().setVisible(true));
        btnHopDong.addActionListener(e -> new HopDongForm().setVisible(true));
        btnChamCong.addActionListener(e -> new ChamCongForm().setVisible(true));
        
        // Các phân hệ nhạy cảm yêu cầu quyền Admin
        btnBangLuong.addActionListener(e -> checkAndOpen(new BangLuongForm()));
        btnChiNhanh.addActionListener(e -> new ChiNhanhForm().setVisible(true));
        btnKhuVuc.addActionListener(e -> new KhuVucForm().setVisible(true));
        btnCaLamViec.addActionListener(e -> new CaLamViecForm().setVisible(true));
        btnLichLam.addActionListener(e -> new LichLamViecForm().setVisible(true));
        btnLuânChuyen.addActionListener(e -> new LichSuLuanChuyenForm().setVisible(true));
        btnQuyPhep.addActionListener(e -> new QuyPhepNamForm().setVisible(true));
        btnKyLuong.addActionListener(e -> checkAndOpen(new KyLuongForm()));
        btnTaiKhoan.addActionListener(e -> checkAndOpen(new TaiKhoanForm()));
        btnVaiTro.addActionListener(e -> checkAndOpen(new VaiTroForm()));

        // Thêm đầy đủ 15 nút vào lưới giao diện Grid 3x5
        panelGrid.add(btnNhanVien);
        panelGrid.add(btnPhongBan);
        panelGrid.add(btnChucVu);
        panelGrid.add(btnHopDong);
        panelGrid.add(btnChamCong);
        panelGrid.add(btnBangLuong);
        panelGrid.add(btnChiNhanh);
        panelGrid.add(btnKhuVuc);
        panelGrid.add(btnCaLamViec);
        panelGrid.add(btnLichLam);
        panelGrid.add(btnLuânChuyen);
        panelGrid.add(btnQuyPhep); // Đã sửa lại đúng biến btnQuyPhep
        panelGrid.add(btnKyLuong);
        panelGrid.add(btnTaiKhoan);
        panelGrid.add(btnVaiTro);

        panelMain.add(panelGrid, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ================= 3. FOOTER =================
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(bgAppColor);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel lblFooter = new JLabel("Cổng thông tin quản lý nhân sự trực tuyến • Tài khoản: " + currentUsername);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(148, 163, 184));
        panelFooter.add(lblFooter);
        add(panelFooter, BorderLayout.SOUTH);
    }

    // Hàm kiểm tra quyền Admin từ cơ sở dữ liệu dựa vào Username
    private void checkAdminRole() {
        if ("admin".equalsIgnoreCase(currentUsername)) {
            isAdmin = true;
            return;
        }

        String sql = "SELECT v.TenVaiTro FROM TaiKhoan t " +
                     "JOIN TaiKhoan_VaiTro tv ON t.Username = tv.Username " +
                     "JOIN VaiTro v ON tv.MaVaiTro = v.MaVaiTro " +
                     "WHERE t.Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("TenVaiTro");
                if (role != null && (role.toLowerCase().contains("admin") || role.toLowerCase().contains("quản trị"))) {
                    isAdmin = true;
                    break;
                }
            }
        } catch (SQLException e) {
            isAdmin = "admin".equalsIgnoreCase(currentUsername);
        }
    }

    // Kiểm tra quyền trước khi mở form nhạy cảm
    private void checkAndOpen(JFrame form) {
        if (!isAdmin) {
            JOptionPane.showMessageDialog(this, 
                "Tài khoản thường không có quyền truy cập phân hệ này!\nVui lòng liên hệ Quản trị viên để được cấp quyền.", 
                "Hạn chế quyền hạn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        form.setVisible(true);
    }

    private JButton createWebNavLink(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(16, 185, 129));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 41, 59));
            }
        });
        return btn;
    }

    private JButton createWebCardButton(String title, String subtitle) {
        JButton btn = new JButton("<html><div style='text-align: center;'><b>" + title + "</b><br/><span style='font-size:9px; color:#64748b;'>" + subtitle + "</span></div></html>");
        styleCardButton(btn, Color.WHITE, new Color(30, 41, 59), new Color(203, 213, 225));
        return btn;
    }

    private JButton createModulCardButton(String title, String subtitle) {
        JButton btn = new JButton("<html><div style='text-align: center;'><b>" + title + "</b><br/><span style='font-size:9px; color:#64748b;'>" + subtitle + "</span></div></html>");
        styleCardButton(btn, new Color(238, 242, 255), new Color(79, 70, 229), new Color(199, 210, 254));
        return btn;
    }

    private JButton createModuleHighlightButton(String title, String subtitle) {
        JButton btn = new JButton("<html><div style='text-align: center;'><b>" + title + "</b><br/><span style='font-size:9px; color:#64748b;'>" + subtitle + "</span></div></html>");
        styleCardButton(btn, new Color(240, 253, 244), new Color(22, 101, 52), new Color(187, 247, 208));
        return btn;
    }

    private void styleCardButton(JButton btn, Color bgColor, Color fgColor, Color borderColor) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(224, 231, 255));
                btn.setForeground(new Color(79, 70, 229));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
                btn.setForeground(fgColor);
            }
        });
    }
}