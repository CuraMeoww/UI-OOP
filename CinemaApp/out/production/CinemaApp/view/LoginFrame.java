package view;

import util.Config;
import util.SoundManager; // Nhớ import cái SoundManager mình viết lúc nãy
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginFrame extends BaseFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        super("Cinema System - Đăng Nhập");
        setSize(450, 420);
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

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Dùng Font trực tiếp nếu Config chưa có
        lblTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        // 2. Tài khoản
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        mainPanel.add(new JLabel("Tài khoản:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        // 3. Mật khẩu
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        // 4. Nút Đăng nhập
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(btnLogin, gbc);

        // 5. Quên mật khẩu & Đăng ký
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(Color.WHITE);

        JLabel lblForgot = new JLabel("<html><u>Quên mật khẩu?</u></html>");
        lblForgot.setForeground(Color.GRAY);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Vui lòng liên hệ Admin để cấp lại mật khẩu!");
            }
        });

        JLabel lblRegister = new JLabel("<html><u>Đăng ký tài khoản</u></html>");
        lblRegister.setForeground(new Color(41, 128, 185));
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new RegisterFrame().setVisible(true);
            }
        });

        actionPanel.add(lblForgot);
        actionPanel.add(lblRegister);
        gbc.gridy = 4;
        mainPanel.add(actionPanel, gbc);

        // --- LOGIC XỬ LÝ ĐĂNG NHẬP & PHÁT NHẠC ---
        btnLogin.addActionListener(e -> {
            String inputUser = txtUsername.getText();
            String inputPass = new String(txtPassword.getPassword());
            String role = checkLogin(inputUser, inputPass);

            if (role != null) {
                new Thread(() -> {
                    SoundManager.playMusic("src/res/ala.wav");
                }).start();

                if (role.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Chào Admin!");
                    this.dispose();
                    new AdminPanel(inputUser).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào " + inputUser);
                    this.dispose();
                    new UserApp(inputUser).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private String checkLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if (parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                        return parts[2].trim(); // Trả về role: admin hoặc customer
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file accounts.txt: " + e.getMessage());
        }
        return null;
    }
}