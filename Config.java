import java.awt.Color;

public class Config {
    public static final Level[] LEVELS = new Level[]{
        new Level("Easy", 8, 10, 20),
        new Level("Medium", 18, 14, 40),
        new Level("Hard", 40, 40, 20),
    };

    public static final Color[] NUMBER_COLORS = new Color[]{
        new Color(255, 255, 255), // 0
        new Color(25, 118, 210), // 1
        new Color(57, 142, 60), // 2
        new Color(211, 48, 48), // 3
        new Color(123, 31, 162), // 4
        new Color(244, 67, 54), // 5
        new Color(244, 67, 54), // 6
        new Color(244, 67, 54), // 7
        new Color(244, 67, 54), // 8
    };

    public static final Color BOARD_DARK_COLOR = new Color(142, 204, 57);
    public static final Color BOARD_LIGHT_COLOR = new Color(167, 217, 72);
    
    public static final Color BOARD_DISABLED_DARK_COLOR = new Color(215, 184, 153);
    public static final Color BOARD_DISABLED_LIGHT_COLOR = new Color(229, 194, 159);
}
