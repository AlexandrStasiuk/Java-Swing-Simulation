package components;

import laba.files.Habitat;

import javax.swing.*;
import java.awt.*;

//Компонент текста с таймером
public class TextTime extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        Font font = new Font("Times New Roman", Font.BOLD, 18);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.drawString("Время работы - " + Habitat.timeElapsed + " сек.", 0, 20);
    }
}
