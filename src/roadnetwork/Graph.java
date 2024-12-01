package roadnetwork;

import java.util.*;

// Classe représentant le graphe contenant les nœuds et arêtes
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    // Ajoute un nœud au graphe
    // Si le nœud n'existe pas déjà, il est ajouté avec une liste d'arêtes vide
    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Ajoute une arête entre deux nœuds
    // Utilise une factory pour créer l'arête et l'ajoute à la liste d'adjacence
    public void addEdge(Node from, Node to, double travelTime) {
        Edge edge = EdgeFactory.createEdge(from, to, travelTime);
        adjacencyList.get(from).add(edge);
    }

    // Retourne la liste des arêtes sortantes d'un nœud donné
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    // Retourne l'ensemble des nœuds présents dans le graphe
    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    // Représentation en chaîne de caractères du graphe (utile pour le débogage)
    @Override
    public String toString() {
        return "Graph{" + "adjacencyList=" + adjacencyList + '}';
    }
}