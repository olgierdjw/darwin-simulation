package org.world;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.animal.Animal;
import org.animal.AnimalDirection;
import org.animal.AnimalPosition;
import org.animal.behaviour.AnimalMovingBehaviourEnum;
import org.animal.mutation.AnimalMutationSystem;
import org.gui.WorldGuiFactory;
import org.settings.GameOption;
import org.settings.GameOptions;
import org.settings.WorldSettings;
import org.world.MapMovementSystem.DefaultMovementSystem;
import org.world.MapMovementSystem.MapMovementSystem;

import java.util.*;
import java.util.stream.IntStream;

import static org.animal.behaviour.AnimalRotationBehaviour.generateGenes;

public class World {
    WorldSettings worldSettings;
    public final WorldSize worldSize;
    private final WorldGuiFactory guiFactory;
    MapMovementSystem mapMovementSystem;
    AnimalFollow animalFollow;
    private Boolean draught = false;

    private final HashMap<Vector2d, LinkedList<Animal>> liveAnimals = new HashMap<>();
    private final LinkedList<Animal> deadAnimals = new LinkedList<>();
    private final HashMap<Vector2d, Grass> grass = new HashMap<>();
    int grassCounter = 0;


    public World(WorldSettings worldInitSettings) {
        this.guiFactory = new WorldGuiFactory();
        this.worldSettings = worldInitSettings;

        this.mapMovementSystem = new DefaultMovementSystem();

        GameOption<Integer> x = worldInitSettings.get(GameOptions.WORLD_SIZE);
        this.worldSize = new WorldSize(new Vector2d(0, 0), new Vector2d(x.getValue(), x.getValue()));

    }

    public void grassGrow() {
        IntStream.rangeClosed(0, worldSize.maxX()).forEach(x -> IntStream.rangeClosed(0, worldSize.maxY()).forEach(y -> {
            Vector2d mapPosition = new Vector2d(x, y);
            if (!grass.containsKey(mapPosition)) {
                spawnGrass(mapPosition);
            }
        }));

    }

    private void addGrass(Grass g, Vector2d position) {
        if (!grass.containsKey(position)) {
            grass.put(position, g);
            grassCounter += 1;
        }
    }

    private void spawnGrass(Vector2d position) {
        Random random = new Random();

        int worldMid = worldSize.maxY() / 2;
        boolean greenField = position.y() < worldMid + 2 && position.y() > worldMid - 2;
        if (draught) {
            if (random.nextDouble() <= 0.01) addGrass(new Grass(2), position);
            return;
        }

        if (greenField) {
            if (random.nextDouble() <= 0.50) addGrass(new Grass(4), position);
        } else {
            if (random.nextDouble() <= 0.07) addGrass(new Grass(2), position);
        }
    }


    public void setDraught() {
        draught = true;
        System.out.println("TRUE");
    }

    public void disableDraught() {
        draught = false;
        System.out.println("FALSE");

    }

    public void createAnimal(int energy, int genesNumber, Vector2d initialPosition, int[] genom) {
        GameOption<AnimalMovingBehaviourEnum> animalMovingBehaviourOption = worldSettings.get(GameOptions.ANIMAL_MOVING_BEHAVIOUR);

        Animal animal = new Animal(new AnimalPosition(initialPosition, AnimalDirection.DEG90), genom, energy, new AnimalMutationSystem() {
            @Override
            public int getNextAnimalGen() {
                return 0;
            }
        }, animalMovingBehaviourOption.getValue().createNew(genesNumber), AnimalDirection.DEG0, this);

        setAnimalLocation(animal, initialPosition);
    }

    public void createAnimal(int energy, int genesNumber, Vector2d initialPosition) {
        createAnimal(energy, genesNumber, initialPosition, generateGenes(genesNumber));

    }

    public void createAnimal(int energy, int genesNumber) {
        createAnimal(energy, genesNumber, worldSize.randomMapPosition());
    }

    public void createAnimal(Animal parent1, Animal parent2) {
        int energy = parent1.getEnergy() + parent2.getEnergy();
        int[] g1 = parent1.getGenes();
        int[] g2 = parent2.getGenes();

        int[] childGenome = new int[g1.length];
        Random random = new Random();
        for (int i = 0; i < g1.length; i++) {
            if (random.nextBoolean()) childGenome[i] = g1[i];
            else childGenome[i] = g2[i];
        }

        createAnimal(4, g1.length, parent1.getPosition(), childGenome);
    }

    public void initAnimals() {
        GameOption<Integer> animalNumberOption = worldSettings.get(GameOptions.ANIMAL_INIT_NUMBER);
        GameOption<Integer> startingEnergyOption = worldSettings.get(GameOptions.STARTING_ENERGY);
        GameOption<Integer> genomeLengthOption = worldSettings.get(GameOptions.GENOME_LENGTH);

        for (int i = 0; i < animalNumberOption.getValue(); i++)
            this.createAnimal(startingEnergyOption.getValue(), genomeLengthOption.getValue());

    }

