package components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Parameters.flStart;
import static constants.Parameters.flVisibleTime;
import static constants.Components.*;

public class TimerButtonsMenu {
    //Кнопки для скрытия таймера
    public static void addTimerButtonsMenu(){
        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTimeMenu);
        buttonGroup.add(hiddenTimeMenu);

        showTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = !flVisibleTime;
                if (flStart)
                    textTime.setVisible(flVisibleTime);
                showTime.setSelected(showTimeMenu.isSelected());
            }
        });
        jMenu.add(showTimeMenu);

        hiddenTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = !flVisibleTime;
                if (flStart)
                    textTime.setVisible(flVisibleTime);
                hiddenTime.setSelected(hiddenTimeMenu.isSelected());
            }
        });
        jMenu.add(hiddenTimeMenu);
        if(flVisibleTime){
            showTimeMenu.setSelected(true);
            hiddenTimeMenu.setSelected(false);
        }else {
            showTimeMenu.setSelected(false);
            hiddenTimeMenu.setSelected(true);
        }
    }
}
