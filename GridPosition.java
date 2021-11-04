public class GridPosition {
    private final int gridx;
    private final int gridy;

    public GridPosition(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public int getGridx() {
        return this.gridx;
    }

    public int getGridy() {
        return this.gridy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        GridPosition other = (GridPosition) obj;
        if (this.gridx != other.gridx)
            return false;
        if (this.gridy != other.gridy)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.gridx + "," + this.gridy + ")";
    }
}
