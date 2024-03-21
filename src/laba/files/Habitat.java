package laba.files;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

import components.*;
import components.Menu;
import static constants.Components.*;
import static constants.Parameters.*;

public class Habitat {
    public static void main(String[] args){
        //Добавление сохранения кофиг файла при завершении работы программы
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                createConfigFile();
                System.out.println("Конфигурационный файл сохранен");
            }
        });
        //clientServerConnection();
        getConfigFile();
        Menu.addMenu();
        Panels.addPanels();
        UserInterface.addUserInterface();
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
        //Если есть подгруженные объекты из файла, то отрисовать их
        if(flPreloadPets){
            petsList.forEach(pets -> {
                panelImages.add(pets.getImageComponent());
            });
            panelImages.repaint();
            flPreloadPets = false;
        }
        //System.out.println("Смена" + petsChanged);
        //if(!petsChanged.isEmpty()){
        //    System.out.println("Замена");
        //    petsChanged.forEach(pet -> {
        //        panelImages.add(pet.getImageComponent());
        //    });
        //    panelImages.repaint();
        //    petsChanged.clear();
        //}
        //Удаление объекта
        petsTimeBirthMap.entrySet().removeIf(petMap -> {
            Integer key = petMap.getKey();
            Integer value = petMap.getValue();
            //Проверка на истекшее время жизни
            if(value <= timeElapsed - L1){
                //Перебор объектов
                for(Pets pet: petsList){
                    //Если объект соответсвует параметрам, то удалить его везде
                    if(pet.getType().equals("cat") && pet.getTimeBirth() == value && pet.getId() == key){
                        panelImages.remove(pet.getImageComponent());
                        petsIdsSet.remove(key);
                        petsList.remove(pet);
                        return true;
                    }
                }
            }
            //Проверка на истекшее время жизни
            if(value <= timeElapsed - L2){
                //Перебор объектов
                for(Pets pet: petsList){
                    //Если объект соответсвует параметрам, то удалить его везде
                    if(pet.getType().equals("dog") && pet.getTimeBirth() == value && pet.getId() == key){
                        panelImages.remove(pet.getImageComponent());
                        petsIdsSet.remove(key);
                        petsList.remove(pet);
                        return true;
                    }
                }
            }
            return false;
        });
        panelImages.repaint();
        //Добавление кота
        if (N1 != 0 && timeElapsed % N1 == 0 && random.nextDouble() <= P1) {
            //Поток генерации
            Thread generationThread = new Thread(new GenerationThread("cat"));
            //Запуск потока
            generationThread.start();
            //Ожидание выполнения потока
            try {
                generationThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //Добавление собаки
        if (N2 != 0 && timeElapsed % N2 == 0 && random.nextDouble() <= P2) {
            //Поток генерации
            Thread generationThread = new Thread(new GenerationThread("dog"));
            //Запуск потока
            generationThread.start();
            //Ожидание выполнения потока
            try {
                generationThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //Проверка на инициализированный thread pool
        if(petsMoveThreads == null || petsMoveThreads.isShutdown())
            petsMoveThreads = Executors.newCachedThreadPool();
        //Проверка на пустой список
        if(!petsList.isEmpty())
            //Движение каждого из объектов в отдельном потоке
            petsList.forEach(pets -> {
                if(pets.getType().equals("cat")){
                    CatsAI catsAI = new CatsAI(pets, V1);
                    catsAI.changeCatsAI();
                    petsMoveThreads.execute(catsAI);
                }else{
                    DogsAI dogsAI = new DogsAI(pets, V2);
                    dogsAI.changeDogsAI();
                    petsMoveThreads.execute(dogsAI);
                }

            });
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
        petsList.clear();
        petsIdsSet.clear();
        petsTimeBirthMap.clear();
        timeElapsed = 0;
        Pets.countCats = 0;
        Pets.countDogs = 0;
        panelImages.removeAll();
        panelImages.repaint();
    }
    //Установка пользовательских значений
    public static void setUserData() throws NumberFormatException{
        if(timerCats.getText().length() > 9 || timerDogs.getText().length() > 9 )
            throw new NumberFormatException("Слишком большое время рождения объектов");
        if(timeLifeCats.getText().length() > 9  || timeLifeDogs.getText().length() > 9 )
            throw new NumberFormatException("Слишком большое время жизни объектов");
        if(speedCats.getText().length() > 9  || speedDogs.getText().length() > 9)
            throw new NumberFormatException("Скорость объектов не может быть больше 400");
        int tempN1 = Integer.parseInt(timerCats.getText());
        int tempN2 = Integer.parseInt(timerDogs.getText());
        int tempL1 = Integer.parseInt(timeLifeCats.getText());
        int tempL2 = Integer.parseInt(timeLifeDogs.getText());
        int tempV1 = Integer.parseInt(speedCats.getText());
        int tempV2 = Integer.parseInt(speedDogs.getText());
        if(tempN1 < 0 || tempN2 < 0)
            throw new NumberFormatException("Время рождения объектов не должно быть отрицательным");
        if(tempL1 <= 0 || tempL2 <= 0)
            throw new NumberFormatException("Время жизни объектов должно быть больше 0");
        if(tempV1 <= 0 || tempV2 <= 0)
            throw new NumberFormatException("Скорость объектов должна быть больше 0");
        else if(tempV1 > 400 || tempV2 > 400 )
            throw new NumberFormatException("Скорость объектов не может быть больше 400");
        N1 = tempN1;
        N2 = tempN2;
        L1 = tempL1;
        L2 = tempL2;
        V1 = tempV1;
        V2 = tempV2;
        P1 = (float)percents[comboBoxCats.getSelectedIndex()]/100;
        P2 = (float)percents[comboBoxDogs.getSelectedIndex()]/100;
    }
    //Создание и сохранение файла конфигурации
    public static void createConfigFile(){
        try(BufferedWriter configFile = new BufferedWriter(new FileWriter("config.txt"))){
            HashMap<String, Integer> parameters = new HashMap<>();
            parameters.put("Время рождения котов", N1);
            parameters.put("Время рождения собак", N2);
            parameters.put("Вероятность рождения котов", (int)(P1*100));
            parameters.put("Вероятность рождения собак", (int)(P2*100));
            parameters.put("Время жизни котов", L1);
            parameters.put("Время жизни собак", L2);
            parameters.put("Скорость передвижения котов", V1);
            parameters.put("Скорость передвижения собак", V2);
            parameters.forEach((k, v) -> {
                try {
                    configFile.write(k + "=" + v + "\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            HashMap<String, Boolean> settings = new HashMap<>();
            settings.put("Показывать информацию", flShowInfo);
            settings.put("Показывать время", flVisibleTime);
            settings.put("Передвижение котов", BaseAI.moveCats);
            settings.put("Передвижение собак", BaseAI.moveDogs);
            settings.put("Генерация котов", generationCats.isSelected());
            settings.put("Генерация собак", generationDogs.isSelected());
            settings.forEach((k, v) -> {
                try {
                    configFile.write(k + "=" + v + "\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    //Получение данных из файла конфигурации
    private static void getConfigFile(){
        try(BufferedReader configFile = new BufferedReader(new FileReader("config.txt"))){
            String line = "";
            while((line = configFile.readLine()) != null){
                String[] keyAndValue = line.split("=");
                switch (keyAndValue[0]){
                    case "Время рождения котов":
                        N1 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Время рождения собак":
                        N2 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Вероятность рождения котов":
                        P1 = (float)Integer.parseInt(keyAndValue[1])/100;
                        break;
                    case "Вероятность рождения собак":
                        P2 = (float)Integer.parseInt(keyAndValue[1])/100;
                        break;
                    case "Время жизни котов":
                        L1 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Время жизни собак":
                        L2 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Скорость передвижения котов":
                        V1 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Скорость передвижения собак":
                        V2 = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Показывать информацию":
                        flShowInfo = Boolean.parseBoolean(keyAndValue[1]);
                        break;
                    case "Показывать время":
                        flVisibleTime = Boolean.parseBoolean(keyAndValue[1]);
                        break;
                    case "Передвижение котов":
                        BaseAI.moveCats = Boolean.parseBoolean(keyAndValue[1]);
                        break;
                    case "Передвижение собак":
                        BaseAI.moveDogs = Boolean.parseBoolean(keyAndValue[1]);
                        break;
                    case "Генерация котов":
                        generationCats.setSelected(Boolean.parseBoolean(keyAndValue[1]));
                        break;
                    case "Генерация собак":
                        generationDogs.setSelected(Boolean.parseBoolean(keyAndValue[1]));
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            createConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void clientServerConnection(){
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение с сервером установлено у клиента - " + socket);
            clientPort = socket.getLocalPort();
            clientsList = (ArrayList<String>) inputStream.readObject();
            System.out.println("Клиенты - " + clientsList);
            new ClientThread(socket).start();
        }catch (ConnectException e){
            e.printStackTrace(System.out);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static ArrayList<Pets> getAllCats(){
        ArrayList cats = new ArrayList();
        petsList.forEach(pet -> {
            if(pet.getType().equals("cat"))
                cats.add(pet);
        });
        return cats;
    }
    public static ArrayList<Pets> getAllDogs(){
        ArrayList dogs = new ArrayList();
        petsList.forEach(pet -> {
            if(pet.getType().equals("dog"))
                dogs.add(pet);
        });
        return dogs;
    }
}