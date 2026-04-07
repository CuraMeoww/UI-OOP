package view;

import util.Config;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends BaseFrame {
    private JTabbedPane tabbedPane;
    private String adminName;

    // Màu sắc chủ đạo (Modern UI)
    private final Color HEADER_COLOR = new Color(44, 62, 80);
    private final Color BG_COLOR = new Color(245, 246, 250);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);

    public AdminPanel(String username) {
        super("Quản trị Rạp Chiếu Phim - Admin: " + username);
        this.adminName = username;
        setSize(1200, 800);
        initUI();
    }

    @Override
    public void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // --- HEADER (Giữ nguyên phần chào Admin và Đăng xuất) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblTitle = new JLabel("CINEMA MANAGEMENT SYSTEM", SwingConstants.LEFT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel userAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        userAction.setOpaque(false);
        JLabel lblUser = new JLabel("Chào, " + adminName + " | ");
        lblUser.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Đăng xuất");
        styleButton(btnLogout, new Color(231, 76, 60));
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        userAction.add(lblUser);
        userAction.add(btnLogout);
        header.add(lblTitle, BorderLayout.WEST);
        header.add(userAction, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- TABBED PANE CUSTOMIZATION ---
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        tabbedPane.addTab("🎬 Quản lý Phim", createMovieManager());
        tabbedPane.addTab("🍿 Đồ ăn & Uống", createFoodManager());
        tabbedPane.addTab("📊 Thống kê", createReportManager());

        add(tabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    // --- 1. QUẢN LÝ PHIM ---
    private JPanel createMovieManager() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);

        // --- 1.1. THANH TÌM KIẾM PHÍA TRÊN ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Tìm kiếm phim: "));

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnFilter = new JButton("Lọc");
        styleButton(btnFilter, ACCENT_COLOR);

        searchPanel.add(txtSearch);
        searchPanel.add(btnFilter);
        panel.add(searchPanel, BorderLayout.NORTH);

        // --- 1.2. BẢNG HIỂN THỊ PHIM ---
        String[] cols = {"ID", "Tên Phim", "Thời lượng", "Thể loại", "Độ tuổi", "Trạng thái"};
        DefaultTableModel movieModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Nạp dữ liệu 6 phim khớp với danh sách bên UserApp
        movieModel.addRow(new Object[]{"M01", "Thỏ Ơi!", "127p", "Tâm lý/Kinh dị", "T18", "Đang chiếu"});
        movieModel.addRow(new Object[]{"M02", "Cảm Ơn Người Đã Thức Cùng Tôi", "137p", "Lãng mạn", "T13", "Đang chiếu"});
        movieModel.addRow(new Object[]{"M03", "Quỷ Nhập Tràng 2", "105p", "Kinh dị", "T18", "Sắp chiếu"});
        movieModel.addRow(new Object[]{"M04", "Doraemon: Nobita và Thế giới...", "108p", "Hoạt hình", "P", "Đang chiếu"});
        movieModel.addRow(new Object[]{"M05", "Conan: Dư Ảnh Của Độc Nhãn", "110p", "Trinh thám", "T13", "Đang chiếu"});
        movieModel.addRow(new Object[]{"M06", "Conan: Ngôi Sao 5 Cánh 1 Triệu Đô", "111p", "Trinh thám", "T13", "Sắp chiếu"});

        JTable table = createStyledTable(movieModel);

        // --- 1.3. BỘ LỌC DỮ LIỆU (ROW SORTER) ---
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(movieModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new javax.swing.border.LineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // --- 1.4. LOGIC CHO NÚT LỌC (TÌM KIẾM) ---
        btnFilter.addActionListener(e -> {
            String text = txtSearch.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null); // Nếu ô trống thì hiện hết
            } else {
                // Lọc không phân biệt chữ hoa chữ thường (?i)
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Nhấn Enter trong ô tìm kiếm cũng tự kích hoạt nút Lọc
        txtSearch.addActionListener(e -> btnFilter.doClick());

        return panel;
    }

    private JPanel createFoodManager() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);

        // 3.1. Tiêu đề và nút làm mới
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Danh mục Sản phẩm & Combo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnRefresh = new JButton("🔄 Làm mới kho");
        styleButton(btnRefresh, ACCENT_COLOR);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // 3.2. Bảng dữ liệu
        String[] cols = {"Mã món", "Tên món/Combo", "Phân loại", "Giá niêm yết", "Tồn kho", "Trạng thái"};
        DefaultTableModel foodModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // GỌI HÀM ĐỌC FILE THỰC TẾ
        loadFoodDataFromFile(foodModel);

        JTable table = createStyledTable(foodModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Xử lý nút làm mới: Click vào là đọc lại file foods.txt
        btnRefresh.addActionListener(e -> {
            loadFoodDataFromFile(foodModel);
            System.out.println("Đã cập nhật lại kho đồ ăn.");
        });

        return panel;
    }

    // --- HÀM ĐỌC FILE FOODS.TXT (Dùng chung cho cả Admin) ---
    private void loadFoodDataFromFile(DefaultTableModel model) {
        model.setRowCount(0); // Xóa bảng cũ
        try (BufferedReader br = new BufferedReader(new FileReader("foods.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    String id = p[0];
                    String name = p[1];
                    // Định dạng giá tiền cho đẹp (ví dụ: 55,000)
                    String price = String.format("%,d", Integer.parseInt(p[2]));
                    String stock = p[3];

                    // Phân loại tự động dựa trên mã ID
                    String type = "Khác";
                    if (id.startsWith("P")) type = "Bỏng ngô";
                    else if (id.startsWith("C")) type = "Combo";
                    else if (id.startsWith("D")) type = "Nước uống";
                    else if (id.startsWith("S")) type = "Đồ ăn kèm";

                    String status = Integer.parseInt(stock) > 0 ? "Còn hàng" : "Hết hàng";

                    model.addRow(new Object[]{id, name, type, price, stock, status});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy file foods.txt!");
        }
    }

    // --- 5. THỐNG KÊ DOANH THU THỰC TẾ ---
    private JPanel createReportManager() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);

        Object[] data = calculateBusinessData();
        long totalRevenue = (long) data[0];
        int totalTickets = (int) data[1];
        String topMovie = (String) data[2];

        statsPanel.add(createStatCard("TỔNG DOANH THU", String.format("%,d VNĐ", totalRevenue), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("VÉ ĐÃ BÁN", totalTickets + " Vé", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("PHIM ĂN KHÁCH", topMovie, new Color(155, 89, 182)));

        panel.add(statsPanel, BorderLayout.NORTH);

        String[] cols = {"Tài khoản", "Phim", "Suất chiếu", "Ghế", "Doanh thu"};
        DefaultTableModel reportModel = new DefaultTableModel(cols, 0);
        loadAllHistoryToTable(reportModel);

        panel.add(new JScrollPane(createStyledTable(reportModel)), BorderLayout.CENTER);

        return panel;
    }

    // --- HELPERS ---
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblT = new JLabel(title);
        lblT.setForeground(new Color(255, 255, 255, 200));
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblV = new JLabel(value);
        lblV.setForeground(Color.WHITE);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(lblT, BorderLayout.NORTH);
        card.add(lblV, BorderLayout.CENTER);
        return card;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(HEADER_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        return table;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
    }

    private JPanel createPlaceholder(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER));
        return p;
    }

    // --- LOGIC XỬ LÝ DỮ LIỆU ---
    private Object[] calculateBusinessData() {
        long totalRev = 0; int totalTix = 0;
        Map<String, Integer> movieCount = new HashMap<>();
        java.io.File folder = new java.io.File(".");
        java.io.File[] files = folder.listFiles((dir, name) -> name.startsWith("history_") && name.endsWith(".txt"));

        if (files != null) {
            for (java.io.File f : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] p = line.split(" \\| ");
                        if (p.length >= 6) {
                            totalRev += Long.parseLong(p[5].replaceAll("[^0-9]", ""));
                            int count = p[4].split(",").length;
                            totalTix += count;
                            movieCount.put(p[0], movieCount.getOrDefault(p[0], 0) + count);
                        }
                    }
                } catch (IOException e) {}
            }
        }
        String hot = "N/A"; int max = 0;
        for (String m : movieCount.keySet()) { if (movieCount.get(m) > max) { max = movieCount.get(m); hot = m; } }
        return new Object[]{totalRev, totalTix, hot};
    }

    private void loadAllHistoryToTable(DefaultTableModel model) {
        java.io.File folder = new java.io.File(".");
        java.io.File[] files = folder.listFiles((dir, name) -> name.startsWith("history_") && name.endsWith(".txt"));
        if (files != null) {
            for (java.io.File f : files) {
                String user = f.getName().replace("history_", "").replace(".txt", "");
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] p = line.split(" \\| ");
                        if (p.length >= 6) model.addRow(new Object[]{user, p[0], p[2] + " " + p[3], p[4], p[5]});
                    }
                } catch (IOException e) {}
            }
        }
    }
}