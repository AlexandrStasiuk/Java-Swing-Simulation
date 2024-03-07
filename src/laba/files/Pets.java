package laba.files;

import constants.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.Random;
import static constants.Parameters.*;

//Абстрактный класс животных
public abstract class Pets implements IBehaviour{
    private String type;
    private int timeBirth;
    private int id;
    int speedTraectoryX = 1;
    int speedTraectoryY = 1;

    public String getType() {
        return type;
    }

    private ImageComponent imageComponent;

    public ImageComponent getImageComponent() {
        return imageComponent;
    }

    public int getTimeBirth() {
        return timeBirth;
    }

    public int getId() {
        return id;
    }

    public Pets(int timeBirth, String fileName, String type) {
        this.imageComponent = new ImageComponent(fileName);
        this.timeBirth = timeBirth;
        this.type = type;
        Random random = new Random();
        //генерация id и проверка на уникальность
        int id = random.ints(1, 1000000).findFirst().getAsInt();
        //Проверка id на уникальность
        while(petsIdsSet.contains(id)){
            id = random.ints(1, 1000000).findFirst().getAsInt();
        }
        this.id = id;
    }
    //Счетчики отдельных животных
    public static int countCats = 0;
    public static int countDogs = 0;
    //Компонент картинки животного
    public static class ImageComponent extends JComponent {
        private String fileImage;
        public ImageComponent(String fileImage) {
            this.fileImage = fileImage;
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Image image = new ImageIcon(fileImage).getImage();
            g2.drawImage(image, 0, 0, null);
        }
        //Получение размеров картинки
        public int getHeightImage(){
            return new ImageIcon(fileImage).getImage().getHeight(null);
        }
        public int getWidthImage(){
            return new ImageIcon(fileImage).getImage().getWidth(null);
        }
    }
    //Генерация рандомных координат для картинки
    public Point2D generatePoints(){
        return new Point2D.Double(
                new Random().
                        ints(0, WIDTH*75/100-100).
                        findFirst().
                        getAsInt(),
                new Random().
                        ints(0, HEIGHT-150).
                        findFirst().
                        getAsInt()
        );
    }
    //Абстрактный метод добавления картинки
    public abstract void setImage();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pets pets = (Pets) o;
        return timeBirth == pets.timeBirth && id == pets.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash( timeBirth, id);
    }

    @Override
    public String toString() {
        return "Животное = '" + type + '\'' +
                ", Время рождения = " + timeBirth +
                ", Id = " + id;
    }
}
