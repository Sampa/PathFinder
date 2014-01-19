package graphs;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sampa on 2013-12-26.
 */
public interface Graph<TYPE> {
    boolean pathExists(TYPE from, TYPE to);
    List<TYPE> getPath(TYPE from, TYPE to);
    NeuronPair getNeuronPair(TYPE n1,TYPE n2);
    List<Edge> getEdgesFrom(TYPE neuron);
    Edge getEdgeBetween(NeuronPair np);
    Edge getEdgeBetween(TYPE n1, TYPE n2);
    HashMap<TYPE,List<Edge>> getNodes();
}