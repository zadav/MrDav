package com.david.mrdav;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    public List<SnakePart> parts = new ArrayList<>();
    public Direction direction;

    public Snake() {
        direction = Direction.UP;
        parts.add(new SnakePart(5, 6));
        parts.add(new SnakePart(5, 7));
        parts.add(new SnakePart(5, 8));
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public void eat() {
        var tail = parts.get(parts.size() - 1);
        parts.add(new SnakePart(tail.x(), tail.y()));
    }

    public void advance() {
        var head = parts.get(0);
        int newX = head.x();
        int newY = head.y();

        switch (direction) {
            case UP    -> newY -= 1;
            case LEFT  -> newX -= 1;
            case DOWN  -> newY += 1;
            case RIGHT -> newX += 1;
        }

        if (newX > 9)  newX = 0;
        if (newX < 0)  newX = 9;
        if (newY < 0)  newY = 12;
        if (newY > 12) newY = 0;

        var newParts = new ArrayList<SnakePart>(parts.size());
        newParts.add(new SnakePart(newX, newY));
        for (int i = 0; i < parts.size() - 1; i++) {
            newParts.add(parts.get(i));
        }
        parts = newParts;
    }

    public boolean checkBitten() {
        var head = parts.get(0);
        for (int i = 1; i < parts.size(); i++) {
            var part = parts.get(i);
            if (part.x() == head.x() && part.y() == head.y())
                return true;
        }
        return false;
    }
}
