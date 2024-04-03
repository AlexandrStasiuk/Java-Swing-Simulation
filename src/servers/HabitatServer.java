package servers;

import laba.files.Pets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static constants.Parameters.port;
//Сервер
public class HabitatServer {
    //Мап для потоков вывода
    private static Map<Socket, ObjectOutputStream> clientsOutputs = new HashMap<>();
    //Мап для потоков ввода
    private static Map<Socket, ObjectInputStream> clientsInputs = new HashMap<>();
    //Мап для хранения запроса обмена
    private static Map<Socket, ArrayList<Pets>> changerDogsMap = new HashMap<>();
    public static void main(String[] args) {
        //Подключение сервера
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Сервер успешно подключен");
            //Бесконечный цикл для постоянной обработки клиентов
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент получен - " + clientSocket);
                //Добавление потоков в мапы
                clientsOutputs.put(clientSocket, new ObjectOutputStream(clientSocket.getOutputStream()));
                clientsInputs.put(clientSocket, new ObjectInputStream(clientSocket.getInputStream()));
                //Обработка каждого клиента в отдельном потоке
                new ServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Поток для обработки клиентов
    private static class ServerThread extends Thread{
        //Сокет текущего клиента
        private Socket clientSocket;
        //Сокета клиента с которым происходит обмен
        private Socket targetChangerSocket;

        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        //Обновление списка клиентов у всех пользователей
        private static void updateLists(){
            LinkedList<String> ports = new LinkedList<>();
            clientsOutputs.forEach((client, value) -> {
                ports.add(String.valueOf(client.getPort()));
            });
            clientsOutputs.forEach((client, value) -> {
                try {
                    value.writeObject(ports);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void run() {
            try {
                updateLists();
                //Бесконечный цикл для постоянной обработки данных
                while (true) {
                    try {
                        //Считывание передаваемых данных
                        Object data = clientsInputs.get(clientSocket).readObject();
                        //Обработка списка котов
                        if(data instanceof String && !data.equals("request")){
                            ArrayList<Pets> cats;
                            //Получение пользователя для обмена
                            String changerPort = (String) data;
                            if((cats = (ArrayList<Pets>)clientsInputs.get(clientSocket).readObject()) != null) {
                                System.out.println("Коты для обмена с клиентом - " + changerPort + " " + cats);
                                for (Map.Entry<Socket, ObjectOutputStream> entry : clientsOutputs.entrySet()) {
                                    if (changerPort.equals(entry.getKey().getPort() + "")) {
                                        targetChangerSocket = entry.getKey();
                                        break;
                                    }
                                }
                                //Отправка котов пользователю
                                clientsOutputs.get(targetChangerSocket).writeObject("change");
                                clientsOutputs.get(targetChangerSocket).writeObject(cats);
                                //Ожидание поступление ответного списка с собаками
                                while (true){
                                    if(changerDogsMap.containsKey(targetChangerSocket)){
                                        clientsOutputs.get(clientSocket).writeObject("requestDogs");
                                        clientsOutputs.get(clientSocket).writeObject(changerDogsMap.get(targetChangerSocket));
                                        changerDogsMap.remove(targetChangerSocket);
                                        targetChangerSocket = null;
                                        System.out.println("Собаки успешно отправлены клиенту - " + clientSocket.getPort());
                                        break;
                                    }
                                    Thread.sleep(100);
                                }
                            }
                        }
                        //Обработка ответа на запрос обмена
                        if(data instanceof String && data.equals("request")){
                            ArrayList<Pets> dogs;
                            if((dogs = (ArrayList<Pets>)clientsInputs.get(clientSocket).readObject()) != null) {
                                System.out.println("Собаки для обмена " + dogs);
                                changerDogsMap.put(clientSocket, dogs);
                            }
                        }
                        Thread.sleep(100);
                    }catch (EOFException exception){
                        return;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }catch (SocketException e){
                //Обработка отключения клиента от сервера
                clientsOutputs.remove(clientSocket);
                clientsInputs.remove(clientSocket);
                System.out.println("Клиент завершил работу - " + clientSocket.getPort());
                updateLists();
            }catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
