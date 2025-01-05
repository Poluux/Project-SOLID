package ClientServer_Communication.Request;

import roadnetwork.Graph;
import roadnetwork.Node;

/**
 * Handles update requests to modify travel times between nodes in the graph.
 */
public class UpdateHandler implements RequestHandler {
    private final Graph graph; // The graph instance for updating travel times.

    /**
     * Constructor to initialize the handler with a graph.
     *
     * @param graph The graph instance.
     */
    public UpdateHandler(Graph graph) {
        this.graph = graph;
    }

    /**
     * Handles an update request to modify travel times.
     * The request format is expected to be "startNode:endNode:time reason".
     *
     * @param request The incoming request string.
     * @return A response indicating success or an error message.
     */
    @Override
    public String handle(String request) {
        String answer = ""; // Response to be returned.

        // Split the request into parts: start node, end node, and additional info.
        String[] parts = request.split(":", 4);
        if (parts.length < 3) {
            return "Invalid request format. Expected format: startNode:endNode:time reason\nEND_OF_RESPONSE";
        }

        String startName = parts[0]; // Start node name.
        String endName = parts[1];   // End node name.

        // Split the time and reason part.
        String[] distanceCommSeparator = parts[2].split(" ", 2);
        String distanceValue = distanceCommSeparator[0]; // Travel time value.
        String reason = distanceCommSeparator[1].trim(); // Reason for the update.

        // Validate the travel time value.
        double newDistance;
        try {
            newDistance = Double.parseDouble(distanceValue);
        } catch (NumberFormatException e) {
            return "Invalid distance value. It must be a valid number.\nEND_OF_RESPONSE";
        }

        // Find the start and end nodes in the graph.
        Node start = findNodeByName(startName);
        Node end = findNodeByName(endName);

        // Validate the nodes and update travel time if valid.
        if (start == null || end == null) {
            if (start == null && end == null) {
                return "Both start and end nodes not found in the graph.\nEND_OF_RESPONSE";
            } else if (start == null) {
                return "Start node '" + startName + "' not found in the graph.\nEND_OF_RESPONSE";
            } else {
                return "End node '" + endName + "' not found in the graph.\nEND_OF_RESPONSE";
            }
        }

        boolean updated = graph.updateTravelTime(start, end, newDistance, reason);
        if (updated) {
            answer = "Travel time updated successfully. Reason: " + reason;
        } else {
            answer = "Edge not found between '" + startName + "' and '" + endName + "'.";
        }

        return answer + "\nEND_OF_RESPONSE";
    }

    /**
     * Finds a node in the graph by its name.
     *
     * @param name The name of the node to search for.
     * @return The Node object if found; null otherwise.
     */
    private Node findNodeByName(String name) {
        for (Node node : graph.getNodes()) {
            if (node.getId().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;
    }
}