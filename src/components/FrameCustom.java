package components;

import laba.files.Habitat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;

import static constants.Parameters.*;
import static constants.Parameters.updateTimer;


public class FrameCustom {
    //Создание окна симуляции
    public static JFrame getFrame(){
        JFrame jFrame = new JFrame();
        //Изменение адаптера закрытия окна
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Habitat.createConfigFile();
                jFrame.dispose();
            }
        });
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        //Определение рамеров окна
        WIDTH = dimension.width*3/4;
        HEIGHT = dimension.height*3/4;
        jFrame.setBounds(
                dimension.width/2-WIDTH/2,
                dimension.height/2-HEIGHT/2,
                WIDTH,
                HEIGHT
        );
        jFrame.setTitle("Лабораторная работа");
        jFrame.setLayout(null);
        return  jFrame;
    }
}
