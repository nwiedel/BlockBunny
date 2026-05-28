package de.nicolas.config;

import com.badlogic.gdx.graphics.Color;

public class GameConfig {

    /** globale Hintergrundsfarbe */
    public static final Color CORNFLOWER_BLUE = new Color(0.39f, 0.58f, 0.93f, 1f);

    public static final String TITLE = "Block Bunny";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;

    // Box2D Zeug
    public static final float PPM = 100;

    // category bits
    public static final short BIT_PLAYER = 2;
    public static final short BIT_RED = 4;
    public static final short BIT_GREEN = 8;
    public static final short BIT_BLUE = 16;
    public static final short BIT_CRYSTAL = 32;
}
