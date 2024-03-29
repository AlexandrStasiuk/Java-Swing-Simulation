package components;

import constants.Parameters;
import laba.files.Habitat;
import laba.files.Pets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.Timer;

import static constants.Components.jFrame;
import static constants.Components.textTime;
import static constants.Parameters.*;

//Кастомное диалоговое окно с информацией
public class DialogInfo extends JDialog {
    public DialogInfo() {
        //Создание модального окна
        super(jFrame, "Информация об объектах", true);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(
                dimension.width/2- Parameters.WIDTH/4,
                dimension.height/2-Parameters.HEIGHT/4,
                Parameters.WIDTH/2,
                Parameters.HEIGHT/2
        );
        //Изменение адаптера закрытия окна
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updateTimer = new Timer();
                updateTimer.schedule(new Habitat.UpdateTask(),0,  1000);
                dispose();
            }
        });

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.WHITE);
        jPanel.setLayout(null);
        add(jPanel);

        Font font = new Font("Times New Roman", Font.BOLD, 14);

        //Вывод информации в TextArea
        JTextArea textStatistics = new JTextArea();
        String s = MessageFormat.format(
                "\tВремя работы симуляции - {0} сек.\n\tКошек сгенерировано - {1}\n\tСобак сгенерировано - {2}",
                timeElapsed,
                Pets.countCats,
                Pets.countDogs
        );
        textStatistics.setText(s);
        textStatistics.setEditable(false);
        textStatistics.setBounds(0, 50, getWidth(), getHeight()/3);
        textStatistics.setFont(font);
        textStatistics.setTabSize(15);
        jPanel.add(textStatistics);

        //Завершение симуляции
        JButton okBtn = new JButton("ОК");
        okBtn.setBounds(75, getHeight()-100, 100, 30);
        jPanel.add(okBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Habitat.setEnableStart(true);
                //Очистка старой симуляции
                Habitat.clearSession();
                textTime.setVisible(false);
                dispose();
            }
        });

        //Продолжение симуляции
        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.setBounds(getWidth()-175, getHeight()-100, 100, 30);
        jPanel.add(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer = new Timer();
                updateTimer.schedule(new Habitat.UpdateTask(),0,  1000);
                dispose();
            }
        });
    }
}
