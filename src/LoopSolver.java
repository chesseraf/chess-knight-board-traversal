public class LoopSolver extends Solver
{
    public LoopSolver(int rows, int cols)
    {
        super(rows, cols);
    }

     // merges loops until no more merges can be made
    // returns if a solutions was found. It is stored in loops[0]
    @Override
    public boolean solve()
    {
        while(board.makeMerge()){}
        return board.getNumLoops() == 1;
    }

    @Override
    public Loop answer()
    {
        return (Loop) board.answer();
    }
}
