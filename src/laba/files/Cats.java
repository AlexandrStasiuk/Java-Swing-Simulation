package laba.files;

import java.awt.geom.Point2D;

import static constants.Components.panelImages;

public class Cats extends Pets{
    public static int timeLife;

    public Cats(int timeBirth, int timeLife) {
        super(timeBirth, "img/cat.png", "cat");
        Cats.timeLife = timeLife;
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
        Pets.countCats += 1;
    }

}
