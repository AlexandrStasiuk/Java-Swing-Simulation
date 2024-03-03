package components;

import actions.ShowInfoAction;
import actions.StopAction;
import actions.UpdateAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Parameters.*;
import static constants.Components.*;
import static laba.files.Habitat.setUserData;

public class UserInterface {
    private static final Font font = new Font("Times New Roman", Font.BOLD, 14);
    //Пользовательское меню
    public static void addUserInterface(){
        AbstractAction updateAction = new UpdateAction();
        AbstractAction stopAction = new StopAction();

        //Текст таймера
        TextTime.addTextTime();

        //Кнопка старта симуляции
        startButton.setBounds(20, 35, 150, 30);
        startButton.addActionListener(updateAction);
        startButton.setFont(font);
        startButton.setEnabled(true);
        panelButtons.add(startButton);
        //Кнопка остановки симуляции
        stopButton.setBounds(20, 70, 150, 30);
        stopButton.addActionListener(stopAction);
        stopButton.setEnabled(false);
        stopButton.setFont(font);
        panelButtons.add(stopButton);
        //Добавление событий при нажатии на клавиши
        KeyStroke updateKey = KeyStroke.getKeyStroke("B");
        KeyStroke stopKey = KeyStroke.getKeyStroke("E");
        InputMap inputMap = panelImages.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(updateKey, "updateStart");
        inputMap.put(stopKey, "updateStop");
        ActionMap actionMap = panelImages.getActionMap();
        actionMap.put("updateStart", updateAction);
        actionMap.put("updateStop", stopAction);

        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTime);
        buttonGroup.add(hiddenTime);
        //Кнопки для таймера
        showTime.setBounds(20, 110, 250, 30);
        showTime.setFont(font);
        showTime.setBackground(new Color(208, 208, 208));
        showTime.setSelected(true);
        showTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTime.setVisible(flVisibleTime);
            }
        });
        panelButtons.add(showTime);
        hiddenTime.setBounds(20, 140, 250, 30);
        hiddenTime.setFont(font);
        hiddenTime.setBackground(new Color(208, 208, 208));
        hiddenTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTime.setVisible(flVisibleTime);
            }
        });
        panelButtons.add(hiddenTime);

        //Поле с чекбоксом
        showInfo.setBounds(20, 170, 250, 30);
        showInfo.setFont(font);
        showInfo.setBackground(new Color(208, 208, 208));
        showInfo.addActionListener(new ShowInfoAction());
        panelButtons.add(showInfo);

        //Поля с параметрами
        UserFields.addFields();

        //Кнопка обновления параметров
        updateParameters.setBounds(20, 445, 150, 30);
        updateParameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //Установка пользовательских значений
                    setUserData();
                }catch (NumberFormatException exception){
                    //Обработка неправильных значений
                    timerCats.setText(String.valueOf(N1));
                    timerDogs.setText(String.valueOf(N2));
                    comboBoxCats.setSelectedItem((int)(P1*100));
                    comboBoxDogs.setSelectedItem((int)(P2*100));
                    timeLifeCats.setText(String.valueOf(L1));
                    timeLifeDogs.setText(String.valueOf(L2));
                    //Вывод диалогового окна с ошибкой
                    JOptionPane.showMessageDialog(
                            panelImages,
                            "Вводимые данные должны содержать только целые положительные числа",
                            "Ошибка вводимых данных",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        updateParameters.setFont(font);
        panelButtons.add(updateParameters);
    }
}
