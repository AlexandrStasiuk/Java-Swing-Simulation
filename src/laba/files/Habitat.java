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
    private static int N1 = 2;
    private static float P1 = 0.5f;
    private static int N2 = 6;
    private static float P2 = 0.7f;
    //Размеры экрана
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    //Таймер обновления
    public static Timer updateTimer;
    private static final Font font = new Font("Times New Roman", Font.BOLD, 14);
    //Компоненты окна
    public static JFrame jFrame = getFrame();
    public static JPanel panelImages = new JPanel();
    public static JPanel panelButtons = new JPanel();
    public static JButton startButton = new JButton("Старт");
    public static JButton stopButton = new JButton("Стоп");
    public static JMenuItem startButtonMenu = new JMenuItem("Старт");
    public static JMenuItem stopButtonMenu = new JMenuItem("Стоп");
    public static JCheckBox showInfo = new JCheckBox("Показывать информацию");
    public static JCheckBox showInfoMenu = new JCheckBox("Показывать информацию");
    private static final Integer[] percents = {0, 10, 20, 30, 40, 50, 60, 70, 80 ,90, 100};
    public static JComboBox<Integer> comboBoxCats = new JComboBox<>(percents);
    public static JComboBox<Integer> comboBoxDogs = new JComboBox<>(percents);
    public static JLabel labelComboCats = new JLabel("Вероятность рождения кота(%)");
    public static JLabel labelComboDogs = new JLabel("Вероятность рождения собаки(%)");
    public static TextField timerCats = new TextField();
    public static TextField timerDogs = new TextField();
    public static JLabel labelTimerCats = new JLabel("Период рождения котов(сек.)");
    public static JLabel labelTimerDogs = new JLabel("Период рождения собак(сек.)");
    public static JRadioButton showTime = new JRadioButton("Показать время работы");
    public static JRadioButton hiddenTime = new JRadioButton("Скрыть время работы");
    public static JButton updateParameters = new JButton("Обновить");
    public static JRadioButtonMenuItem showTimeMenu = new JRadioButtonMenuItem("Показать время работы");
    public static JRadioButtonMenuItem hiddenTimeMenu = new JRadioButtonMenuItem("Скрыть время работы");
    public static TextTime textTime = new TextTime();
    public static JMenuBar jMenuBar = new JMenuBar();
    public static JMenu jMenu = new JMenu("Меню");
    //Запущена ли симуляция
    public static boolean flStart = false;
    //Видимость времени
    private static boolean flVisibleTime = true;
    //Видимость диалогового окна
    private static boolean flShowInfo = false;

    public static void main(String[] args) {
        addMenu();
        addPanels();
        addUserInterface();
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
                    setEnableStart(false);
                    //Создание таймера и добавление периода
                    updateTimer = new Timer();
                    updateTimer.schedule(new UpdateTask(),0,  1000);
                }catch (NumberFormatException exception){
                    //Обработка неправильных значений
                    timerCats.setText(String.valueOf(N1));
                    timerDogs.setText(String.valueOf(N2));
                    comboBoxCats.setSelectedItem((int)(P1*100));
                    comboBoxDogs.setSelectedItem((int)(P2*100));
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
            //Проверка на включенное диалоговое окно
            if(flShowInfo && flStart){
                addDialogInfo();
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
    //Событие скрытия таймера
    private static class HiddenTimeAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            flVisibleTime = !flVisibleTime;
            if (flVisibleTime) {
                showTimeMenu.setSelected(true);
                showTime.setSelected(true);
            } else {
                hiddenTimeMenu.setSelected(true);
                hiddenTime.setSelected(true);
            }
            if(flStart){
                textTime.setVisible(flVisibleTime);
            }
        }
    }
    //Событие показа информации
    private static class ShowInfoAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            flShowInfo = !flShowInfo;
            showInfo.setSelected(flShowInfo);
            showInfoMenu.setSelected(flShowInfo);
        }
    }
    //Отключение кнопок
    public static void setEnableStart(boolean fl){
        flStart = !fl;
        startButton.setEnabled(fl);
        stopButton.setEnabled(!fl);
        startButtonMenu.setEnabled(fl);
        stopButtonMenu.setEnabled(!fl);
    }
    //Очистка сессии
    public static void clearSession(){
        petsArray.clear();
        timeElapsed = 0;
        Pets.countCats = 0;
        Pets.countDogs = 0;
        panelImages.removeAll();
        panelImages.repaint();
    }
    //Добавление кнопок
    private static void addButtonsMenu(){
        AbstractAction updateAction = new UpdateAction();
        AbstractAction stopAction = new StopAction();

        //Кнопка старта симуляции
        startButtonMenu.addActionListener(updateAction);
        startButtonMenu.setEnabled(true);
        startButtonMenu.setAccelerator(KeyStroke.getKeyStroke("B"));
        jMenu.add(startButtonMenu);

        //Кнопка остановки симуляции
        stopButtonMenu.addActionListener(stopAction);
        stopButtonMenu.setEnabled(false);
        stopButtonMenu.setAccelerator(KeyStroke.getKeyStroke("E"));
        jMenu.add(stopButtonMenu);

    }
    //Добавление текста с таймером
    private static void addTextTime(){
        textTime.setBounds(20, 5, WIDTH*25/100 - 50, 25);
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
    private static void addTimerButtonsMenu(){
        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTimeMenu);
        buttonGroup.add(hiddenTimeMenu);

        showTimeMenu.setSelected(true);
        showTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = true;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(showTimeMenu);

        hiddenTimeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Изменение видимости таймера
                flVisibleTime = false;
                textTime.setVisible(flVisibleTime);
            }
        });
        jMenu.add(hiddenTimeMenu);
    }
    //Текстовые поля для заполнения
    private static void addFields(){
        //Лейбел для комбобокса у котов
        labelComboCats.setBounds(20, 200, 250, 30);
        labelComboCats.setFont(font);
        labelComboCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboCats);

        //Комбобокс у котов
        comboBoxCats.setBounds(20, 230, 200, 30);
        comboBoxCats.setFont(font);
        panelButtons.add(comboBoxCats);

        //Лейбел для комбобокса у собак
        labelComboDogs.setBounds(20, 260, 250, 30);
        labelComboDogs.setFont(font);
        labelComboDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelComboDogs);

        //Комбобокс у собак
        comboBoxDogs.setBounds(20, 290, 200, 30);
        comboBoxDogs.setFont(font);
        panelButtons.add(comboBoxDogs);

        //Лейбел для текстового поля у котов
        labelTimerCats.setBounds(20, 320, 250, 30);
        labelTimerCats.setFont(font);
        labelTimerCats.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerCats);

        //Текстовое поле у котов
        timerCats.setBounds(20, 350, 200, 30);
        timerCats.setFont(font);
        panelButtons.add(timerCats);

        //Лейбел для текстового поля у собак
        labelTimerDogs.setBounds(20, 380, 250, 30);
        labelTimerDogs.setFont(font);
        labelTimerDogs.setBackground(new Color(208, 208, 208));
        panelButtons.add(labelTimerDogs);

        //Текстовое поле у собак
        timerDogs.setBounds(20, 410, 200, 30);
        timerDogs.setFont(font);
        panelButtons.add(timerDogs);
    }
    //Пользовательское меню
    private static void addUserInterface(){
        AbstractAction updateAction = new UpdateAction();
        AbstractAction stopAction = new StopAction();

        //Текст таймера
        addTextTime();

        //Кнопка старта симуляции
        startButton.setBounds(20, 35, 150, 30);
        startButton.addActionListener(updateAction);
        startButton.setFont(font);
        startButton.setEnabled(true);
        panelButtons.add(startButton);
        //Кнопка остановки симуляции
        stopButton.setBounds(20, 70, 150, 30);
        stopButton.addActionListener(stopAction);
        stopButton.setEnabled(false);
        stopButton.setFont(font);
        panelButtons.add(stopButton);
        //Добавление событий при нажатии на клавиши
        KeyStroke updateKey = KeyStroke.getKeyStroke("B");
        KeyStroke stopKey = KeyStroke.getKeyStroke("E");
        InputMap inputMap = panelImages.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(updateKey, "updateStart");
        inputMap.put(stopKey, "updateStop");
        ActionMap actionMap = panelImages.getActionMap();
        actionMap.put("updateStart", updateAction);
        actionMap.put("updateStop", stopAction);

        //Группа кнопок
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTime);
        buttonGroup.add(hiddenTime);
        //Кнопки для таймера
        showTime.setBounds(20, 110, 250, 30);
        showTime.setFont(font);
        showTime.setBackground(new Color(208, 208, 208));
        showTime.setSelected(true);
        showTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTime.setVisible(flVisibleTime);
            }
        });
        panelButtons.add(showTime);
        hiddenTime.setBounds(20, 140, 250, 30);
        hiddenTime.setFont(font);
        hiddenTime.setBackground(new Color(208, 208, 208));
        hiddenTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTime.setVisible(flVisibleTime);
            }
        });
        panelButtons.add(hiddenTime);

        //Поле с чекбоксом
        showInfo.setBounds(20, 170, 250, 30);
        showInfo.setFont(font);
        showInfo.setBackground(new Color(208, 208, 208));
        showInfo.addActionListener(new ShowInfoAction());
        panelButtons.add(showInfo);

        //Поля с параметрами
        addFields();

        //Кнопка обновления параметров
        updateParameters.setBounds(20, 445, 150, 30);
        updateParameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //Установка пользовательских значений
                    setUserData();
                }catch (NumberFormatException exception){
                    //Обработка неправильных значений
                    timerCats.setText(String.valueOf(N1));
                    timerDogs.setText(String.valueOf(N2));
                    comboBoxCats.setSelectedItem((int)(P1*100));
                    comboBoxDogs.setSelectedItem((int)(P2*100));
                    //Окно с ошибкой
                    showErrorDialog();
                }
            }
        });
        updateParameters.setFont(font);
        panelButtons.add(updateParameters);
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
    //Добавление меню
    private static void addMenu(){
        jFrame.setJMenuBar(jMenuBar);
        jMenuBar.add(jMenu);
        //Поле с кнопками
        addButtonsMenu();
        jMenu.addSeparator();
        //Поле с чекбоксом
        addTimerButtonsMenu();
        jMenu.addSeparator();
        //Показ информации
        showInfoMenu.addActionListener(new ShowInfoAction());
        jMenu.add(showInfoMenu);
    }
}