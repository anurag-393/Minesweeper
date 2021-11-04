import java.util.ArrayList;

public class Cell {
    private final GridPosition position;
    private boolean bomb;
    private boolean isRevealed;
    private boolean isFlagged;
    private int bombsNearby;
    private ArrayList<Cell> neighbours;
    
    Cell(GridPosition position, boolean bomb) {
        this.position = position;
        this.bomb = bomb;
        this.isFlagged = false;
        this.isRevealed = false;
        this.bombsNearby = 0;
        this.neighbours = new ArrayList<>();
    }
    
    public GridPosition getPosition() {
        return position;
    }

    public boolean hasBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public int getBombsNearby() {
        return bombsNearby;
    }

    public void setBombsNearby(int bombsNearby) {
        this.bombsNearby = bombsNearby;
    }

    public ArrayList<Cell> getNeighbours() {
        return this.neighbours;
    }
}
