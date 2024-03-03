package actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static constants.Parameters.*;
import static constants.Components.*;
//Событие скрытия таймера
public class HiddenTimeAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        flVisibleTime = !flVisibleTime;
        if (flVisibleTime) {
            showTimeMenu.setSelected(true);
            showTime.setSelected(true);
        } else {
            hiddenTimeMenu.setSelected(true);
            hiddenTime.setSelected(true);
        }
        if(flStart){
            textTime.setVisible(flVisibleTime);
        }
    }
}
