// 1 000 000 (1 million) solutions in about 20 seconds 8x8 board
// 1000x1000 board solved in ~20 seconds
// 1100 repeats out of 100 000 solutions for 8x8 board
// NO repeats on larger boards, 8x12 for 100 000 solutions
// fail rate <0.005 for 8x8
// fails: 47,321/10,000,000 for 8x8 board
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
        return board.getNumLines() == 1;
    }

    @Override
    public Loop answer()
    {
        return (Loop) board.answer();
    }
}
