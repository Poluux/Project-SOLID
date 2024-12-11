package ClientServer_Communication.Request;

import roadnetwork.Graph;

public class GraphRequestProcessor implements RequestProcessor{
    private final Graph graph;

    public GraphRequestProcessor(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String process(String request) {
        String[] parts = request.split(" ",2);
        String requestType = parts[0];
        String requestBody = parts.length > 1 ? parts[1]:"";

        RequestHandler handler = RequestHandlerFactory.getHandler(requestType,graph);
        return handler.handle(requestBody);
    }
}
