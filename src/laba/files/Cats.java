package laba.files;

import java.awt.geom.Point2D;

public class Cats extends Pets{
    //Переопределенный метод добавления картинки
    @Override
    public void setImage() {
        Point2D p = generatePoints();
        //Добавление компонента
        ImageComponent imageComponent = new ImageComponent("img/cat.png");
        imageComponent.setBounds(
                (int)p.getX(),
                (int)p.getY(),
                imageComponent.getWidthImage(),
                imageComponent.getHeightImage()
        );
        Habitat.panelImages.add(imageComponent);
        //Перерисовка картинок
        Habitat.panelImages.repaint();
        Pets.countCats += 1;
        System.out.println("Cat " + p);
    }
}
