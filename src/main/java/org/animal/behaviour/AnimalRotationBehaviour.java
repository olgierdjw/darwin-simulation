package org.animal.behaviour;


import org.animal.AnimalDirection;

public abstract class AnimalRotationBehaviour {
    int lastUsedIndex;
    int genomeLength;

    public AnimalRotationBehaviour(int genomeLength) {
        this.genomeLength = genomeLength;
        lastUsedIndex = getRandomNumber(0, genomeLength - 1);
    }

    public abstract AnimalDirection nextDirection(AnimalDirection currentDirection, int[] genome);

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int[] generateGenes(int howMany) {
        int[] genes = new int[howMany];
        for (int i = 0; i < howMany; i++)
            genes[i] = getRandomNumber(0, 7);
        return genes;
    }

}
