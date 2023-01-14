package util;

import java.util.ArrayList;
import java.util.List;

public class NullCheckImpl implements NullCheck{

    private final RandomGenerationId generationId;

    public NullCheckImpl(){
        this.generationId = new RandomGenerationId();
    }


    @Override
    public String check(String value) {
        return value == null ? "not specified" : value;
    }

    @Override
    public Long check(Long value) {
        return value == null ? generationId.generationId() : value;
    }

    @Override
    public List<String> check(List<String> value) {
        return value == null ? new ArrayList<>() : value;
    }
}
