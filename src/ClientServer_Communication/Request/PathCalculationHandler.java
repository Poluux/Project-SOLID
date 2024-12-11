package ClientServer_Communication.Request;

import roadnetwork.Graph;
import roadnetwork.Node;
import roadnetwork.DijkstraPathFindingStrategy;
import roadnetwork.PathFindingStrategy;

public class PathCalculationHandler implements RequestHandler {
    private Graph graph;  // Vous devrez initialiser le graphe ici
    private PathFindingStrategy pathFindingStrategy;

    public PathCalculationHandler(Graph graph) {
        this.graph = graph;
        this.pathFindingStrategy = new DijkstraPathFindingStrategy();
    }

    @Override
    public String handle(String request) {
        String answer;

        // Ici, nous allons supposer que `request` contient une commande dans le format "startNode:endNode"
        String[] nodes = request.split(":");
        if (nodes.length == 2) {
            Node start = findNodeByName(nodes[0]);
            Node end = findNodeByName(nodes[1]);

            if (start != null && end != null) {
                answer = pathFindingStrategy.findPath(graph, start, end);
            } else {
                answer = "Invalid node names.";
            }
        } else {
            answer= "Invalid request format. Expected format: startNode:endNode";
        }

        return answer;
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
