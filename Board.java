import java.util.ArrayList;

public class Board {
    private final Cell[][] cells;
    private final ArrayList<GridPosition> bombPositions;
    
    Board(Level level) {
        cells = new Cell[level.getRows()][level.getCols()];

        bombPositions = new ArrayList<>();
        while(bombPositions.size() < level.getBombs()) {
            GridPosition randPosition = new GridPosition((int) (Math.random() * level.getRows()), (int) (Math.random() * level.getCols()));
            if(!bombPositions.contains(randPosition)) {
                bombPositions.add(randPosition);
            }
        }

        System.out.println(bombPositions);

        for(int i = 0; i < level.getRows(); i++){
            for (int j = 0; j < level.getCols(); j++) {
                cells[i][j] = new Cell(new GridPosition(i, j), bombPositions.contains(new GridPosition(i, j)));
            }
        }
        

        for (int i = 0; i < level.getRows(); i++) {
            for (int j = 0; j < level.getCols(); j++) {
                Cell cell = cells[i][j];

                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int yOffset = -1; yOffset <= 1; yOffset++) {
                        int x = i + xOffset;
                        int y = j + yOffset;
                            
                        if (!(xOffset == 0 && yOffset == 0) && (0 <= x && x < level.getRows()) && (0 <= y && y < level.getCols())) {
                            if (cells[x][y].hasBomb()) {
                                cell.setBombsNearby(cell.getBombsNearby() + 1);
                            }
                        }
                    }
                }
            }
        }

    }


    Cell[][] getCells() {
        return cells;
    }
    
    ArrayList<GridPosition> getBombPositions() {
        return bombPositions;
    }
}
