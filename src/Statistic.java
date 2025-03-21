public class  Statistic  {
    public static final int UNUSED = -1;
    private int fails = UNUSED, repeats = UNUSED, solutions = UNUSED, targetSolutions = UNUSED;

    //public Statistic() {}

    public int getFails() {
        return fails;
    }
    public int getRepeats() {
        return repeats;
    }
    public int getSolutions() {
        return solutions;
    }
    public int getTargetSolutions() {
        return targetSolutions;
    }
    public  void trackRepeats() {
        if(repeats == UNUSED)
            repeats = 0;
    }
    public  void trackSolutions() {
        if(solutions == UNUSED)
            solutions = 0;
    }
    public void trackFails()
    {
        if(fails == UNUSED)
            fails = 0;
    }
    public boolean usesRepeats() {
        return repeats != UNUSED;
    }
    public boolean usesSolutions() {
        return solutions != UNUSED;
    }
    public boolean usesFails() {
        return fails != UNUSED;
    }
    public void fail() {
        fails++;
    }
    public void repeat() {
        repeats++;
    }
    public void solution() {
        solutions++;
    }

    public Statistic(int target) {
        targetSolutions = target;
    }

    @Override
    public String toString() {
        String fin = "";
        if(usesFails())
            fin += "Fails: "+fails;
        if(usesRepeats())
            fin += "  Repeats: "+repeats;
        if(usesSolutions())
        {
            fin += "  Solutions: "+solutions;
            if(targetSolutions != UNUSED)
                fin += "/"+targetSolutions;
        }
        return fin;
    }
    public void add(Statistic other) {
        fails += other.fails;
        if(repeats != UNUSED && other.repeats != UNUSED)
            repeats += other.repeats;
        if(solutions != UNUSED && other.solutions != UNUSED)
            solutions += other.solutions;
    }

    
}
