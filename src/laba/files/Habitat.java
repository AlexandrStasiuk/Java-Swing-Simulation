package laba.files;

import java.io.*;
import java.util.*;
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
        clientServerConnection();
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
        //Если есть подгруженные объекты из файла или БД, то отрисовать их
        if(flPreloadFilePets || flPreloadSQLPets){
            petsList.forEach(pets -> {
                panelImages.add(pets.getImageComponent());
            });
            panelImages.repaint();
            if (flPreloadFilePets)
                flPreloadFilePets = false;
            if (flPreloadSQLPets)
                flPreloadSQLPets = false;
        }
        //Проверка на наличие животных с обмена
        if(!petsChangedList.isEmpty())
            changePets();
        petsTimeBirthMap.entrySet().removeIf(petMap -> {
            Integer key = petMap.getKey();
            Integer value = petMap.getValue();
            //Проверка на истекшее время жизни
            if(value <= timeElapsed - Cats.timeLife){
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
            if(value <= timeElapsed - Dogs.timeLife){
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
        Cats.timeLife = tempL1;
        Dogs.timeLife = tempL2;
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
            parameters.put("Время жизни котов", Cats.timeLife);
            parameters.put("Время жизни собак", Dogs.timeLife);
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
                        Cats.timeLife = Integer.parseInt(keyAndValue[1]);
                        break;
                    case "Время жизни собак":
                        Dogs.timeLife = Integer.parseInt(keyAndValue[1]);
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
    //Подключение к серверу в отдельном потоке
    private static void clientServerConnection(){
        new ClientThread().start();
    }
    //Получение всех котов
    public static ArrayList<Pets> getAllCats(){
        ArrayList cats = new ArrayList();
        petsList.forEach(pet -> {
            if(pet.getType().equals("cat"))
                cats.add(pet);
        });
        return cats;
    }
    //Получение всех собак
    public static ArrayList<Pets> getAllDogs(){
        ArrayList dogs = new ArrayList();
        petsList.forEach(pet -> {
            if(pet.getType().equals("dog"))
                dogs.add(pet);
        });
        return dogs;
    }
    //Добовление животных после обмена
    private static void changePets(){
        petsChangedList.forEach(pet -> {
            petsList.add(pet);
            petsIdsSet.add(pet.getId());
            petsTimeBirthMap.put(pet.getId(), pet.getTimeBirth());
            panelImages.add(pet.getImageComponent());
        });
        petsChangedList.clear();
        Collections.sort(petsList);
        panelImages.repaint();
    }
}
