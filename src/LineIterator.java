
import java.util.Iterator;
public class LineIterator implements Iterator<CoordinatePair> {
    protected  Coordinate arr[];
    protected  int cur = 0; //index of the next element
    protected  int size;
    public enum Traversal{forward, random}
    protected  Traversal traversal = Traversal.forward;
    protected int order[];

    //for a line, random is between reverse and forwards
    public LineIterator(Coordinate coords[], Traversal trav)
    {
        arr = coords;
        size = arr.length - 1;
        setForwardOrder();

        //shuffle
        if(trav == Traversal.random)
        {
            shuffleOrder();
        }
        
    }
    protected final void setForwardOrder()
    {
        order = new int[size];
        for(int i=0; i<size; i++)
        {
            order[i] = i;
        }
    }
    protected final void shuffleOrder()
    {
        shuffleArr(order);
    }
    public static void shuffleArr(int arr[])
    {
        shuffleArr(arr, arr.length);
    }

    public static void shuffleArr(int arr[], int size)
    {
        for(int i= size-1; i>0; i--)
        {
            int swapi = (int)(Math.random()*i);
            int temp = arr[swapi];
            arr[swapi] = arr[i];
            arr[i] = temp;
        }
    }

    @Override
    public boolean hasNext()
    {
        return cur < size;
    }

    @Override
    public CoordinatePair next()
    {
        CoordinatePair toRet = new CoordinatePair(arr[order[cur]], arr[order[cur]+1]);
        cur++;
        return toRet;
    }
}
