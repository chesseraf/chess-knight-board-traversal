
import java.util.Arrays;

public class Loop{
    private Coordinate coords[];
    private boolean isSolution;
    private int rows[];
    private int cols[];
    private int maxr, minr, maxc, minc;
    
    public Loop(Coordinate arr[])
    {
        this(arr, true);        
    }

    //isLoops is a potential change to make. 
    //It would allow the final answer to not be a loop
    // there would be one non-loop sequence, and in mergers it will retain its end points, which would be specified at the start
    // this would require a different algorithm to divide the original board into loops.
    public Loop(Coordinate arr[], boolean isLoops) 
    {
        coords = arr;
        isSolution = false;
        updateMinMax();
    }  
    
    public void updateMinMax()
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

    // if not yet filled, makes the rows and cols arrays have, at index i, the row or col of the the ith coordinate in the loop
    // with the 0th coordinate being 0,0 which marks the start of the loop. Used for comparing, hashing, printing
    // marks the loop as a solution, so this is not repeated
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

    

    // keep all loops clockwise always
    //returns weather the merge was succesful

    public boolean farAway(Loop other)
    {
        return (minc - other.maxc > 2
        || other.minc - maxc > 2
        || minr - other.maxr > 2
        || other.minr - maxr > 2);
    }

    public boolean mergeLoops(Loop other)
    {
        Coordinate curC1, curC2, otherC1, otherC2;
        int curLen = coords.length, otherLen = other.coords.length;

        //if loops are too far away they cannot be adjacent

        if(farAway(other)) return false;

        //more randomization when combining loop
        int startCur = (int)(Math.random()*curLen);
        int startOther = (int)(Math.random()*curLen);

        // coordinate 1 is always ahead of 2
        // coordinates 1,2 start at the end so the loop terminates once the last check has been done
        for(int curi=0; curi<curLen; curi++)
        {
            curC1 = coords[(curi+1+startCur)%curLen];
            curC2 = coords[(curi+startCur)%curLen];

            for(int otheri = 0; otheri < otherLen; otheri++){
                otherC1 = other.coords[(otheri+1+startOther)%otherLen];
                otherC2 = other.coords[(otheri+startOther)%otherLen];
                // both are clockwise so never the case
                if(curC1.adjacent(otherC1) && curC2.adjacent(otherC2))
                {
                    //coords = mergeArrays(coords, other.coords, curi, otheri, true);
                    coords = mergeArrays(coords, other.coords, (curi + startCur + curLen)%curLen, (otheri + startOther+otherLen)%otherLen, true);
                    minc = Math.min(minc, other.minc);
                    maxc = Math.max(maxc, other.maxc);
                    minr = Math.min(minr, other.minr);
                    maxr = Math.max(maxr, other.maxr);
                    return true;
                }
                else if(curC1.adjacent(otherC2) && curC2.adjacent(otherC1))
                {
                    coords = mergeArrays(coords, other.coords, (curi + startCur + curLen)%curLen, (otheri + startOther+otherLen)%otherLen, false);
                    //coords = mergeArrays(coords, other.coords, curi, otheri, false);
                    minc = Math.min(minc, other.minc);
                    maxc = Math.max(maxc, other.maxc);
                    minr = Math.min(minr, other.minr);
                    maxr = Math.max(maxr, other.maxr);
                    
                    return true;
                }
            }
        }
        return false;
    }

    //include i as part of the first part of the array
    //returs [arr1[0-split1] arr2[split2+1 - split2-1 wrapping] arr1[split1+1 - end]]
    public Coordinate[] mergeArrays(Coordinate arr1[], Coordinate arr2[], int split1, int split2, boolean reverseArr2)
    {
        int len1 = arr1.length, len2 = arr2.length;

        Coordinate mergedArr[] = new Coordinate[len1 + len2];
        for(int i=0; i<= split1; i++)
        {
            //copy over first array's curi elements
            mergedArr[i] = coords[i];
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
    static void printBoardArr(int arr[][])
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
    
    //prints the loop on the board, starting from the 1st coordinate rather than 0,0
    public void printLoop(int boardRow, int boardCols)
    {
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
        printBoardArr(arr);
    }

    //prints the loop on the board, starting from 0,0
    public void printLoopStart00(int boardRow, int boardCols)
    {
        fillSolution();
        if(!isSolution)
            printLoop(boardRow, boardCols);
        else
        {
            int arr[][] = new int[boardRow][boardCols];
            for(int i=0; i<coords.length; i++)
            {
                arr[cols[i]][rows[i]] = i+1;
            }
            printBoardArr(arr);
        }
    }

    //finds 0,0 coordinate in loop and returns its index, -1 if not there
    public int find00()
    {
        Coordinate zero = new Coordinate(0, 0);
        for(int i=0; i<coords.length; i++)
        {
            if(coords[i].equals(zero))
            {
                return i;
            }
                
        }
        return -1;
    }

    //loops are the same even if they start at different spots, but not if they traverse in different directions
    //the coordinate sequence must be the same
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || getClass() != obj.getClass()) return false;  // Different type
        Loop other = (Loop) obj;
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

    //returns whether all coordinates of the loop are adjacent to neiboring ones
    public boolean valid(){
        for(int i=0; i<coords.length; i++)
        {
            if(!coords[i].adjacent(coords[(i+1)%coords.length]))
            {
                return false;
            }
        }
        return(true); //loop wraps check
    }
}
