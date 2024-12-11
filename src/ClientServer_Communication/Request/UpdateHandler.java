package ClientServer_Communication.Request;
import roadnetwork.Graph;
import roadnetwork.Node;

public class UpdateHandler implements RequestHandler {
    private final Graph graph;

    public UpdateHandler(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String handle(String request) {
        String answer;
        // Requête au format : "startNode:endNode:newDistance"
        String[] parts = request.split(":");
        if (parts.length != 3) {
            answer = "Invalid request format. Expected format: startNode:endNode:newDistance";
        }

        String startName = parts[0];
        String endName = parts[1];
        double newDistance=0;
        try {
            newDistance = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            answer = "Invalid distance value. It must be a number.";
        }

        Node start = findNodeByName(startName);
        Node end = findNodeByName(endName);

        if (start == null || end == null) {
            answer = "One or both nodes not found.";
        }

        boolean updated = graph.updateTravelTime(start, end, newDistance);
         answer = updated ? "Travel time updated successfully." : "Edge not found between the specified nodes.";
         answer += "\nEND_OF_RESPONSE";

         return answer;
    }

    private Node findNodeByName(String name) {
        for (Node node : graph.getNodes()) {
            if (node.getId().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;
    }
}