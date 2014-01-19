package graphs;

/**
 * Created by Sampa on 2014-01-10.
 */
class DataHolder<TYPE> {
    private int weight;
    private boolean hasWeightSet;
    private TYPE cameFrom;

    public DataHolder(int weight, boolean hasWeightSet,TYPE cameFrom) {
        this.weight = weight;
        this.hasWeightSet = hasWeightSet;
        this.cameFrom = cameFrom;
    }


    public void setAttributes(int weight,boolean hasWeightSet,TYPE cameFrom) {
        setWeight(weight);
        setHasWeightSet(hasWeightSet);
        setCameFrom(cameFrom);
    }
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isHasWeightSet() {
        return hasWeightSet;
    }

    public void setHasWeightSet(boolean hasWeightSet) {
        this.hasWeightSet = hasWeightSet;
    }

    public void setCameFrom(TYPE cameFrom) {
        this.cameFrom = cameFrom;
    }

    public TYPE getCameFrom() {
        return cameFrom;
    }
}