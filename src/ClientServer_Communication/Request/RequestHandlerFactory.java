package ClientServer_Communication.Request;

import roadnetwork.Graph;

/**
 * A factory class for creating RequestHandler instances based on request type.
 * This class centralizes the logic for selecting the appropriate handler for each type of request.
 */
public class RequestHandlerFactory {
    /**
     * Returns a RequestHandler instance that can handle the given request type.
     */
    public static RequestHandler getHandler(String requestType, Graph graph) {
        return switch (requestType.toUpperCase()) {
            case "UPDATE" -> new UpdateHandler(graph);
            case "PATH" -> new PathCalculationHandler(graph);
            default -> throw new IllegalArgumentException("Unknown request type: " + requestType);
        };
    }
}
