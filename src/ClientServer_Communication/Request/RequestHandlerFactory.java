package ClientServer_Communication.Request;

import roadnetwork.Graph;

public class RequestHandlerFactory {
    public static RequestHandler getHandler(String requestType, Graph graph) {
        return switch (requestType) {
            case "UPDATE" -> new UpdateHandler();
            case "PATH" -> new PathCalculationHandler(graph);
            default -> throw new IllegalArgumentException("Unknown request type");
        };
    }
}
