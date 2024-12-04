package roadnetwork;
import java.util.*;

// Classe représentant le graphe contenant les nœuds et arêtes
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();
    private final Map<Edge, Double> defaultTravelTimes = new HashMap<>(); // Temps par défaut

    // Ajoute un nœud au graphe
    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Ajoute une arête entre deux nœuds avec un temps de trajet par défaut
    public void addEdge(Node from, Node to, double travelTime) {
        Edge edge = EdgeFactory.createEdge(from, to, travelTime);
        adjacencyList.get(from).add(edge);
        defaultTravelTimes.put(edge, travelTime); // Stocker le temps par défaut
    }

    // Récupère les arêtes sortantes d'un nœud donné
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    // Retourne l'ensemble des nœuds présents dans le graphe
    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    // Met à jour le temps de trajet entre deux nœuds
    public boolean updateTravelTime(Node from, Node to, double newTravelTime) {
        for (Edge edge : adjacencyList.get(from)) {
            if (edge.getTo().equals(to)) {
                edge.setTravelTime(newTravelTime);
                return true; // Mise à jour réussie
            }
        }
        return false; // Arête introuvable
    }

    // Réinitialise les temps de trajet à leurs valeurs par défaut
    public void resetTravelTimes() {
        for (Map.Entry<Edge, Double> entry : defaultTravelTimes.entrySet()) {
            entry.getKey().setTravelTime(entry.getValue());
        }
    }

    // Affiche le graphe dans un format lisible
    public void printGraph() {
        System.out.println("\n=== Représentation du Graphe ===");
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node fromNode = entry.getKey();
            List<Edge> edges = entry.getValue();

            System.out.print(fromNode.getId() + " : ");
            if (edges.isEmpty()) {
                System.out.println("Aucune connexion");
            } else {
                for (Edge edge : edges) {
                    System.out.print("→ " + edge.getTo().getId() + " (" + edge.getTravelTime() + " min) ");
                }
                System.out.println();
            }
        }
    }

    // Représentation en chaîne de caractères du graphe (utile pour le débogage)
    @Override
    public String toString() {
        return "Graph{" + "adjacencyList=" + adjacencyList + '}';
    }
}