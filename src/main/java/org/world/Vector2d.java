package org.world;

import org.animal.AnimalDirection;
import org.animal.AnimalPosition;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] readPosition() {
        return new int[]{x, y};
    }

    public Vector2d getPosition() {
        return new Vector2d(x, y);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Vector2d add(int x, int y) {
        return new Vector2d(this.x + x, this.y + y);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        return this.x == ((Vector2d) other).x && this.y == ((Vector2d) other).y;
    }

    public Vector2d forwardPosition(AnimalPosition animalPosition) {
        Vector2d goFrom = animalPosition.position();
        AnimalDirection toDirection = animalPosition.direction();
        return switch (toDirection) {
            case DEG0 -> goFrom.add(0, 1);
            case DEG45 -> goFrom.add(1, 1);
            case DEG90 -> goFrom.add(1, 0);
            case DEG135 -> goFrom.add(1, -1);
            case DEG180 -> goFrom.add(0, -1);
            case DEG225 -> goFrom.add(-1, -1);
            case DEG270 -> goFrom.add(-1, 0);
            case DEG315 -> goFrom.add(-1, 1);
        };
    }


}
