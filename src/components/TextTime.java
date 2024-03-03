package components;

import actions.HiddenTimeAction;
import constants.Parameters;

import javax.swing.*;
import java.awt.*;

import static constants.Components.*;
import static constants.Parameters.timeElapsed;

//Компонент текста с таймером
public class TextTime extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        Font font = new Font("Times New Roman", Font.BOLD, 18);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.drawString("Время работы - " + timeElapsed + " сек.", 0, 20);
    }
    //Добавление текста с таймером
    public static void addTextTime(){
        textTime.setBounds(20, 5, Parameters.WIDTH*25/100 - 50, 25);
        panelButtons.add(textTime);
        textTime.setVisible(false);

        //Добавление события скрытия таймера
        KeyStroke hiddenKey = KeyStroke.getKeyStroke("T");
        InputMap inputMap = panelButtons.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(hiddenKey, "hiddenTime");
        ActionMap actionMap = panelButtons.getActionMap();
        actionMap.put("hiddenTime", new HiddenTimeAction());
    }
}
