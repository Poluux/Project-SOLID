package roadnetwork;

// Factory Method pour la création des arêtes
public class EdgeFactory {

    // Crée une nouvelle arête entre deux nœuds avec un temps de trajet donné
    public static Edge createEdge(Node from, Node to, double travelTime) {
        return new Edge(from, to, travelTime);
    }
}