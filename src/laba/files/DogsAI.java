package laba.files;

import static constants.Parameters.*;
import static constants.Parameters.petsList;

public class DogsAI extends BaseAI{
    public DogsAI(Pets pet, int speed) {
        super(pet, speed);
    }

    //Логика смещения объекта
    @Override
    public void move() {
        Pets target = petsList.get(0);
        int x = pet.getImageComponent().getX();
        int y = pet.getImageComponent().getY();
        //Перебор объектов для получения ближайщшего к собаке кота
        for(Pets pet: petsList){
            if(target.getType().equals("cat") && pet.getType().equals("cat")){
                int target_dist = (int)Math.sqrt(Math.pow(target.getImageComponent().getX()-x, 2) + Math.pow(target.getImageComponent().getY()-y, 2));
                int pet_dist = (int)Math.sqrt(Math.pow(pet.getImageComponent().getX()-x, 2) + Math.pow(pet.getImageComponent().getY()-y, 2));
                if(target_dist < pet_dist)
                    target = pet;
            }
            if(target.getType().equals("dog") && pet.getType().equals("cat"))
                target = pet;
        }
        if(target.getType().equals("dog"))
            return;
        int x_target = target.getImageComponent().getX();
        int y_target = target.getImageComponent().getY();

        pet.speedTraectoryX = (x < x_target) ?  1 : -1;
        pet.speedTraectoryY = (y < y_target) ?  1 : -1;

        if(x + speed <= x_target && pet.speedTraectoryX > 0){
            x += speed;
        }else if(x - speed >= x_target && pet.speedTraectoryX < 0){
            x -= speed;
        }
        if(y + speed <= y_target && pet.speedTraectoryY > 0){
            y += speed;
        }else if(y - speed >= y_target && pet.speedTraectoryY < 0){
            y -= speed;
        }
        smoothMovement(x, y);
    }
    //Запуск потока
    @Override
    public void run() {
        //Синхронизация на объекте
        synchronized (pet){
            if(moveDogs){
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
