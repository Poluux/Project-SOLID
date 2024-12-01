package roadnetwork;

import java.util.*;

// Classe représentant un graphe
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    // Ajoute un nœud au graphe
    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Ajoute une arête entre deux nœuds
    public void addEdge(Node from, Node to, double travelTime) {
        Edge edge = EdgeFactory.createEdge(from, to, travelTime); // Factory Method
        adjacencyList.get(from).add(edge);
    }

    // Récupère les arêtes sortantes d'un nœud
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    @Override
    public String toString() {
        return "roadnetwork.Graph{" + "adjacencyList=" + adjacencyList + '}';
    }
}