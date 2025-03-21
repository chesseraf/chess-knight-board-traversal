import java.util.Iterator;

public class Loop extends Line{
    
    public Loop(Coordinate arr[])
    {
        super(arr); 
    }

    public static Loop makeLoop(Coordinate arr[], Board b)
    {
        Loop fin = new Loop(arr);
        fin.linkCoords();
        fin.board = b;
        return fin;
    }

    @Override
    public Line linearize()
    {
        return  Line.create(coords, board);
    }

    // if not yet filled, makes the rows and cols arrays have, at index i, the row or col of the the ith coordinate in the loop
    // with the 0th coordinate being 0,0 which marks the start of the loop. Used for comparing, hashing, printing
    // marks the loop as a solution, so this is not repeated
    @Override
    public void prepareCompare()
    {
        if(!preCompareDone)
        {
            int start = findUpperLeftInd();
            rows = new int[coords.length];
            cols = new int[coords.length];
            
            for(int i=0; i<coords.length; i++)
            {
                rows[i] = coords[(i+start)%coords.length].getRows();
                cols[i] = coords[(i+start)%coords.length].getCols();
            }
            preCompareDone = true;
        }
    }   

    @Override
    public Iterator<CoordinatePair> iterator()
    {
        return new LoopIterator(coords, traversal);
    }
    
    public static void printBoardArr(int arr[][])
    {
        System.out.println(Line.arrToString(arr, false));
    }

    @Override
    public String toString()
    {
        prepareCompare();
        int boardRow = board.getRows();
        int boardCols = board.getCols();

        int arr[][] = new int[boardRow][boardCols];
        for(int i=0; i<coords.length; i++)
        {
            arr[rows[i]][cols[i]] = i+1;
        }
        return (Line.arrToString(arr, false));
    }

    @Override
    public boolean mergeLoopsAnywhere()
    {
        return mergeLoopsAnywhereAdjacentPaths();
    }
}
