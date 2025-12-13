package tetris.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomBag {
    private final Random rng = new Random();
    private final List<ShapeType> bag = new ArrayList<>();

    public ShapeType next() {
        if (bag.isEmpty()) refill();
        return bag.remove(bag.size() - 1);
    }

    private void refill() {
        bag.clear();
        Collections.addAll(bag, ShapeType.values());
        Collections.shuffle(bag, rng);
    }
}
