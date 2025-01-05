package ClientServer_Communication.Request;

/**
 * Interface for handling different types of requests.
 * Implementing classes must define the `handle` method to process requests.
 */
public interface RequestHandler {
    /**
     * Processes the incoming request and returns a response.
     *
     * @param request The incoming request as a string.
     * @return The response as a string.
     */
    String handle(String request);
}