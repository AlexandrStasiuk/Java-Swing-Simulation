package laba.files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;
import components.*;

public class Habitat {
    //Список животных
    public static ArrayList<Pets> petsArray = new ArrayList<>();
    //Время работы
    public static int timeElapsed = 0;
    //Параметры согласно варианту
    private static int N1 = 0;
    private static float P1 = 0;
    private static int N2 = 0;
    private static float P2 = 0;
    //Размеры экрана
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    //Таймер обновления
    public static Timer updateTimer;
    //Компоненты окна
    public static JFrame jFrame = getFrame();
    public static JPanel panelImages = new JPanel();
    public static JPanel panelButtons = new JPanel();
    public static JMenuItem startButton = new JMenuItem("Старт");
    public static JMenuItem stopButton = new JMenuItem("Стоп");
    public static JCheckBox showInfo = new JCheckBox("Показывать информацию");
    private static final Integer[] percents = {0, 10, 20, 30, 40, 50, 60, 70, 80 ,90, 100};
    public static JComboBox<Integer> comboBoxCats = new JComboBox<>(percents);
    public static JComboBox<Integer> comboBoxDogs = new JComboBox<>(percents);
    public static JLabel labelComboCats = new JLabel("Вероятность рождения кота(%)");
    public static JLabel labelComboDogs = new JLabel("Вероятность рождения собаки(%)");
    public static TextField timerCats = new TextField();
    public static TextField timerDogs = new TextField();
    public static JLabel labelTimerCats = new JLabel("Период рождения котов(сек.)");
    public static JLabel labelTimerDogs = new JLabel("Период рождения собак(сек.)");
    public static JRadioButtonMenuItem showTime = new JRadioButtonMenuItem("Показать время работы");
    public static JRadioButtonMenuItem hiddenTime = new JRadioButtonMenuItem("Скрыть время работы");
    public static TextTime textTime = new TextTime();
    public static JMenuBar jMenuBar = new JMenuBar();
    public static JMenu jMenu = new JMenu("Меню");
    //Запущена ли симуляция
    public static boolean flStart = false;
    //Видимость времени
    private static boolean flVisibleTime = true;
    //Видимость диалогового окна
    private static boolean flShowInfo = true;

