

public class Board {
    private Line loops[];
    private int numLoops;
    private final int rows, cols;
    private final Coordinate gameBoard[][];
    private Coordinate targetEndpoints[];

    public static final int INVALID = -1;

    public int getRows() {return rows;}
    public int getCols() {return cols;}
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
    //rs and cs must be multiples of 4
    //they are rounded down to a multiple of 4
    //the board is rs x cs
    public static Board make4x4BoardSolver(int rs, int cs)
    {
        Board fin = new Board(rs/4*4, cs/4*4); //multiples of 4
        fin.linkCoordsToBoard();
        int l=0;

        fin.numLoops = fin.rows*fin.cols/4;
        fin.loops = new Loop[fin.numLoops];
        
        for(int r=0; r<fin.rows/4; r++)
        {
            for(int c=0; c<fin.cols/4; c++)
            {
                for(int corner=0; corner<2; corner++)
                {
                    for(int left = 0; left<2;left++)
                    {
                        //all starter loops covererd
                        fin.loops[l] = fin.makeLoop4x4Square(c, r, corner==0,left==0);
                        fin.loops[l].setNumInBoard(l);
                        fin.loops[l].setBoard(fin);
                        fin.loops[l].linkCoords();
                        l++;
                    }
                }
            }
        }
        return fin;
    }

    public static Board makeEndPoint4x4Solver(Coordinate s, Coordinate f, int r, int c)
    {
        Board fin = make4x4BoardSolver(r,c);
        Coordinate start = fin.getCoordinate(s.getRows(), s.getCols());
        Coordinate end = fin.getCoordinate(f.getRows(), f.getCols());
        fin.targetEndpoints = new Coordinate[2];
        fin.targetEndpoints[0] = start;
        fin.targetEndpoints[1] = end;

        for(Coordinate cord: fin.targetEndpoints)
        {
            fin.loops[cord.getLine().getNumInBoard()] = fin.loops[cord.getLine().getNumInBoard()].linearize();
        }

        //TODO linearize the loops
        //make the lines not jump from the fixed point
        //make the lines merge with each other and other loops
        return fin;
        
    }

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

    public Coordinate getCoordinate(int r, int c)
    {
        return gameBoard[r][c];
    }

    public boolean removeLine(Line l)
    {
        if(l.getNumInBoard() < 0 || l.getNumInBoard() >= numLoops) //includes invalid
        {
            System.err.println("remove line failed");
            return false;
        }
        int numRem = l.getNumInBoard();
        Line temp = loops[numRem];
        loops[numRem] = loops[numLoops-1];
        loops[numLoops-1] = temp; //not used later, but to see what was removed
        loops[numRem].setNumInBoard(numRem);
        numLoops--;
        if(numLoops == 0)
        {
            System.err.println("remove line failed");
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

    // public int getNumRows(){return rows;}
    // public int getNumCols(){return cols;}
    // public int getNumLoop(){return numLoops;}
    

    private boolean makeMerge()
    {
        int start = (int)(Math.random()*numLoops);
        for(int i=0; i<numLoops; i++)
        {
            if(loops[(i+start)%numLoops].mergeLoopsAnywhere())
            {
                return true;
            }
        }

        return false;
    }

    // merges loops until no more merges can be made
    // returns if a solutions was found. It is stored in loops[0]
    public boolean solveLoop()
    {
        while(makeMerge()){}

        return numLoops == 1;
    }

    // merges loops until 2 are left.
    // combines them with 1 arbitrary move forming a line
    public boolean solveLine()
    {
        while(numLoops>2)
        {
            if(!makeMerge())
            {
                return false;
            }
        }
        return loops[0].linearMerge((Loop)loops[1]);
    }

    // returns the answer the loop found
    // works if solve was succesful previously
    public Line answer(){
        return loops[0];
    }

    //prints the answer, starting at 0,0
    public void printAnswer(){
        System.out.println(answer());
    }

    @Override
    public String toString()
    {
        int boardVal[][] = new int[rows][cols];
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                //boardVal[r][c] = gameBoard[r][c].getNumInLine();
                boardVal[r][c] = gameBoard[r][c].getLine().getNumInBoard();
            }
        }
        return Line.arrToString(boardVal, true);
    }
}
