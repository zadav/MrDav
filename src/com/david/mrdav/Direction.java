package com.david.mrdav;

public enum Direction {
    UP, LEFT, DOWN, RIGHT;

    public Direction turnLeft() {
        return values()[(ordinal() + 1) % 4];
    }

    public Direction turnRight() {
        return values()[(ordinal() + 3) % 4];
    }
}
