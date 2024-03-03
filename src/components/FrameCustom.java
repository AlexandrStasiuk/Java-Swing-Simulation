package components;

import javax.swing.*;
import java.awt.*;

import static constants.Parameters.HEIGHT;
import static constants.Parameters.WIDTH;


public class FrameCustom {
    //Создание окна симуляции
    public static JFrame getFrame(){
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
