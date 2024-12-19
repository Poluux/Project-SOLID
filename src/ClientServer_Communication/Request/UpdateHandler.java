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

        // Vérifier si la requête contient au moins 3 `:` séparés par des espaces
        String[] parts = request.split(":", 4); // Limite à 4 parties pour éviter de scinder le commentaire
        if (parts.length < 3) {
            return "Invalid request format. Expected format: startNode:endNode:time reason\nEND_OF_RESPONSE";
        }

        // Extraire les trois premiers champs
        String startName = parts[0];
        String endName = parts[1];

        String[] distanceCommSeparator = parts[2].split(" ",2);
        String distanceValue = distanceCommSeparator[0];
        String reason;

        if (distanceCommSeparator.length > 1)
            reason = distanceCommSeparator[1].trim();
        else
            reason = "no reason given";

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
        boolean updated = graph.updateTravelTime(start, end, newDistance, reason);
        if (updated) {
            answer = "Travel time updated successfully. Reason: " + reason;
        } else {
            answer = "Edge not found between '" + startName + "' and '" + endName + "'.";
        }

        return answer + "\nEND_OF_RESPONSE";
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