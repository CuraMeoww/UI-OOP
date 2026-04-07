package view;

import util.Config;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserApp extends BaseFrame {
    private List<JPanel> movieCards = new ArrayList<>();
    private JPanel mainContentPanel;
    private JScrollPane mainScroll;
    private String currentUsername; // Lưu tên người dùng hiện tại

    // DATA PHIM
    String[][] movieData = {
            {"Thỏ Ơi!", "Lấy cảm hứng từ những câu chuyện có thật, Thỏ Ơi!! phơi bày mặt tối của tình yêu và hôn nhân, nơi những chiếc \"mặt nạ\" sự thật dần bị tháo bỏ.", "82%", "17/02/2026", "127 phút", "T18", "Lyly, Pháo, Văn Mai Hương, Quốc Anh, Trấn Thành", "Trấn Thành","src/assets/tho oi ngang.jpg","src/assets/tho oi doc.jpg"},
            {"Cảm Ơn Người Đã Thức Cùng Tôi", "Cảm Ơn Người Đã Thức Cùng Tôi là một hành trình cảm xúc của những người trẻ đi tìm đáp án cho câu hỏi “Ước mơ của bạn là gì?”, để rồi chính họ khi bước vào thế giới trưởng thành dần nhận ra câu hỏi quan trọng nhất là “Mình muốn thực hiện ước mơ đó cùng ai?”", "78%", "27/02/2026", "137 phút", "T13", "Trần Doãn Hoàng, Võ Phan Kim Khánh, Nguyễn Hùng", "Chung Chí Công","src/assets/cc ngang.jpg","src/assets/cc doc.jpg"},
            {"Quỷ Nhập Tràng 2", "Quỷ Nhập Tràng 2 là tiền truyện của nhân vật Minh Như, trở về xưởng nhuộm gia đình sau nhiều năm bị xua đuổi. Tại đây, cô phải đối mặt với những hiện tượng ma quái cùng sự thật tàn khốc về cái chết của mẹ và giao ước đẫm máu năm xưa. Ác giả ác báo, liệu Minh Như có thoát khỏi vòng vây của quỷ dữ?", "88%", "13/03/2026", "~105 phút", "T18", "Khả Như, Doãn Quốc Đam", "Pom Nguyễn","src/assets/quy ngang.jpg","src/assets/quy doc.jpg"},
            {"Doraemon: Nobita và thế giới trong tranh", "Một hố đen kì lạ đã đưa Nobita cùng người bạn mèo máy Doraemon và nhóm bạn bước vào một thế giới trong tranh đầy bí ẩn, nơi cậu gặp gỡ cô bé Claire và cùng khám phá Công quốc Artoria xinh đẹp nhưng còn điều gì vẫn còn ẩn giấu…", "94%", "07/03/2025", "108 phút", "P", "Mizuta Wasabi, Ohara Megumi...", "Yukiyo Teramoto","src/assets/dora ngang.jpg","src/assets/dora doc.jpg"},
            {"Conan: Dư Ảnh Của Độc Nhãn", "Một vụ tấn công bí ẩn tại đài quan sát Nobeyama kéo Conan và các thám tử vào cuộc điều tra rùng rợn giữa núi tuyết. Thanh tra Yamato Kansuke buộc phải đối mặt với quá khứ đau thương từ vụ tuyết lở nhiều năm trước. Càng điều tra, những mối liên hệ mờ ám giữa các nhân vật từ Tokyo đến Nagano dần hiện rõ, trong đó ký ức của Kansuke chính là mảnh ghép quyết định sự thật.", "90%", "18/04/2025", "110 phút", "T13", "Takayama Minami, Hayashibara Megumi", "Katsuya Shigehara","src/assets/conan ngang.jpg","src/assets/conan doc.jpg"},
            {"Conan: Ngôi Sao 5 Cánh 1 Triệu Đô", "Cuộc truy tìm thanh kiếm báu tại Hakodate với sự góp mặt của siêu đạo chích Kid và Hattori.", "92%", "13/04/2026", "111 phút", "T13", "Takayama Minami, Yamaguchi Kappei", "Chika Nagaoka","src/assets/conan5 ngang.jpg","src/assets/conan5 doc.jpg"}
    };

    // DATA ĐỒ ĂN
    String[][] popcornData = {
            {"Bỏng ngô Vị Truyền Thống", "Vị ngọt nhẹ, cơ bản. - Size Vừa", "55000"},
            {"Bỏng ngô Vị Phô Mai", "Món 'quốc dân' ở rạp, thơm và ngậy. - Size Vừa", "65000"},
            {"Bỏng ngô Vị Caramel", "Giòn tan, ngọt đậm đà. - Size Lớn", "70000"},
            {"Bỏng ngô Vị Trà Xanh (Matcha)", "Dành cho ai thích vị hơi nhặng đắng và thơm mùi trà. - Size Lớn", "75000"},
            {"Bỏng ngô 2 Vị (Mix)", "Cho phép khách chọn 2 vị khác nhau trong cùng một hộp lớn.", "85000"}
    };

    String[][] comboData = {
            {"Combo Đơn (Solo)", "1 Bỏng ngô (Vừa) + 1 Nước (Lớn).", "99000"},
            {"Combo Đôi (Couple)", "1 Bỏng ngô (Lớn) + 2 Nước (Lớn).", "129000"},
            {"Combo Gia Đình (Family)", "2 Bỏng ngô (Lớn) + 4 Nước (Lớn) + 1 Snack túi lớn.", "250000"},
            {"Combo Phim (Limited)", "1 Bỏng ngô + 1 Ly Nhân Vật đặc biệt.", "199000"}
    };

    String[][] snackData = {
            {"Nước ngọt có gas", "Coca-Cola, Sprite, Fanta, Diet Coke.", "35000"},
            {"Nước suối", "Dasani hoặc Aquafina.", "20000"},
            {"Xúc xích nướng", "Đồ ăn nóng, thơm, rất dễ bán kèm.", "45000"},
            {"Khoai tây lắc phô mai", "Đựng trong túi giấy nhỏ, tiện cầm tay.", "50000"}
    };

    // Constructor nhận username để cá nhân hóa dữ liệu
    public UserApp(String username) {
        super("Cinema Management - Khám phá điện ảnh");
        this.currentUsername = username;
        initUI();
    }

    @Override
    public void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel logo = new JLabel("CINEMA SYSTEM");
        logo.setFont(Config.HEADER_FONT);
        logo.setForeground(Config.PRIMARY);
        header.add(logo, BorderLayout.WEST);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(Config.PRIMARY);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        String[] menuItems = {"🏠 Trang chủ", "🎫 Vé của tôi", "🍔 Đồ ăn & uống", "📜 Điều khoản"};
        for (String item : menuItems) {
            JButton btn = createNavButton(item);
            if (item.contains("Trang chủ")) btn.addActionListener(e -> showMovies());
            if (item.contains("Vé của tôi")) btn.addActionListener(e -> showBookingHistory());
            if (item.contains("Đồ ăn & uống")) btn.addActionListener(e -> showFoods());
            if (item.contains("Điều khoản")) btn.addActionListener(e -> showTerms());
            sidebar.add(btn);
        }
        add(sidebar, BorderLayout.WEST);

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(240, 240, 240));

        mainScroll = new JScrollPane(mainContentPanel);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(20);
        add(mainScroll, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        showMovies();
    }

    private void showMovies() {
        mainContentPanel.removeAll();
        JPanel grid = new JPanel(new GridLayout(0, 3, 25, 25));
        grid.setBackground(new Color(240, 240, 240));
        grid.setBorder(new EmptyBorder(25, 25, 25, 45));
        for (String[] data : movieData) grid.add(createMovieCard(data));
        mainContentPanel.add(grid);
        mainContentPanel.add(createFooter());
        refreshView();
    }

    private void showFoods() {
        mainContentPanel.removeAll();
        addCategorySection("🍿 DANH MỤC BỎNG NGÔ (POPCORN)", popcornData);
        addCategorySection("🎁 COMBO TIẾT KIỆM", comboData);
        addCategorySection("🥤 NƯỚC UỐNG & ĐỒ ĂN KÈM", snackData);
        mainContentPanel.add(createFooter());
        refreshView();
    }

    private void addCategorySection(String title, String[][] data) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setBorder(new EmptyBorder(30, 30, 10, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(lbl);

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(new Color(240, 240, 240));
        grid.setBorder(new EmptyBorder(10, 25, 20, 45));
        for (String[] item : data) grid.add(createFoodCard(item));
        mainContentPanel.add(grid);
    }

    private JPanel createFoodCard(String[] food) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 5));
        info.setBorder(new EmptyBorder(15, 15, 15, 15));
        info.setBackground(Color.WHITE);

        JLabel name = new JLabel(food[0]);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel desc = new JLabel("<html><body style='width: 150px;'>" + food[1] + "</body></html>");
        desc.setForeground(Color.GRAY);

        int unitPrice = Integer.parseInt(food[2]);
        JLabel priceLabel = new JLabel(String.format("%,d VNĐ", unitPrice));
        priceLabel.setForeground(new Color(52, 152, 219));

        info.add(name); info.add(desc); info.add(priceLabel);

        JPanel action = new JPanel(new BorderLayout());
        action.setBackground(Color.WHITE);
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        qtyPanel.setOpaque(false);
        JButton btnM = new JButton("-");
        JLabel lblQ = new JLabel("1", SwingConstants.CENTER);
        lblQ.setPreferredSize(new Dimension(30, 20));
        JButton btnP = new JButton("+");
        qtyPanel.add(btnM); qtyPanel.add(lblQ); qtyPanel.add(btnP);

        JButton btnBuy = new JButton("MUA NGAY: " + String.format("%,d", unitPrice) + "đ");
        btnBuy.setBackground(new Color(46, 204, 113));
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnP.addActionListener(e -> {
            int q = Integer.parseInt(lblQ.getText()) + 1;
            lblQ.setText(String.valueOf(q));
            btnBuy.setText("MUA NGAY: " + String.format("%,d", q * unitPrice) + "đ");
        });

        btnM.addActionListener(e -> {
            int q = Integer.parseInt(lblQ.getText());
            if (q > 1) {
                q--; lblQ.setText(String.valueOf(q));
                btnBuy.setText("MUA NGAY: " + String.format("%,d", q * unitPrice) + "đ");
            }
        });

        btnBuy.addActionListener(e -> {
            int q = Integer.parseInt(lblQ.getText());
            String selectedFoodName = food[0];
            updateFoodStock(selectedFoodName, q); // FIX: Gọi hàm trừ kho ở đây
            JOptionPane.showMessageDialog(this, "<html><center>Thanh toán thành công!<br><b>" + selectedFoodName + " x" + q + "</b></center></html>");
            lblQ.setText("1");
            btnBuy.setText("MUA NGAY: " + String.format("%,d", unitPrice) + "đ");
        });

        action.add(qtyPanel, BorderLayout.NORTH);
        action.add(btnBuy, BorderLayout.SOUTH);
        card.add(info, BorderLayout.CENTER);
        card.add(action, BorderLayout.SOUTH);
        return card;
    }

    // --- HÀM TRỪ KHO ---
    private void updateFoodStock(String foodName, int quantityToSubtract) {
        File file = new File("foods.txt");
        StringBuilder newContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equalsIgnoreCase(foodName)) {
                    int currentStock = Integer.parseInt(parts[3]);
                    int updatedStock = Math.max(0, currentStock - quantityToSubtract);
                    line = parts[0] + "," + parts[1] + "," + parts[2] + "," + updatedStock;
                }
                newContent.append(line).append("\n");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(newContent.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createMovieCard(String[] data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // --- BẮT ĐẦU PHẦN SỬA ĐỂ HIỂN THỊ ẢNH ---
        JLabel poster = new JLabel("", SwingConstants.CENTER); // Mặc định không có chữ
        poster.setPreferredSize(new Dimension(280, 200)); // Tăng kích thước khung ảnh cho đẹp
        poster.setBackground(Config.SECONDARY); // Màu nền khi chưa load được ảnh
        poster.setOpaque(true);

        // Logic Load ảnh thực tế
        try {
            // Lấy đường dẫn ảnh từ cột cuối cùng của mảng (data[8])
            String imagePath = data[8];

            // Kiểm tra xem đường dẫn có trống không
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);

                // Nếu ảnh tồn tại và load được
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image img = icon.getImage();
                    // Scale ảnh cho khớp với khung (280x200) để không bị vỡ/méo
                    Image scaledImg = img.getScaledInstance(280, 200, Image.SCALE_SMOOTH);
                    poster.setIcon(new ImageIcon(scaledImg));
                    poster.setBackground(Color.WHITE); // Đổi nền trắng khi có ảnh
                } else {
                    // Nếu không load được (sai đường dẫn, file hỏng)
                    poster.setText("<html><center>ẢNH LỖI<br><font size='2' color='gray'>Vui lòng kiểm tra file assets</font></center></html>");
                    System.err.println("Lỗi load ảnh: " + imagePath);
                }
            } else {
                poster.setText("CHƯA CÓ ẢNH");
            }
        } catch (Exception e) {
            poster.setText("LỖI HỆ THỐNG");
            e.printStackTrace();
        }
        // --- KẾT THÚC PHẦN SỬA ĐỂ HIỂN THỊ ẢNH ---

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblT = new JLabel(data[0]); lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblD = new JLabel(data[4] + " | " + data[5]); lblD.setForeground(Color.GRAY);
        info.add(lblT); info.add(lblD);

        JButton btn = new JButton("XEM CHI TIẾT");
        btn.setBackground(Config.ACCENT); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> {
            this.dispose();
            new MovieDetailFrame(data, currentUsername).setVisible(true);
        });

        card.add(poster, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    private void showBookingHistory() {
        String[] columns = {"Phim", "Rạp", "Ngày", "Giờ", "Ghế", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String fileName = "history_" + currentUsername + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 6) model.addRow(parts);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Bạn chưa có lịch sử đặt vé nào!");
            return;
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        JDialog d = new JDialog(this, "VÉ CỦA TÔI", true);
        d.setSize(900, 450);
        d.add(new JScrollPane(table));
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void showTerms() {
        String terms = "<html><body style='width: 550px; font-family: Segoe UI; padding: 15px; background-color: white;'>" +
                "<h1 style='color: #2c3e50; text-align: center; border-bottom: 2px solid #2c3e50; padding-bottom: 10px;'>ĐIỀU KHOẢN SỬ DỤNG DỊCH VỤ</h1>" +
                "<p><b>1. QUY ĐỊNH VỀ TÀI KHOẢN NGƯỜI DÙNG</b><br>" +
                "• <b>Đăng ký:</b> Người dùng phải cung cấp thông tin chính xác về Họ tên, Số điện thoại và Email. Thông tin này là căn cứ duy nhất để nhận vé.<br>" +
                "• <b>Bảo mật:</b> Người dùng tự chịu trách nhiệm bảo mật mật khẩu cá nhân.<br>" +
                "• <b>Độ tuổi:</b> Người dưới 13 tuổi phải có sự giám sát của người giám hộ hợp pháp.</p>" +
                "<p><b>2. QUY TRÌNH ĐẶT VÉ VÀ THANH TOÁN</b><br>" +
                "• <b>Xác nhận:</b> Giao dịch hoàn tất khi hệ thống gửi mã vé (QR Code) thành công qua App và Email.<br>" +
                "• <b>Giá vé:</b> Đã bao gồm VAT, có thể chưa bao gồm phí tiện ích tùy theo đối tác thanh toán.<br>" +
                "• <b>Giới hạn:</b> Sau khi chọn ghế, người dùng có <b>05 - 10 phút</b> để thanh toán. Quá thời gian ghế sẽ tự động giải phóng.</p>" +
                "<p><b>3. CHÍNH SÁCH HỦY VÉ VÀ HOÀN TIỀN</b><br>" +
                "• <b>Nguyên tắc:</b> Vé đã thanh toán thành công <b>KHÔNG THỂ</b> thay đổi suất chiếu, rạp, vị trí ghế và không được hoàn tiền.<br>" +
                "• <b>Ngoại lệ:</b> Hoàn tiền nếu suất chiếu bị hủy do lỗi từ nhà rạp hoặc lỗi trùng ghế hệ thống.<br>" +
                "• <b>Thời gian:</b> Tùy phương thức thanh toán (7-45 ngày làm việc).</p>" +
                "<p style='background-color:#fff5f5; padding:10px; border:1px solid #fab1a0;'> " +
                "<b style='color:#d63031;'>4. QUY ĐỊNH VỀ PHÂN LOẠI ĐỘ TUỔI (CỰC KỲ QUAN TRỌNG)</b><br>" +
                "Người dùng tự chịu trách nhiệm chọn phim phù hợp với độ tuổi:<br>" +
                "• <b>P:</b> Mọi độ tuổi | <b>K:</b> Dưới 13 tuổi xem cùng người giám hộ.<br>" +
                "• <b>T13, T16, T18:</b> Chỉ dành cho người xem từ đủ 13, 16, 18 tuổi trở lên.<br>" +
                "<b>Lưu ý:</b> Nhân viên rạp <b>SẼ KIỂM TRA CCCD</b>. Nếu không đúng tuổi, khách không được vào rạp và rạp không hoàn trả tiền vé.</p>" +
                "<p><b>5. QUY ĐỊNH TẠI RẠP</b><br>" +
                "• <b>Kiểm tra:</b> Xuất trình mã vé điện tử. Vé chụp màn hình có thể bị từ chối.<br>" +
                "• <b>Đồ ăn:</b> KHÔNG mang đồ ăn, thức uống từ bên ngoài vào rạp.<br>" +
                "• <b>Bản quyền:</b> NGHIÊM CẤM quay phim, chụp ảnh. Vi phạm sẽ bị xử lý theo pháp luật.</p>" +
                "<p><b>6. QUYỀN RIÊNG TƯ VÀ DỮ LIỆU</b><br>" +
                "• App thu thập thông tin vị trí và lịch sử mua vé để tối ưu hóa trải nghiệm.<br>" +
                "• Thông tin cá nhân có thể dùng để gửi thông báo khuyến mãi, quà tặng.</p>" +
                "<p><b>7. MIỄN TRỪ TRÁCH NHIỆM</b><br>" +
                "• Hệ thống không chịu trách nhiệm nếu khách hàng cung cấp sai Email/Số điện thoại.<br>" +
                "• Không chịu trách nhiệm lỗi từ phía ngân hàng hoặc ví điện tử khi thanh toán.</p>" +
                "</body></html>";
        JEditorPane ed = new JEditorPane("text/html", terms);
        ed.setEditable(false);
        ed.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(ed);
        scroll.setPreferredSize(new Dimension(650, 500));
        scroll.setBorder(null);
        JDialog d = new JDialog(this, "Chính sách Cinema", true);
        d.setLayout(new BorderLayout());
        d.add(scroll, BorderLayout.CENTER);
        JButton btnClose = new JButton("ĐÃ HIỂU VÀ ĐỒNG Ý");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setBackground(new Color(46, 204, 113));
        btnClose.setForeground(Color.WHITE);
        btnClose.addActionListener(e -> d.dispose());
        d.add(btnClose, BorderLayout.SOUTH);
        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void refreshView() {
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        SwingUtilities.invokeLater(() -> mainScroll.getVerticalScrollBar().setValue(0));
    }

    private JPanel createFooter() {
        Color footerBg = new Color(33, 37, 41);
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(footerBg);
        footer.setBorder(new EmptyBorder(40, 50, 40, 50));
        footer.setMaximumSize(new Dimension(5000, 220));
        JPanel left = new JPanel(); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS)); left.setOpaque(false);
        left.add(new JLabel("<html><b style='color:white; font-size:15px;'>CÔNG TY TNHH LINH CHÂU</b></html>"));
        left.add(Box.createVerticalStrut(10));
        left.add(new JLabel("<html><div style='color:#bdc3c7; font-size:11px;'>Số ĐKKD: 3636363636 · Nơi cấp: Học viện Bưu chính Viễn thông<br>Địa chỉ: Số 33, đường Đại Mỗ, Nam Từ Liêm, Hà Nội</div></html>"));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT)); right.setOpaque(false);
        right.add(new JLabel("<html><div style='text-align:right; color:#bdc3c7;'><b style='color:white;'>ĐỐI TÁC THANH TOÁN</b><br>Momo | ZaloPay | VNPAY<br><br><span style='color:#27ae60; border:1px solid #27ae60; padding:3px;'>✓ ĐÃ THÔNG BÁO BỘ CÔNG THƯƠNG</span></div></html>"));
        footer.add(left, BorderLayout.WEST); footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private JButton createNavButton(String t) {
        JButton b = new JButton(t); b.setPreferredSize(new Dimension(200, 45));
        b.setBackground(Config.PRIMARY); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false); return b;
    }
    private void applyHoverEffect(JButton btn) {
        // 1. Lưu lại màu gốc của nút
        Color originalColor = btn.getBackground();

        // 2. Tạo màu sáng hơn
        Color hoverColor = originalColor.brighter();

        // 3. Lắng nghe sự kiện chuột lướt qua
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Lướt chuột VÀO -> Đổi màu sáng lên
                btn.setBackground(hoverColor);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hiện bàn tay cho chuyên nghiệp
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Lướt chuột RA -> Trả lại màu gốc
                btn.setBackground(originalColor);
            }
        });
    }
}