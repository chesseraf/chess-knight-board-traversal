
public class LoopIterator extends LineIterator{
    public LoopIterator(Coordinate arr[], Traversal t)
    {
        super(arr, t);
        size = arr.length; //loop around 
        setForwardOrder();
        if(t == Traversal.random)
            shuffleOrder();
    }

    @Override
    public CoordinatePair next()
    {
        CoordinatePair toRet = new CoordinatePair(arr[order[cur]], arr[(order[cur]+1)%arr.length]);
        cur++;
        if(!toRet.consecutiveInLoop())
        {
            System.out.println("Not consecutive");
        }
        return toRet;
    }
    
}
