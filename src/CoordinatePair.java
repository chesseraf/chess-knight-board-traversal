
public class CoordinatePair {
    private final Coordinate a, b;
    public CoordinatePair(Coordinate f, Coordinate s)
    {
        a = f;
        b = s;
    }
    public Coordinate first(){
        return a;
    }
    public Coordinate second()
    {
        return b;
    }
    public boolean adjacent()
    {
        return a.adjacent(b);
    }
    public boolean forwardOrdered()
    {
        return ((b.getNumInLine()-a.getNumInLine()+getLine().getSize())%getLine().getSize())==1;
    }
    public CoordinatePair moveDir(Direction dir)
    {
        return new CoordinatePair(a.moveDir(dir), b.moveDir(dir));
    }
    public boolean consecutiveInLoop()
    {
        return a.consecutiveInLoop(b);
    }
    public boolean adjacentForward(CoordinatePair other)
    {
        return a.adjacent(other.a) && b.adjacent(other.b);
    }
    public boolean adjacentBackwards(CoordinatePair other)
    {
        return a.adjacent(other.b) && b.adjacent(other.a);
    }
    public boolean adjacent(CoordinatePair other)
    {
        return (adjacentBackwards(other) || adjacentForward(other));
    }
    //returns first's line
    public Line getLine()
    {
        return a.getLine();
    }

    @Override
    public String toString()
    {
        return a.toString()+" "+b.toString();
    }

}
