import java.util.HashMap;
import java.util.Scanner;


public class App {
    public static final boolean RUN_DEFAULT = false;
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
            
        
        
        // 1 000 000 (1 million) solutions in a few seconds for 8x8 board, no repeat checks
        // 100k solutions, 875 repeats along the way 8x8 before 2nd randomization
        // 435 repeats after 2nd randomization
    }

    //fail rate 0.0028 for 8x8 when merging loop
    // 0.002 for 8x12 board
    // ~20 min for 1000 x 1000 board

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
        HashMap<Integer,Loop> hash = new HashMap<>();
        int repeats = 0;
        int fails = 0;
        int percentDone = 0;
        
        for(int i=0; i<num; i++)
        {
            Board board = Board.make4x4BoardSolver(numRows, numCols);
            while(!board.solveLoop() || hash.containsValue(board.answer()))
            {
                if(hash.containsValue(board.answer()))
                {
                    //System.out.println(i);
                    repeats+=1;
                    //System.exit(1);
                }
                else
                {
                    fails++;
                }
                //occasionally the merge sequence does not result in a solved board
                board = Board.make4x4BoardSolver(numRows, numCols);
            }

            hash.put(board.answer().hashCode(), board.answer());
            if(print)
                board.printAnswer();
            else
            {
                if((i*100)/num > percentDone&&num>5000)
                {
                    percentDone = i*100/num;
                    System.out.println("%"+percentDone+"  Reapeats: "+repeats+"  fails: "+ fails);                   
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