package actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static constants.Parameters.*;
import static constants.Components.*;
//Событие показа информации
public class ShowInfoAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        flShowInfo = !flShowInfo;
        showInfo.setSelected(flShowInfo);
        showInfoMenu.setSelected(flShowInfo);
    }
}
