



public class Coordinate implements Comparable<Coordinate> {
    private final int r,c;
    private Line line;
    private int numInLoop;
    private Board board;

    public Coordinate(int row, int col, Line l, int num)
    {
        r = row;
        c = col;
        line = l;
        numInLoop = num;
    }

    public Coordinate(int row, int col)
    {
        this(row, col, null, -1);
    }

    @Override
    public String toString()
    {
        return ("("+r+", "+c+")");
    }

    public int getRows(){return r;}
    public int getCols(){return c;}
    public Line getLine(){return line;}
    public int getNumInLine(){return numInLoop;}
    public void setLine(Line l, int num)
    {
        line = l;
        numInLoop = num;
    }
    public void setBoard(Board b)
    {
        board = b;
    }
    
    //returns whether the coordinates are adjacent, based on a knight move
    public boolean adjacent(Coordinate other)
    {
        return(Math.abs((r-other.r)*(c-other.c)) == 2);
    }

    // A coordinate with a smaller row is smaller
    // A coordinte with smaller column is smaller if rows are equal
    // If both are equal, they are equal
    @Override
    public int compareTo(Coordinate other)
    {
        if(r == other.r)
        {
            return c - other.c;
        }
        return r - other.r;
    }

    //returns the direction in which the other coordinate is adjacent
    //they should be adjacent
    public Direction adjecentDir(Coordinate other)
    {
        return new Direction(r>other.r, c>other.c, Math.abs(r-other.r) > Math.abs(c-other.c));
    }

    public Board getBoard()
    {
        return board;
    }

    //returns the coordinate that is this coordinate moved once in the given direction
    //the coordinate is on the same board if possible
    //if out of bounds, returns coordinate without a board but with corresponding row and col
    public Coordinate moveDir(Direction dir){
        int row = r + dir.getUp();
        int col = c + dir.getRight();
        if(board!=null && row >=0 && col >=0 && row <board.getRows() && col< board.getCols())
        {
            return board.getCoordinate(row, col);
        }
        return new Coordinate(row, col);
    }

    public boolean consecutiveInLoop(Coordinate other)
    {
        return (line != null && line == other.line && (line instanceof Loop) && Math.abs((numInLoop - other.numInLoop)%line.getSize())==1);
    }

    
    public boolean equals(Coordinate other)
    {
        return r == other.r && c==other.c;
    }
}
