package org.world.MapMovementSystem;

import org.animal.AnimalPosition;
import org.world.World;

public interface MapMovementSystem {
    abstract AnimalPosition mapNextStepDecision(World world, AnimalPosition start, AnimalPosition destination);
}
