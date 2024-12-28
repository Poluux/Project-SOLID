package ClientServer_Communication.Request;

import roadnetwork.Graph;

/**
 * An implementation of RequestProcessor that processes client requests
 * related to a road network graph.
 * It delegates request handling to appropriate RequestHandler instances
 * based on the type of the request.
 */
public class GraphRequestProcessor implements RequestProcessor{
    /**
     * The road network graph used for processing requests.
     */
    private final Graph graph;

    /**
     * Constructs GraphRequestProcessor with the given graph.
     */
    public GraphRequestProcessor(Graph graph) {
        this.graph = graph;
    }

    /**
     * Processes a client request by identifying the request type and delegating
     * the request to the appropriate handler.
     */
    @Override
    public String process(String request) {
        // Split the request into type and body
        String[] parts = request.split(" ",2);
        String requestType = parts[0];
        String requestBody = parts.length > 1 ? parts[1]:"";

        // Get the appropriate handler for the request type
        RequestHandler handler = RequestHandlerFactory.getHandler(requestType,graph);

        // Process the request body and return the response
        return handler.handle(requestBody);
    }
}
