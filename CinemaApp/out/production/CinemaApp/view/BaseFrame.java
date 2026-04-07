package view;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    public BaseFrame(String title) {
        setTitle(title);
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Sử dụng BorderLayout làm khung chính [cite: 84]
        setLocationRelativeTo(null);
    }

    // Phương thức thuần ảo (Abstract Method) buộc các con phải tự định nghĩa [cite: 33]
    public abstract void initUI();
}