    public static void main(String[] args) {
        addMenu();
        addPanels();
        addUserFields();
        jFrame.setVisible(true);
    }
    //Обновление таймера
    public static class UpdateTask extends TimerTask{
        @Override
        public void run() {
            update();
        }
    }
    //Функция обновления таймера
    private static void update(){
        Random random = new Random();
        //Изменение времени
        timeElapsed += 1;
        textTime.repaint();
        //Добавление кота
        if (N1 != 0 && timeElapsed % N1 == 0 && random.nextDouble() <= P1) {
            Cats cat = new Cats();
            petsArray.add(cat);
            cat.setImage();
        }
        //Добавление собаки
        if (N2 != 0 && timeElapsed % N2 == 0 && random.nextDouble() <= P2) {
            Dogs dog = new Dogs();
            petsArray.add(dog);
            dog.setImage();
        }
    }
    //Создание окна симуляции
    private static JFrame getFrame(){
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
    //Добавление панелей
    private static void addPanels(){
        //Панель для картинок
        panelImages.setLayout(null);
        panelImages.setBounds(0, 0, WIDTH*75/100, HEIGHT);
        panelImages.setBackground(new Color(188, 255, 241));
        jFrame.add(panelImages);

        //Панель кнопок
        panelButtons.setLayout(null);
        panelButtons.setBounds(WIDTH*75/100, 0, WIDTH*25/100, HEIGHT);
        panelButtons.setBackground(new Color(208, 208, 208));
        jFrame.add(panelButtons);
    }
    //Событие обновления при клике
    private static class UpdateAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            //Проверка на запущенную симуляцию
            if(!flStart){
                try{
                    //Установка пользовательских значений
                    setUserData();
                    textTime.setVisible(flVisibleTime);
                    flStart = true;
                    setEnabledFields(false);
                    //Создание таймера и добавление периода
                    updateTimer = new Timer();
                    updateTimer.schedule(new UpdateTask(),0,  1000);
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                }catch (NumberFormatException exception){
                    //Обработка неправильных значений
                    timerCats.setText(String.valueOf(N1));
                    timerDogs.setText(String.valueOf(N2));
                    //Окно с ошибкой
                    showErrorDialog();
                }
            }
        }
    }
    //Остановка симуляции
    private static class StopAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            flShowInfo = showInfo.isSelected();
            //Проверка на включенное диалоговое окно
            if(flShowInfo && flStart){
                addDialogInfo();
            }else if(flStart){
                //Проверка на запущенную симуляцию
                textTime.setVisible(false);
                flStart = false;
                setEnabledFields(true);
                //Очистка старой симуляции
                Habitat.petsArray.clear();
                Habitat.timeElapsed = 0;
                Pets.countCats = 0;
                Pets.countDogs = 0;
                panelImages.removeAll();
                panelImages.repaint();
                //Очистка таймера
                if(updateTimer != null)
                    updateTimer.cancel();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        }
    }
    //Событие скрытия таймера
    private static class HiddenTimeAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            flVisibleTime = !flVisibleTime;
            if (flVisibleTime) {
                showTime.setSelected(true);
            } else {
                hiddenTime.setSelected(true);
            }
            if(flStart){
                textTime.setVisible(flVisibleTime);
            }
        }
    }
    //Добавление кнопок
    private static void addButtons(){
        AbstractAction updateAction = new UpdateAction();
        AbstractAction stopAction = new StopAction();

        //Кнопка старта симуляции
        startButton.addActionListener(updateAction);
        startButton.setEnabled(true);
        startButton.setAccelerator(KeyStroke.getKeyStroke("B"));
        jMenu.add(startButton);

        //Кнопка остановки симуляции
        stopButton.addActionListener(stopAction);
        stopButton.setEnabled(false);
        stopButton.setAccelerator(KeyStroke.getKeyStroke("E"));
        jMenu.add(stopButton);

    }
    //Добавление текста с таймером
    private static void addTextTime(){
        textTime.setBounds(20, 5, WIDTH*25/100 - 50, 50);
        panelButtons.add(textTime);
        textTime.setVisible(false);

        HiddenTimeAction hiddenTimeAction = new HiddenTimeAction();

        //Добавление события скрытия таймера
        KeyStroke hiddenKey = KeyStroke.getKeyStroke("T");
        InputMap inputMap = panelButtons.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(hiddenKey, "hiddenTime");
        ActionMap actionMap = panelButtons.getActionMap();
        actionMap.put("hiddenTime", hiddenTimeAction);
    }
    //Диалоговое окно с информацией
    private static void addDialogInfo(){
        //Отключение симуляции во время работы окна
        if(updateTimer != null)
            updateTimer.cancel();
        DialogInfo dialogInfo = new DialogInfo();
        dialogInfo.setVisible(true);
    }
    //Кнопки для скрытия таймера
    private static void addTimerButtons(){
        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTime);
        buttonGroup.add(hiddenTime);

        showTime.setSelected(true);
        showTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = true;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(showTime);

        hiddenTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = false;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(hiddenTime);
    }
    //Текстовые поля для заполнения
    private static void addFields(){
        Font font = new Font("Times New Roman", Font.BOLD, 14);

        //Лейбел для комбобокса у котов
        labelComboCats.setBounds(20, 30, 250, 30);
        labelComboCats.setFont(font);
        labelComboCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboCats);

        //Комбобокс у котов
        comboBoxCats.setBounds(20, 60, 200, 30);
        comboBoxCats.setFont(font);
        panelButtons.add(comboBoxCats);

        //Лейбел для комбобокса у собак
        labelComboDogs.setBounds(20, 100, 250, 30);
        labelComboDogs.setFont(font);
        labelComboDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboDogs);

        //Комбобокс у собак
        comboBoxDogs.setBounds(20, 130, 200, 30);
        comboBoxDogs.setFont(font);
        panelButtons.add(comboBoxDogs);

        //Лейбел для текстового поля у котов
        labelTimerCats.setBounds(20, 180, 250, 30);
        labelTimerCats.setFont(font);
        labelTimerCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerCats);

        //Текстовое поле у котов
        timerCats.setBounds(20, 210, 200, 30);
        timerCats.setFont(font);
        panelButtons.add(timerCats);

        //Лейбел для текстового поля у собак
        labelTimerDogs.setBounds(20, 250, 250, 30);
        labelTimerDogs.setFont(font);
        labelTimerDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerDogs);

        //Текстовое поле у собак
        timerDogs.setBounds(20, 280, 200, 30);
        timerDogs.setFont(font);
        panelButtons.add(timerDogs);
    }
    //Пользовательское меню
    private static void addUserFields(){
        //Текст таймера
        addTextTime();
        //Поля с параметрами
        addFields();
    }
    //Установка пользовательских значений
    private static void setUserData() throws NumberFormatException{
        int tempN1 = Integer.parseInt(timerCats.getText());
        int tempN2 = Integer.parseInt(timerDogs.getText());
        if(tempN1 < 0 || tempN2 < 0){
            throw new NumberFormatException();
        }
        N1 = tempN1;
        N2 = tempN2;
        P1 = (float)percents[comboBoxCats.getSelectedIndex()]/100;
        P2 = (float)percents[comboBoxDogs.getSelectedIndex()]/100;
    }
    //Вывод диалогового окна с ошибкой
    private static void showErrorDialog(){
        JOptionPane.showMessageDialog(
                panelImages,
                "Вводимые данные должны содержать только целые положительные числа",
                "Ошибка вводимых данных",
                JOptionPane.ERROR_MESSAGE
        );
    }
    //Отключение пользовательских полей во время работы симуляции
    public static void setEnabledFields(boolean enabled){
        comboBoxCats.setEnabled(enabled);
        comboBoxDogs.setEnabled(enabled);
        labelComboCats.setEnabled(enabled);
        labelComboDogs.setEnabled(enabled);
        timerCats.setEnabled(enabled);
        timerDogs.setEnabled(enabled);
        labelTimerCats.setEnabled(enabled);
        labelTimerDogs.setEnabled(enabled);
    }
    private static void addMenu(){
        jFrame.setJMenuBar(jMenuBar);
        jMenuBar.add(jMenu);
        //Поле с кнопками
        addButtons();
        jMenu.addSeparator();
        //Поле с чекбоксом
        addTimerButtons();
        jMenu.addSeparator();
        //Показ информации
        jMenu.add(showInfo);
    }
}