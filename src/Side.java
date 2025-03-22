public  enum Side{
    //0,0 is top left
    LEFT, RIGHT, UP, DOWN;
    public boolean isHorizontal()
    {
        return this == LEFT || this == RIGHT;
    }
    public boolean isVertical()
    {
        return this == UP || this == DOWN;
    }
    public Side opposite()
    {
        return switch (this) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case UP -> DOWN;
            case DOWN -> UP;
            default -> null;
        };
    }
    public int furthur(Coordinate l, Coordinate r)
    {
        return switch (this) {
            case LEFT -> r.getCols() - l.getCols();
            case RIGHT -> l.getCols() - r.getCols();
            case UP -> r.getRows() - l.getRows();
            case DOWN -> l.getRows() - r.getRows();
        };
    }
}