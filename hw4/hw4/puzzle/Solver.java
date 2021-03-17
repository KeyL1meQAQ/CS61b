package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Solver {

    private MinPQ<SearchNode> nodesPriorityQueue;

    private int moves;

    private Stack<WorldState> solutionSequence;

    private Map<WorldState, Integer> distanceToGoal = new HashMap<>();

    /**
     * A search node, each search node instance defines one "move sequence"
     */
    private class SearchNode {

        /**
         * Current world state
         */
        private WorldState state;

        /**
         * The number of moves have been made
         */
        private int numberOfMoves;

        /**
         * The previous search node
         */
        private SearchNode previousNode;

        /**
         * The priority of this node
         */
        private Integer priority;

        /**
         * Constructor
         */
        SearchNode(WorldState worldState, int numberOfMoves, SearchNode previousNode) {
            this.state = worldState;
            this.numberOfMoves = numberOfMoves;
            this.previousNode = previousNode;
            if (distanceToGoal.containsKey(this.state)) {
                this.priority = numberOfMoves + distanceToGoal.get(this.state);
            } else {
                int estimatedDistance = this.state.estimatedDistanceToGoal();
                this.priority = numberOfMoves + estimatedDistance;
                distanceToGoal.put(this.state, estimatedDistance);
            }
        }
    }

    private class NodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return o1.priority.compareTo(o2.priority);
        }
    }

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial The initial world state
     */
    public Solver(WorldState initial) {
        nodesPriorityQueue = new MinPQ<>(new NodeComparator());
        nodesPriorityQueue.insert(new SearchNode(initial, 0, null));
        solutionSequence = new Stack<>();
        while (!nodesPriorityQueue.isEmpty()) {
            SearchNode nodeMin = nodesPriorityQueue.delMin();
            if (nodeMin.state.isGoal()) {
                moves = nodeMin.numberOfMoves;
                for (SearchNode node = nodeMin; node != null; node = node.previousNode) {
                    solutionSequence.push(node.state);
                }
                return;
            } else {
                for (WorldState state : nodeMin.state.neighbors()) {
                    if (nodeMin.previousNode == null || !state.equals(nodeMin.previousNode.state)) {
                        nodesPriorityQueue.insert(new SearchNode(state,
                                nodeMin.numberOfMoves + 1, nodeMin));
                    }
                }
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     * @return The minimum number of moves
     */
    public int moves() {
        return moves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     * @return The sequence of world states from the initial world state
     */
    public Iterable<WorldState> solution() {
        return solutionSequence;
    }
}
