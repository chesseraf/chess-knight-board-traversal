import java.util.HashMap;
import java.util.HashSet;

public abstract class Solver {
    protected Board board;
    protected Statistic stat;     
    
    public Solver(int rows, int cols)
    {
        board = Board.make4x4BoardSolver(rows, cols);
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
            stat.fail();
            restartSolver();     
        }
        stat.solution();
        if(!answer().valid())
            System.err.println("wrone");

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
    public Statistic makeAndPrintRepeatedSolutions(int num, boolean print, boolean displayProgress)
    {
        HashMap<Line, Integer> hash = new HashMap<>((int)(num/0.75)+1);
        stat = new Statistic(num);
        stat.trackRepeats();
        stat.trackSolutions();
        stat.trackFails();
        int repeatThreshold = 2;

        
        for(int i=0; i<num; i++)
        {
            
            while(hash.containsKey(makeSolution()))
            {
                Integer numRep = hash.get(answer());
                numRep++;
                hash.put(answer(), numRep);
                stat.repeat();
                if(numRep >= repeatThreshold)
                {
                    if(numRep > repeatThreshold)
                    {
                        System.out.println("***");
                    }
                    if(print)
                    {
                        System.out.println(answer());
                    }
                    System.err.println("repeat amount: "+numRep);
                }
                restartSolver();
            }
            hash.put(answer(),1);
            if(displayProgress)
            {
                displayProgress();
            }
            restartSolver();
        }
        return stat;
    }
    
    public Statistic makeAndPrintDifferentSolution(int num, boolean print, boolean displayProgress)
    {
        HashSet<Line> hash = new HashSet<>((int)(num/0.75)+1);
        stat = new Statistic(num);
        stat.trackRepeats();
        stat.trackSolutions();
        stat.trackFails();
        
        for(int i=0; i<num; i++)
        {
            
            while(hash.contains(makeSolution()))
            {
                stat.repeat();
                restartSolver();
            }
            hash.add(answer());
            
            if(print)
                System.out.println(answer()+"\n"+answer().start()+"->"+answer().end());
            else if(displayProgress)
            {
                displayProgress();
            }
            restartSolver();
        }
        return stat;
    }
    public void displayProgress()
    {
        if(stat.getSolutions()%20000 == 0 && stat.getSolutions()>0)
        {
            System.out.println(stat);
        }
    }
    public Statistic makeAndPrintSolution(int num, boolean print, boolean displayProgress)
    {
        stat = new Statistic(num);
        stat.trackSolutions();
        stat.trackFails();
        
        for(int i=0; i<num; i++)
        {
            restartSolver();
            makeSolution();
            if(print)
                System.out.println(answer());
            else if(displayProgress)
            {
                displayProgress();
            }
        }
        return stat;
    }

    
}
