package view;

import util.Config;
import model.Seat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFrame extends BaseFrame {
    private List<String> selectedSeats = new ArrayList<>();
    private JLabel lblInfo;

    // Biến username để lưu lịch sử riêng tư
    private String movieName, theaterName, showTime, showDate, username;

    // Constructor nhận đủ tham số
    public SeatSelectionFrame(String movie, String theater, String time, String date, String username) {
        super("Cinema - Chọn Ghế Ngồi");
        this.movieName = movie;
        this.theaterName = theater;
        this.showTime = time;
        this.showDate = date;
        this.username = username; // Nhận tên người dùng đang đăng nhập
        initUI();
    }

    @Override
    public void initUI() {
        // --- HEADER ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JButton btnBack = new JButton(" ← QUAY LẠI ");
        btnBack.setBackground(Config.PRIMARY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            this.dispose();
            // Truyền username ngược lại cho UserApp
            new UserApp(username).setVisible(true);
        });

        String subTitle = String.format("<html><center>%s<br><font size='3' color='gray'>%s | %s | %s</font></center></html>",
                movieName, theaterName, showDate, showTime);
        JLabel lblHeader = new JLabel(subTitle, SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(lblHeader, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Lưới ghế ---
        JPanel seatGrid = new JPanel(new GridLayout(8, 10, 8, 8));
        seatGrid.setBorder(new EmptyBorder(30, 50, 30, 50));
        seatGrid.setBackground(Color.WHITE);

        for (int i = 0; i < 80; i++) {
            String row = String.valueOf((char)('A' + i / 10));
            int col = (i % 10) + 1;
            String seatId = row + col;
            String type = (i >= 30 && i < 50) ? "VIP" : "Normal";
            Seat seat = new Seat(seatId, type, false);
            seatGrid.add(createSeatButton(seat));
        }
        add(new JScrollPane(seatGrid), BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        footer.setBackground(Config.HIGHLIGHT);

        lblInfo = new JLabel("Chưa có ghế nào được chọn");
        lblInfo.setFont(Config.NORMAL_FONT);

        JButton btnPay = new JButton("XÁC NHẬN THANH TOÁN");
        btnPay.setBackground(Config.ACCENT);
        btnPay.setForeground(Color.WHITE);
        btnPay.setPreferredSize(new Dimension(220, 40));
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnPay.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế!");
            } else {
                showBill();
            }
        });

        footer.add(lblInfo, BorderLayout.WEST);
        footer.add(btnPay, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton createSeatButton(Seat seat) {
        JButton btn = new JButton(seat.getId());
        Color originalColor = seat.getType().equals("VIP") ? Config.HIGHLIGHT : Config.SECONDARY;
        btn.setBackground(originalColor);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            if (btn.getBackground().equals(Config.ACCENT)) {
                btn.setBackground(originalColor);
                btn.setForeground(Color.BLACK);
                selectedSeats.remove(seat.getId());
            } else {
                btn.setBackground(Config.ACCENT);
                btn.setForeground(Color.WHITE);
                selectedSeats.add(seat.getId());
            }
            lblInfo.setText("Ghế chọn: " + selectedSeats.toString());
        });
        return btn;
    }

    private void showBill() {
        int pricePerSeat = 85000;
        long total = (long) selectedSeats.size() * pricePerSeat;

        String billInfo = String.format(
                "🎬 Phim: %s\n📍 Rạp: %s\n📅 Ngày: %s\n🕒 Giờ: %s\n💺 Ghế: %s\n💰 Tổng tiền: %,d VNĐ",
                movieName, theaterName, showDate, showTime, selectedSeats.toString(), total
        );

        int choice = JOptionPane.showConfirmDialog(this, billInfo + "\n\nXác nhận đặt vé?", "XÁC NHẬN HÓA ĐƠN", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // Định dạng lưu trữ (Username không cần ghi vào nội dung file nữa vì mình lưu file riêng)
            String record = String.format("%s | %s | %s | %s | %s | %,d VNĐ",
                    movieName, theaterName, showDate, showTime, selectedSeats.toString(), total);

            saveBookingHistory(record);
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            this.dispose();
            // Truyền username ngược lại cho UserApp sau khi mua vé xong
            new UserApp(username).setVisible(true);
        }
    }

    private void saveBookingHistory(String content) {
        // Lưu vào file riêng theo từng User: history_linh.txt, history_chau.txt...
        String fileName = "history_" + username + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}