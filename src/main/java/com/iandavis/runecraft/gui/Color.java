package com.iandavis.runecraft.gui;

public class Color {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Color(int newRed, int newGreen, int newBlue, int newAlpha) {
        red = newRed;
        green = newGreen;
        blue = newBlue;
        alpha = newAlpha;
    }

    public int getIntValue() {
        return (red << 16) | (green << 8) | (blue) | (alpha << 24);
    }
}
