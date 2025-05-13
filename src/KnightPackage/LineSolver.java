package KnightPackage;


import java.util.Iterator;

/**
 * SUCCESS RATE: 100%
 * 75 repeats of 100k solutions
 * 100k solutions in a few seconds
 * 1000 x 1000 board ~20 seconds
 */
public class LineSolver extends Solver
{
    public LineSolver(int rows, int cols, BoardCreator bc)
    {
        super(rows, cols, bc);
    }

     // merges loops until no more merges can be made
    // returns if a solutions was found. It is stored in loops[0]
    @Override
    public boolean solve()
    {
        while(board.makeMerge() && board.getNumLines()>2){}

        if(board.getNumLines()>2)
            return false;
        if(!answer().valid())
        {
            System.err.println("");
        }
        return finalLoopsIntoLine();
    }

    private  boolean finalLoopsIntoLine()
    {
        Loop small =(Loop)board.getLoop(0), big = (Loop)board.getLoop(1);
        if(small.getSize()>big.getSize())
        {
            Loop temp = small;
            small = big;
            big = temp;
        }
        Coordinate c;
        for(Iterator<Coordinate> it = small.coordIt(); it.hasNext(); )
        {
            c=it.next();
            for(Direction d:Direction.allDirections())
            {
                Coordinate moved = c.moveDir(d);
                if(moved.getLine() == big)
                {
                    //do the merge
                    Coordinate newCoords[]=new Coordinate[small.getSize()+big.getSize()];
                    // answer: small [split+1-split with wrapping] big[split - split-1 with wrapping]
                    int spliltSmall = c.getNumInLine();
                    int splitBig = moved.getNumInLine();
                    int i=0;
                    for(Coordinate s:small.coordIt(spliltSmall+1)) 
                    {
                        newCoords[i] = s;
                        i++;
                    }
                    Line.CordIt itBig = big.coordIt(splitBig);
                    for(Coordinate b:itBig)
                    {
                        newCoords[i] = b;
                        i++;
                    }
                    board.setAnswer(Line.create(newCoords, board));
                    board.answer().linkCoords();
                    return true;
                }
            }
        }
        System.out.println("ERROR MERGING INTO LINE, SHOULD NOT HAPPEN");
        return false;
    }
}
