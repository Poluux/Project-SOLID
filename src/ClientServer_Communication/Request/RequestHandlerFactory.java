package ClientServer_Communication.Request;

public class RequestHandlerFactory {
    public static RequestHandler getHandler(String requestType) {
        return switch (requestType) {
            case "UPDATE" -> new UpdateHandler();
            case "PATH" -> new PathCalculationHandler();
            default -> throw new IllegalArgumentException("Unknown request type");
        };
    }
}
