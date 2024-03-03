package components;

import java.awt.*;

import static constants.Parameters.HEIGHT;
import static constants.Parameters.WIDTH;
import static constants.Components.*;

public class Panels {
    //Добавление панелей
    public static void addPanels(){
        //Панель для картинок
        panelImages.setLayout(null);
        panelImages.setBounds(0, 0, WIDTH*75/100, HEIGHT);
        panelImages.setBackground(new Color(188, 255, 241));
        jFrame.add(panelImages);

        //Панель кнопок
        panelButtons.setLayout(null);
        panelButtons.setBounds(WIDTH*75/100, 0, WIDTH*25/100, HEIGHT);
        panelButtons.setBackground(new Color(208, 208, 208));
        jFrame.add(panelButtons);
    }
}
