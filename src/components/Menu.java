package components;

import actions.ShowInfoAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Parameters.petsList;
import static constants.Components.*;
//Добавление меню
public class Menu {
    public static void addMenu(){
        jFrame.setJMenuBar(jMenuBar);
        jMenuBar.add(jMenu);
        //Поле с кнопками
        ButtonsMenu.addButtonsMenu();
        jMenu.addSeparator();
        //Поле с чекбоксом
        TimerButtonsMenu.addTimerButtonsMenu();
        jMenu.addSeparator();
        //Показ информации
        showInfoMenu.addActionListener(new ShowInfoAction());
        jMenu.add(showInfoMenu);
        jMenu.addSeparator();
        //Живые объекты
        jMenu.add(showLiveObjects);
        showLiveObjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogLiveObjects(petsList).setVisible(true);
            }
        });
    }
}
