package components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Parameters.flVisibleTime;
import static constants.Components.*;

public class TimerButtonsMenu {
    //Кнопки для скрытия таймера
    public static void addTimerButtonsMenu(){
        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTimeMenu);
        buttonGroup.add(hiddenTimeMenu);

        showTimeMenu.setSelected(true);
        showTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = true;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(showTimeMenu);

        hiddenTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = false;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(hiddenTimeMenu);
    }
}
