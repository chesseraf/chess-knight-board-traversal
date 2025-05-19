package KnightPackage;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Solver {
    protected Board board;
    protected Statistic stat;     
    protected BoardCreator bc;
    private int maxConsecutiveFailsUntilStop = 10000;
    
    public Solver(int rows, int cols, BoardCreator bC)
    {
        bc = bC;
        board = bc.createBoard(rows, cols);
        stat = new Statistic(1);
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
        board = bc.createBoard(board.getRows(), board.getCols());
    }
    /**
     * 
     * @return an answer created by the solver if one was found and increase stat.solution by 1
     * Otherwise, return an incomplete answer, (not null) but not increment stat.solution
     */
    public Line makeSolution()
    {
        int orignalFails = stat.getFails();
        while(!solve() && stat.getFails() - orignalFails < maxConsecutiveFailsUntilStop)
        {
            stat.fail();
            restartSolver();     
        }
        if(stat.getFails() - orignalFails >= maxConsecutiveFailsUntilStop)
        {
            return answer(); // non-null object, stat.solut
        }
        stat.solution();
        if(!answer().valid())
            System.err.println("wrong");

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
        int prevSolutions;
        
        for(int i=0; i<num; i++)
        {
            prevSolutions = stat.getSolutions();
            int consecutiveRepeats = 0;
            while(hash.containsKey(makeSolution()))
            {
                Integer numRep = hash.get(answer());
                numRep++;
                hash.put(answer(), numRep);
                stat.repeat();
                if(numRep >= repeatThreshold)
                {
                    if(print)
                    {
                        System.out.println(answer());
                    }
                    System.err.println("repeat amount: "+numRep);
                }
                restartSolver();
                if(consecutiveRepeats>30)
                {
                    System.out.println("These are all the solutions found\n");
                    return stat;
                }
            }
            if(stat.getSolutions()==prevSolutions)
            {
                if(stat.getSolutions() == 0)
                    System.out.print("No such solutions were found\n");
                else
                    System.out.println("These are all the solutions found\n");
                return stat;
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
        int prevSolutions;
        
        for(int i=0; i<num; i++)
        {            
            prevSolutions = stat.getSolutions();
            int consecutiveRepeats = 0;
            while(hash.contains(makeSolution()))
            {
                stat.repeat();
                restartSolver();
                consecutiveRepeats++;
                if(consecutiveRepeats>30)
                {
                    System.out.println("These are all the solutions found\n");
                    return stat;
                }
            }
            if(stat.getSolutions()==prevSolutions)
            {
                if(stat.getSolutions() == 0)
                    System.out.print("No such solutions were found\n");
                else
                    System.out.println("These are all the solutions found\n");
                return stat;
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
        int prevSolutions;
        for(int i=0; i<num; i++)
        {
            prevSolutions = stat.getSolutions();
            restartSolver();
            makeSolution();
            if(stat.getSolutions()==prevSolutions)
            {
                if(stat.getSolutions() == 0)
                    System.out.print("No such solutions were found\n");
                else
                    System.out.println("These are all the solutions found\n");
                return stat;
            }
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
