package ClientServer_Communication.Request;

/**
 * An interface for handling specific types of client requests.
 * Implementations of this interface define the logic for processing
 * and responding to a particular category of requests.
 */
public interface RequestHandler {
    /**
     * Handles a client request and returns the approriate response.
     */
    String handle(String request);
}
