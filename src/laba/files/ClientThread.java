package laba.files;

import components.Menu;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import static constants.Components.panelImages;
import static constants.Parameters.*;
//Поток обработки клиента
public class ClientThread extends Thread{
    @Override
    public void run() {
        try(Socket socket = new Socket(InetAddress.getLocalHost(), port);){
            //Инициализация потоков
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Соединение с сервером установлено у клиента - " + socket);
            clientPort = socket.getLocalPort();
            //Чтение списка клиентов
            clientsList = (LinkedList<String>) inputStream.readObject();
            System.out.println("Клиенты - " + clientsList);
            //Бесконечный цикл для постоянной обработки поступаемых данных
            while (true){
                //Чтение данных
                Object tempData = inputStream.readObject();
                //Обработка списка клиентов
                if(tempData instanceof LinkedList) {
                    if (tempData != clientsList) {
                        clientsList = (LinkedList<String>) tempData;
                        System.out.println("Список клиентов обновлен - " + clientsList);
                        Menu.updateClientsMenu();
                    }
                }
                //Обработка запроса на обмен
                if(tempData instanceof String && ((String)tempData).equals("change")){
                    //Получение котов
                    ArrayList<Pets> cats = (ArrayList<Pets>)inputStream.readObject();
                    System.out.println("Коты получены - " + cats);
                    petsChangedList.addAll(cats);
                    //Отправка собак
                    outputStream.writeObject("request");
                    outputStream.writeObject(Habitat.getAllDogs());
                    //Удаление собак
                    petsList.removeIf(pet -> {
                        if(pet.getType().equals("dog")){
                            petsTimeBirthMap.remove(pet.getId());
                            petsIdsSet.remove(pet.getId());
                            panelImages.remove(pet.getImageComponent());
                            panelImages.repaint();
                            return true;
                        }
                        return false;
                    });
                }
                //Обработка запроса ответа на обмен
                if(tempData instanceof String && ((String)tempData).equals("requestDogs")){
                    //Получение собак
                    ArrayList<Pets> dogs = (ArrayList<Pets>)inputStream.readObject();
                    System.out.println("Собаки получены - " + dogs);
                    petsChangedList.addAll(dogs);
                    //Удаление котов
                    petsList.removeIf(pet -> {
                        if(pet.getType().equals("cat")){
                            petsTimeBirthMap.remove(pet.getId());
                            petsIdsSet.remove(pet.getId());
                            panelImages.remove(pet.getImageComponent());
                            panelImages.repaint();
                            return true;
                        }
                        return false;
                    });
                }
                Thread.sleep(100);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(
                    panelImages,
                    e.getMessage(),
                    "Ошибка подключения к серверу",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
