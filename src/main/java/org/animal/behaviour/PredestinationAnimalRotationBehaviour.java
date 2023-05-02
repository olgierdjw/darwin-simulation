package org.animal.behaviour;

import org.animal.AnimalDirection;

public class PredestinationAnimalRotationBehaviour extends AnimalRotationBehaviour {

    public PredestinationAnimalRotationBehaviour(int genomeLength) {
        super(genomeLength);
    }

    @Override
    public AnimalDirection nextDirection(AnimalDirection currentDirection, int[] genome) {
        AnimalDirection direction = AnimalDirection.values()[(currentDirection.ordinal() + genome[lastUsedIndex]) % AnimalDirection.values().length];
        lastUsedIndex = (lastUsedIndex + 1) % genomeLength;
        return direction;
    }
}
