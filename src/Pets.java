import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

//Абстрактный класс животных
public abstract class Pets implements IBehaviour{
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
                        ints(0, Habitat.WIDTH*75/100-100).
                        findFirst().
                        getAsInt(),
                new Random().
                        ints(0, Habitat.HEIGHT-115).
                        findFirst().
                        getAsInt()
        );
    }
    //Абстрацтный метод добавления картинки
    public abstract void setImage();
}
