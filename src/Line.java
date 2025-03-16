import java.util.Arrays;
import java.util.Iterator;

public class Line implements Iterable<CoordinatePair>{
    
    protected  Coordinate coords[];
    protected  boolean isSolution;
    protected  int rows[];
    protected  int cols[];
    protected  int maxr, minr, maxc, minc;
    protected  Board board;
    private int numInBoard = Board.INVALID;
    protected  LineIterator.Traversal traversal = LineIterator.Traversal.forward;
    
    public Line(Coordinate arr[])
    {
        coords = arr;
        isSolution = false;
        updateMinMax();    
        board = null; 
    }

    public static Line create(Coordinate arr[], Board b)
    {
        Line fin = new Line(arr);
        fin.linkCoords();
        fin.board = b;
        return fin;
    }

    public void setTraversalType(LineIterator.Traversal t)
    {
        traversal = t;
    }

    public void setNumInBoard(int num)
    {
        numInBoard = num;
    }

    public int getNumInBoard()
    {
        return numInBoard;
    }


    public Coordinate getCoord(int num)
    {
        return(coords[num]);
    }

    public int getSize()
    {
        return coords.length;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board b)
    {
        board = b;
    }

    @Override
    public Iterator<CoordinatePair> iterator()
    {
        return new LineIterator(coords, traversal);
    }

    //avoiding leaking this in constructor
    //linking coordinates with loops
    public final void linkCoords()
    {
        int i=0;
        for(Coordinate c:coords)
        {
            c.setLine(this, i);
            i++;
        }
    }
    
    private void updateMinMax()
    {
        maxr = minr = coords[0].getRows();
        maxc = minc = coords[0].getCols();
        for(Coordinate c : coords)
        {
            if(c.getRows() < minr)
                minr = c.getRows();
            if(c.getRows() > maxr)
                maxr = c.getRows();
            if(c.getCols() < minc)
                minc = c.getCols();
            if(c.getCols() > maxc)
                maxc = c.getCols();
        }
    }    

    // keep all loops clockwise always
    //returns weather the merge was succesful

    public boolean farAway(Line other)
    {
        return (minc - other.maxc > 2
        || other.minc - maxc > 2
        || minr - other.maxr > 2
        || other.minr - maxr > 2);
    }

    public void fillSolution()
    {
        if(!isSolution)
        {

            rows = new int[coords.length];
            cols = new int[coords.length];
            
            for(int i=0; i<coords.length; i++)
            {
                rows[i] = coords[(i)].getRows();
                cols[i] = coords[(i)].getCols();
            }
            isSolution = true;
        }
    }

