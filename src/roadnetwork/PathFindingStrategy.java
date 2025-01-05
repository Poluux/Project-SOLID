package roadnetwork;

/**
 * Interface for defining pathfinding strategies in a graph.
 * Implementing classes should provide the logic for finding paths between nodes.
 */
public interface PathFindingStrategy {
    /**
     * Finds the path between two nodes in a graph.
     *
     * @param graph The graph instance containing the nodes and edges.
     * @param start The starting node of the path.
     * @param end   The destination node of the path.
     * @return A string describing the path and its details.
     */
    String findPath(Graph graph, Node start, Node end);
}