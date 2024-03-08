package components;

import actions.ShowInfoAction;
import laba.files.BaseAI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Parameters.generationThread;
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
        jMenu.addSeparator();
        //Приоритеты потоков
        String[] prioritiesText = {"Минимальный", "Стандартный", "Максимальный"};
        jMenu.add(mainPriority);
        for (int i = 0; i < 3; i++) {
            JMenuItem jMenuItem = new JMenuItem(prioritiesText[i]);
            mainPriority.add(jMenuItem);
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(jMenuItem.getText().equals("Минимальный"))
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                    else if(jMenuItem.getText().equals("Стандартный"))
                        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                    else if(jMenuItem.getText().equals("Максимальный"))
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
            });
        }
        jMenu.add(generationPriority);
        for (int i = 0; i < 3; i++) {
            JMenuItem jMenuItem = new JMenuItem(prioritiesText[i]);
            generationPriority.add(jMenuItem);
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(jMenuItem.getText().equals("Минимальный"))
                        generationThread.setPriority(Thread.MIN_PRIORITY);
                    else if(jMenuItem.getText().equals("Стандартный"))
                        generationThread.setPriority(Thread.NORM_PRIORITY);
                    else if(jMenuItem.getText().equals("Максимальный"))
                        generationThread.setPriority(Thread.MAX_PRIORITY);
                }
            });
        }
    }
}
