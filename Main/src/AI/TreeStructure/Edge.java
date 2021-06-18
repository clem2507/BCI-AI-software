package AI.TreeStructure;

public class Edge  {

    /** source node of the edge */
    private final Node source;
    /** destination node of the edge */
    private final Node destination;

    /**
     * constructor for the edge
     * @param source node
     * @param destination node
     */
    public Edge(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * getter for the destination node
     * @return destination node
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * getter for the source node
     * @return source node
     */
    public Node getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }

}
