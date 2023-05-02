package org.animal.behaviour;

public enum AnimalMovingBehaviourEnum {
    PREDESTINATION {
        @Override
        public AnimalRotationBehaviour createNew(int genomeLength) {
            return new PredestinationAnimalRotationBehaviour(genomeLength);
        }
    },
    MADNESS {
        @Override
        public AnimalRotationBehaviour createNew(int genomeLength) {
            return new PredestinationAnimalRotationBehaviour(genomeLength);
        }
    };

    public abstract AnimalRotationBehaviour createNew(int genomeLength);

}
