package laba.files;

public abstract class BaseAI implements Runnable{
    //Объект для синхронизации
    final Pets pet;
    //Скорость передвижения
    int speed;
    //Передвигаются ли объекты
    static public boolean moveCats = true;
    static public boolean moveDogs = true;

    public BaseAI(Pets pet, int speed) {
        this.pet = pet;
        this.speed = speed;
    }
    //Включение/выключение передвижения у котов
    public void changeCatsAI(){
        if(moveCats){
            synchronized (pet){
                pet.notify();
            }
        }
    }
    //Включение/выключение передвижения у собак
    public void changeDogsAI(){
        if(moveDogs){
            synchronized (pet){
                pet.notify();
            }
        }
    }
    //Плавное передвежение с течением времени
    void smoothMovement(int x, int y){
        int x_old = pet.getImageComponent().getX();
        int y_old = pet.getImageComponent().getY();
        while (x_old != x || y_old != y){
            if(x_old != x){
                if(pet.speedTraectoryX > 0)
                    x_old += 1;
                else
                    x_old -= 1;
            }
            if(y_old != y){
                if(pet.speedTraectoryY > 0)
                    y_old += 1;
                else
                    y_old -= 1;
            }
            pet.getImageComponent().setLocation(x_old, y_old);
            pet.getImageComponent().repaint();
            try {
                Thread.sleep(1000/speed-1, 499999);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //Абстрактный метод передвижения
    abstract void move();
}
