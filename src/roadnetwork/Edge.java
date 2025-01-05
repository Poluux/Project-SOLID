package roadnetwork;

/**
 * Represents an edge in the graph, connecting two nodes with a travel time.
 */
public class Edge {
    private final Node from; // The source node.
    private final Node to; // The destination node.
    private double travelTime; // The travel time between the source and destination nodes.
    private String changeReason; // Reason for travel time changes, if applicable.

    /**
     * Constructs an edge with specified source, destination, and travel time.
     *
     * @param from       The source node.
     * @param to         The destination node.
     * @param travelTime The travel time between the nodes.
     */
    public Edge(Node from, Node to, double travelTime) {
        this.from = from;
        this.to = to;
        this.travelTime = travelTime;
    }

    /**
     * Sets the reason for a travel time change.
     *
     * @param changeReason The reason for the change.
     */
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    /**
     * Gets the source node of the edge.
     *
     * @return The source node.
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Gets the destination node of the edge.
     *
     * @return The destination node.
     */
    public Node getTo() {
        return to;
    }

    /**
     * Gets the travel time for the edge.
     *
     * @return The travel time.
     */
    public double getTravelTime() {
        return travelTime;
    }

    /**
     * Sets the travel time for the edge.
     *
     * @param travelTime The new travel time.
     */
    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * Provides a string representation of the edge for debugging.
     *
     * @return A string representation of the edge.
     */
    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + ", travelTime=" + travelTime + '}';
    }
}