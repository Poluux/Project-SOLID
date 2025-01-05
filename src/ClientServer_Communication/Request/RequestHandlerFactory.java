package ClientServer_Communication.Request;

import roadnetwork.Graph;

/**
 * Factory class for creating appropriate RequestHandler instances
 * based on the request type.
 */
public class RequestHandlerFactory {
    /**
     * Returns a RequestHandler instance based on the request type.
     *
     * @param requestType The type of the request (e.g., "UPDATE", "PATH").
     * @param graph       The graph instance to be used by the handler.
     * @return An appropriate RequestHandler instance.
     * @throws IllegalArgumentException If the request type is unknown.
     */
    public static RequestHandler getHandler(String requestType, Graph graph) {
        // Determine the appropriate handler using the request type.
        return switch (requestType.toUpperCase()) {
            case "UPDATE" -> new UpdateHandler(graph); // Handles update requests.
            case "PATH" -> new PathCalculationHandler(graph); // Handles path calculation requests.
            default -> throw new IllegalArgumentException("Unknown request type: " + requestType);
        };
    }
}