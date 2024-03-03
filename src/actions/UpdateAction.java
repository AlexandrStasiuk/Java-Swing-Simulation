package actions;

import laba.files.Habitat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Timer;

import static laba.files.Habitat.*;
import static constants.Components.*;
import static constants.Parameters.*;

//Событие обновления при клике
public class UpdateAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        //Проверка на запущенную симуляцию
        if(!flStart){
            try{
                //Установка пользовательских значений
                setUserData();
                textTime.setVisible(flVisibleTime);
                setEnableStart(false);
                //Создание таймера и добавление периода
                updateTimer = new Timer();
                updateTimer.schedule(new Habitat.UpdateTask(),0,  1000);
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
    }
}
