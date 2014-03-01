package graphs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Happyjuiced on 2014-02-28.
 */
public class MatrixGraph<TYPE> implements Graph<TYPE>, Serializable {

    @Override
    public boolean pathExists(TYPE from, TYPE to) {
        return GraphMethods.pathExists(from,to,this);
    }

    @Override
    public List<TYPE> getPath(TYPE from, TYPE to) {
        return GraphMethods.getPath(from,to,this);
    }

    @Override
    public NeuronPair getNeuronPair(TYPE n1, TYPE n2) {
        return null;
    }

    @Override
    public List<Edge> getEdgesFrom(TYPE neuron) {
        return null;
    }

    @Override
    public Edge getEdgeBetween(NeuronPair np) {
        return null;
    }

    @Override
    public Edge getEdgeBetween(TYPE n1, TYPE n2) {
        return null;
    }

    @Override
    public HashMap<TYPE, List<Edge>> getNodes() {
        return null;
    }
}
