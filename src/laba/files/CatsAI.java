package laba.files;

import static constants.Parameters.HEIGHT;
import static constants.Parameters.WIDTH;

public class CatsAI extends BaseAI{
    public CatsAI(Pets pet, int speed) {
        super(pet, speed);
    }
    //Логика смещения объекта
    @Override
    public void move() {
        int x = pet.getImageComponent().getX();
        int y = pet.getImageComponent().getY();
        if(x + speed <= WIDTH*75/100-100 && pet.speedTraectoryX > 0){
            x += speed;
        }else if(x + speed > WIDTH*75/100-100 && pet.speedTraectoryX > 0){
            pet.speedTraectoryX = -1;
            x -= speed;
        }else if(x - speed >= 0 && pet.speedTraectoryX < 0){
            x -= speed;
        }else if(x - speed < 0 && pet.speedTraectoryX < 0){
            pet.speedTraectoryX = 1;
            x += speed;
        }
        if(y + speed <= HEIGHT-150 && pet.speedTraectoryY > 0){
            y += speed;
        }else if(y + speed > HEIGHT-150 && pet.speedTraectoryY > 0){
            pet.speedTraectoryY = -1;
            y -= speed;
        }else if(y - speed >= 0 && pet.speedTraectoryY < 0){
            y -= speed;
        }else if(y - speed < 0 && pet.speedTraectoryY < 0){
            pet.speedTraectoryY = 1;
            y += speed;
        }
        smoothMovement(x, y);
    }
    //Запуск потока
    @Override
    public void run() {
        //Синхронизация на объекте
        synchronized (pet){
            if(moveCats){
                move();
            }else {
                try {
                    pet.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
