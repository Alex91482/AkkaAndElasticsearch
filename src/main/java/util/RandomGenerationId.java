package util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerationId {

    //long id = new Random().nextLong();

    public Long generationId(){
        long id = ThreadLocalRandom.current().nextLong();
        if(id < 0){
            id = id * (-1);
        }
        return id;
    }
}
