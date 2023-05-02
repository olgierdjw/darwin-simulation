package org.world;

public record WorldSize(Vector2d leftDown, Vector2d rightUp) {
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public int randomX() {
        return getRandomNumber(leftDown.readPosition()[0], rightUp.readPosition()[0]);
    }

    public int randomY() {
        return getRandomNumber(leftDown.readPosition()[1], rightUp.readPosition()[1]);
    }

    public Vector2d randomMapPosition() {
        return new Vector2d(randomX(), randomY());
    }

    public int maxY() {
        return rightUp.readPosition()[1];
    }

    public int minY() {
        return leftDown.readPosition()[1];
    }

    public int maxX() {
        return rightUp.readPosition()[0];
    }

    public int minX() {
        return leftDown.readPosition()[0];
    }

    public int area() {
        return Math.abs(minX() - maxX()) * Math.abs(minY() - maxY());
    }
}
