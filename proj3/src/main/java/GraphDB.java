import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    private Map<Long, Node> nodeMap = new LinkedHashMap<>();
    private Map<Long, Way> wayMap = new LinkedHashMap<>();

    static class Node {
        private long id;
        private double lon;
        private double lat;
        private Set<Long> connected;
        private String name;

        Node(String id, String lon, String lat) {
            this.id = Long.parseLong(id);
            this.lon = Double.parseDouble(lon);
            this.lat = Double.parseDouble(lat);
            connected = new HashSet<>();
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }
    }

    static class Way {
        private long id;
        private List<Node> nodeList;
        private Node lastNode;
        private String maxSpeed;
        private boolean valid;
        private String type;
        private String name;

        Way(String id) {
            this.id = Long.parseLong(id);
            nodeList = new ArrayList<>();
        }

        public void addNode(Node node) {
            this.nodeList.add(node);
            this.lastNode = node;
        }

        public void setMaxSpeed(String maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean getValid() {
            return valid;
        }

        public void connect() {
            if (nodeList.size() == 1) {
                return;
            }
            nodeList.get(0).connected.add(nodeList.get(1).id);
            for (int i = 1; i < nodeList.size() - 1; i++) {
                nodeList.get(i).connected.add(nodeList.get(i - 1).id);
                nodeList.get(i).connected.add(nodeList.get(i + 1).id);
            }
            nodeList.get(nodeList.size() - 1).connected.add(nodeList.get(nodeList.size() - 2).id);
        }
    }

    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterable<Long> keys = vertices();
        LinkedList<Long> removeKeys = new LinkedList<>();
        for (long key : keys) {
            Node node = nodeMap.get(key);
            if (node.connected.isEmpty()) {
                removeKeys.add(key);
            }
        }
        for (long key : removeKeys) {
            nodeMap.remove(key);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodeMap.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node node = nodeMap.get(v);
        return node.connected;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Iterable<Long> keys = vertices();
        double distance = Double.MAX_VALUE;
        long closestKey = 0;
        for (long key : keys) {
            Node node = nodeMap.get(key);
            double dist = distance(lon, lat, node.lon, node.lat);
            if (dist < distance) {
                distance = dist;
                closestKey = key;
            }
        }
        return closestKey;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodeMap.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodeMap.get(v).lat;
    }

    public void addNode(Node node) {
        nodeMap.put(node.id, node);
    }

    public Node getNode(String id) {
        return nodeMap.get(Long.parseLong(id));
    }

    public Node getNode(long id) {
        return nodeMap.get(id);
    }

    public void addWay(Way way) {
        wayMap.put(way.id, way);
    }
}
