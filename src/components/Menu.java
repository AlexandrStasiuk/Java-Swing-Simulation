package components;

import actions.LoadPetsAction;
import actions.SavePetsAction;
import actions.ShowInfoAction;
import laba.files.BaseAI;
import laba.files.Habitat;
import laba.files.Pets;
import laba.files.PetsSQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static constants.Components.*;
import static constants.Parameters.*;

//Добавление меню
public class Menu {
    public static void addMenu(){
        jFrame.setJMenuBar(jMenuBar);
        jMenuBar.add(jMenu);
        //Поле с кнопками
        ButtonsMenu.addButtonsMenu();
        jMenu.addSeparator();
        //Поле с чекбоксом
        TimerButtonsMenu.addTimerButtonsMenu();
        jMenu.addSeparator();
        //Показ информации
        showInfoMenu.addActionListener(new ShowInfoAction());
        jMenu.add(showInfoMenu);
        showInfoMenu.setSelected(flShowInfo);
        jMenu.addSeparator();
        //Живые объекты
        jMenu.add(showLiveObjects);
        showLiveObjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogLiveObjects(petsList).setVisible(true);
            }
        });
        jMenu.addSeparator();
        //Движение объектов
        moveCats.setSelected(BaseAI.moveCats);
        moveCats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseAI.moveCats = moveCats.isSelected();
            }
        });
        moveDogs.setSelected(BaseAI.moveDogs);
        moveDogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseAI.moveDogs = moveDogs.isSelected();
            }
        });
        jMenu.add(moveCats);
        jMenu.add(moveDogs);
        jMenu.addSeparator();
        //Приоритеты потоков
        String[] prioritiesText = {"Минимальный", "Стандартный", "Максимальный"};
        jMenu.add(mainPriority);
        for (int i = 0; i < 3; i++) {
            JMenuItem jMenuItem = new JMenuItem(prioritiesText[i]);
            mainPriority.add(jMenuItem);
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(jMenuItem.getText().equals("Минимальный"))
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                    else if(jMenuItem.getText().equals("Стандартный"))
                        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                    else if(jMenuItem.getText().equals("Максимальный"))
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
            });
        }
        jMenu.addSeparator();
        //Генерация объектов
        jMenu.add(generationCats);
        jMenu.add(generationDogs);
        jMenu.addSeparator();
        //Консоль
        jMenu.add(showConsole);
        showConsole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogConsole().setVisible(true);
            }
        });
        jMenu.addSeparator();
        //Загрузка и сохранение объектов в файл
        jMenu.add(savePets);
        savePets.addActionListener(new SavePetsAction());
        jMenu.add(loadPets);
        loadPets.addActionListener(new LoadPetsAction());
        //jMenu.addSeparator();
        //Список всех подключенных клиентов
        //jMenu.add(clientsMenu);
        //updateClientsMenu();
        //Сохранение объектов в базу данных
        jMenu.addSeparator();
        jMenu.add(menuSavePetsSQL);
        menuSavePetsSQL.add(saveAllPetsSQL);
        saveAllPetsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.savePets();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
        menuSavePetsSQL.add(saveCatsSQL);
        saveCatsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.saveCats();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
        menuSavePetsSQL.add(saveDogsSQL);
        saveDogsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.saveDogs();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
        //Загрузка объектов из базы данных
        jMenu.add(menuLoadPetsSQL);
        menuLoadPetsSQL.add(loadAllPetsSQL);
        loadAllPetsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.getPets();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
        menuLoadPetsSQL.add(loadCatsSQL);
        loadCatsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.getCats();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
        menuLoadPetsSQL.add(loadDogsSQL);
        loadDogsSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PetsSQL.getDogs();
                        } catch (SQLException ex) {
                            ex.printStackTrace(System.err);
                        }
                    }
                }).start();
            }
        });
    }

    public static void updateClientsMenu(){
        clientsMenu.removeAll();
        JMenuItem jMenuItemMe = new JMenuItem("Мой клиент");
        jMenuItemMe.setEnabled(false);
        clientsMenu.add(jMenuItemMe);
        if (clientsList.size() > 1)
            clientsMenu.addSeparator();
        clientsList.forEach(client -> {
            if(!client.equals(clientPort + "")){
                JMenuItem jMenuItem = new JMenuItem(client);
                jMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            outputStream.writeObject(client);
                            outputStream.writeObject(Habitat.getAllCats());
                            outputStream.flush();
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
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                clientsMenu.add(jMenuItem);
            }
        });
    }

}
