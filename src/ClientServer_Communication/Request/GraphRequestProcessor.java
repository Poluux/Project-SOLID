package ClientServer_Communication.Request;

import roadnetwork.Graph;

/**
 * A processor that handles incoming requests related to graph operations.
 * It delegates specific requests to appropriate handlers determined by the request type.
 */
public class GraphRequestProcessor implements RequestProcessor {
    private final Graph graph; // The graph instance used for operations.

    /**
     * Constructor that initializes the GraphRequestProcessor with a graph.
     *
     * @param graph The graph on which the requests will operate.
     */
    public GraphRequestProcessor(Graph graph) {
        this.graph = graph;
    }

    /**
     * Processes a request string by delegating it to the appropriate handler.
     *
     * @param request The incoming request in the format "TYPE body".
     * @return The response from the corresponding RequestHandler.
     */
    @Override
    public String process(String request) {
        // Split the request into two parts: the type and the body.
        String[] parts = request.split(" ", 2);
        String requestType = parts[0]; // Extract the request type.
        String requestBody = parts.length > 1 ? parts[1] : ""; // Extract the body if available.

        // Retrieve the handler for the given request type using the factory.
        RequestHandler handler = RequestHandlerFactory.getHandler(requestType, graph);

        // Delegate the request to the handler and return the response.
        return handler.handle(requestBody);
    }
}