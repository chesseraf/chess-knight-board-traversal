package KnightPackage;

import java.util.Arrays;
import java.util.Iterator;

public class Line implements Iterable<CoordinatePair>{
    
    protected  Coordinate coords[];
    protected  boolean preCompareDone = false;
    protected  int rows[];
    protected  int cols[];
    protected  int maxr, minr, maxc, minc;
    protected  Board board;
    private int numInBoard = Board.INVALID;
    protected  LineIterator.Traversal traversal = LineIterator.Traversal.forward;
    public static int UNLABELED = -1;
    private int label=UNLABELED;
    private  boolean endLocked = false;
    private  boolean startLocked = false;
    private  Line nextInPath;

    public void setLocked(boolean l)
    {
        endLocked = l;
        startLocked = l;
    }
    public void lockStart()
    {
        startLocked = true;
    }
    public void lockEnd()
    {
        endLocked = true;
    }

    public void setNextInPath(Line l)
    {
        nextInPath = l;
    }
    public Line getNextInPath()
    {
        return nextInPath;
    }
    
    public Line(Coordinate arr[])
    {
        coords = arr;
        preCompareDone = false;
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

    public int getLabel()
    {
        return label;
    }

    public void setLabel(int l)
    {
        label = l;
    }

    public void setTraversalType(LineIterator.Traversal t)
    {
        traversal = t;
    }

    public Line linearize()
    {
        return this;
    }
    public Line linearize(Coordinate s, Coordinate f)
    {
        if(s == start() && f == end())
            return this;
        if(s == end() && f == start())
        {
            reverseLine();
            return this;
        }
        return null;
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

    public void prepareCompare()
    {
        if(!preCompareDone)
        {
            rows = new int[coords.length];
            cols = new int[coords.length];
            
            for(int i=0; i<coords.length; i++)
            {
                rows[i] = coords[(i)].getRows();
                cols[i] = coords[(i)].getCols();
            }
            preCompareDone = true;
        }
    }

    public boolean lineStepMerge()
    {
        return((!startLocked && lineStepMergeEndPoint(true))
            || (!endLocked && lineStepMergeEndPoint(false)));
        
    }

    public boolean isSolution()
    {
        return coords.length == board.getRows()*board.getCols();
    }

    public boolean lineStepMergeEndPoint(boolean start)
    {
        for(Direction d:Direction.allDirections())
        {
            Coordinate moved;
            if(start) 
                moved = start().moveDir(d);
            else 
                moved = end().moveDir(d);

            Line l = moved.getLine();
            if(l != null && l!=this)
            {
                if(l instanceof Loop)
                {
                    //do the merge
                    Coordinate newCoords[] = new Coordinate[coords.length+l.coords.length];
                    int split = moved.getNumInLine();
                    if(start)
                    {                        
                        System.arraycopy(l.coords, split+1, newCoords, 0, l.coords.length-split-1); //after split
                        System.arraycopy(l.coords, 0, newCoords, l.coords.length-split-1, split+1); //from beginning to split, inclusive
                        System.arraycopy(coords, 0, newCoords, l.coords.length, coords.length); // the original line
                    }
                    else //end
                    {
                        System.arraycopy(coords, 0, newCoords, 0, coords.length); // the original line
                        System.arraycopy(l.coords, split, newCoords, coords.length, l.coords.length-split); //split and after
                        System.arraycopy(l.coords, 0, newCoords, coords.length + l.coords.length-split, split); //from beginning to split, exclusive
                    }
                    board.removeLine(l);
                    coords = newCoords;
                    linkCoords();   
                    preCompareDone = false;  
                    return true;               
                }
                else if(isStart(moved))
                {

                }
                else if(isEnd(moved))
                {
                    
                }
            }
        }
        return false;
    }

    public static boolean isStart(Coordinate c)
    {
        return c.getNumInLine()==0;
    }
    public static boolean isEnd(Coordinate c)
    {
        Line l = c.getLine();
        if(l==null) return false;
        return c.getNumInLine()==(l.coords.length-1);
    }
    
    public Coordinate start()
    {
        return coords[0];
    }

    public Coordinate end()
    {
        return coords[coords.length-1];
    }

    public boolean mergeLoopsAnywhere()
    {
        return  lineStepMerge() || mergeLoopsAnywhereAdjacentPaths();
    }

    public void reverseLine()
    {
        // reverse the coordinates
        Coordinate newCoords[] = new Coordinate[coords.length];
        for(int i=0; i<coords.length; i++)
        {
            newCoords[i] = coords[coords.length-i-1];
        }
        coords = newCoords;
        linkCoords();
    }


    //merges the line with another loop
    //returns if a merge was succesful
    public boolean mergeLoopsAnywhereAdjacentPaths()
    {
        //check all adjacent pairs of the current loop, and for them, look at all 
        // easy to make it check if it can loop with anyone rather than a specific other loop
        CoordinatePair moved;
        setTraversalType(LineIterator.Traversal.random);
        for(CoordinatePair cp: this)
        {
            for(Direction d:Direction.allDirections())
            {
                moved = cp.moveDir(d);
                
                if(moved.consecutiveInLoop())
                {
                    // must be in a different loop
                    if(this != moved.getLine() )
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
                        if(!valid())
                        {
                            System.err.println("invalid");
                        }
                        preCompareDone = false;
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

        //copy over first array's elements before and including split1
        System.arraycopy(arr1, 0, mergedArr, 0, split1+1);
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
        
        //copy over the second half of the first array
        System.arraycopy(arr1, split1+1, mergedArr, split1+1+len2, len1-1-split1);
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



    public CordIt coordIt(int offset)
    {
        return new CordIt(this, offset);
    }
    public CordIt coordIt()
    {
        return coordIt(0);
    }
    
    public Coordinate[] getCoords()
    {
        return coords;
    }

    public class CordIt implements Iterator<Coordinate>, Iterable<Coordinate>
    {
        Line line;
        int cur = 0;
        int size;
        int offset;
        boolean reverse = false;
        public CordIt(Line l, int off)
        {
            offset = off;
            line = l;
            size = line.getSize();
        }
        public CordIt(Line l)
        {
            this(l,0);
        }
        @Override
        public CordIt iterator() {return this;}
        @Override
        public boolean hasNext() {
            return cur<line.getSize();
        }

        public void setReverse()
        {
            reverse = true;
        }

        @Override
        public Coordinate next()
        {
            Coordinate fin;
            if(!reverse)
               fin = line.coords[(cur+offset)%size];
            else
                fin = line.coords[(size-cur+offset-1)%size];
            cur++;
            return fin;
        }
    }

    public int find00() {return findCoord(new Coordinate(0, 0)); }
    public int findUpperLeftInd()
    {
        if(isSolution())
            return find00();
        int min = 0;
        Coordinate minC = coords[0];
        for(int i=1; i<coords.length; i++)
        {
            if(coords[i].compareTo(minC) < 0)
            {
                min = i;
                minC = coords[i];
            }
        }
        return min;
    }

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
        App.numComparisons++;
        if(coords.length == other.coords.length)
        {
            prepareCompare();
            other.prepareCompare();
            return (Arrays.equals(rows, other.rows) && Arrays.equals(cols, other.cols));
        }
        return false;
    }

    //hashes the rows and cols
    @Override
    public int hashCode() {
        prepareCompare();
        return Arrays.hashCode(rows) + 11*Arrays.hashCode(cols);
    }

    //returns if all neigboring coordinates are adjacent
    public boolean valid(){
        int i=0;
        for(CoordinatePair cp : this)
        {
            if(!cp.adjacent())
            {
                System.err.println("not adjacent in pair+"+cp);
                return false;
            }
                
            if(coords[i].getNumInLine() != i)
            {
                return false;
            }
            i++;
        } 
        return(true);
    }

    public boolean linearConnect(Line other)
    {
        if(end().adjacent(other.start()))
        {
            Coordinate newCoords[] = new Coordinate[coords.length+other.coords.length];
            System.arraycopy(coords, 0, newCoords, 0, coords.length);
            System.arraycopy(other.coords, 0, newCoords, coords.length, other.coords.length);
            coords = newCoords;
            linkCoords();
            preCompareDone = false;
            board.removeLine(other);
            nextInPath = other.nextInPath;
            label = other.label;
            return true;

        }
        System.err.println("linear connect failed");
        return false;
    }
}