    public boolean mergeLoopsAnywhere()
    {
        //check all adjacent pairs of the current loop, and for them, look at all 
        // easy to make it check if it can loop with anyone rather than a specific other loop
        // TODO remove the loop that was joined
        // done, double check needed
        CoordinatePair moved;
        Direction dirs[] = Direction.allDirections();

        setTraversalType(LineIterator.Traversal.random);
        for(CoordinatePair cp: this)
        {
            for(Direction d:dirs)
            {
                moved = cp.moveDir(d);
                
                if(moved.consecutiveInLoop())
                {
                    // must be in a different loop
                    if(this != moved.getLine())
                    {
                        if(moved.forwardOrdered())
                        {
                            coords = mergeArrays(coords, moved.getLine().coords, cp.first().getNumInLine(), moved.first().getNumInLine(), true);
                        }
                        else
                        {
                            coords = mergeArrays(coords, moved.getLine().coords, cp.first().getNumInLine(), moved.second().getNumInLine(), false);
                        }
               
                        board.removeLine(moved.getLine());
                        linkCoords();
                        //System.out.println(board);
                        if(!valid())
                        {
                            System.err.println("invalid");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //splits are between split and split+1
    //returns [arr1[0-split1] arr2[split2+1 - split2 wrapping] arr1[split1+1 - end]]
    //arr2 may be reversed if requested arr2[split2,split2-1,...,split2+1]
    //splits index are the last index before the split
    public static Coordinate[] mergeArrays(Coordinate arr1[], Coordinate arr2[], int split1, int split2, boolean reverseArr2)
    {
        int len1 = arr1.length, len2 = arr2.length;
        Coordinate mergedArr[] = new Coordinate[len1 + len2];
        for(int i=0; i<= split1; i++)
        {
            //copy over first array's curi elements
            mergedArr[i] = arr1[i];
        }
        if(!reverseArr2){
            for(int i=0; i< len2; i++)
            {
                //copy over all second array's elements, starting at split2+1
                mergedArr[i + split1 + 1] = arr2[(split2+1+i)%len2];
            }
        }
        else //reverse arr 2
        {
            for(int i=0; i< len2; i++)
            {
                //copy over all second array's elements, starting at split2 in reverse order, with wrapping
                mergedArr[i + split1 + 1] = arr2[(split2-i+len2)%len2];
            }
        }
        
        for(int i=split1+1; i<len1; i++)
        {
            //copy over the second half of the first array
            mergedArr[i+len2] = arr1[i];
        }
        return mergedArr;
    }

    // prints an array formatted with a 1 in the 1st coordinate of the loop, 2 in the 2nd, ...
    // empty in cells that are not part of the lop
    public static String arrToString(int arr[][], boolean displayZeroes)
    {
        int maxR = arr.length;
        int maxC = arr[0].length;
        int maxDigits = Integer.toString(maxC*maxR).length();
        int maxLen = (int)Math.pow(10, maxDigits);
        int n;
        String fin = "";
        for(int i=0; i<(maxDigits+3)*maxC; i++)
            fin +=("_");
        fin+=("\n");
        for(int r=0; r<maxR; r++)
        {
            for(int c=0; c<maxC; c++)
            {
                n = arr[r][c];
                if(n==0)
                {
                    if(!displayZeroes)
                        fin+=("|    ");
                    else
                    {
                        fin+=("| "+n  );
                        n=1;
                        while(n<maxLen)
                        {
                            fin+=(" ");
                            n*=10;
                        }  
                    }
                }
                else
                {
                    fin+=("| "+n  );

                    while(n<maxLen)
                    {
                        fin+=(" ");
                        n*=10;
                    }                    
                }   
            }
            fin+=("|\n");
        }
        return fin;
    }
    
    //prints the loop on the board, starting from the 1st coordinate rather than 0,0
    @Override
    public String toString()
    {
        int boardRow = board.getRows();
        int boardCols = board.getCols();

        int arr[][] = new int[boardRow][boardCols];
        for(int r=0; r<boardRow; r++)
        {
            for(int c=0; c<boardCols; c++)
            {
                arr[r][c] = 0;
            }
        }
        for(int i=0; i<coords.length; i++)
        {
            arr[coords[i].getRows()][coords[i].getCols()] = i+1;
        }
        return arrToString(arr, false);
    }

    public int find00() {return findCoord(new Coordinate(0, 0)); }

    public int findCoord(int r, int c){return findCoord(new Coordinate(r, c)); }

    //finds 0,0 coordinate in loop and returns its index, -1 if not there
    public int findCoord(Coordinate c)
    {
        for(int i=0; i<coords.length; i++)
        {
            if(coords[i].equals(c))
            {
                return i;
            }
                
        }
        return -1;
    }

    
    //loops are the same even if they start at different spots, but not if they traverse in different directions
    //obj may be of a different class as long as it is a subclass
    //the coordinate sequence must be the same
    //fills the solution if equal coord lengths, so recomended to call on solutions only
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof Line)) return false; //not a line
        Line other = (Line) obj;
        if(coords.length == other.coords.length)
        {
            fillSolution();
            other.fillSolution();
            return (Arrays.equals(rows, other.rows) && Arrays.equals(cols, other.cols));
        }
        return false;
    }

    //hashes the rows and cols
    @Override
    public int hashCode() {
        return Arrays.hashCode(rows) + 11*Arrays.hashCode(cols);
    }

    //returns if all neigboring coordinates are adjacent
    public boolean valid(){
        int i=0;
        for(CoordinatePair cp : this)
        {
            if(!cp.adjacent())
                return false;
            if(coords[0].getNumInLine() != i)
            {
                return false;
            }
        } 
        return(true);
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
