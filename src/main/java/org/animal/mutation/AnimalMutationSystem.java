package org.animal.mutation;

public abstract class AnimalMutationSystem {
    int[] gens;
    int lastIndexUsed = -1;

    public AnimalMutationSystem(){


    }
    abstract public int getNextAnimalGen();
}
