import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.Timer;

public class Habitat {
    //Список животных
    private static ArrayList<Pets> pets = new ArrayList<>();
    //Время работы
    private static int timeElapsed = 0;
    //Параметры согласно варианту
    private final static int N1 = 5;
    private final static float P1 = 0.9f;
    private final static int N2 = 10;
    private final static float P2 = 0.5f;
    //Размеры экрана
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    //Таймер обновления
    private static Timer updateTimer;
    //Компоненты окна
    public static JFrame jFrame = getFrame();
    public static JPanel panelImages = new JPanel();
    public static JPanel panelButtons = new JPanel();
    public static JButton startButton = new JButton("Старт");
    public static JButton stopButton = new JButton("Стоп");
    public static TextTime textTime = new TextTime();
    public static TextStatistic textStatistic = new TextStatistic();
    //Запущена ли симуляция
    private static boolean flStart = false;
    //Видимость времени
    private static boolean flVisibleTime = true;

    public static void main(String[] args) {
        addPanels();
        addButtons();
    }
    //Обновление таймера
    private static class UpdateTask extends TimerTask{
        @Override
        public void run() {
            update();
        }
    }
    //Функция обновления таймера
    static void update(){
        Random random = new Random();
        addTextTime();
        //Изменение времени
        timeElapsed += 1;
        textTime.repaint();
        //Добавление кота
        if (timeElapsed % N1 == 0 && random.nextDouble() <= P1) {
            Cats cat = new Cats();
            pets.add(cat);
            System.out.println(timeElapsed);
            cat.setImage();
            //Перерисовка картинок
            panelImages.repaint();
        }
        //Добавление собаки
        if (timeElapsed % N2 == 0 && random.nextDouble() <= P2) {
            Dogs dog = new Dogs();
            pets.add(dog);
            System.out.println(timeElapsed);
            dog.setImage();
            //Перерисовка картинок
            panelImages.repaint();
        }
    }
    //Создание окна симуляции
    static JFrame getFrame(){
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        //Определение рамеров окна
        WIDTH = dimension.width/2;
        HEIGHT = dimension.height/2;
        jFrame.setBounds(
                WIDTH/2,
                HEIGHT/2,
                WIDTH,
                HEIGHT
        );
        jFrame.setTitle("Лабораторная работа");
        jFrame.setLayout(null);
        return  jFrame;
    }
    //Добавление панелей
    static void addPanels(){
        //Панель для картинок
        panelImages.setLayout(null);
        panelImages.setBounds(0, 0, WIDTH*75/100, HEIGHT);
        panelImages.setBackground(new Color(188, 255, 241));
        jFrame.add(panelImages);

        //Панель кнопок
        panelButtons.setLayout(null);
        panelButtons.setBounds(WIDTH*75/100, 0, WIDTH*25/100, HEIGHT);
        panelButtons.setBackground(new Color(188, 255, 241));
        jFrame.add(panelButtons);
    }
    //Событие обновления при клике
    static class UpdateAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            //Проверка на запущенную симуляцию
            if(!flStart){
                //Очистка сессии
                pets.clear();
                timeElapsed = 0;
                Pets.countCats = 0;
                Pets.countDogs = 0;
                flStart = true;
                //Создание таймера и добавление периода
                updateTimer = new Timer();
                updateTimer.schedule(new UpdateTask(),0,  1000);
            }
        }
    }
    //Остановка симуляции
    static class StopAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            //Проверка на запущенную симуляцию
            if(flStart){
                flStart = false;
                //Очистка таймера
                if(updateTimer != null)
                    updateTimer.cancel();
                //Вывод статистики
                addTextStatistic();
            }
        }
    }
    //Событие скрытия таймера
    static class HiddenTimeAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            flVisibleTime = !flVisibleTime;
            textTime.setVisible(flVisibleTime);
        }
    }
    //Компонент текста с таймером
    static class TextTime extends JComponent{
        @Override
        protected void paintComponent(Graphics g) {
            Font font = new Font("Times New Roman", Font.BOLD, 14);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(font);
            g2.drawString("Время работы - " + timeElapsed + " сек.", 0, 20);
        }
    }
    //Компонент текста со статистикой
    static class TextStatistic extends JComponent{
        @Override
        protected void paintComponent(Graphics g) {
            Font font = new Font("Times New Roman", Font.BOLD, 14);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(font);
            g2.drawString("Время работы - " + timeElapsed + " сек.", 0, 20);
            g2.drawString("Кошек - " + Pets.countCats, 0, 40);
            g2.drawString("Собак - " + Pets.countDogs, 0, 60);
        }
    }
    //Добавление кнопок
    static void addButtons(){
        AbstractAction updateAction = new UpdateAction();
        AbstractAction stopAction = new StopAction();

        //Кнопка старта симуляции
        startButton.setBounds(20, HEIGHT/5-50, WIDTH*25/100 - 50, 50);
        startButton.addActionListener(updateAction);
        panelButtons.add(startButton);

        //Кнопка остановки симуляции
        stopButton.setBounds(20, HEIGHT/5 + 20, WIDTH*25/100 - 50, 50);
        stopButton.addActionListener(stopAction);
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

    }
    //Добавление текста с таймером
    static void addTextTime(){
        textTime.setBounds(20, HEIGHT/5 + 100, WIDTH*25/100 - 50, 50);
        panelButtons.add(textTime);
        textTime.setVisible(flVisibleTime);
        textStatistic.setVisible(false);

        HiddenTimeAction hiddenTimeAction = new HiddenTimeAction();

        //Добавление события скрытия таймера
        KeyStroke hiddenKey = KeyStroke.getKeyStroke("T");
        InputMap inputMap = panelButtons.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(hiddenKey, "hiddenTime");
        ActionMap actionMap = panelButtons.getActionMap();
        actionMap.put("hiddenTime", hiddenTimeAction);
    }
    //Добавление компонента текста со статистикой
    static void addTextStatistic(){
        textStatistic.setBounds(20, HEIGHT/5 + 100, WIDTH*25/100 - 50, 100);
        panelButtons.add(textStatistic);
        textTime.setVisible(false);
        textStatistic.setVisible(true);
    }
}