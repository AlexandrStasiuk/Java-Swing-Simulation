package servers;

import laba.files.Pets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static constants.Parameters.port;

public class HabitatServer {
    private static Map<Socket, ObjectOutputStream> clientsOutputs = new HashMap<>();
    private static Map<Socket, ObjectInputStream> clientsInputs = new HashMap<>();
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Сервер успешно подключен");
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент получен - " + clientSocket);
                clientsOutputs.put(clientSocket, new ObjectOutputStream(clientSocket.getOutputStream()));
                clientsInputs.put(clientSocket, new ObjectInputStream(clientSocket.getInputStream()));
                new ServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static class ReadWriteData{
        public static void updateLists(){
            ArrayList<String> ports = new ArrayList<>();
            clientsOutputs.forEach((client, value) -> {
                ports.add(String.valueOf(client.getPort()));
            });
            clientsOutputs.forEach((client, value) -> {
                try {
                    value.writeObject(ports);
                    value.flush();
                    value.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        public static void infiniteLoop(Socket clientSocket) throws IOException, ClassNotFoundException {
            while (true) {
                try {
                    ObjectOutputStream out = clientsOutputs.get(clientSocket);
                    ObjectInputStream in = clientsInputs.get(clientSocket);
                    Object changeData = null;
                    if((changeData = in.readObject()) == null)
                        return;
                    ArrayList<Pets> cats;
                    if(changeData instanceof String){
                        if((cats = (ArrayList<Pets>)clientsInputs.get(clientSocket).readObject()) != null) {
                            ObjectOutputStream changeOut = null;
                            ObjectInputStream changeIn = null;
                            System.out.println(changeData + " " + cats);
                            Socket changerSocket = null;
                            for (Map.Entry<Socket, ObjectOutputStream> entry : clientsOutputs.entrySet()) {
                                if (changeData.equals(entry.getKey().getPort() + "")) {
                                    changerSocket = entry.getKey();
                                    changeOut = clientsOutputs.get(changerSocket);
                                    changeIn = clientsInputs.get(changerSocket);
                                    break;
                                }
                            }
                            //System.out.println(clientSocket + " " + clientInput);
                            //System.out.println(changerSocket + " " + changerInput);
                            changeOut.writeObject(cats);
                            changeOut.flush();
                            //changeOut.reset();
                            //out.reset();
                            // Добавляем обработку данных от второго клиента
                            out.writeObject(changeIn.readObject());
                            out.flush();
                            out.reset();
                            break;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }catch (EOFException exception){
                    return;
                }

            }
        }
    }
    private static class ServerThread extends Thread{
        private Socket clientSocket;
        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                ReadWriteData.updateLists();
                ReadWriteData.infiniteLoop(clientSocket);
            }catch (SocketException e){
                clientsOutputs.remove(clientSocket);
                clientsInputs.remove(clientSocket);
                System.out.println("Клиент завершил работу - " + clientSocket.getPort());
                ReadWriteData.updateLists();
                return;
            }catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
