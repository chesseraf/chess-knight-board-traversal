import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 100 000 solutions in a few seconds for 8x8 board
 * 0.4% fail rate
 * 2.8k reapeats of 100k solutions
 * 1000 x 1000 board in ~20 seconds
 */

public class EndPointSolver extends Solver {
    private Coordinate targetStart, targetEnd;
    
    
    public EndPointSolver(int rows, int cols, Coordinate s, Coordinate f) {
        super(rows, cols);
        targetStart = board.getCoordinate(s.getRows(), s.getCols());
        targetEnd = board.getCoordinate(f.getRows(), f.getCols());        
    }

    @Override
    public void restartSolver()
    {
        super.restartSolver();
        targetStart = board.getCoordinate(targetStart.getRows(), targetStart.getCols());
        targetEnd = board.getCoordinate(targetEnd.getRows(), targetEnd.getCols());     
    }

    public boolean connectEnds()
    {
        //board.loops[cord.getLine().getNumInBoard()] = board.loops[cord.getLine().getNumInBoard()].linearize();
        //same colored square if the rows and cols all add up to an even
        if(sameColor(targetStart, targetEnd))
            return false;
        if(targetStart.consecutiveInLoop(targetEnd))
        {
            ((Loop)targetStart.getLine()).linearize(targetStart, targetEnd);
            targetEnd.getLine().setLocked(true);
            return true;
        }
        
        if(!findPath())
        {
            System.err.println("Path not found");
            return false;
        }
        followPath();
        targetStart.getLine().setLocked(true);

        return true;
    }

    private  ArrayList<Side> setQuadPath()
    {
        //TODO make use of Quad
        int quadSR = targetStart.getRows()/4;
        int quadSC = targetStart.getCols()/4;
        int quadER = targetEnd.getRows()/4;
        int quadEC = targetEnd.getCols()/4;
        Quad end = new Quad(quadER, quadEC);
        Quad start = new Quad(quadSR, quadSC);

        int dist = Math.abs(quadEC-quadSC)+Math.abs(quadSR - quadER);
        ArrayList<Side> moves = new ArrayList<>();
        if(dist < 2)
        {
            if(quadSR == quadER)
            {
                if(quadSR != 0)
                {
                    moves.add(Side.UP);
                    quadSR--;
                }
                else
                {
                    moves.add(Side.DOWN);
                    quadSR++;
                }
                dist++;  
            }
            if(quadEC == quadSC)
            {
                if(quadEC != 0)
                {
                    moves.add(Side.LEFT);
                    quadSC--;
                }
                else
                {
                    moves.add(Side.RIGHT);
                    quadSC++;
                }
                dist++;
            }
        }
        boolean horizontalFirst = !moves.isEmpty() && moves.get(moves.size()-1).isVertical();
        if(horizontalFirst)
        {
            while(quadSC > quadEC)
            {
                moves.add(Side.LEFT);
                quadSC--;
            }
            while(quadSC < quadEC)
            {
                moves.add(Side.RIGHT);
                quadSC++;
            }
            while(quadSR > quadER)
            {
                moves.add(Side.UP);
                quadSR--;
            }
            while(quadSR < quadER)
            {
                moves.add(Side.DOWN);
                quadSR++;
            }
        }
        else
        {
            while(quadSR > quadER)
            {
                moves.add(Side.UP);
                quadSR--;
            }
            while(quadSR < quadER)
            {
                moves.add(Side.DOWN);
                quadSR++;
            }
            while(quadSC > quadEC)
            {
                moves.add(Side.LEFT);
                quadSC--;
            }
            while(quadSC < quadEC)
            {
                moves.add(Side.RIGHT);
                quadSC++;
            }
        }
        return moves;
    }


    public boolean findPath()
    {
        Queue<Line> arrivableLines = new LinkedList<>();

        Line finalLine = targetEnd.getLine();
        finalLine.setLabel(0);
        arrivableLines.add(finalLine);
        while(targetStart.getLine().getLabel()==Line.UNLABELED)
        {
            if(arrivableLines.peek()==null)
                return false;
            Line cur = arrivableLines.poll();
            ArrayList<Coordinate> cp = getCoordEndColor(cur, false);
            for(Direction d:Direction.allDirections())
            {
                for(Coordinate c:cp)
                {
                    Coordinate moved = c.moveDir(d);
                    if(moved.getLine()!=null)
                    {
                        Line discovered = moved.getLine();
                        if(discovered.getLabel()==Line.UNLABELED)
                        {
                            discovered.setLabel(cur.getLabel()+1);
                            arrivableLines.add(discovered);
                            discovered.setNextInPath(cur);
                        }
                    }
                }
                
            }
        }
        return true;
    }

