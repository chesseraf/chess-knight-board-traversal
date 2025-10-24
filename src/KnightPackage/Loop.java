package KnightPackage;
//package knightstour;
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

    /**
     * 
     * @param s
     * @param f must be consecutive with s
     * @return the line that starts at s and ends at f, with this loop's sequence
     */
    @Override
    public Line linearize(Coordinate s, Coordinate f)
    {
        if(!s.consecutiveInLoop(f))
        {
            System.err.println("linearization failed at :"+s+f);
            return null;
        }
        CordIt it; 
        if(s.nextInLine() ==f)
        {
            it = coordIt(f.getNumInLine());
            it.setReverse();
        }
        else
        {
            it = coordIt(s.getNumInLine());
        }
        Coordinate arr[] = new Coordinate[coords.length];
        for(int i=0; i<coords.length; i++)
        {
            arr[i] = it.next();
        }
        Line linear = Line.create(arr, board);
        linear.setNextInPath(getNextInPath());
        if(board != null)
        {
            board.update(this, linear);
        }
        if((linear.start()!=s) ||(linear.end() != f)||(!linear.valid()))
        {
            System.err.print("linearize failed");
            return null;
        }
        return linear;

    }

    // if not yet filled, makes the rows and cols arrays have, at index i, the row or col of the the ith coordinate in the loop
    // with the 0th coordinate being 0,0 which marks the start of the loop. Used for comparing, hashing, printing
    // sets the prepare compare flag, so this is not repeated
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
    public Coordinate start()
    {
        prepareCompare();
        return board.getCoordinate(rows[0], cols[0]);
    }

    @Override
    public Coordinate end()
    {
        prepareCompare();
        return board.getCoordinate(rows[rows.length-1], cols[cols.length-1]);
    }

    @Override
    public boolean mergeLoopsAnywhere()
    {
        return mergeLoopsAnywhereAdjacentPaths();
    }
}
