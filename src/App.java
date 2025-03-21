import java.util.Scanner;


public class App {
    public static final boolean RUN_DEFAULT = false;
    public static int numComparisons = 0;
    public static final int RAND_LINES = 0, RAND_LOOPS = 1, ENDPOINT_LINES = 2;
    public static void main(String[] args) throws Exception {
        int solverType;
        int defaultr = 8, defaultc = 8;
        int defaultNumSolutions = 10;
        boolean defaultDisplayAllAns = false;
        boolean defaultEnsureDifferentAns = true;
        boolean defaultDisplayRepeatsOnly = true;
        boolean defaultDisplayProgress = true;

        boolean running;
        do 
        {
            System.out.println("Hello!");
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the board row count (mulitple of 4, default 8): ");
            int r = getNumber(input, defaultr);
            if(r<8) r=8;
            System.out.print("Enter the board col count (mulitple of 4, default 8): ");
            int c = getNumber(input, defaultc);
            if(c<8) c=8;
            System.out.print("Enter the number of solutions: ");
            int numSolutions = getNumber(input, defaultNumSolutions);
            if(numSolutions < 1) numSolutions = 1;
            System.out.print("Should the answers be printed? [y/n] ");
            boolean displayAns = getBool(input, defaultDisplayAllAns);

            System.out.println("Specify solver type: 0 for lines, 1 for loops");
            solverType = getNumber(input, RAND_LOOPS);

            System.out.print("Ensure all answers are different? [y/n] ");
            boolean ensureDifferentAns = getBool(input, defaultEnsureDifferentAns);

            boolean displayRepeatsOnly = false;
            if(ensureDifferentAns && !displayAns)
            {
                System.out.print("Display only repeats? [y/n] ");
                displayRepeatsOnly = getBool(input, defaultDisplayRepeatsOnly);
            }

            boolean displayProgress = false;
            if(numSolutions >= 50000 && !displayAns)
            {
                System.out.print("Show % progress and statistics? [y/n] ");
                displayProgress = getBool(input, defaultDisplayProgress);
            }

            System.out.println("");
            Statistic stat;
            Solver solver;
            if(solverType == RAND_LINES) 
                solver = new LineSolver(r,c);
            else if(solverType == RAND_LOOPS)
                solver = new LoopSolver(r,c);
            else
                solver = new LoopSolver(r,c);
            if(ensureDifferentAns)
            {
                if(displayRepeatsOnly)
                    stat=solver.makeAndPrintRepeatedSolutions(numSolutions, displayAns, displayProgress);
                else
                    stat=solver.makeAndPrintDifferentSolution(numSolutions, displayAns, displayProgress);
            }
            else
            {
                stat = solver.makeAndPrintSolution(numSolutions, displayAns,displayProgress);
            }
            System.out.println(stat);
            System.out.println("Would you like to run again? [y/n]");
            running = getBool(input, true);
        }while(running);
    }

    static int getNumber(Scanner scan, int def)
    {
        if(RUN_DEFAULT)
            return def;
        try {
            return scan.nextInt();
        } catch (Exception e) {
            scan.nextLine();
            return def;
        }
    }
    public static boolean getBool(Scanner scan, boolean def)
    {
        if(RUN_DEFAULT)
            return def;
        char ch = scan.next().charAt(0);
        return (ch == 'y' || ch == 'Y');
    }
}