package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s = 0;
    private boolean cycleDetected = false;
    private int cycleStartPointer;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        edgeTo[s] = 0;
        distTo[s] = 0;
        dfsDetectCycle(s);
    }

    // Helper methods go here
    private void dfsDetectCycle(int v) {
        marked[v] = true;
        for (int w : maze.adj(v)) {
            if (marked[w] && Math.abs(distTo[w] - distTo[v]) > 1) {
                cycleStartPointer = edgeTo[w];
                edgeTo[w] = v;
                announce();
                cycleDetected = true;
                return;
            }
            if (!marked[w]) {
                edgeTo[w] = v;
                distTo[w] = distTo[v] + 1;
                announce();
                dfsDetectCycle(w);
                if (cycleDetected) {
                    if (w == cycleStartPointer) {
                        cycleStartPointer = edgeTo[w];
                        edgeTo[w] = w;
                        announce();
                    }
                    return;
                }
            }
        }
    }
}

