package graphs;

/**
 * Created by Sampa on 2014-01-06.
 */
public class NeuronPair<TYPE> {
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
        //int result = n1.hashCode();
        //result = result + n2.hashCode();
        return 10;
    }

    public static void man(String[] args) {

    }
}
