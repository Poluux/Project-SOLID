package ClientServer_Communication.Request;

import roadnetwork.Graph;

public class RequestHandlerFactory {
    public static RequestHandler getHandler(String requestType, Graph graph) {
        return switch (requestType.toUpperCase()) {
            case "UPDATE" -> new UpdateHandler(graph); // Passez le graphe ici
            case "PATH" -> new PathCalculationHandler(graph);
            default -> throw new IllegalArgumentException("Unknown request type: " + requestType);
        };
    }
}
