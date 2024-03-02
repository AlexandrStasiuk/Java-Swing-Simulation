package laba.files;

import java.awt.geom.Point2D;

public class Dogs extends Pets{
    public static int timeLife;

    public Dogs(int timeBirth, int timeLife) {
        super(timeBirth, "img/dog.png", "dog");
        Dogs.timeLife = timeLife;
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
        Habitat.panelImages.add(getImageComponent());
        //Перерисовка картинок
        Habitat.panelImages.repaint();
        Pets.countDogs += 1;
    }
}
