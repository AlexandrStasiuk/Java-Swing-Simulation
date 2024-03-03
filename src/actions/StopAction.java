package actions;

import components.DialogInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static constants.Components.textTime;
import static laba.files.Habitat.*;
import static constants.Parameters.*;

//Остановка симуляции
public class StopAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        //Проверка на включенное диалоговое окно
        if(flShowInfo && flStart){
            //Отключение симуляции во время работы окна
            if(updateTimer != null)
                updateTimer.cancel();
            //Диалоговое окно с информацией
            DialogInfo dialogInfo = new DialogInfo();
            dialogInfo.setVisible(true);
        }else if(flStart){
            //Проверка на запущенную симуляцию
            textTime.setVisible(false);
            setEnableStart(true);
            //Очистка старой симуляции
            clearSession();
            //Очистка таймера
            if(updateTimer != null)
                updateTimer.cancel();
        }
    }
}
