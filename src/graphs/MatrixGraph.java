package graphs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Happyjuiced on 2014-02-28.
 */
public class MatrixGraph<TYPE> extends GraphMethods<TYPE> implements Graph<TYPE>, Serializable {
    @Override
    public void setConnectionWeight(TYPE n1, TYPE n2, int newWeight) {

    }

    @Override
    public boolean pathExists(TYPE from, TYPE to) {
        return pathExists(this, from, to, this);
    }

    @Override
    public List<Edge> getPath(TYPE from, TYPE to) {
        return getPath(this, from, to, this);
    }

    @Override
    public NeuronPair getNeuronPair(TYPE n1, TYPE n2) {
        return null;
    }

    @Override
    public List<Edge> getEdgesFrom(TYPE neuron) {
        return getEdgesFrom(neuron);
    }


    @Override
    public Edge getEdgeBetween(NeuronPair np) {
        return null;
    }

    public Edge getEdgeBetween(TYPE n1, TYPE n2) {
        return null;
    }

    @Override
    public HashMap<TYPE, List<Edge>> getNodes() {
        return null;
    }
}
