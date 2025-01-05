package roadnetwork;

/**
 * A factory for creating Edge instances.
 */
public class EdgeFactory {

    /**
     * Creates a new edge between two nodes with a specified travel time.
     *
     * @param from       The source node.
     * @param to         The destination node.
     * @param travelTime The travel time between the nodes.
     * @return A new Edge instance.
     */
    public static Edge createEdge(Node from, Node to, double travelTime) {
        return new Edge(from, to, travelTime);
    }
}
