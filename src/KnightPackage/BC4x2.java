package KnightPackage;

//15% fail rate for loops on 8x8 board
class BC4x2 implements BoardCreator
{
    @Override
    public Board createBoard(int r, int c)
    {
        Board orig = (new BC4x4()).createBoard(r, c);
        int numsLinesOg = orig.getNumLines();
        Loop newLines[] = new Loop[numsLinesOg*2];
        int numL=0;
        for(int i = 0; i<numsLinesOg; i++)
        {
            Line l = orig.getLine(i);
            boolean rand = Math.random()>0.5;
            if(rand)
            {
                Coordinate arr1[] = {l.getCoord(0), l.getCoord(1)};
                newLines[numL] = new Loop(arr1);  
                numL++;
                Coordinate arr2[] = {l.getCoord(2), l.getCoord(3)};
                newLines[numL] = new Loop(arr2); 
                numL++;
            }
            else
            {
                Coordinate arr1[] = {l.getCoord(3), l.getCoord(0)};
                newLines[numL] = new Loop(arr1); 
                numL++;
                Coordinate arr2[] = {l.getCoord(1), l.getCoord(2)};
                newLines[numL] = new Loop(arr2); 
                numL++;
            }
        }

        Board fin = Board.createLoopedBoard(r, c, newLines);
        fin.getCoordinate(0, 0).getLine().mergeLoopsAnywhere();
        fin.getCoordinate(r-1, c-1).getLine().mergeLoopsAnywhere();
        fin.getCoordinate(r-1, 0).getLine().mergeLoopsAnywhere();
        fin.getCoordinate(0, c-1).getLine().mergeLoopsAnywhere();
        return fin;
    }
}
