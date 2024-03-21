package actions;

import laba.files.Pets;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.*;

import static constants.Components.panelImages;
import static constants.Components.textTime;
import static constants.Parameters.*;
import static constants.Parameters.petsTimeBirthMap;
import static laba.files.Habitat.clearSession;
import static laba.files.Habitat.setEnableStart;

public class LoadPetsAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        //Файловый диалог
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith("bin");
            }

            @Override
            public String getDescription() {
                return "Только bin файлы";
            }
        });
        jFileChooser.showDialog(panelImages, "Выбрать файл");
        File file = jFileChooser.getSelectedFile();
        //ВВыбран ли файл
        if(file == null)
            return;
        //Проверка формата файла
        if(!file.getName().endsWith("bin")){
            JOptionPane.showMessageDialog(
                    panelImages,
                    "Файл должен быть .bin",
                    "Неверный формат файла",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try(ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))){
            petsList.clear();
            flPreloadFilePets = true;
            if(flStart){
                //Проверка на запущенную симуляцию
                textTime.setVisible(false);
                setEnableStart(true);
                //Очистка старой симуляции
                clearSession();
                //Очистка таймера
                if(updateTimer != null)
                    updateTimer.cancel();
                //Остановка потока
                if(petsMoveThreads != null && !petsMoveThreads.isShutdown())
                    petsMoveThreads.shutdown();
            }
            Pets pet = null;
            int correctTime = -1;
            //Перебор объектов в файле
            while ((pet = (Pets) stream.readObject()) != null){
                //Корректирование времени
                if(correctTime == -1)
                    correctTime = pet.getTimeBirth();
                pet.setTimeBirth(pet.getTimeBirth() - correctTime + 1);
                //Добавление объектов
                petsList.add(pet);
                petsIdsSet.add(pet.getId());
                petsTimeBirthMap.put(pet.getId(), pet.getTimeBirth());
            }
        } catch (EOFException ex) {
            // Достигнут конец потока
            return;
        } catch (StreamCorruptedException ex){
            JOptionPane.showMessageDialog(
                    panelImages,
                    "Файл не содержить объекты Pets",
                    "Неверный формат файла",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
