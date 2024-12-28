package ClientServer_Communication.Request;
import roadnetwork.Graph;
import roadnetwork.Node;

/**
 * A RequestHandler implementation that handles update requests for the road network.
 * This handler is responsible for updating the travel time between two nodes in the graph,
 * based on the request provided by the client.
 */
public class UpdateHandler implements RequestHandler {
    /**
     * The road network graph used for updating travel times.
     */
    private final Graph graph;

    /**
     * Constructs an UpdateHandler with the specified graph.
     */
    public UpdateHandler(Graph graph) {
        this.graph = graph;
    }

    /**
     * Handles the update request by parsing the input and updating the travel time between two nodes.
     * The request is expected to be in the format : "startNode:endNode:distance reason".
     */
    @Override
    public String handle(String request) {
        String answer = "";

        // Split the request into parts based on ":"
        String[] parts = request.split(":", 4); // Limit to 4 parts to avoid splitting the reason
        if (parts.length < 3) {
            return "Invalid request format. Expected format: startNode:endNode:time reason\nEND_OF_RESPONSE";
        }

        // Extract the start node, end node, and the distance with the reason
        String startName = parts[0];
        String endName = parts[1];

        String[] distanceCommSeparator = parts[2].split(" ",2);
        String distanceValue = distanceCommSeparator[0];
        String reason;

        if (distanceCommSeparator.length > 1)
            reason = distanceCommSeparator[1].trim();
        else
            reason = "no reason given";

        // Validate the distance value
        double newDistance;
        try {
            newDistance = Double.parseDouble(distanceValue);
        } catch (NumberFormatException e) {
            return "Invalid distance value. It must be a valid number.\nEND_OF_RESPONSE";
        }

        // Validate the existence of the nodes
        Node start = findNodeByName(startName);
        Node end = findNodeByName(endName);

        if (start == null || end == null) {
            if (start == null && end == null) {
                return "Both start and end nodes not found in the graph.\nEND_OF_RESPONSE";
            } else if (start == null) {
                return "Start node '" + startName + "' not found in the graph.\nEND_OF_RESPONSE";
            } else {
                return "End node '" + endName + "' not found in the graph.\nEND_OF_RESPONSE";
            }
        }

        // Attempt to update the travel time between the nodes
        boolean updated = graph.updateTravelTime(start, end, newDistance, reason);
        if (updated) {
            answer = "Travel time updated successfully. Reason: " + reason;
        } else {
            answer = "Edge not found between '" + startName + "' and '" + endName + "'.";
        }

        return answer + "\nEND_OF_RESPONSE";
    }

    /**
     * Finds a node by its name in the graph.
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