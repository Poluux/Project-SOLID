package ClientServer_Communication.Request;

import roadnetwork.Graph;
import roadnetwork.Node;
import roadnetwork.DijkstraPathFindingStrategy;
import roadnetwork.PathFindingStrategy;

/**
 * A RequestHandler implementation that handles path calculation requests
 * using a specific pathfinding strategy (Dijkstra).
 * This handler processes requests to calculate the shortest path between two nodes in the graph.
 */
public class PathCalculationHandler implements RequestHandler {
    /**
     * The road network graph used for pathfinding operations
     */
    private Graph graph;
    /**
     * The strategy used for pathfinding in the graph (Disjkstra's algorithm in this case).
     */
    private PathFindingStrategy pathFindingStrategy;

    /**
     * Constructs a PathCalculationHandler with the specified graph.
     * Initializes the pathfinding strategy to use Dijkstra's algorithm.
     */

    public PathCalculationHandler(Graph graph) {
        this.graph = graph;
        this.pathFindingStrategy = new DijkstraPathFindingStrategy();
    }

    /**
     * Handles the pathfinding request by finding the shortest path between two nodes.
     * The request is expected to be in the format "startNode:endNode".
     */
    @Override
    public String handle(String request) {
        String answer;

        // Parse the request to extract start and end node names
        String[] nodes = request.split(":");
        if (nodes.length == 2) {
            Node start = findNodeByName(nodes[0]);
            Node end = findNodeByName(nodes[1]);

            if (start != null && end != null) {
                // Calculate the shortest path using the pathfinding strategy
                answer = pathFindingStrategy.findPath(graph, start, end);
            } else {
                answer = "Invalid node names.";
            }
        } else {
            answer= "Invalid request format. Expected format: startNode:endNode";
        }

        answer += "\nEND_OF_RESPONSE";

        return answer;
    }

    /**
     * Finds a node by its name in the graph.
     */
    private Node findNodeByName(String name) {
        for (Node node : graph.getNodes()) {
            if (node.getId().equals(name)) {
                return node;
            }
        }
        return null;
    }
}
