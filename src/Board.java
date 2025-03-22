public class Board {
    private Line lines[];
    private int numLines;
    private final int rows, cols;
    private final Coordinate gameBoard[][];
    

    public static final int INVALID = -1;

    //getters
    public int getRows() {return rows;}
    public int getCols() {return cols;}
    public Coordinate getCoordinate(int r, int c)
    {
        return gameBoard[r][c];
    }
    public int getNumLines()
    {
        return numLines;
    }
    public Line getLoop(int num)
    {
        if(num>=0 && num<numLines)
            return(lines[num]);
        else
        {
            return null;
        }
    }
    
    public boolean  update(Line toChange, Line replacement)
    {
        if(!contains(toChange))
            return false;
        int ind = toChange.getNumInBoard();
        lines[ind] = replacement;
        replacement.linkCoords();
        return true;
    }

    public boolean contains(Line l)
    {
        return (l.getBoard() == this);
    }
    //sets up the game board and coordinates. No loop or linking
    private Board(int rs, int cs)
    {
        rows = rs;
        cols = cs;
        gameBoard = new Coordinate[rows][cols];
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                gameBoard[r][c] = new Coordinate(r, c);
            }
        }
    }
    
    // rs and cs must be multiples of 4
    // they are rounded down to a multiple of 4
    // the board is rs x cs
    // the board is split into 4x4 quadrants, and each quadrant is split into 4 loops of 4 coordinates
    // coordinates, loops, and the board are all linked with each other
    public static Board make4x4BoardSolver(int rs, int cs)
    {
        Board fin = new Board(rs/4*4, cs/4*4); //multiples of 4
        fin.linkCoordsToBoard();
        int l=0;

        fin.numLines = fin.rows*fin.cols/4;
        fin.lines = new Line[fin.numLines];
        
        for(int r=0; r<fin.rows/4; r++)
        {
            for(int c=0; c<fin.cols/4; c++)
            {
                for(int corner=0; corner<2; corner++)
                {
                    for(int left = 0; left<2;left++)
                    {
                        //all starter loops covererd
                        fin.lines[l] = fin.makeLoop4x4Square(c, r, corner==0,left==0);
                        fin.lines[l].setNumInBoard(l);
                        fin.lines[l].setBoard(fin);
                        fin.lines[l].linkCoords();
                        l++;
                    }
                }
            }
        }
        return fin;
    }

    // makes the board's coordinates have access to the board
    public void linkCoordsToBoard()
    {
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                gameBoard[r][c].setBoard(this);
            }
        }
    }


    // removes the specified line from the lines and decreases line count
    // returns if the removal was succesful
    public boolean removeLine(Line l)
    {
        if(l.getNumInBoard() < 0 || l.getNumInBoard() >= numLines) //includes invalid
        {
            System.err.println("remove line failed");
            return false;
        }
        int numRem = l.getNumInBoard();
        Line temp = lines[numRem];
        lines[numRem] = lines[numLines-1];
        lines[numLines-1] = temp; //not used later, but to see what was removed
        lines[numRem].setNumInBoard(numRem);
        numLines--;
        if(numLines == 0)
        {
            System.err.println("remove line failed");
        }
        return true;
    }

    public boolean valid()
    {
        Line l;
        for(int i=0; i<numLines; i++)
        {
            l=lines[i];
            if(!l.valid())
                return false;
        }
        for(Coordinate c[] : gameBoard )
        {
            for(Coordinate r:c)
            {
                if(r.getBoard() == null)
                {
                    return false;
                }
            }
        }
        return true;
    }

    //create a loop in 1 of the 4x4 squares
    // it can be a corner loop or not, 
    // it can have its bottom most square on the left or right half of the quadrant
    //col4 is 0 for a-d, 1 for e-h,... x-coord of 4x4 square where loop is made
    //row4 y-coord of 4x4 square where loop is made
    private Loop makeLoop4x4Square(int col4, int row4, boolean leftHalf, boolean isCorner)
    {
        int sqCol = col4*4;
        int sqRow = row4*4;
        Coordinate coords[] = new Coordinate[4];
        if(!leftHalf){
            if(isCorner)
            {
                //right bottom corner start
                //like d1, b2, a4, c3
                sqCol += 3;
                coords[0] = gameBoard[sqRow][sqCol]; 
                coords[1] = coords[0].moveDir(new Direction(false, true, true));
                coords[2] = coords[1].moveDir(new Direction(false, true, false));
                coords[3] = coords[2].moveDir(new Direction(true, false, true));                
            }
            else
            {
                //start like c1, a2, b4, d3
                sqCol += 2;
                coords[0] = gameBoard[sqRow][sqCol];
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
                //coords[0] = gameBoard[x1Coord][y1Coord];
                coords[0] = gameBoard[sqRow][sqCol];
                
                coords[1] = coords[0].moveDir(new Direction(true, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(false, false, false));    
            }
            else
            {
                //ex b1, a3, c4, d2
                sqCol += 1;
                coords[0] = gameBoard[sqRow][sqCol];
                coords[1] = coords[0].moveDir(new Direction(false, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(true, false, false));    
            }
            
        }
        Loop finLoop = new Loop(coords);
        return finLoop;
    }

    // finds 2 loops or a line and loop, and merges them
    // returns if it was succesful
    // no merge may be possible
    public boolean makeMerge()
    {
        int start = (int)(Math.random()*numLines);
        int i;

        for(i=0; i<numLines; i++)
        {
            if(lines[(i+start)%numLines].mergeLoopsAnywhere())
            {
                return true;
            }
        }

        return false;
    }

    // returns the answer the loop found
    // works if solve was succesful previously
    public Line answer(){
        return lines[0];
    }

    //prints the answer
    public void printAnswer(){
        System.out.println(answer());
    }

    /**
     * sets the answer  
     * the given line becomes the answer
     * @param l is an answer
     */
    public void setAnswer(Line l)
    {
        lines[0] = l;
        numLines = 1;
    }

    /**
     * @return the board is formatted as a 2D array, with a number in each cell corresponding to its number in the solution
     */
    @Override
    public String toString()
    {
        int boardVal[][] = new int[rows][cols];
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                boardVal[r][c] = gameBoard[r][c].getLine().getNumInBoard();
            }
        }
        return Line.arrToString(boardVal, true);
    }
}
