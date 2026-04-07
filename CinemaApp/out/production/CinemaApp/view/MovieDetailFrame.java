package view;

import util.Config;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MovieDetailFrame extends BaseFrame {
    private String[] data;
    private String selectedDate = "09/04/2026";
    private String username;
    private JPanel theatersContainer;

    public MovieDetailFrame(String[] movieData, String username) {
        super("Chi tiết & Lịch chiếu: " + movieData[0]);
        this.data = movieData;
        this.username = username;
        initUI();
    }

    @Override
    public void initUI() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);

        // --- PHẦN 1: HEADER THÔNG TIN PHIM  ---
        JPanel headerPanel = new JPanel(new BorderLayout(25, 0));
        headerPanel.setBackground(new Color(20, 20, 20));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        headerPanel.setMaximumSize(new Dimension(2000, 320));

        JLabel lblPoster = new JLabel("", SwingConstants.CENTER);
        lblPoster.setPreferredSize(new Dimension(180, 260));
        lblPoster.setBackground(new Color(40, 40, 40));
        lblPoster.setOpaque(true);

        try {
            String imagePath = data[9];
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                    Image scaledImg = icon.getImage().getScaledInstance(180, 260, Image.SCALE_SMOOTH);
                    lblPoster.setIcon(new ImageIcon(scaledImg));
                }
            }
        } catch (Exception e) {}

        headerPanel.add(lblPoster, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblName = new JLabel(data[0]);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblName.setForeground(Color.WHITE);

        String detailHtml = String.format(
                "<html><font color='white'>⭐ <b>%s</b> &nbsp;&nbsp; 📅 <b>%s</b> &nbsp;&nbsp; ⏳ <b>%s</b> &nbsp;&nbsp; 🔞 <b>%s</b><br><br>" +
                        "<p style='width: 550px;'>%s</p><br>🎬 <b>Đạo diễn:</b> %s<br>👥 <b>Diễn viên:</b> %s</font></html>",
                data[2], data[3], data[4], data[5], data[1], data[7], data[6]
        );
        JLabel lblDetails = new JLabel(detailHtml);
        textPanel.add(lblName);
        textPanel.add(Box.createVerticalStrut(15));
        textPanel.add(lblDetails);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        // --- PHẦN 2: LỊCH CHIẾU ---
        JPanel scheduleSection = new JPanel();
        scheduleSection.setLayout(new BoxLayout(scheduleSection, BoxLayout.Y_AXIS));
        scheduleSection.setBackground(Color.WHITE);
        scheduleSection.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 2.1. Bộ lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        filterPanel.setMaximumSize(new Dimension(2000, 50));
        filterPanel.add(new JLabel("📍 Khu vực:"));
        filterPanel.add(new JComboBox<>(new String[]{"Bắc Ninh", "Hà Nội", "Tp. Hồ Chí Minh"}));
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("🎞️ Định dạng:"));
        filterPanel.add(new JComboBox<>(new String[]{"2D Phụ đề", "IMAX", "Lồng tiếng"}));

        // 2.2. THANH CHỌN NGÀY
        JPanel datePanel = new JPanel(new GridLayout(1, 7, 5, 0));
        datePanel.setOpaque(false);
        datePanel.setMaximumSize(new Dimension(2000, 80));
        datePanel.setPreferredSize(new Dimension(900, 80));

        String[] dates = {"9/4\nTh 5", "10/4\nTh 6", "11/4\nTh 7", "12/4\nCN", "13/4\nTh 2", "14/4\nTh 3", "15/4\nTh 4"};

        for (String d : dates) {
            JButton btnDate = new JButton("<html><center>" + d.replace("\n", "<br>") + "</center></html>");
            btnDate.setBackground(Color.WHITE);
            btnDate.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDate.addActionListener(e -> {
                // Logic xử lý ngày chọn để không bị lỗi parse
                String dayPart = d.split("\n")[0];
                String[] p = dayPart.split("/");
                selectedDate = String.format("%02d/%02d/2026", Integer.parseInt(p[0]), Integer.parseInt(p[1]));
                updateSchedule(); // Vẽ lại lịch chiếu
            });
            datePanel.add(btnDate);
        }

        // 2.3. Container chứa các rạp và giờ chiếu
        theatersContainer = new JPanel();
        theatersContainer.setLayout(new BoxLayout(theatersContainer, BoxLayout.Y_AXIS));
        theatersContainer.setOpaque(false);

        // Nút quay lại
        JButton btnBack = new JButton("← Quay lại trang chủ");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.addActionListener(e -> { this.dispose(); new UserApp(username).setVisible(true); });

        scheduleSection.add(filterPanel);
        scheduleSection.add(Box.createVerticalStrut(10));
        scheduleSection.add(datePanel);
        scheduleSection.add(Box.createVerticalStrut(20));
        scheduleSection.add(theatersContainer);

        container.add(headerPanel);
        container.add(scheduleSection);
        container.add(btnBack);

        add(new JScrollPane(container));
        updateSchedule(); // Nạp lịch lần đầu

        setSize(950, 800);
        setLocationRelativeTo(null);
    }

    private void updateSchedule() {
        if (theatersContainer == null) return;
        theatersContainer.removeAll();

        String[] theaterNames = {"BHD Star Cineplex - Vincom", "CGV Cinema - Center Point", "Lotte Cinema - Kinh Dương Vương"};
        String[][] allTimes = { {"09:30", "12:45", "18:30"}, {"10:00", "14:15", "19:00", "22:30"}, {"08:45", "15:00", "21:15"} };

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate releaseDate = LocalDate.parse(data[3], dtf);
            LocalDate currentSelectedDate = LocalDate.parse(selectedDate, dtf);

            for (int i = 0; i < theaterNames.length; i++) {
                final String tName = theaterNames[i];
                final String[] times = allTimes[i];

                JPanel theaterBox = new JPanel(new BorderLayout());
                theaterBox.setBackground(Color.WHITE);
                TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), tName);
                border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
                theaterBox.setBorder(border);

                JPanel timeFlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
                timeFlow.setOpaque(false);

                for (String t : times) {
                    JButton btnTime = new JButton(t);
                    btnTime.setFont(new Font("Segoe UI", Font.BOLD, 13));

                    if (currentSelectedDate.isBefore(releaseDate)) {
                        btnTime.setEnabled(false); // Khóa nếu chưa chiếu
                        btnTime.setBackground(new Color(235, 235, 235));
                    } else {
                        btnTime.setEnabled(true);
                        btnTime.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        btnTime.addActionListener(ev -> {
                            this.dispose();
                            new SeatSelectionFrame(data[0], tName, t, selectedDate, username).setVisible(true);
                        });
                    }
                    timeFlow.add(btnTime);
                }
                theaterBox.add(timeFlow);
                theatersContainer.add(theaterBox);
                theatersContainer.add(Box.createVerticalStrut(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        theatersContainer.revalidate();
        theatersContainer.repaint();
    }
}