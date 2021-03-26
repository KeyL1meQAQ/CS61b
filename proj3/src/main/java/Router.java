
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    static class Info {
        private long id;
        private double distToS;
        private Double priority;
        private boolean marked;
        private long lastNode;

        Info(long id) {
            this.id = id;
            marked = false;
            distToS = Double.MAX_VALUE;
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long startID = g.closest(stlon, stlat);
        long endID = g.closest(destlon, destlat);
        Map<Long, Info> infoMap = new HashMap();
        Queue<Info> infoQueue = new PriorityQueue<>(new InfoComparator());
        Info startInfo = new Info(startID);
        Info endInfo = new Info(endID);
        startInfo.distToS = 0;
        infoQueue.add(startInfo);
        infoMap.put(startID, startInfo);
        infoMap.put(endID, endInfo);
        while (!infoQueue.isEmpty()) {
            Info curInfo = infoQueue.remove();
            curInfo.marked = true;
            Iterable<Long> adjacent = g.adjacent(curInfo.id);
            for (long id : adjacent) {
                if (!infoMap.containsKey(id)) {
                    Info info = new Info(id);
                    infoMap.put(id, info);
                }
                if (id == endID) {
                    LinkedList<Long> result = new LinkedList<>();
                    endInfo.lastNode = curInfo.id;
                    for (long rid = endID; rid != startID; rid = infoMap.get(rid).lastNode) {
                        result.addFirst(rid);
                    }
                    result.addFirst(startID);
                    return result;
                }
                Info nextInfo = infoMap.get(id);
                if (!nextInfo.marked) {
                    if (curInfo.distToS + g.distance(id, curInfo.id) < nextInfo.distToS) {
                        if (infoQueue.contains(nextInfo)) {
                            infoQueue.remove(nextInfo);
                        }
                        nextInfo.distToS = curInfo.distToS + g.distance(id, curInfo.id);
                        nextInfo.priority = nextInfo.distToS + g.distance(id, endID);
                        nextInfo.lastNode = curInfo.id;
                        infoQueue.add(nextInfo);
                    }
                }
            }
        }
        return null;
    }

    private static class InfoComparator implements Comparator<Info> {
        @Override
        public int compare(Info o1, Info o2) {
            return o1.priority.compareTo(o2.priority);
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        ArrayList<Long> path = new ArrayList<>(route);
        String way = "START";
        int direction = NavigationDirection.START;
        double distance = 0;
        List<NavigationDirection> naviList = new LinkedList<>();
        for (int i = 0, j = 1; j < path.size(); i++, j++) {
            long first = path.get(i);
            long second = path.get(j);
            String curWay = getWayName(g, first, second);
            if (way.equals("START") || curWay.equals(way)) {
                distance += g.distance(first, second);
                way = curWay;
            } else {
                NavigationDirection navi = new NavigationDirection();
                navi.way = way;
                navi.distance = distance;
                navi.direction = direction;
                naviList.add(navi);
                way = curWay;
                distance = g.distance(first, second);
                direction = getDirection(g.bearing(path.get(i - 1), path.get(i)),
                        g.bearing(first, second));
            }
        }
        NavigationDirection navi = new NavigationDirection();
        navi.way = way;
        navi.distance = distance;
        navi.direction = direction;
        naviList.add(navi);
        return naviList;
    }

    private static String getWayName(GraphDB g, long v, long w) {
        GraphDB.Node node1 = g.getNode(v);
        GraphDB.Node node2 = g.getNode(w);
        List<Long> ways1 = node1.getWays();
        List<Long> ways2 = node2.getWays();
        List<Long> intersection = new LinkedList<>(ways1);
        intersection.retainAll(ways2);

        if (!intersection.isEmpty()) {
            if (g.getWay(intersection.get(0)).getName() == null) {
                return "";
            } else {
                return g.getWay(intersection.get(0)).getName();
            }
        }

        return "";
    }

    private static int getDirection(double prevDegree, double thisDegree) {
        double degree = thisDegree - prevDegree;
        if (degree > 180) {
            degree -= 360;
        } else if (degree < -180) {
            degree += 360;
        }
        if (degree > -15 && degree < 15) {
            return NavigationDirection.STRAIGHT;
        }
        if (degree > -30 && degree <= -15) {
            return NavigationDirection.SLIGHT_LEFT;
        }
        if (degree >= 15 && degree < 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        }
        if (degree > -100 && degree <= -30) {
            return NavigationDirection.LEFT;
        }
        if (degree >= 30 && degree < 100) {
            return NavigationDirection.RIGHT;
        }
        if (degree <= -100) {
            return NavigationDirection.SHARP_LEFT;
        }
        if (degree >= 100) {
            return NavigationDirection.SHARP_RIGHT;
        }
        return -1;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }

    public static void main(String[] args) {

    }
}
