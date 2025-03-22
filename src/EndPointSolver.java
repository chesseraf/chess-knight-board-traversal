import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
        
        ArrayList<Side> moves = setQuadPath();
        int dist = moves.size(); 
        Side lastMove = moves.get(dist-1);

        followPathUntil2Away(moves);
        //labelLoopsNearEnd(lastMove);
        board.getLoop(0).setLocked(true);

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


    public void labelLoopsNearEnd(Side lastMove)
    {
        int numIterations = 3;
        Quad endQ = Quad.getQuad(targetEnd);
        Quad arriveFromQ = endQ.move(lastMove.opposite());

        Coordinate prevFinC = targetEnd.prevInLine(), postFinC = targetEnd.nextInLine();
        Coordinate arriveFromC = lastMove.furthur(postFinC, prevFinC)>0?prevFinC:postFinC; //closer the the quad we arrive from
        Queue<Line> arrivableLines = new LinkedList<>();

        Line finalLine = targetEnd.getLine();
        finalLine.setLabel(0);
        arrivableLines.add(finalLine);
        // while(arrivableLines.peek()!=null && arrivableLines.peek().getLabel()<numIterations)
        // {
        //     Line cur = arrivableLines.poll();
        //     CoordinatePair cp = getCoordNotEndColor(cur);
        //     for(Direction d:Direction.allDirections())
        //     {
        //         Coordinate moved = cp.first().moveDir(d);
        //         if(moved.getLine()!=null)
        //         {
        //             Line discovered = moved.getLine();
        //             if(discovered.getLabel()==Line.UNLABELED)
        //             {
        //                 discovered.setLabel(cur.getLabel()+1);
        //                 arrivableLines.add(discovered);
        //             }
        //         }
        //     }
        // }

        // labeling all loop in the arriveFrom quadrant from which the final merging will be easy
        for(Direction d:Direction.allDirections())
        {
            if(arriveFromQ.hasCoord(arriveFromC.moveDir(d)))
            {
                Line l = arriveFromC.moveDir(d).getLine();
                if(l.getLabel() ==Line.UNLABELED)
                {
                    arrivableLines.add(l);
                    l.setLabel(1);
                }
            }
        }
        for(Direction d:Direction.allDirections())
        {
            for(Line l:arrivableLines)
            {
                Coordinate cLab1 = getCoordNotEndColor(l).first();
                if(arriveFromQ.hasCoord(cLab1.moveDir(d)))
                {
                    Line movedTo = cLab1.moveDir(d).getLine();
                    if(movedTo.getLabel() == Line.UNLABELED)
                    {
                        arrivableLines.add(movedTo);
                        movedTo.setLabel(2);
                    }
                }
            }
        }
    }

    public boolean followPathUntil2Away(ArrayList<Side> moves)
    {
        // following the path until 2 away from the target quadrant
        Side s;
        Coordinate curLoopEnter = targetStart;
        Coordinate curLoopEnd;
        Line curLoop, linearized;
        Quad curQ = Quad.getQuad(targetStart);
        for(int i=0; i<moves.size()-3; i++)
        {
            s = moves.get(i);
            curLoop = curLoopEnter.getLine();
            //move to that quadrant
            curLoopEnd = (s.furthur(curLoopEnter.nextInLine(), curLoopEnter.prevInLine())>0)
                ?(curLoopEnter.nextInLine()):(curLoopEnter.prevInLine());
            //board.curLine.getNumInBoard()((Loop)curLoopEnter.getLine()).linearize(curLoopEnter, curLoopEnd);
            linearized = ((Loop)curLoop).linearize(curLoopEnter, curLoopEnd);
            if(linearized == null)
            {
                return false;
            }
            if(i!=0)
            {
                if(!targetStart.getLine().linearConnect(linearized))
                    return false;
            }
            
            //move to the next quadrant
            curQ = curQ.move(s);
            for(Direction d: Direction.allDirections())
            {
                if(curQ.hasCoord(curLoopEnd.moveDir(d)))
                {
                    curLoopEnter = curLoopEnd.moveDir(d);
                    break;
                }
            }
            
        }
        return true;
    }

    public boolean reachEnd()
    {
        System.err.println("not reaching end yet");
        return false;
    }

    public boolean sameColor(Coordinate l, Coordinate r)
    {
        return (l.isWhite()==r.isWhite());
    }

    public CoordinatePair getCoordNotEndColor(Line l)
    {
        if(!sameColor(l.getCoord(0), targetEnd))
            return new CoordinatePair(l.getCoord(0), l.getCoord(2));
        return new CoordinatePair(l.getCoord(1), l.getCoord(3));
    }

    @Override
    public boolean solve() {
        if(!connectEnds())
        {
            return false;
        }

        int i=0;
        while (board.makeMerge()) {i++;}
        return board.getNumLines() == 1;
    }
}
