package roadnetwork;

// Interface représentant une stratégie de calcul de chemin
public interface PathFindingStrategy {

    // Méthode à implémenter pour trouver le chemin entre deux nœuds
    void findPath(Graph graph, Node start, Node end);
}