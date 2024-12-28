package ClientServer_Communication.Request;

/**
 * An interface for processing client requests.
 * Implementation of this interface define how specific types of requests are handled.
 */
public interface RequestProcessor {
    /**
     * Processes a client request and return the appropriate response.
     */
    String process(String request);
}
