import java.util.HashSet;

public abstract class Solver {
    protected Board board;
    protected Statistic stat = new Statistic();     
    
    public Solver(int rows, int cols)
    {
        board = Board.make4x4BoardSolver(rows, cols);
        stat = new Statistic();
    }
    abstract public boolean solve();
    public Line answer()
    {
        return board.answer();
    }
    public Statistic getStat()
    {
        return stat;
    }
    public void restartSolver()
    {
        board = Board.make4x4BoardSolver(board.getRows(), board.getCols());
    }
    public Line makeSolution()
    {
        while(!solve())
        {
            restartSolver();
            stat.fail();
        }
        stat.solution();
        return answer();
    }

    public String answerStr()
    {
        return board.answer().toString();
    }

    @Override
    public String toString()
    {
        return (board.toString());
    }
    public Statistic makeAndPrintDifferentSolution(int num, boolean print, boolean displayProgress)
    {
        HashSet<Line> hash = new HashSet<>((int)(num/0.75)+1);
        stat.repeatsUsed();
        stat.solutionsUsed();
        
        for(int i=0; i<num; i++)
        {
            restartSolver();
            while(hash.contains(makeSolution()))
            {
                stat.repeat();
            }
            hash.add(answer());
            
            if(print)
                System.out.println(answerStr());
            else if(displayProgress)
            {
                if(i%10000 == 0 && i!=0)
                {
                    System.out.print("Progress: "+(i*100/num)+"%");
                    System.out.println("  Reapeats: "+stat.getRepeats()+"  fails: "+ stat.getFails());                   
                }
            }
        }
        return stat;
    }
    public Statistic makeAndPrintSolution(int num, boolean print, boolean displayProgress)
    {
        stat.solutionsUsed();
        
        for(int i=0; i<num; i++)
        {
            restartSolver();
            makeSolution();
            if(print)
                System.out.println(answerStr());
            else if(displayProgress)
            {
                if(i%10000 == 0 && i!=0)
                {
                    System.out.print("Progress: "+(i*100/num)+"%");
                    System.out.println("  fails: "+ stat.getFails());                   
                }
            }
        }
        return stat;
    }

    
}
