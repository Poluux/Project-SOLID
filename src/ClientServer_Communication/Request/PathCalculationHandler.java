package ClientServer_Communication.Request;

import roadnetwork.Graph;
import roadnetwork.Node;
import roadnetwork.DijkstraPathFindingStrategy;
import roadnetwork.PathFindingStrategy;

/**
 * A handler for processing path calculation requests.
 * It uses a graph and a pathfinding strategy to compute the shortest path between nodes.
 */
public class PathCalculationHandler implements RequestHandler {
    private final Graph graph; // The graph instance used for path calculations.
    private final PathFindingStrategy pathFindingStrategy; // Strategy for finding paths in the graph.

    /**
     * Constructor to initialize the handler with a graph.
     * It defaults to using Dijkstra's algorithm for pathfinding.
     *
     * @param graph The graph on which path calculations will be performed.
     */
    public PathCalculationHandler(Graph graph) {
        this.graph = graph;
        this.pathFindingStrategy = new DijkstraPathFindingStrategy(); // Default pathfinding strategy.
    }

    /**
     * Handles a path calculation request.
     * The request format is expected to be "startNode:endNode".
     *
     * @param request The incoming request string.
     * @return A response string with the shortest path or an error message.
     */
    @Override
    public String handle(String request) {
        String answer; // Response to be returned.

        // Assuming the `request` is in the format "startNode:endNode".
        String[] nodes = request.split(":"); // Split the request into start and end node names.
        if (nodes.length == 2) {
            // Find the start and end nodes by their names.
            Node start = findNodeByName(nodes[0]);
            Node end = findNodeByName(nodes[1]);

            if (start != null && end != null) {
                // Use the pathfinding strategy to compute the shortest path.
                answer = pathFindingStrategy.findPath(graph, start, end);
            } else {
                // Return an error message if one or both nodes are invalid.
                answer = "Invalid node names.";
            }
        } else {
            // Return an error message if the request format is incorrect.
            answer = "Invalid request format. Expected format: startNode:endNode";
        }

        return answer; // Return the response.
    }

    /**
     * Finds a node in the graph by its name.
     *
     * @param name The name of the node to search for.
     * @return The Node object if found; null otherwise.
     */
    private Node findNodeByName(String name) {
        // Iterate through the graph's nodes to find a match by ID.
        for (Node node : graph.getNodes()) {
            if (node.getId().equals(name)) {
                return node; // Return the matching node.
            }
        }
        return null; // Return null if no matching node is found.
    }
}