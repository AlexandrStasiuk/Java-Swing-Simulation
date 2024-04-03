package constants;

import laba.files.Pets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Parameters {
    //Проценты вероятности рождения
    public static final Integer[] percents = {0, 10, 20, 30, 40, 50, 60, 70, 80 ,90, 100};
    //Список животных
    public static ArrayList<Pets> petsList = new ArrayList<>();
    public static TreeSet<Integer> petsIdsSet = new TreeSet<>();
    public static HashMap<Integer, Integer> petsTimeBirthMap = new HashMap<>();
    //Thread pool для расчета интеллекта объектов
    public static ExecutorService petsMoveThreads = Executors.newCachedThreadPool();
    //Время работы
    public static int timeElapsed = 0;
    //Параметры согласно варианту
    public static int N1 = 1;
    public static float P1 = 1f;
    public static int V1 = 100;
    public static int N2 = 1;
    public static float P2 = 1f;
    public static int V2 = 100;
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
    //Есть ли подгруженные объекты из файла
    public static boolean flPreloadFilePets = false;
    //Есть ли подгруженные объекты из БД
    public static boolean flPreloadSQLPets = false;
    //Список обмененных животных
    public static ArrayList<Pets> petsChangedList= new ArrayList<>();
    //Порт для сервера
    public static int port = 8080;
    //Список клиентов
    public static List<String> clientsList = new LinkedList<>();
    //Порт текущего клиента
    public static int clientPort;
    //Потоки для работы с сервером
    public static ObjectInputStream inputStream;
    public static ObjectOutputStream outputStream;

}
