package view;

import util.Config;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterFrame extends BaseFrame {
    private JTextField txtUser;
    private JPasswordField txtPass, txtConfirm;

    public RegisterFrame() {
        super("Cinema System - Đăng Ký");
        setSize(450, 450);
        initUI();
    }

    @Override
    public void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("ĐĂNG KÝ MỚI", SwingConstants.CENTER);
        lblTitle.setFont(Config.HEADER_FONT);
        lblTitle.setForeground(Config.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        mainPanel.add(new JLabel("Tài khoản:"), gbc);
        txtUser = new JTextField(15);
        gbc.gridx = 1; mainPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        txtPass = new JPasswordField(15);
        gbc.gridx = 1; mainPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Xác nhận:"), gbc);
        txtConfirm = new JPasswordField(15);
        gbc.gridx = 1; mainPanel.add(txtConfirm, gbc);

        JButton btnReg = new JButton("TẠO TÀI KHOẢN");
        btnReg.setBackground(new Color(46, 204, 113)); // Màu xanh lá cho tươi mới
        btnReg.setForeground(Color.WHITE);
        btnReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        mainPanel.add(btnReg, gbc);

        // Nút quay lại đăng nhập
        JButton btnBack = new JButton("Quay lại Đăng nhập");
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setForeground(Color.GRAY);
        gbc.gridy = 5;
        mainPanel.add(btnBack, gbc);
        btnBack.addActionListener(e -> { this.dispose(); new LoginFrame().setVisible(true); });

        // LOGIC ĐĂNG KÝ
        btnReg.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng không để trống!");
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            saveAccount(user, pass);
        });

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void saveAccount(String user, String pass) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt", true))) {
            // Thêm \n ở đầu để phòng trường hợp dòng cuối file cũ chưa xuống dòng
            bw.write("\n" + user + "," + pass + ",customer");
            bw.flush(); // Ép Java ghi dữ liệu ngay lập tức

            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
            this.dispose();
            new LoginFrame().setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi file!");
        }
    }
}