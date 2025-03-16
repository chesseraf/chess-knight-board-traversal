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

    // if not yet filled, makes the rows and cols arrays have, at index i, the row or col of the the ith coordinate in the loop
    // with the 0th coordinate being 0,0 which marks the start of the loop. Used for comparing, hashing, printing
    // marks the loop as a solution, so this is not repeated
    @Override
    public void fillSolution()
    {
        if(!isSolution)
        {
            int start = find00();
            if(start == -1) start = 0;
            rows = new int[coords.length];
            cols = new int[coords.length];
            
            for(int i=0; i<coords.length; i++)
            {
                rows[i] = coords[(i+start)%coords.length].getRows();
                cols[i] = coords[(i+start)%coords.length].getCols();
            }
            isSolution = true;
        }
    }

    @Override
    public Iterator<CoordinatePair> iterator()
    {
        return new LoopIterator(coords, traversal);
    }
    

    // prints an array formatted with a 1 in the 1st coordinate of the loop, 2 in the 2nd, ...
    // empty in cells that are not part of the lop
    public static void printBoardArr(int arr[][])
    {
        int maxR = arr.length;
        int maxC = arr[0].length;
        int maxDigits = Integer.toString(maxC*maxR).length();
        int maxLen = (int)Math.pow(10, maxDigits);
        int n;
        for(int i=0; i<(maxDigits+3)*maxC; i++)
            System.out.print("_");
        System.out.println("");
        for(int r=0; r<maxR; r++)
        {
            for(int c=0; c<maxC; c++)
            {
                n = arr[r][c];
                if(n==0)
                {
                    System.out.print("|    ");
                }
                else
                {
                    System.out.print("| "+n  );

                    while(n<maxLen)
                    {
                        System.out.print(" ");
                        n*=10;
                    }                    
                }   
            }
            System.out.println("|");
        }
    }

    //prints the loop on the board, starting from 0,0
    public void printLoopStart00(int boardRow, int boardCols)
    {
        fillSolution();
        if(!isSolution)
            System.out.print(toString());
        else
        {
            int arr[][] = new int[boardRow][boardCols];
            for(int i=0; i<coords.length; i++)
            {
                arr[rows[i]][cols[i]] = i+1;
            }
            printBoardArr(arr);
        }
    }

    // merges 2 loops into a line
    // the 2nd one becomes a part of the first one
    public boolean linearMerge(Loop other)
    {
        if(farAway(other))
            return false;
        
        
        return false;
    }
}
