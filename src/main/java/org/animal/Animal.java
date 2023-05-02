package org.animal;

import org.animal.behaviour.AnimalRotationBehaviour;
import org.animal.mutation.AnimalMutationSystem;
import org.world.Grass;
import org.world.MapObject;
import org.world.Vector2d;
import org.world.World;

public class Animal implements MapObject {

    private AnimalPosition animalPosition;
    private final int[] genes;
    private int energy;
    World world;
    AnimalRotationBehaviour animalRotationBehaviour;
    AnimalMutationSystem animalMutationSystem;
    int childrenCounter = 0;

    public Animal(AnimalPosition animalPosition, int[] genes, int energy, AnimalMutationSystem animalMutationSystem, AnimalRotationBehaviour animalRotationBehaviour, AnimalDirection animalDirection, World world) {
        this.animalPosition = animalPosition;
        this.genes = genes;
        this.energy = energy;
        this.animalMutationSystem = animalMutationSystem;
        this.animalRotationBehaviour = animalRotationBehaviour;
        this.world = world;
    }

    public int getEnergy() {
        return energy;
    }

    public int[] getGenes() {
        return genes;
    }

    public Vector2d getPosition() {
        return animalPosition.position();
    }

    private void rotate() {
        animalPosition = new AnimalPosition(animalPosition.position(), animalRotationBehaviour.nextDirection(animalPosition.direction(), genes));
    }

    public void move() {
        rotate();
        Vector2d dreamPosition = animalPosition.position().forwardPosition(animalPosition);
        AnimalPosition forwardLocation = new AnimalPosition(dreamPosition, animalPosition.direction());
        animalPosition = world.requestAnimalPositionChange(this, animalPosition, forwardLocation);
        energy -= 1;
    }

    @Override
    public String toString() {
        return String.format("position: %s, energy: %d", animalPosition.position().toString(), energy);
    }


    public void eat(Grass grass) {
        energy += grass.energyValue();
    }

    public int getChildrenCounter() {
        return this.childrenCounter;
    }

    public void createBaby(int requiredEnergy) {
        energy -= requiredEnergy;
        childrenCounter += 1;
    }
}





