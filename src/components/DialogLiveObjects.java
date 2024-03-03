package components;

import constants.Parameters;
import laba.files.Habitat;
import laba.files.Pets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static constants.Components.jFrame;

//Модальное окно со списком живых объектов
public class DialogLiveObjects extends JDialog {
    public DialogLiveObjects(ArrayList<Pets> list) {
        //Создание модального окна
        super(jFrame, "Все живые объекты", true);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(
                dimension.width/2- Parameters.WIDTH/4,
                dimension.height/2-Parameters.HEIGHT/4,
                Parameters.WIDTH/2,
                Parameters.HEIGHT/2
        );

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.WHITE);
        jPanel.setLayout(null);
        add(jPanel);

        //Создание списка
        StringBuilder textList = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            textList.append(i + 1).append(") ").append(list.get(i).toString()).append("\n");
        }
        //Добавление текстового поля со списком
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setText(textList.toString());
        jTextArea.setEditable(false);
        jTextArea.setBackground(Color.WHITE);
        jTextArea.setLineWrap(true);
        jTextArea.setFont(font);

        //Добавление скролла для текстового поля
        JScrollPane jScrollPane = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.setBounds(10, 10, getWidth(), getHeight() - 110);
        jPanel.add(jScrollPane);

        //Закрытие окна
        JButton okBtn = new JButton("ОК");
        okBtn.setBounds(getWidth()/2-50, getHeight()-100, 100, 30);
        jPanel.add(okBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
