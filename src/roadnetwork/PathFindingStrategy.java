package roadnetwork;

// Strategy Pattern: Préparation pour des algorithmes de calcul
interface PathFindingStrategy {
    void findPath(Graph graph, Node start, Node end);
}