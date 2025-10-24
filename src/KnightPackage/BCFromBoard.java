package KnightPackage;
/*
 * Creats a board whose loops are almost a random bipartite matching of a board
 * It is made by obtaining a solution board, then pairing up consecutive coordinates in the solution into loops of size 2
 */
public class BCFromBoard implements BoardCreator {
    private Board baseBoard;
    private BoardCreator bc;
    public BCFromBoard(Board b)
    {
        baseBoard = b;
    }
    public BCFromBoard(BoardCreator bc) {
        this.bc = bc;
    }
    public BCFromBoard() { }
    
    @Override
    public Board createBoard(int r, int c) {
        if (baseBoard != null) {
            return createfromBoard(r, c, baseBoard);
        }
        if (bc != null) {
            return createfromBoard(r, c, bc.createBoard(r, c));
        }

        Solver s = new LoopSolver(r, c, new BC4x2());
        return createfromBoard(r, c, s.makeSolution().getBoard());
    }

    public Board createfromBoard(int r, int c, Board b)
    {        
        Loop loops[] = new Loop[r * c / 2];
        int l = 0;
        int locInLine = 0;
        Coordinate loopArr[] = new Coordinate[2];
        for (int i = 0; i< b.getNumLines(); i++) {
            for (Coordinate cord : b.getLine(i).getCoords()) {
                if (locInLine % 2 == 0) {
                    loopArr = new Coordinate[2];
                    loopArr[0] = cord;
                } else {
                    loopArr[1] = cord;
                    loops[l] = new Loop(loopArr);
                    l++;
                }
                locInLine++;
            }
        }
        Board fin = Board.createLoopedBoard(r, c, loops);
        return fin;
    }
}
