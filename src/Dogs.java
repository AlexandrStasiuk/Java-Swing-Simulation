import javax.swing.*;
import java.awt.geom.Point2D;

public class Dogs extends Pets{
    //Переопределенный метод добавления картинки
    @Override
    public void setImage() {
        Point2D p = generatePoints();
        //Добавление компонента
        ImageComponent imageComponent = new ImageComponent("img/dog.png");
        imageComponent.setBounds(
                (int)p.getX(),
                (int)p.getY(),
                imageComponent.getWidthImage(),
                imageComponent.getHeightImage()
        );
        Habitat.panelImages.add(imageComponent);
        //Перерисовка картинок
        Habitat.panelImages.repaint();
        Pets.countDogs += 1;
        System.out.println("Dog " + p);
    }
}
