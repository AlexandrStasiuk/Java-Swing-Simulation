package components;

import java.awt.*;

import static constants.Components.*;

public class UserFields {
    private static final Font font = new Font("Times New Roman", Font.BOLD, 14);
    //Текстовые поля для заполнения
    public static void addFields(){
        //Лейбел для комбобокса у котов
        labelComboCats.setBounds(20, 200, 250, 20);
        labelComboCats.setFont(font);
        labelComboCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboCats);

        //Комбобокс у котов
        comboBoxCats.setBounds(20, 220, 200, 20);
        comboBoxCats.setFont(font);
        panelButtons.add(comboBoxCats);

        //Лейбел для комбобокса у собак
        labelComboDogs.setBounds(20, 240, 250, 20);
        labelComboDogs.setFont(font);
        labelComboDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboDogs);

        //Комбобокс у собак
        comboBoxDogs.setBounds(20, 260, 200, 20);
        comboBoxDogs.setFont(font);
        panelButtons.add(comboBoxDogs);

        //Лейбел для текстового поля у котов
        labelTimerCats.setBounds(20, 280, 250, 20);
        labelTimerCats.setFont(font);
        labelTimerCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerCats);

        //Текстовое поле у котов
        timerCats.setBounds(20, 300, 200, 20);
        timerCats.setFont(font);
        panelButtons.add(timerCats);

        //Лейбел для текстового поля у собак
        labelTimerDogs.setBounds(20, 320, 250, 20);
        labelTimerDogs.setFont(font);
        labelTimerDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerDogs);

        //Текстовое поле у собак
        timerDogs.setBounds(20, 340, 200, 20);
        timerDogs.setFont(font);
        panelButtons.add(timerDogs);

        //Лейбел времени жизни у котов
        labelTimeLifeCats.setBounds(20, 360, 250, 20);
        labelTimeLifeCats.setFont(font);
        labelTimeLifeCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimeLifeCats);

        //Текстовое поле времени жизни у котов
        timeLifeCats.setBounds(20, 380, 200, 20);
        timeLifeCats.setFont(font);
        panelButtons.add(timeLifeCats);

        //Лейбел времени жизни у собак
        labelTimeLifeDogs.setBounds(20, 400, 250, 20);
        labelTimeLifeDogs.setFont(font);
        labelTimeLifeDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimeLifeDogs);

        //Текстовое поле времени жизни у собак
        timeLifeDogs.setBounds(20, 420, 200, 20);
        timeLifeDogs.setFont(font);
        panelButtons.add(timeLifeDogs);

        //Лейбел скорости у котов
        labelSpeedCats.setBounds(20, 440, 250, 20);
        labelSpeedCats.setFont(font);
        labelSpeedCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelSpeedCats);

        //Текстовое поле скорости котов
        speedCats.setBounds(20, 460, 200, 20);
        speedCats.setFont(font);
        panelButtons.add(speedCats);

        //Лейбел скорости у собак
        labelSpeedDogs.setBounds(20, 480, 250, 20);
        labelSpeedDogs.setFont(font);
        labelSpeedDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelSpeedDogs);

        //Текстовое поле скорости собак
        speedDogs.setBounds(20, 500, 200, 20);
        speedDogs.setFont(font);
        panelButtons.add(speedDogs);
    }
}
