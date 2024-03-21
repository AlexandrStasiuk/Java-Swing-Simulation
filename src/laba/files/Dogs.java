package laba.files;

import java.awt.geom.Point2D;

import static constants.Components.panelImages;

public class Dogs extends Pets{
    public static int timeLife = 10;
    public Dogs(String type, int timeBirth, int id, int speedTraectoryX, int speedTraectoryY, int X, int Y) {
        super(type, timeBirth, id, speedTraectoryX, speedTraectoryY, X, Y);
    }

    public Dogs(int timeBirth) {
        super(timeBirth, "img/dog.png", "dog");
    }

    //Переопределенный метод добавления картинки
    @Override
    public void setImage() {
        Point2D p = generatePoints();
        //Добавление компонента
        getImageComponent().setBounds(
                (int)p.getX(),
                (int)p.getY(),
                getImageComponent().getWidthImage(),
                getImageComponent().getHeightImage()
        );
        panelImages.add(getImageComponent());
        //Перерисовка картинок
        panelImages.repaint();
        Pets.countDogs += 1;
    }
}
