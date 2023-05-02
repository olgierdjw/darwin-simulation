package org.settings;

import org.settings.options.*;

import java.io.File;
import java.util.HashMap;

public class WorldSettings {

    HashMap<GameOptions, GameOption<?>> gameSettings;

    public WorldSettings() {
        gameSettings = defaultSettings();
    }

    private HashMap<GameOptions, GameOption<?>> defaultSettings() {
        HashMap<GameOptions, GameOption<?>> initOptions = new HashMap<>();
        initOptions.put(GameOptions.STARTING_ENERGY, new StartingEnergy());
        initOptions.put(GameOptions.WORLD_SIZE, new WorldSize());
        initOptions.put(GameOptions.ANIMAL_INIT_NUMBER, new AnimalInitNumber());
        initOptions.put(GameOptions.GENOME_LENGTH, new GenomeLengthOption());
        initOptions.put(GameOptions.ANIMAL_MOVING_BEHAVIOUR, new AnimalMovingBehaviour());

        return initOptions;
    }

    public <T> GameOption<T> get(GameOptions option) {
        Object value = gameSettings.get(option);
        if (value == null) {
            throw new IllegalArgumentException("Invalid option: " + option);
        }
        return (GameOption<T>) value;
    }

    public <T> T getValue(GameOptions option) {
        if (defaultSettings().containsKey(option)) {
            GameOption<T> gameOption = (GameOption<T>) defaultSettings().get(option);
            return gameOption.getValue();
        } else {
            throw new IllegalArgumentException("Invalid game option: " + option);
        }
    }
}
