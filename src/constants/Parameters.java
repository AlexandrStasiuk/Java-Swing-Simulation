package constants;

import laba.files.Pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TreeSet;

public class Parameters {
    //Проценты вероятности рождения
    public static final Integer[] percents = {0, 10, 20, 30, 40, 50, 60, 70, 80 ,90, 100};
    //Список животных
    public static ArrayList<Pets> petsList = new ArrayList<>();
    public static TreeSet<Integer> petsIdsSet = new TreeSet<>();
    public static HashMap<Integer, Integer> petsTimeBirthMap = new HashMap<>();
    //Время работы
    public static int timeElapsed = 0;
    //Параметры согласно варианту
    public static int N1 = 1;
    public static float P1 = 1f;
    public static int L1 = 10;
    public static int N2 = 1;
    public static float P2 = 1f;
    public static int L2 = 1;
    //Размеры экрана
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    //Таймер обновления
    public static Timer updateTimer;
    //Запущена ли симуляция
    public static boolean flStart = false;
    //Видимость времени
    public static boolean flVisibleTime = true;
    //Видимость диалогового окна
    public static boolean flShowInfo = false;
}
