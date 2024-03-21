package components;

import constants.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

import static constants.Components.*;

public class DialogConsole  extends JDialog {
    //Поток для записи команды из консоли
    private static PipedOutputStream outputCommandStream;
    //Поток для чтения команды из консоли
    private static PipedInputStream inputCommandStream;
    //Команды консоли
    private static final String STOP_GENERATION_CATS = "остановить генерацию котов";
    private static final String STOP_GENERATION_DOGS = "остановить генерацию собак";
    private static final String STOP_GENERATION = "остановить генерацию объектов";
    private static final String RESUME_GENERATION_CATS = "продолжить генерацию котов";
    private static final String RESUME_GENERATION_DOGS = "продолжить генерацию собак";
    private static final String RESUME_GENERATION = "продолжить генерацию объектов";
    public DialogConsole() {
        //Создание модального окна
        super(jFrame, "Консоль", false);
        setBounds(
                jFrame.getX(),
                jFrame.getY(),
                Parameters.WIDTH,
                Parameters.HEIGHT
        );

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.BLACK);
        jPanel.setLayout(null);
        add(jPanel);

        //Добавление текстового поля
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setBackground(Color.BLACK);
        jTextArea.setForeground(Color.GREEN);
        jTextArea.setLineWrap(true);
        jTextArea.setFont(font);
        jTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Запрет редактирования текста с других строк
                int caretPosition = jTextArea.getCaretPosition();
                if (caretPosition < jTextArea.getText().length()) {
                    e.consume();
                }
                //Обработка новой команды
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    //Инициализацию потоков ввода/вывода
                    outputCommandStream = new PipedOutputStream();
                    try {
                        inputCommandStream = new PipedInputStream(outputCommandStream);
                    } catch (IOException error) {
                        throw new RuntimeException(error);
                    }
                    //Передача команды и ответа через потоки ввода/вывода
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputCommandStream))){
                        //Получение команды
                        String[] str = jTextArea.getText().split("\n");
                        if(str.length == 0)
                            return;
                        String command = str[str.length - 1].toLowerCase();
                        //Запись команды в поток
                        outputCommandStream.write(command.getBytes());
                        outputCommandStream.flush();
                        //Чтение команды из потока
                        String commandStream = "";
                        while (reader.ready()) {
                            commandStream += (char) reader.read();
                        }
                        //Генерация ответа на команду и его запись в поток
                        if(!commandStream.isEmpty()){
                            outputCommandStream.write(getRequestCommand(commandStream).getBytes());
                        }
                        //Чтение ответа из потока
                        String request = "";
                        while (reader.ready()) {
                            request += (char) reader.read();
                        }
                        //Вывод ответа на команду в консоль
                        if(!request.isEmpty())
                            jTextArea.setText(jTextArea.getText() + "\n\t" + request);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }finally {
                        //Закрытие потоков
                        try {
                            outputCommandStream.close();
                            inputCommandStream.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                //Обработка стирания текста
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if(jTextArea.getText().isEmpty())
                        e.consume();
                    if(jTextArea.getText().endsWith("\n"))
                        e.consume();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                int caretPosition = jTextArea.getCaretPosition();
                if (caretPosition < jTextArea.getText().length()) {
                    e.consume();
                }
            }
        });

        //Добавление скролла для текстового поля
        JScrollPane jScrollPane = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.setBounds(10, 10, getWidth(), getHeight() - 110);
        jPanel.add(jScrollPane);

        //Закрытие окна
        JButton okBtn = new JButton("Закрыть");
        okBtn.setBounds(getWidth()/2-50, getHeight()-80, 100, 30);
        jPanel.add(okBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    //Ответ консоли на команды
    private static String getRequestCommand(String command){
        switch (command){
            case STOP_GENERATION_CATS:
                if(!generationCats.isSelected()) {
                    return "Генерация котов уже остановлена";
                }
                generationCats.setSelected(false);
                return "Генерация котов успешно остановлена";
            case STOP_GENERATION_DOGS:
                if(!generationDogs.isSelected()) {
                    return "Генерация собак уже остановлена";
                }
                generationDogs.setSelected(false);
                return "Генерация собак успешно остановлена";
            case RESUME_GENERATION_CATS:
                if(generationCats.isSelected()) {
                    return "Генерация котов уже продолжается";
                }
                generationCats.setSelected(true);
                return "Генерация котов успешно возобновлена";
            case RESUME_GENERATION_DOGS:
                if(generationDogs.isSelected()) {
                    return "Генерация собак уже продолжается";
                }
                generationDogs.setSelected(true);
                return "Генерация собак успешно возобновлена";
            case STOP_GENERATION:
                if(!generationDogs.isSelected() && !generationCats.isSelected()) {
                    return "Генерация объектов уже остановлена";
                }
                generationCats.setSelected(false);
                generationDogs.setSelected(false);
                return "Генерация объектов успешно остановлена";
            case RESUME_GENERATION:
                if(generationDogs.isSelected() && generationCats.isSelected()) {
                    return "Генерация объектов уже возобновлена";
                }
                generationCats.setSelected(true);
                generationDogs.setSelected(true);
                return "Генерация объектов успешно возобновлена";
            default:
                return "Неверная команда";
        }
    }
}