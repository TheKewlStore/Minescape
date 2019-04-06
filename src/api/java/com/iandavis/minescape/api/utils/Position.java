package com.iandavis.minescape.api.utils;

public class Position {
    private float x;
    private float y;

    public Position(float newX, float newY) {
        x = newX;
        y = newY;
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
