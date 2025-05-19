package KnightPackage;

public class StartpointSolver extends Solver{
    private Coordinate targetStart;
    public StartpointSolver(int rows, int cols, Coordinate s, BoardCreator bc) {
        super(rows, cols, bc);
        targetStart = board.getCoordinate(s.getRows(), s.getCols());
    }

    @Override
    public boolean solve()
    {
        targetStart.getLine().linearize(targetStart, targetStart.nextInLine());
        targetStart.getLine().lockStart();
        while (board.makeMerge()) {}

        return board.answer().start().equals(targetStart) && board.getNumLines() == 1 && board.answer().valid();
    }

    @Override
    public void restartSolver() {
        super.restartSolver();
        targetStart = board.getCoordinate(targetStart.getRows(), targetStart.getCols());
    }

}
