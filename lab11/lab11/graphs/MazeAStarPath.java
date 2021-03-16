package lab11.graphs;

import edu.princeton.cs.algs4.IndexMinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int source) {
        // TODO
        IndexMinPQ<Integer> fringePQ = new IndexMinPQ<>(maze.V());
        for (int i = 0; i < maze.V(); i++) {
            if (i != s) {
                fringePQ.insert(i, distTo[i]);
            }
        }
        marked[s] = true;
        for (int w : maze.adj(s)) {
            edgeTo[w] = s;
            announce();
            distTo[w] = distTo[s] + 1;
            fringePQ.changeKey(w, distTo[w] + h(w));
        }
        while (!fringePQ.isEmpty()) {
            int v = fringePQ.delMin();
            marked[v] = true;
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    fringePQ.changeKey(w, distTo[w] + h(w));
                    if (w == t) {
                        marked[w] = true;
                        announce();
                        targetFound = true;
                    }
                    if (targetFound) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

