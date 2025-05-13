package KnightPackage;
import java.util.ArrayList;
import java.util.Collections;

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
    public static ArrayList<Direction> allDirections()
    {
        ArrayList<Direction> list = new ArrayList<>(8);
        for(int i = 0; i<8; i++)
        {
            list.add(new Direction(i%2 == 0, (i/2)%2 == 0, (i/4)%2==1));
        }
        Collections.shuffle(list);
        return list;
    }
}
