import java.awt.Color;
import java.awt.Font;

public class Config {
    public static final Level[] LEVELS = new Level[]{
        new Level("Easy", 8, 10, 10),
        new Level("Medium", 14, 18, 40),
        new Level("Hard", 20, 24, 99),
    };

    public static final Color[] NUMBER_COLORS = new Color[]{
        new Color(255, 255, 255), // 0
        new Color(25, 118, 210), // 1
        new Color(57, 142, 60), // 2
        new Color(211, 48, 48), // 3
        new Color(123, 31, 162), // 4
        new Color(255, 143, 0), // 5
        new Color(0, 105, 92), // 6
        new Color(192, 192, 192), // 7
        new Color(64, 64, 128), // 8
    };

    public static final Color BOARD_DARK_COLOR = new Color(142, 204, 57);
    public static final Color BOARD_LIGHT_COLOR = new Color(167, 217, 72);
    
    public static final Color BOARD_DISABLED_DARK_COLOR = new Color(215, 184, 153);
    public static final Color BOARD_DISABLED_LIGHT_COLOR = new Color(229, 194, 159);

    public static final Color GAME_DETAILS_FLAG_COUNT_COLOR = new Color(255, 255, 255);
    public static final Color GAME_DETAILS_BG_COLOR = new Color(74, 117, 44);

    public static final Font FONT_FOR_BUTTONS = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

    public static final String FLAG_ICON_URL = "https://www.google.com/logos/fnbx/minesweeper/flag_icon.png";
    public static final String BOMB_ICON_URL = "https://apprecs.org/gp/images/app-icons/300/af/com.cgames.minesweeper.jpg";
    public static final String TIMER_ICON_URL = "https://images.vexels.com/media/users/3/128840/isolated/preview/c091629800ce3d91d8527d32d60bc46f-stopwatch-timer.png";
}
