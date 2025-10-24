package KnightPackage;

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
    public Line getLine(int num)
    {
        if (num >= 0 && num < numLines)
            return (lines[num]);
        else
        {
            return null;
        }
    }
    
    // Replaces one line with another
    // They must contain the same coordinates. The order and type of line may be different 
    public boolean update(Line toChange, Line replacement)
    {
        if (!contains(toChange))
            return false;
        int ind = toChange.getNumInBoard();
        lines[ind] = replacement;
        replacement.setNumInBoard(ind);
        replacement.setBoard(this);
        replacement.linkCoords();
        return true;
    }

    public boolean contains(Line l)
    {
        return (l.getBoard() == this && l.getNumInBoard() < numLines);
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

    // Creates a new Board that is split up into loops as specified 
    // The loops should cover all coordinates on the board exactly once
    public static Board createLoopedBoard(int rs, int cs, Loop[] loops)
    {
        Board b = new Board(rs, cs);
        b.numLines = loops.length;
        b.lines = new Line[loops.length];
        for(int i=0; i<loops.length; i++)
        {
            b.lines[i] = loops[i];
            b.lines[i].setNumInBoard(i);
            b.lines[i].setBoard(b);
            b.lines[i].linkCoords();
            for (Coordinate coord : loops[i].getCoords()) {
                b.gameBoard[coord.getRows()][coord.getCols()] = coord;
            }
        }
        b.linkCoordsToBoard();
        return b;
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
