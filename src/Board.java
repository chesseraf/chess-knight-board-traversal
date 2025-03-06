public class Board {
    private Loop loops[];
    private int numLoops;
    private int rows, cols;

    public Board()
    {
        this(8,8);
    }


    //rs and cs must be multiples of 4
    //they are rounded down to a multiple of 4
    //the board is rs x cs
    public Board(int rs, int cs)
    {
        int l=0;
        rows = rs/4*4;
        cols = cs/4*4;
        numLoops = rows*cols/4;
        loops = new Loop[numLoops];
        
        for(int r=0; r<rows/4; r++)
        {
            for(int c=0; c<cols/4; c++)
            {
                for(int corner=0; corner<2; corner++)
                {
                    for(int left = 0; left<2;left++)
                    {
                        //all starter loops covererd
                        loops[l] = makeLoop4x4Square(c, r, corner==0,left==0);
                        l++;
                    }
                }
            }
        }
    }

    //create a loop in 1 of the 4x4 squares
    // it can be a corner loop or not, 
    // it can have its bottom most square on the left or right half of the quadrant
    //col4 is 0 for a-d, 1 for e-h,... x-coord of 4x4 square where loop is made
    //row4 y-coord of 4x4 square where loop is made
    public Loop makeLoop4x4Square(int col4, int row4, boolean leftHalf, boolean isCorner)
    {
        int x1Coord = col4*4;
        int y1Coord = row4*4;
        Coordinate coords[] = new Coordinate[4];
        if(!leftHalf){
            if(isCorner)
            {
                //right bottom corner start
                //like d1, b2, a4, c3
                x1Coord += 3;
                coords[0] = new Coordinate(x1Coord, y1Coord);
                coords[1] = coords[0].moveDir(new Direction(false, true, true));
                coords[2] = coords[1].moveDir(new Direction(false, true, false));
                coords[3] = coords[2].moveDir(new Direction(true, false, true));                
            }
            else
            {
                //start like c1, a2, b4, d3
                x1Coord += 2;
                coords[0] = new Coordinate(x1Coord, y1Coord);
                coords[1] = coords[0].moveDir(new Direction(false, true, true));
                coords[2] = coords[1].moveDir(new Direction(true, true, false));
                coords[3] = coords[2].moveDir(new Direction(true, false, true));    
            }
        }
        else
        {
            if(isCorner)
            {
                // ex a1, b3, d4, c2
                coords[0] = new Coordinate(x1Coord, y1Coord);
                coords[1] = coords[0].moveDir(new Direction(true, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(false, false, false));    
            }
            else
            {
                //ex b1, a3, c4, d2
                x1Coord += 1;
                coords[0] = new Coordinate(x1Coord, y1Coord);
                coords[1] = coords[0].moveDir(new Direction(false, true, false));
                coords[2] = coords[1].moveDir(new Direction(true, true, true));
                coords[3] = coords[2].moveDir(new Direction(true, false, false));    
            }
            
        }
        Loop finLoop = new Loop(coords);
        finLoop.updateMinMax();
        return finLoop;
    }

    // public int getNumRows(){return rows;}
    // public int getNumCols(){return cols;}
    // public int getNumLoop(){return numLoops;}
    

    // check all possible pairs of loops, attempting to merge them
    // stop once a merge happens
    // return if a merge happend
    public boolean makeMerge()
    {
        int startI = (int)(Math.random()*numLoops);
        int startJ = (int)(Math.random()*numLoops);
        int effI, effJ;
        
        for(int i=0; i<numLoops; i++)
        {
            for(int j=0;j<numLoops;j++)
            {
                effI = (i+startI)%numLoops;
                effJ = (j+startJ)%numLoops;
                if(effI != effJ)
                    if(loops[effI].mergeLoops(loops[effJ]))
                    {
                        loops[effJ] = loops[numLoops-1];
                        numLoops--;
                        // if(numLoops%300 == 0)
                        //     System.out.println(numLoops);
                        return true;
                }
            }
        }
        return false;
    }

    // merges loops until no more merges can be made
    // returns if a solutions was found. It is stored in loops[0]
    public boolean solve()
    {
        while(makeMerge()){}

        return numLoops == 1;
    }

    // returns the answer the loop found
    // works if solve was succesful previously
    public Loop answer(){
        return loops[0];
    }

    //prints the answer, starting at 0,0
    public void printAnswer(){
        loops[0].printLoopStart00(rows, cols);
    }
}
