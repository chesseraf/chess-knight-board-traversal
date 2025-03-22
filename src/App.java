import java.util.Scanner;


public class App {
    public static final boolean RUN_DEFAULT = true;
    public static final int RANDOMIZE = 0;
    public static int numComparisons = 0;
    public static final int RAND_LINES = 0, RAND_LOOPS = 1, ENDPOINT_LINES = 2;
    public static void main(String[] args) throws Exception {
        
        int defaultSoleverType = ENDPOINT_LINES;
        int defaultr = 8, defaultc = 8;
        int defaultNumSolutions = 1;
        boolean defaultDisplayAllAns = false;
        boolean defaultEnsureDifferentAns = true;
        boolean defaultDisplayRepeatsOnly = true;
        boolean defaultDisplayProgress = true;
        Coordinate defaultSPoint = new Coordinate(0, 0), defaultEPoint = new Coordinate(2, 1);

        int solverType;
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

            System.out.println("Specify solver type: 0 for lines, 1 for loops, anything else for endpoint lines");
            solverType = getNumber(input, defaultSoleverType);

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
            for(int rC1=0; rC1<8;rC1++)
            for(int cC1=0; cC1<8;cC1++)
            for(int rC2=0; rC2<8;rC2++)
            for(int cC2=0; cC2<8;cC2++)
            {
                defaultSPoint = new Coordinate(rC1, cC1);

                defaultEPoint = new Coordinate(rC2, cC2);
                if(EndPointSolver.sameColor(defaultSPoint, defaultEPoint))
                    continue; 
            
            Statistic stat;
            Solver solver;
            solver = switch (solverType) {
                case RAND_LINES -> new LineSolver(r,c);
                case RAND_LOOPS -> new LoopSolver(r,c);
                default -> new EndPointSolver(r,c, defaultSPoint, defaultEPoint);
            };
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
            //System.out.println("Would you like to run again? [y/n]");
            
            }
            //running = getBool(input, false);
        
        }while(false);
    }

    // returns a number from the scanner
    // if a number was not found, returns the default
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

    // returns a boolean from the scanner as [y/n] by scanning a character
    // if the character was not y/n, returns default
    public static boolean getBool(Scanner scan, boolean def)
    {
        if(RUN_DEFAULT)
            return def;
        char ch = scan.next().charAt(0);
        if(ch == 'y' || ch == 'Y')
            return true;
        if(ch == 'n' || ch == 'N')
            return false;
        return def;
    }
}