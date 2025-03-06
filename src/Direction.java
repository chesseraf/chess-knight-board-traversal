public class Direction {
    private final boolean right, up, horizontal;
    
    public Direction(boolean isR, boolean isU, boolean isHorizontal)
    {
        right = isR;
        up = isU;
        horizontal = isHorizontal;
    }

    public int getRight()
    {
        return (right?1:-1) * (horizontal?2:1);
    }
    public int getUp()
    {
        return (up?1:-1) * (horizontal?1:2);
    }
}
