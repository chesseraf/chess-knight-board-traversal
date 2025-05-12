public class SingleAnswer
{
   // 8x8 solution
   public static Line solve8x8line()
   {
        LineSolver ls = new LineSolver(8, 8, new BoardCreator.RandLessAccurateCreator());
        return ls.makeSolution();
   }
    
   public static Line solve8x8endPointLine(Coordinate s, Coordinate f)
   {
    if(s.isWhite()==f.isWhite() || s.getCols() <0 || s.getCols()>8 || s.getRows()<0 || s.getRows()>8 || f.getCols() <0 || f.getCols()>8 || f.getRows()<0 || f.getRows()>8)
    {
        System.err.println("Invalid coordinates");
        return null;
    }
    EndPointSolver ls = new EndPointSolver(8, 8, s, f, new BoardCreator.RandLessAccurateCreator());
    return ls.makeSolution();
   }

    public static Line solve8x8endPointLine(int sRow, int sCol, int fRow, int fCol)
    {
         return solve8x8endPointLine(new Coordinate(sRow, sCol), new Coordinate(fRow, fCol));
    }

    public static Line solve8x8loop()
    {
        LoopSolver ls = new LoopSolver(8, 8, new BoardCreator.RandLessAccurateCreator());
        return ls.makeSolution();
    }
}
