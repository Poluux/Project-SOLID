import java.util.*;

// Classe représentant le graphe
class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node from, Node to, double travelTime) {
        Edge edge = new Edge(from, to, travelTime);
        adjacencyList.get(from).add(edge);
    }

    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    @Override
    public String toString() {
        return "Graph{" + "adjacencyList=" + adjacencyList + '}';
    }
}