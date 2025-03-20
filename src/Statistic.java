public class  Statistic  {
    private int fails = 0, repeats = UNUSED, solutions = UNUSED;
    public Statistic() {    }
    public int getFails() {
        return fails;
    }
    public int getRepeats() {
        return repeats;
    }
    public int getSolutions() {
        return solutions;
    }
    public static final int UNUSED = -1;
    public  void repeatsUsed() {
        if(repeats == UNUSED)
            repeats = 0;
    }
    public  void solutionsUsed() {
        if(solutions == UNUSED)
            solutions = 0;
    }

    public Statistic(int numSolutions) {
        solutions = numSolutions;
    }
    @Override
    public String toString() {
        String fin = "Fails: "+fails;
        if(repeats != UNUSED)
            fin += "  Repeats: "+repeats;
        if(solutions != UNUSED)
            fin += "  Solutions: "+solutions;
        return fin;
    }
    public void add(Statistic other) {
        fails += other.fails;
        if(repeats != UNUSED && other.repeats != UNUSED)
            repeats += other.repeats;
        if(solutions != UNUSED && other.solutions != UNUSED)
            solutions += other.solutions;
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
}