    public AnimalPosition requestAnimalPositionChange(Animal animal, AnimalPosition stateBefore, AnimalPosition stateExpected) {
        removeCurrentAnimalLocation(animal);
        AnimalPosition stateAccepted = mapMovementSystem.mapNextStepDecision(this, stateBefore, stateExpected);
        setAnimalLocation(animal, stateAccepted.position());
        return stateAccepted;
    }

    private void setAnimalLocation(Animal animal, Vector2d newPosition) {
        if (liveAnimals.containsKey(newPosition)) {
            liveAnimals.get(newPosition).add(animal);
        } else {
            LinkedList<Animal> names = new LinkedList<>();
            names.add(animal);
            liveAnimals.put(newPosition, names);
        }
    }

    private void removeCurrentAnimalLocation(Animal animal) {
        Vector2d position = animal.getPosition();
        if (liveAnimals.containsKey(position)) {
            LinkedList<Animal> animalList = liveAnimals.get(position);
            if (animalList.size() - 1 >= 1) animalList.remove(animal);
            else if (animalList.size() - 1 == 0) {
                liveAnimals.remove(position);
            }
        }
    }

    public int animalCount() {
        return liveAnimals.values().parallelStream()
                .mapToInt(List::size)
                .sum();
    }

    public int energySum() {
        return liveAnimals.values().stream()
                .flatMap(Collection::stream)
                .mapToInt(Animal::getEnergy)
                .sum();
    }

    public int grassCount() {
        return this.grassCounter;
    }


    public void animalMove() {
        this.liveAnimals.values()
                .stream()
                .flatMap(Collection::stream)
                .toList()
                .forEach(Animal::move);
    }


    public void deleteDeadAnimals() {
        this.liveAnimals.values().stream()
                .flatMap(
                        animalList -> animalList
                                .stream()
                                .filter(animal -> animal.getEnergy() <= 0)
                )
                .toList()
                .forEach(animal -> {
                    removeCurrentAnimalLocation(animal);
                    deadAnimals.add(animal);
                });
    }


    public void feedAnimals() {
        liveAnimals.forEach((vector2d, animals) ->
                Optional.ofNullable(grass.get(vector2d))
                        .ifPresent(
                                grass1 -> animals.stream()
                                        .findFirst()
                                        .ifPresent(animal -> {
                                            animal.eat(grass1);
                                            grass.remove(vector2d);
                                            grassCounter -= 1;
                                        })));
    }

    protected Optional<Animal> liveAnimal(Vector2d position) {
        Animal animal = null;
        if (liveAnimals.containsKey(position)) {
            animal = liveAnimals.get(position).getFirst();
        }
        return Optional.ofNullable(animal);
    }

    public void setAnimalFollow(AnimalFollow animalFollowGui) {
        this.animalFollow = animalFollowGui;
    }

    public HBox representation() {
        GridPane mapGrid = guiFactory.emptyWorldMap(worldSize);

        Optional<Vector2d> highlight;
        if (animalFollow != null) {
            highlight = animalFollow.positionToHighlight();
            if (highlight.isPresent() && !liveAnimals.containsKey(highlight.get())) animalFollow.stopFollowing();
        } else {
            highlight = Optional.empty();
        }
        for (int x = 0; x <= worldSize.maxX(); x++) {
            for (int y = 0; y <= worldSize.maxY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (highlight.isPresent() && position.equals(highlight.get())) {
                    mapGrid.add(guiFactory.gridElement(liveAnimals.get(position).getFirst(), true), x, y);
                } else if (liveAnimals.containsKey(position)) {
                    mapGrid.add(guiFactory.gridElement(liveAnimals.get(position).getFirst()), x, y);
                } else if (grass.containsKey(position)) {
                    mapGrid.add(guiFactory.gridElement(grass.get(position)), x, y);
                }
            }
        }
        return new HBox(mapGrid);
    }

    public int minimumToProcreation() {
        int poorThreshold = (int) ((energySum() / animalCount()) * 0.35);
        return Math.max(10, poorThreshold);
    }

    public void procreation() {
        int minimumEnergy = minimumToProcreation();
        int cost = (int) (minimumEnergy * 0.85);
        liveAnimals.entrySet()
                .parallelStream()
                .filter(entry -> entry.getValue().size() >= 2 && Math.min(entry.getValue().get(0).getEnergy(), entry.getValue().get(1).getEnergy()) > minimumEnergy)
                .forEach(entry -> {
                    entry.getValue().get(0).createBaby(cost);
                    entry.getValue().get(1).createBaby(cost);
                    createAnimal(entry.getValue().get(0), entry.getValue().get(1));
                });
    }

}
