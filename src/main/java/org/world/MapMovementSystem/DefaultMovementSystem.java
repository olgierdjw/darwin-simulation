package org.world.MapMovementSystem;

import org.animal.AnimalPosition;
import org.world.Vector2d;
import org.world.World;
import org.world.WorldSize;

public class DefaultMovementSystem implements MapMovementSystem {
    @Override
    public AnimalPosition mapNextStepDecision(World world, AnimalPosition start, AnimalPosition destination) {
        WorldSize worldSize = world.worldSize;
        int positionX = destination.position().x();
        int positionY = destination.position().y();
        if (positionY > worldSize.maxY() || positionY < worldSize.minY())
            return new AnimalPosition(start.position(), start.direction().opposite());

        if (positionX > worldSize.maxX())
            return new AnimalPosition(new Vector2d(worldSize.minX(), positionY), start.direction());
        if (positionX < worldSize.minX())
            return new AnimalPosition(new Vector2d(worldSize.maxX(), positionY), start.direction());
        return destination;
    }
}
