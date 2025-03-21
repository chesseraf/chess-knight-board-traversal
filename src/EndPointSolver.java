public class EndPointSolver extends Solver {
    private Coordinate targetStart, targetEnd;
    public EndPointSolver(int rows, int cols, Coordinate s, Coordinate f) {
        super(rows, cols);
        targetStart = board.getCoordinate(s.getRows(), s.getCols());
        targetEnd = board.getCoordinate(f.getRows(), f.getCols());        
    }

    public boolean connectEnds()
    {
        //board.loops[cord.getLine().getNumInBoard()] = board.loops[cord.getLine().getNumInBoard()].linearize();
        //same colored square if the rows and cols all add up to an even
        if(sameColor(targetStart, targetEnd))
        {
            return false;
        }
        
        return false;
    }

    public boolean sameColor(Coordinate l, Coordinate r)
    {
        return ((l.getRows()+r.getRows()+l.getCols()+r.getCols())%2==0);
    }


    @Override
    public boolean solve() {
        if(!connectEnds())
        {
            return false;
        }
        while (board.makeMerge()) {}
        return board.getNumLoops() == 1;
    }


    @Override
    public Loop answer() {
        return (Loop) board.answer();
    }
}
