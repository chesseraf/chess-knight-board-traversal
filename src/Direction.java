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
    public static Direction[] allDirections()
    {
        Direction arr[] = new Direction[8];
        int offset = (int)(Math.random()*8)*App.RANDOMIZE;
        for(int i = 0; i<8; i++)
        {
            arr[(i+offset)%8] = new Direction(i%2 == 0, (i/2)%2 == 0, (i/4)%2==1);
        }
        return arr;
    }
}
