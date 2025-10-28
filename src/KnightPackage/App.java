// Main application file for the Knight's Tour solver
package KnightPackage;

import KnightPackage.Solver.PrintMode;
import java.util.Scanner;

public class App {
    public static final boolean RUN_DEFAULT = false;
    public static int numComparisons = 0;  // for performance tracking
    public static final int RAND_LINES = 0, RAND_LOOPS = 1, ENDPOINT_LINES = 2, STARTPOINT_LINES = 3;
    public static void main(String[] args){
        
        int defaultSolverType = RAND_LINES;
        int defaultr = 8, defaultc = 8;
        int defaultNumSolutions = 10;
        int defaultDisplayAllAns = 0;
        boolean defaultEnsureDifferentAns = true;
        boolean defaultDisplayProgress = true;
        Coordinate startC = new Coordinate(1,2), endC = new Coordinate(3,3); //for enpoint lines
        BoardCreator bc;

        int defaultRC = 0;
        // Default row and col for start and end coordinates for user specified endpoints
        // Provides an answer when all parameters are default
        int drC1 = 0, dcC1 = 0, drC2 = 3, dcC2 = 0;
        int solverType;
        boolean running;
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the chess knight's tour solver!");
        System.out.println("The goal is to reach each point on a chess board exactly once without any repeats using knight moves.");
        System.out.println("You can specify the dimensions of the board, the number of solutions, and the type of solutions.");
        System.out.println("You can specify if you want all the solutions to be printed, only have the repeated solutions printed, or none printed.");
        System.err.println("You can choose the endpoints of the solutions if you use the endpoint solver.");
        System.out.println("");
        do 
        {
            // Start by getting user input
            System.out.print("Enter the board row count (multiple of 4, default 8): ");
            int r = getNumber(input, defaultr) / 4 * 4;
            if(r<4) 
                r=4;

            System.out.print("Enter the board col count (multiple of 4, default 8): ");
            int c = getNumber(input, defaultc) / 4 * 4;
            if (c < 4) 
                c = 4;
            if (r == 4 && c == 4) 
                c = 8;

            System.out.print("Enter the number of solutions: ");
            int numSolutions = getNumber(input, defaultNumSolutions);
            if (numSolutions < 1)
                numSolutions = 1;

            System.out.print("Enter 0 to print all answers, 1 to only print repeats, or 2 to not print solutions: ");
            int displayAns = getNumber(input, defaultDisplayAllAns);
            if (displayAns < 0 || displayAns > 2)
                displayAns = defaultDisplayAllAns;
            PrintMode printMode = switch (displayAns) {
                case 2 -> PrintMode.NONE;
                case 1 -> PrintMode.REPEATS;
                default -> PrintMode.ALL;
            };
            
            System.out.println("Specify solver type: 0 for lines, 1 for loops, 2 for lines with specified endpoints, 3 for startpoint solver");
            solverType = getNumber(input, defaultSolverType);
            if (solverType < 0 || solverType > 3)
                solverType = defaultSolverType;
            
            System.out.print("Ensure all answers are different? [y/n] ");
            boolean ensureDifferentAns = getBool(input, defaultEnsureDifferentAns);

            System.out.print("0 for original solver, 1 for a bit extra randomness, 2 for highest randomness(may not find an answer): ");
            int bcType = getNumber(input, 1);
            bc = switch (bcType) {
                case 2 -> new BCFromBoard();
                case 1 -> new BC4x2();
                default -> new BC4x4();
            };

            boolean displayProgress = false;
            if (numSolutions >= 50000 && printMode != PrintMode.ALL)
            {
                System.out.print("Show % progress and statistics? [y/n] ");
                displayProgress = getBool(input, defaultDisplayProgress);
            }

            if (solverType == ENDPOINT_LINES && !RUN_DEFAULT)
            {
                int rC1, cC1, rC2, cC2;
                System.out.println("Enter the end points' start row, start column, end row, and end column.");
                System.out.println("(0,0) is the top left corner. Separate with spaces, e.g. '3 2 0 6'");
                rC1 = getNumber(input, drC1);
                if (rC1 < 0 || rC1 >= r)
                    rC1 = defaultRC;

                cC1 = getNumber(input, dcC1);
                if (cC1 < 0 || cC1 >= c)
                    cC1 = defaultRC;

                rC2 = getNumber(input, drC2);
                if (rC2 < 0 || rC2 >= r)
                    rC2 = defaultRC;

                cC2 = getNumber(input, dcC2);
                if (cC2 < 0 || cC2 >= c)
                    cC2 = defaultRC;
                startC = new Coordinate(rC1, cC1);
                endC = new Coordinate(rC2, cC2);
                
                if (EndPointSolver.sameColor(startC, endC))
                {
                    if (cC2 == 0)
                        cC2++;
                    else 
                        cC2--;
                    endC = new Coordinate(rC2, cC2);
                    System.out.println("Those points were of the same color");
                    System.out.println("instead doing: " + startC + " -> " + endC);
                }
            }
            if (solverType == STARTPOINT_LINES)
            {
                int rC1, cC1;
                System.out.println("Enter the end points' start row, start column.");
                System.out.println("(0,0) is the top left corner. Separate with spaces, e.g. '1 2'");
                rC1 = getNumber(input, defaultRC);
                if (rC1 < 0 || rC1 >= r)
                    rC1 = defaultRC;

                cC1 = getNumber(input, defaultRC);
                if (cC1 < 0 || cC1 >= c)
                    cC1 = defaultRC;
                if (!RUN_DEFAULT)
                    startC = new Coordinate(rC1, cC1);
            }
            Statistic stat;
            Solver solver = switch (solverType) {
                    case RAND_LINES -> new LineSolver(r,c, bc);
                    case RAND_LOOPS -> new LoopSolver(r,c, bc);
                    case ENDPOINT_LINES -> new EndPointSolver(r,c, startC, endC, bc);
                    case STARTPOINT_LINES -> new StartpointSolver(r, c, startC, bc);
                    default -> new LoopSolver(r,c, bc);
            };
            stat = solver.manySolutions(numSolutions, printMode, displayProgress, ensureDifferentAns);

            System.out.println(stat);
            System.out.println("Would you like to run again? [y/n]");            
            running = getBool(input, false);
        
        } while (running);
    }

    // returns a number from the scanner
    // if a number was not found, returns the default
    static int getNumber(Scanner scan, int def)
    {
        if (RUN_DEFAULT)
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
        if (RUN_DEFAULT)
            return def;
        char ch = scan.next().charAt(0);
        if (ch == 'y' || ch == 'Y')
            return true;
        if (ch == 'n' || ch == 'N')
            return false;
        return def;
    }
}