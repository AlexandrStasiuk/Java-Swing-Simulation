package components;

import actions.StopAction;
import actions.UpdateAction;

import javax.swing.*;

import static constants.Components.*;

public class ButtonsMenu {
    //Добавление кнопок
    public static void addButtonsMenu(){
        //Кнопка старта симуляции
        startButtonMenu.addActionListener(new UpdateAction());
        startButtonMenu.setEnabled(true);
        startButtonMenu.setAccelerator(KeyStroke.getKeyStroke("B"));
        jMenu.add(startButtonMenu);

        //Кнопка остановки симуляции
        stopButtonMenu.addActionListener(new StopAction());
        stopButtonMenu.setEnabled(false);
        stopButtonMenu.setAccelerator(KeyStroke.getKeyStroke("E"));
        jMenu.add(stopButtonMenu);

    }
}
