package ClientServer_Communication.Request;

/**
 * Interface for processing requests related to the graph.
 * Implementing classes must define the logic for processing incoming requests.
 */
public interface RequestProcessor {
    /**
     * Processes the incoming request and returns a response.
     *
     * @param request The request as a string.
     * @return The response as a string.
     */
    String process(String request);
}
