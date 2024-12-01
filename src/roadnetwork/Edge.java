package roadnetwork;

// Classe représentant une arête
public class Edge {
    private final Node from;
    private final Node to;
    private double travelTime; // Temps de trajet

    public Edge(Node from, Node to, double travelTime) {
        this.from = from;
        this.to = to;
        this.travelTime = travelTime;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public String toString() {
        return "roadnetwork.Edge{" + "from=" + from + ", to=" + to + ", travelTime=" + travelTime + '}';
    }
}