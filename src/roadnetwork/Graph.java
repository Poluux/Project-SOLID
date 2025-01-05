package roadnetwork;

import java.util.*;

/**
 * Represents a graph with nodes and edges.
 * Provides methods to manage nodes, edges, and travel times.
 */
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>(); // Stores edges for each node.
    private final Map<Edge, Double> defaultTravelTimes = new HashMap<>(); // Stores default travel times for edges.

    /**
     * Adds a node to the graph.
     *
     * @param node The node to add.
     */
    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds an edge between two nodes with a specified travel time.
     *
     * @param from       The source node.
     * @param to         The destination node.
     * @param travelTime The travel time for the edge.
     */
    public void addEdge(Node from, Node to, double travelTime) {
        Edge edge = EdgeFactory.createEdge(from, to, travelTime);
        adjacencyList.get(from).add(edge);
        defaultTravelTimes.put(edge, travelTime); // Store the default travel time.
    }

    /**
     * Retrieves the list of edges for a specific node.
     *
     * @param node The node whose edges are to be retrieved.
     * @return A list of edges originating from the node.
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    /**
     * Retrieves all nodes in the graph.
     *
     * @return A set of all nodes.
     */
    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    /**
     * Updates the travel time for an edge between two nodes.
     *
     * @param from          The source node.
     * @param to            The destination node.
     * @param newTravelTime The new travel time.
     * @param changeReason  The reason for the update.
     * @return True if the edge was found and updated, false otherwise.
     */
    public boolean updateTravelTime(Node from, Node to, double newTravelTime, String changeReason) {
        for (Edge edge : adjacencyList.get(from)) {
            if (edge.getTo().equals(to)) {
                edge.setTravelTime(newTravelTime);
                edge.setChangeReason(changeReason);
                return true; // Successfully updated.
            }
        }
        return false; // Edge not found.
    }

    /**
     * Resets all travel times to their default values.
     */
    public void resetTravelTimes() {
        for (Map.Entry<Edge, Double> entry : defaultTravelTimes.entrySet()) {
            entry.getKey().setTravelTime(entry.getValue());
        }
    }

    /**
     * Prints the graph in a readable format to the console.
     */
    public void printGraph() {
        System.out.println("\n=== Graph Representation ===");
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node fromNode = entry.getKey();
            List<Edge> edges = entry.getValue();

            System.out.print(fromNode.getId() + " : ");
            if (edges.isEmpty()) {
                System.out.println("No connections");
            } else {
                for (Edge edge : edges) {
                    System.out.print("→ " + edge.getTo().getId() + " (" + edge.getTravelTime() + " min) ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Provides a string representation of the graph for debugging.
     *
     * @return A string representation of the graph.
     */
    @Override
    public String toString() {
        return "Graph{" + "adjacencyList=" + adjacencyList + '}';
    }
}