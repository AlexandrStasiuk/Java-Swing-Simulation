package laba.files;

import components.Menu;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static constants.Components.panelImages;
import static constants.Parameters.*;

public class ClientThread extends Thread{
    Socket socket;
    ClientThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true){
            try{
                Object tempClients = inputStream.readObject();
                if(tempClients instanceof ArrayList){
                    if(!((ArrayList<?>) tempClients).isEmpty() && ((ArrayList<?>) tempClients).get(0) instanceof String){
                        if(tempClients != clientsList){
                            clientsList = (ArrayList<String>)tempClients;
                            Menu.updateClientsMenu();
                        }
                    }else if(!((ArrayList<?>) tempClients).isEmpty() && ((ArrayList<?>) tempClients).get(0) instanceof Cats){
                        System.out.println("Коты");
                        System.out.println(tempClients);
                        outputStream.reset();
                        outputStream.writeObject(Habitat.getAllDogs());
                        outputStream.flush();
                        outputStream.reset();
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
                        ArrayList<Pets> cats = null;
                        if((cats = (ArrayList<Pets>) inputStream.readObject()) != null){
                            for(Pets cat: cats){
                                //Добавление Котов
                                petsChanged.add(cat);
                                petsList.add(cat);
                                petsIdsSet.add(cat.getId());
                                petsTimeBirthMap.put(cat.getId(), cat.getTimeBirth());
                                if(flStart){
                                    panelImages.add(cat.getImageComponent());
                                    panelImages.repaint();
                                }
                            }
                            System.out.println(petsList);
                        }
                    }else if(((ArrayList<?>) tempClients).isEmpty()){
                        outputStream.reset();
                        System.out.println(tempClients);
                        outputStream.writeObject(tempClients);
                        outputStream.flush();
                        outputStream.reset();
                        ArrayList<Pets> dogs = (ArrayList<Pets>) inputStream.readObject();
                        System.out.println(dogs);
                    }else if(!((ArrayList<?>) tempClients).isEmpty() && ((ArrayList<?>) tempClients).get(0) instanceof Dogs){
                        System.out.println("Собаки");
                        System.out.println(tempClients);
                        ArrayList<Pets> dogs = null;
                        if((dogs = (ArrayList<Pets>) inputStream.readObject()) != null){
                            for(Pets dog: dogs){
                                //Добавление собак
                                petsChanged.add(dog);
                                petsList.add(dog);
                                petsIdsSet.add(dog.getId());
                                petsTimeBirthMap.put(dog.getId(), dog.getTimeBirth());
                                if(flStart){
                                    panelImages.add(dog.getImageComponent());
                                    panelImages.repaint();
                                }
                            }
                            System.out.println(petsList);
                        }
                    }
                }

            } catch (EOFException ex) {
                // Достигнут конец потока
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
