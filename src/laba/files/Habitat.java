package laba.files;

import java.util.*;
import java.util.concurrent.Executors;

import components.*;
import components.Menu;
import static constants.Components.*;
import static constants.Parameters.*;

public class Habitat {
    public static void main(String[] args) {
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
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Cats cat = new Cats(timeElapsed, L1);
                    petsList.add(cat);
                    petsIdsSet.add(cat.getId());
                    petsTimeBirthMap.put(cat.getId(), cat.getTimeBirth());
                    cat.setImage();
                }
            });
            //Запуск потока
            thread.start();
            //Ожидание выполнения потока
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //Добавление собаки
        if (N2 != 0 && timeElapsed % N2 == 0 && random.nextDouble() <= P2) {
            //Поток генерации
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Dogs dog = new Dogs(timeElapsed, L2);
                    petsList.add(dog);
                    petsIdsSet.add(dog.getId());
                    petsTimeBirthMap.put(dog.getId(), dog.getTimeBirth());
                    dog.setImage();
                }
            });
            //Запуск потока
            thread.start();
            //Ожидание выполнения потока
            try {
                thread.join();
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
                    catsAI.changeCatsAI(moveCats.isSelected());
                    petsMoveThreads.execute(catsAI);
                }else{
                    DogsAI dogsAI = new DogsAI(pets, V2);
                    dogsAI.changeDogsAI(moveDogs.isSelected());
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
        int tempN1 = Integer.parseInt(timerCats.getText());
        int tempN2 = Integer.parseInt(timerDogs.getText());
        int tempL1 = Integer.parseInt(timeLifeCats.getText());
        int tempL2 = Integer.parseInt(timeLifeDogs.getText());
        int tempV1 = Integer.parseInt(speedCats.getText());
        int tempV2 = Integer.parseInt(speedDogs.getText());
        if(tempN1 < 0 || tempN2 < 0 || tempL1 <= 0 || tempL2 <= 0 || tempV1 <= 0 || tempV2 <= 0){
            throw new NumberFormatException();
        }
        N1 = tempN1;
        N2 = tempN2;
        L1 = tempL1;
        L2 = tempL2;
        V1 = tempV1;
        V2 = tempV2;
        P1 = (float)percents[comboBoxCats.getSelectedIndex()]/100;
        P2 = (float)percents[comboBoxDogs.getSelectedIndex()]/100;
    }
}