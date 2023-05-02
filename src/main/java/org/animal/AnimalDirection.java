package org.animal;

public enum AnimalDirection {
    DEG0,
    DEG45,
    DEG90,
    DEG135,
    DEG180,
    DEG225,
    DEG270,
    DEG315;

    public AnimalDirection opposite() {
        return AnimalDirection.values()[(ordinal() + 4) % 8];
    }
}
