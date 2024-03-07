package components;

import actions.ShowInfoAction;
import laba.files.BaseAI;

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
        jMenu.addSeparator();
        //Движение объектов
        moveCats.setSelected(true);
        moveDogs.setSelected(true);
        jMenu.add(moveCats);
        jMenu.add(moveDogs);
    }
}
