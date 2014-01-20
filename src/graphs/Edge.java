package graphs;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by Sampa on 2013-12-26.
 */
public class Edge<TYPE> extends JComponent implements Serializable{
    private TYPE start;
    private TYPE destination;
    private int weight;
    private String name;

    Edge(TYPE start, TYPE destination, int weight, String name) {
        this.start = start;
        this.destination = destination;
        this.weight = weight;
        this.name = name;
    }

    public TYPE getStart() {
        return start;
    }


    @Override
    public String toString() {
        return "Res mellan " + start+ " och " + destination + " med " + name + " vilket tar " + weight;
    }


    public void setWeight(int weight) {
        if(weight < 0)
            throw new IllegalArgumentException();
        this.weight = weight;
    }

    public TYPE getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }
    public void revert() {
        TYPE temp = start;
        start = destination;
        destination = start;
    }
}
