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
        String answer = "";

        // Requête au format : "startNode:endNode:newDistance"
        String[] parts = request.split(":");
        if (parts.length != 3) {
            return "Invalid request format. Expected format: startNode:endNode:newDistance\nEND_OF_RESPONSE";
        }

        String startName = parts[0];
        String endName = parts[1];
        String distanceValue = parts[2];

        // Vérification de la validité du nombre
        double newDistance;
        try {
            newDistance = Double.parseDouble(distanceValue);
        } catch (NumberFormatException e) {
            return "Invalid distance value. It must be a valid number.\nEND_OF_RESPONSE";
        }

        // Vérification de l'existence des nœuds
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

        // Tentative de mise à jour du temps de trajet
        boolean updated = graph.updateTravelTime(start, end, newDistance);
        if (updated) {
            return "Travel time updated successfully.\nEND_OF_RESPONSE";
        } else {
            return "Edge not found between '" + startName + "' and '" + endName + "'.\nEND_OF_RESPONSE";
        }
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