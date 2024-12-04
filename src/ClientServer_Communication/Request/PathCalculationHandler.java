package ClientServer_Communication.Request;

import roadnetwork.Graph;
import roadnetwork.Node;
import roadnetwork.DijkstraPathFindingStrategy;

public class PathCalculationHandler implements RequestHandler {
    private Graph graph;  // Vous devrez initialiser le graphe ici
    private DijkstraPathFindingStrategy pathFindingStrategy;

    public PathCalculationHandler(Graph graph) {
        this.graph = graph;
        this.pathFindingStrategy = new DijkstraPathFindingStrategy();
    }

    @Override
    public void handle(String request) {
        // Ici, nous allons supposer que `request` contient une commande dans le format "startNode:endNode"
        String[] nodes = request.split(":");
        if (nodes.length == 2) {
            Node start = findNodeByName(nodes[0]);
            Node end = findNodeByName(nodes[1]);

            if (start != null && end != null) {
                pathFindingStrategy.findPath(graph, start, end);
            } else {
                System.out.println("Invalid node names.");
            }
        } else {
            System.out.println("Invalid request format. Expected format: startNode:endNode");
        }
    }

    private Node findNodeByName(String name) {
        for (Node node : graph.getNodes()) {
            if (node.getId().equals(name)) {
                return node;
            }
        }
        return null;
    }
}
