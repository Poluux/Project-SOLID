package roadnetwork;

// Classe représentant une arête dans le graphe
public class Edge {
    private final Node from; // Nœud source
    private final Node to;   // Nœud destination
    private double travelTime; // Temps de trajet entre les deux nœuds
    private String changeReason;

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    // Constructeur : Initialise une arête avec les nœuds source/destination et le temps de trajet
    public Edge(Node from, Node to, double travelTime) {
        this.from = from;
        this.to = to;
        this.travelTime = travelTime;
    }

    // Retourne le nœud source de l'arête
    public Node getFrom() {
        return from;
    }

    // Retourne le nœud destination de l'arête
    public Node getTo() {
        return to;
    }

    // Retourne le temps de trajet de l'arête
    public double getTravelTime() {
        return travelTime;
    }

    // Modifie le temps de trajet de l'arête
    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    // Représentation en chaîne de caractères de l'arête (utile pour le débogage)
    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + ", travelTime=" + travelTime + '}';
    }
}