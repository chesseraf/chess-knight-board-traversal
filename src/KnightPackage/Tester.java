package KnightPackage;
public class Tester {
    public static void main(String[] args) {
        // test the singleAnswer class methods
        System.out.println("Testing various solvers with different board creators. \nIf nothing else is printed, all tests passed.");
        BoardCreator bcs[] = {new BC4x2(), new BC4x4()};
        for(BoardCreator bc : bcs)
        {
            Solver s = new LoopSolver(8, 8, bc);
            testSolver(s, bc);
            s = new LineSolver(8, 8, bc);
            testSolver(s, bc);
            s = new EndPointSolver(8, 8, new Coordinate(0,0), new Coordinate(7,6), bc);
            testSolver(s, bc);
            s = new StartpointSolver(8, 8, new Coordinate(0,0), bc);
            testSolver(s, bc);
        }
    }

    public static void testSolver(Solver s, BoardCreator bc)
    {
        for (int i=0; i<100; i++)
        {
            s.makeSolution();
            if (!s.answer().valid())
            {
                System.err.println("Solver produced invalid solution with board creator: " + bc.getClass().getName());
                return;
            }
            s.restartSolver();
        }
    }
}
