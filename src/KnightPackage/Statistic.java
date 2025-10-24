/*
 * Tracks statistics such as number of fails, repeats, and solutions found
 * When passing it as a parameter, it should never be null
 */
package KnightPackage;

public class Statistic  {
    public static final int UNUSED = -1;
    public enum StatType {
        FAILS,
        REPEATS,
        SOLUTIONS
    }
    private final int stats[];
    private int targetSolutions = UNUSED;
    
    public int get(StatType st) {
        return stats[st.ordinal()];
    }

    final public void track(StatType st) {
        if (stats[st.ordinal()] == UNUSED) {
            stats[st.ordinal()] = 0;
        }
        StatType.FAILS.toString();
    }

    final public boolean tracks(StatType st) {
        return stats[st.ordinal()] != UNUSED;
    }

    public void inc(StatType st) {
        if (stats[st.ordinal()] != UNUSED) {
            stats[st.ordinal()]++;
        }
    }

    public Statistic(int target) {
        stats = new int[StatType.values().length];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = UNUSED;
        }
        targetSolutions = target;
        tracks(StatType.FAILS);
    }

    @Override
    public String toString() {
        String fin = "";
        // if(usesFails())
        //     fin += "Fails: "+fails+"  ";
        if(tracks(StatType.REPEATS))
            fin += "Repeats: "+stats[StatType.REPEATS.ordinal()]+"  ";
        if(tracks(StatType.SOLUTIONS))
        {
            fin += "Solutions: "+stats[StatType.SOLUTIONS.ordinal()];
            if(targetSolutions != UNUSED)
                fin += "/"+targetSolutions;
        }
        return fin;
    }
    public void add(Statistic other) {
        for (int i = 0; i < stats.length; i++) {
            if (stats[i] != UNUSED && other.stats[i] != UNUSED) {
                stats[i] += other.stats[i];
            }
        }
    }
}
