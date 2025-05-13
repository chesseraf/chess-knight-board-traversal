package KnightPackage;


/**
 * 100 000 solutions for 8x8 board in a few seconds
 * 0.4% fail rate
 * 577 repeats from 100k solutions
 */
public class LoopSolver extends Solver
{
    public LoopSolver(int rows, int cols, BoardCreator bc)
    {
        super(rows, cols, bc);
    }

     // merges loops until no more merges can be made
    // returns if a solutions was found. It is stored in loops[0]
    @Override
    public boolean solve()
    {
        while(board.makeMerge()){}
        return board.getNumLines() == 1;
    }

    @Override
    public Loop answer()
    {
        return (Loop) board.answer();
    }
}
