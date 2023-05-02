package org.world;

public class Grass implements MapObject {
    int energy;

    public Grass(int energy) {
        this.energy = energy;
    }

    public int energyValue() {
        return energy;
    }
}
