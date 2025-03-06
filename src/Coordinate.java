public class Coordinate {
    private final int r,c;

    public Coordinate(int row, int col)
    {
        r = row;
        c = col;
    }
    public int getRows(){return r;}
    public int getCols(){return c;}
    
    //returns whether the coordinates are adjacent, based on a knight move
    public boolean adjacent(Coordinate other)
    {
        return(Math.abs((r-other.r)*(c-other.c)) == 2);
    }

    //returns the direction in which the other coordinate is adjacent
    //they should be adjacent
    public Direction adjecentDir(Coordinate other)
    {
        return new Direction(r>other.r, c>other.c, Math.abs(r-other.r) > Math.abs(c-other.c));
    }

    //returns the coordinate that is this coordinate moved once in the given direction
    public Coordinate moveDir(Direction dir){
        return new Coordinate(r + dir.getRight(), c + dir.getUp());
    }

    
    public boolean equals(Coordinate other)
    {
        return r == other.r && c==other.c;
    }
}
