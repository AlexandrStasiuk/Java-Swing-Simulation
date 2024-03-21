package laba.files;

import constants.Components;
import static constants.Parameters.*;

public class GenerationThread implements Runnable{
    private String type;

    public GenerationThread(String type) {
        this.type = type;
    }

    private synchronized void generateCat(){
        Cats cat = new Cats(timeElapsed);
        petsList.add(cat);
        petsIdsSet.add(cat.getId());
        petsTimeBirthMap.put(cat.getId(), cat.getTimeBirth());
        cat.setImage();
    }
    private synchronized void generateDog(){
        Dogs dog = new Dogs(timeElapsed);
        petsList.add(dog);
        petsIdsSet.add(dog.getId());
        petsTimeBirthMap.put(dog.getId(), dog.getTimeBirth());
        dog.setImage();
    }
    @Override
    public void run() {
        if(type.equals("cat") && Components.generationCats.isSelected())
            generateCat();
        if (type.equals("dog") && Components.generationDogs.isSelected())
            generateDog();
    }
}
