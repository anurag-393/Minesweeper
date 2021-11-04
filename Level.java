public class Level {
    private final String name;
    private final int rows;
    private final int cols;
    private final int bombs;

    public Level(String name, int rows, int cols, int bombs) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.bombs = bombs;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getBombs() {
        return bombs;
    }
}
