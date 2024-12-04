package roadnetwork;

// Interface représentant une stratégie de calcul de chemin
    public interface PathFindingStrategy {
        String findPath(Graph graph, Node start, Node end);
    }