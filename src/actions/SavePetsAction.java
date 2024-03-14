package actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static constants.Parameters.petsList;

public class SavePetsAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        //Проверка на наличие объектов
        if(!petsList.isEmpty()){
            try(ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("pets.bin")));){
                //Загрузка каждого объекта в файл
                petsList.forEach(pets -> {
                    try {
                        stream.writeObject(pets);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
