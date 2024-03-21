package actions;

import laba.files.Cats;
import laba.files.Dogs;
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
                timeLifeCats.setText(String.valueOf(Cats.timeLife));
                timeLifeDogs.setText(String.valueOf(Dogs.timeLife));
                speedCats.setText(String.valueOf(V1));
                speedDogs.setText(String.valueOf(V2));
                //Вывод диалогового окна с ошибкой
                String errorMsg = "Вводимые данные должны содержать только числа";
                if(!exception.getMessage().contains("For input"))
                    errorMsg = exception.getMessage();
                JOptionPane.showMessageDialog(
                        panelImages,
                        errorMsg,
                        "Ошибка вводимых данных",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
