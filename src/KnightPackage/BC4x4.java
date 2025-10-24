package KnightPackage;
import java.util.function.BiFunction;

class BC4x4 implements BoardCreator
{
    @Override
    public Board createBoard(int rows, int cols)
    {
        rows = rows/4*4;
        cols = cols/4*4;
        int l=0;
        Loop loops[] = new Loop[rows*cols/4];
        
        for(int r=0; r<rows/4; r++)
        {
            for(int c=0; c<cols/4; c++)
            {
                for(int corner=0; corner<2; corner++)
                {
                    for(int left = 0; left<2;left++)
                    {
                        // all starter loops covered
                        loops[l] = makeLoop4x4Square(c, r, corner==0,left==0);
                        loops[l].linkCoords();
                        l++;
                    }
                }
            }
        }
        Board fin = Board.createLoopedBoard(rows, cols, loops);
        return fin;
    }

     //create a loop in 1 of the 4x4 squares
    // it can be a corner loop or not, 
    // it can have its bottom most square on the left or right half of the quadrant
    //col4 is 0 for a-d, 1 for e-h,... x-coord of 4x4 square where loop is made
    //row4 y-coord of 4x4 square where loop is made
    private static Loop makeLoop4x4Square(int col4, int row4, boolean leftHalf, boolean isCorner, Board board)
    {
        int sqCol = col4*4;
        int sqRow = row4*4;
        Coordinate coords[] = new Coordinate[4];
        BiFunction<Integer, Integer, Coordinate> firstCoordSup = (r, c) -> 
            board == null ? new Coordinate(r, c) : board.getCoordinate(r, c);

        if(!leftHalf){
            if(isCorner)
            {
                //right bottom corner start
                //like d1, b2, a4, c3
                sqCol += 3;
                coords[0] = firstCoordSup.apply(sqRow, sqCol);
                coords[1] = coords[0].moveDir(new Direction(false, true, true));
                coords[2] = coords[1].moveDir(new Direction(false, true, false));
                coords[3] = coords[2].moveDir(new Direction(true, false, true));                
            }
            else
            {
                //start like c1, a2, b4, d3
                sqCol += 2;
                coords[0] = firstCoordSup.apply(sqRow, sqCol);
                coords[1] = coords[0].moveDir(new Direction(false, true, true));
                coords[2] = coords[1].moveDir(new Direction(true, true, false));
                coords[3] = coords[2].moveDir(new Direction(true, false, true));    
            }
        }
        else
        {
            if(isCorner)
            {
                // ex a1, b3, d4, c2
                coords[0] = firstCoordSup.apply(sqRow, sqCol);
                coords[1] = coords[0].moveDir(new Direction(true, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(false, false, false));    
            }
            else
            {
                //ex b1, a3, c4, d2
                sqCol += 1;
                coords[0] = firstCoordSup.apply(sqRow, sqCol);
                coords[1] = coords[0].moveDir(new Direction(false, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(true, false, false));    
            }
            
        }
        Loop finLoop = new Loop(coords);
        return finLoop;
    }
    
    private static Loop makeLoop4x4Square(int col4, int row4, boolean leftHalf, boolean isCorner) {
        return makeLoop4x4Square(col4, row4, leftHalf, isCorner, null);
    }
}