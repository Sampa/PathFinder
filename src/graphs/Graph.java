package graphs;

import java.util.HashMap;
import java.util.List;

public interface Graph<TYPE> {
    boolean pathExists(TYPE from, TYPE to);
    List<Edge> getPath(TYPE from, TYPE to);
    NeuronPair getNeuronPair(TYPE n1,TYPE n2);
    List<Edge> getEdgesFrom(TYPE neuron);
    Edge getEdgeBetween(NeuronPair np);
    Edge getEdgeBetween(TYPE from, TYPE to);
    void setConnectionWeight(TYPE n1, TYPE n2,int newWeight);
    boolean add(TYPE n1);
    boolean remove(TYPE n1);
    void disconnect(TYPE n1, TYPE n2);
    HashMap<TYPE,List<Edge>> getNodes();
}