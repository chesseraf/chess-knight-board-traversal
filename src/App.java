import java.util.HashSet;
import java.util.Scanner;


public class App {
    public static final boolean RUN_DEFAULT = false;
    public static int numComparisons = 0;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello!");
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the board row count (mulitple of 4, default 8): ");
        int r = getNumber(input, 8);
        if(r<8) r=8;
        System.out.print("Enter the board col count (mulitple of 4, default 8): ");
        int c = getNumber(input, 12);
        if(c<8) c=8;
        System.out.print("Enter the number of solutions: ");
        int numSolutions = getNumber(input, 10);
        if(numSolutions < 1) numSolutions = 1;
        System.out.print("Should the answers be printed? [y/n] ");
        boolean displayAns = getBool(input, c*r<1000);

        System.out.print("Ensure all answers are different? [y/n] ");
        boolean ensureDifferentAns = getBool(input, true);
        
        System.out.println("");
        int f;
        if(ensureDifferentAns)
        {
            f=makeAndPrintDifferentSolution(numSolutions, displayAns, r,c);
            System.out.println("Repeats: " +f+"/"+numSolutions);
        }
        else
        {
            f = makeAndPrintSolution(numSolutions, displayAns, r,c);
            System.out.println("fails: " +f+"/"+numSolutions);
        }
            
        // 1 000 000 (1 million) solutions in about 20 seconds
        // there are 0 repeats for 830 000 solutions for 8x8 board
        // 1000x1000 board solved in ~20 seconds
        // any repeats from past runs were due to improper hashing and there were likely never any repeats
    }

    //fail rate 0.005 for 8x8 when merging loop
    

    //prints the solutions if print is true
    // finds num solutions to boards with numRows rows and numCols cols
    // returns the number of failed boards while finding solutions
    static int makeAndPrintSolution(int num, boolean print, int numRows, int numCols)
    {
        int fails = 0;
        for(int i=0; i<num; i++)
        {
            Board board = Board.make4x4BoardSolver(numRows, numCols);

            // solve the board until a solution is found
            while(!board.solveLoop())
            {
                board = Board.make4x4BoardSolver(numRows, numCols);
                fails++;
            }

            // if the solution is invaid, algorithm went wrong somewhere and quits
            if(!board.answer().valid())
            {
                board.printAnswer();
                System.err.println("invalid");
                System.exit(1);
            }
            if(print)
            {
                board.printAnswer();
            }
                
        }
        return fails;
    }

    //returns the number of solutions found that were repeats
    static int makeAndPrintDifferentSolution(int num, boolean print, int numRows, int numCols)
    {
        HashSet<Line> hash = new HashSet<>((int)(num/0.75)+1);
        int repeats = 0;
        int fails = 0;
        
        for(int i=0; i<num; i++)
        {
            Board board = Board.make4x4BoardSolver(numRows, numCols);
            while(!board.solveLoop() || hash.contains(board.answer()))
            {
                if(hash.contains(board.answer()))
                {
                    repeats++;
                }
                else
                {
                    fails++;
                }
                //occasionally the merge sequence does not result in a solved board
                board = Board.make4x4BoardSolver(numRows, numCols);
            } 

            hash.add(board.answer());
            if(print)
                board.printAnswer();
            else
            {
                if(i%10000 == 0)
                {
                    System.out.print("Progress: "+(i*100/num)+"%");
                    System.out.println("  Reapeats: "+repeats+"  fails: "+ fails + "  num comparisons: "+numComparisons);                   
                }
            }
        }
        return repeats;
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