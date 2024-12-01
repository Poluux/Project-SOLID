package roadnetwork;

// Factory Method: Simplifie la création des arêtes
public class EdgeFactory {
    public static Edge createEdge(Node from, Node to, double travelTime) {
        return new Edge(from, to, travelTime);
    }
}