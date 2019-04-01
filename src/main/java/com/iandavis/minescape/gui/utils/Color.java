package com.iandavis.minescape.gui.utils;

public class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

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
