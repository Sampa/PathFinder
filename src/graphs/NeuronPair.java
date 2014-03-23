package graphs;

import java.io.Serializable;

/**
 * Created by Sampa on 2014-01-06.
 */
public class NeuronPair<TYPE> implements Serializable{
    private TYPE n1;
    private TYPE n2;

    public NeuronPair(TYPE n1, TYPE n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public TYPE getN1() {
        return n1;
    }

    public TYPE getN2() {
        return n2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeuronPair that = (NeuronPair) o;
        if (n2.equals(that.n2) && n1.equals(that.n1)) return true;
        if (n2.equals(that.n1) && n1.equals(that.n2)) return true;

        return false;
    }

    @Override
    public int hashCode() {
        int n1length = n1.toString().length();
        int n2length = n1.toString().length();
        return 10*(n1length+n2length);
    }

    public static void man(String[] args) {

    }
}
