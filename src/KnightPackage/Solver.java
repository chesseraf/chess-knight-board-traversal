package KnightPackage;
import KnightPackage.Statistic.StatType;
import java.util.HashMap;

public abstract class Solver {
    protected Board board;
    protected Statistic stat;     
    protected BoardCreator bc;
    private int maxConsecutiveFailsUntilStop = 800;
    private static final int MAX_CONSECUTIVE_REPEATS_UNTIL_STOP = 30;
    public enum PrintMode{
        NONE,
        REPEATS,
        ALL
    }
    
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
    public void setMaxConsecutiveFailsUntilStop(int max)
    {
        maxConsecutiveFailsUntilStop = max;
    }
    public int getMaxConsecutiveFailsUntilStop()
    {
        return maxConsecutiveFailsUntilStop;
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
        int originalFails = stat.get(StatType.FAILS);
        while(!solve() && stat.get(StatType.FAILS) - originalFails < maxConsecutiveFailsUntilStop)
        {
            stat.inc(StatType.FAILS);
            restartSolver();     
        }
        if(stat.get(StatType.FAILS) - originalFails >= maxConsecutiveFailsUntilStop)
        {
            return answer(); // non-null object, stat.solut
        }
        stat.inc(StatType.SOLUTIONS);
        if(!answer().valid()) {
            System.err.println("Detected solution is invalid. " + solverDebugMsg());
        }

        return answer();
    }

    public String solverDebugMsg()
    {
        return " Solver type: "+this.getClass().getName()+" Board creator type: "+bc.getClass().getName()+
               " Board size: "+board.getRows()+"x"+board.getCols();
    }

    @Override
    public String toString()
    {
        return (board.toString());
    }
    /**
     * 
     * @param numAns number of answers to find
     * @param pMode printing mode
     * @param displayProgress whether to display progress periodically
     * @param ensureDifferentAns whether to ensure all answers found are different
     * @return statistics about the solving process
     */
    public Statistic manySolutions(int numAns, PrintMode pMode, boolean displayProgress, boolean ensureDifferentAns)
    {
        // If the past answers are not needed, dont store them for efficiency
        final boolean storeAns = (pMode == PrintMode.REPEATS || ensureDifferentAns);
        HashMap<Line, Integer> foundAnswers =new HashMap<>(storeAns ? (int)(numAns/0.75)+1 : 0);
        if (pMode == null) {
            pMode = PrintMode.NONE;
        }
        stat = new Statistic(numAns); 
        if (storeAns) {
            stat.track(StatType.REPEATS);
        }
        stat.track(StatType.SOLUTIONS);
        stat.track(StatType.FAILS);
        
        for(int i=0; i<numAns; i++)
        {
            int prevSolutions = stat.get(StatType.SOLUTIONS);
            int consecutiveRepeats = 0;
            makeSolution();
            if (ensureDifferentAns) {
                while (foundAnswers.containsKey(answer()))
                {
                    stat.inc(StatType.REPEATS);
                    if (pMode == PrintMode.REPEATS)
                    {
                        System.out.println("Num: " + foundAnswers.get(answer()) + " answer:\n" + answer());
                    }
                    restartSolver();
                    consecutiveRepeats++;
                    if(consecutiveRepeats>MAX_CONSECUTIVE_REPEATS_UNTIL_STOP)
                    {
                        System.out.println("These are all the solutions found\n");
                        return stat;
                    }
                    makeSolution();
                }
            }
            if(stat.get(StatType.SOLUTIONS)==prevSolutions)  // Failed to find a solution
            {
                if(stat.get(StatType.SOLUTIONS) == 0)
                    System.out.print("No such solutions were found\n");
                else
                    System.out.println("These are all the solutions found\n");
                return stat;
            }
            boolean toPrint = (pMode == PrintMode.ALL);
            if (storeAns) {
                if (foundAnswers.containsKey(answer())) {
                    foundAnswers.put(answer(), foundAnswers.get(answer()) + 1);
                    toPrint |= (pMode == PrintMode.REPEATS);
                } else {
                    foundAnswers.put(answer(), 1);
                }
            }
            if (toPrint) {
                System.out.println(answer()+"\n"+answer().start()+"->"+answer().end());
            }
            if(displayProgress) {
                displayProgress();
            }
            restartSolver();
        }
        return stat;
    }
    public void displayProgress()
    {
        if(stat.get(StatType.SOLUTIONS)%20000 == 0 && stat.get(StatType.SOLUTIONS) > 0)
        {
            System.out.println(stat);
        }
    } 
}