    public boolean followPath()
    {
        Line curLine = (Loop)targetStart.getLine();
        Line prev= null;
        Coordinate curEndpoint, curStart = targetStart, nextStart = null;
        while(curLine != targetEnd.getLine())
        {
            Loop next = (Loop)curLine.getNextInPath();
            
            ArrayList<Coordinate> endChoices = getCoordEndColor(curLine,true);
            curEndpoint = null;
            for(Coordinate c:endChoices)
            {
                if(adjacent(next, c)!=null)
                {
                    nextStart = adjacent(next, c);
                    curEndpoint = c;
                    break;
                }
            }
            if(curEndpoint==null)
            {
                System.err.println("endpoint not found");
                return false;
            }
                
            curLine = ((Loop)curLine).linearize(curStart, curEndpoint);
            if(prev!=null)
            {
                prev.setNextInPath(curLine);
            }
            if(!properParity(curLine))
            {
                System.err.println("wrong enpoint parity endpoint solver");
                return false;
            }
            if(prev!=null && !(prev.end().adjacent(curLine.start())))
            {
                System.err.println("not adjacent consecutive lines in path");
                return false;
            }
            prev = curLine;
            curStart = nextStart;
            
            curLine = next;
        }
        if(prev == null)
        {
            System.err.println("path did not reach end endpoint solved");
            return false;
        }
        prev.setNextInPath(((Loop)targetEnd.getLine()).linearize(curStart, targetEnd));

        //connect the lines
        int count = 0;
        for(Line l=targetStart.getLine(); l.getNextInPath()!=null; l=l.getNextInPath())
        {

            if(!properParity(l)/*!l.end().adjacent(l.getNextInPath().start())*/)
            {
                System.err.println("not adjacent lines after linearization"+l+l.getNextInPath());
                System.out.println("cur s"+l.start()+"  end: " +l.end()+"  ot start "+l.getNextInPath().start()+"ot end: "+l.getNextInPath().end());
            }
            count++;
        }
        Line startL;
        while(targetStart.getLine()!=targetEnd.getLine())
        {
            startL = targetStart.getLine();
            if(!startL.linearConnect(startL.getNextInPath()))
            {
                System.err.println("connect failed");
                System.err.println(startL+"\n"+startL.getNextInPath());
                return false;
            }
        }

        //check if process worked
        Line achieved = targetStart.getLine();
        if(achieved.start()!=targetStart || achieved.end()!=targetEnd)
        {
            System.out.println("End points were not connected, after path was found");
            return false;
        }
        return true;
    }

    public static Coordinate adjacent(Line l, Coordinate c)
    {
        for(Coordinate co:l.getCoords())
        {
            if(c.adjacent(co))
                return co;
        }
        return null;
    }

    public boolean properParity(Line l)
    {
        return (sameColor(l.start(), targetStart)||sameColor(l.end(), targetEnd));
    }
    public static boolean sameColor(Coordinate l, Coordinate r)
    {
        return (l.isWhite()==r.isWhite());
    }

    public ArrayList<Coordinate> getCoordEndColor(Line l, boolean matchEndColor)
    {
        ArrayList<Coordinate> fin = new ArrayList<>();
        int start = 0;
        if(!sameColor(l.getCoord(0), targetEnd)==matchEndColor)
        {
            start=1;
        }
        for(int i=start; i<l.getSize();i+=2)
        {
            fin.add(l.getCoord(i));
        }
        return fin;
    }

    @Override
    public boolean solve() {
        if(!connectEnds())
        {
            return false;
        }

        int i=0;
        while (board.makeMerge()) {i++;}
        if(board.getNumLines()==1 && !answer().valid())
        {
            System.err.println("wrong end");
        }
        return board.getNumLines() == 1 && board.answer().valid();
    }
}
