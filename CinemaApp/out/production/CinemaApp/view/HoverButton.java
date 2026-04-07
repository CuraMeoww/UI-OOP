package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButton extends JButton {
    private Color normalColor;
    private Color hoverColor;

    public HoverButton(String text, Color normal, Color hover) {
        super(text);
        this.normalColor = normal;
        this.hoverColor = hover;

        setBackground(normalColor);
        setFocusPainted(false);
        setBorderPainted(false);

        // Đóng gói logic xử lý Hover vào chính đối tượng
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { setBackground(normalColor); }
        });
    }
}