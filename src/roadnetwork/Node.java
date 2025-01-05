package roadnetwork;

import java.util.Objects;

/**
 * Represents a node in the graph, identified by a unique ID.
 */
public class Node {
    private final String id; // Unique identifier for the node.

    /**
     * Constructs a node with the specified ID.
     *
     * @param id The unique identifier for the node.
     */
    public Node(String id) {
        this.id = id;
    }

    /**
     * Retrieves the ID of the node.
     *
     * @return The node's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if two nodes are equal based on their IDs.
     *
     * @param obj The object to compare.
     * @return True if the IDs match; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return Objects.equals(id, node.id);
    }

    /**
     * Generates a hash code for the node based on its ID.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Provides a string representation of the node for debugging.
     *
     * @return A string representation of the node.
     */
    @Override
    public String toString() {
        return "Node{" + "id='" + id + '\'' + '}';
    }
}
