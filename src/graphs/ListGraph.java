package graphs;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by Sampa on 2013-12-26.
 */
public class ListGraph<TYPE> extends GraphMethods<TYPE> implements Graph<TYPE>, Serializable {
    @Override
    public String toString() {
        return "ListGraph{" +
                "allNeurons=" + allNeurons +
                ", pairs=" + pairs +
                ", edgePanel=" + edgePanel +
                ", edgeName=" + edgeName +
                ", edgeWeight=" + edgeWeight +
                ", nameLabel=" + nameLabel +
                ", weightLabel=" + weightLabel +
                '}';
    }

    public HashMap<TYPE,List<Edge>> allNeurons;
    public HashMap<NeuronPair,Edge> pairs;
    JPanel edgePanel;
    JTextField edgeName;
    JTextField edgeWeight;
    JLabel nameLabel,weightLabel;

    public ListGraph() {
        edgeWeight = new JTextField(20);
        edgeName = new JTextField(20);
        edgePanel = new JPanel(new GridLayout(2,1,5,0));
        nameLabel = new JLabel("Förbindelsens namn:");
        weightLabel = new JLabel("Förbindelsens vikt:");
        allNeurons = new HashMap();
        pairs = new HashMap();
    }

    public HashMap<TYPE, List<Edge>> getAllNeurons() {
        return allNeurons;
    }

    public void setAllNeurons(HashMap<TYPE, List<Edge>> allNeurons) {
        this.allNeurons = allNeurons;
    }

    public HashMap<NeuronPair, Edge> getPairs() {
        return pairs;
    }

    public void setPairs(HashMap<NeuronPair, Edge> pairs) {
        this.pairs = pairs;
    }

    @Override
    public boolean pathExists(TYPE from, TYPE to) {

        return pathExists(this, from,to,this);
    }

    @Override
    public List<Edge> getPath(TYPE from, TYPE to) {
        return getPath(this, from,to,this);
    }

    public List<Edge> getEdgesFrom(TYPE neuron){
        return allNeurons.get(neuron);
    }
    public boolean add(TYPE n) {
        if(allNeurons.containsKey(n))
            return false;
        allNeurons.put(n, new ArrayList<Edge>());
        return true;
    }

    public boolean neuronsExist(TYPE n1, TYPE n2){
        if(!allNeurons.containsKey(n1) || !allNeurons.containsKey(n2))
            throw new NoSuchElementException();
        return true;
    }
    public NeuronPair getNeuronPair(TYPE n1,TYPE n2){
        NeuronPair np = null;
        for(NeuronPair pair : pairs.keySet()){
            if((pair.getN1()==n1  && pair.getN2()==n2) || (pair.getN1()==n2 && pair.getN2()==n1))
                np = pair;
        }
        return np;
    }

    public NeuronPair modCheck(int weight, TYPE n1, TYPE n2){
        if (weight < 0)
            throw new IllegalArgumentException();
        neuronsExist(n1, n2);
        NeuronPair np = null;
        try{
            np = getNeuronPair(n1, n2);
            if(np == null)
                throw new IllegalStateException();
        }catch(NoSuchElementException e){
            return null;
        }
        return np;
    }
    public void connect(TYPE n1, TYPE n2, String name, int weight){
        if (weight < 0)
            throw new IllegalArgumentException();
        if(getNeuronPair(n1,n2)!=null)
            throw new IllegalStateException();
        NeuronPair np = new NeuronPair(n1,n2);
        Edge<TYPE> edge = new Edge(n1,n2, weight, name);
        pairs.put(np, edge);
        List<Edge> l1 = allNeurons.get(n1);
        List<Edge> l2 = allNeurons.get(n2);
        l1.add(edge);
      //  edge.revert(); //now start is always the node that ovns the list and dest the other one
        l2.add(edge);
    }

    public HashMap<TYPE,List<Edge>> getNodes() {
        return allNeurons;
    }
    public ArrayList<TYPE> getNodesAsList(){
        ArrayList<TYPE> temp = new ArrayList<>();
        for(TYPE e : allNeurons.keySet()){
            temp.add(e);
        }
        return temp;
    }
    public Edge getEdgeBetween(NeuronPair np) {
        TYPE n1 = (TYPE)np.getN1();
        TYPE n2 = (TYPE)np.getN2();
        return  getEdgeBetween(n1,n2);
    }
    public Edge getEdgeBetween(TYPE n1, TYPE n2){
        neuronsExist(n1, n2);
        try{
            NeuronPair np = getNeuronPair(n1, n2);
            return pairs.get(np);
        }catch (Exception e){}
        return null;
    }
    public JPanel showEdgeDialog(NeuronPair neuronPair,Boolean readOnly){
        Edge edge = pairs.get(neuronPair);
        edgeName.setEditable(false);
        if(readOnly)
            edgeWeight.setEditable(false);
        else
            edgeWeight.setEditable(true);
        edgeName.setText(edge.getName());
        edgeWeight.setText(String.valueOf(edge.getWeight()));
        edgePanel.add(nameLabel);
        edgePanel.add(edgeName);
        edgePanel.add(weightLabel);
        edgePanel.add(edgeWeight);
        return edgePanel;
    }
    public void showEditEdgeDialog(Object parentComponent,NeuronPair neuronPair){
        JPanel editEdgePanel = showEdgeDialog(neuronPair, false);
        boolean doLoop = true;
        while(doLoop) {
            try {
                int result = JOptionPane.showOptionDialog((Component) parentComponent, editEdgePanel,
                        "Ändra mellan "+neuronPair.getN1()+" och "+neuronPair.getN2()+"?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Spara", "Avbryt"}, edgeWeight);
                if (result == JOptionPane.YES_OPTION) {
                    int newWeight = Integer.parseInt(edgeWeight.getText());
                    Edge thisEdge = pairs.get(neuronPair);
                    thisEdge.setWeight(newWeight);
                }
                doLoop = false;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog((Component)parentComponent,"Vikten kan bara vara heltal");
            }
        }
    }
    public void showViewEdgeDialog(Object parentComponent,NeuronPair neuronPair){
        JPanel editEdgePanel = showEdgeDialog(neuronPair,true);
        int result = JOptionPane.showOptionDialog((Component) parentComponent, editEdgePanel,
                "Visar förbindelse",
                JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Ok"}, edgeWeight);
    }
    public void disconnect(TYPE n1, TYPE n2) {
        neuronsExist(n1,n2);
        NeuronPair temp = getNeuronPair(n1, n2);
        pairs.remove(temp); //will throw NoSuchElementException if not found
    }
    public boolean remove(TYPE n) {
        try {
            allNeurons.remove(n);
            for ( Map.Entry<NeuronPair, Edge> pair  : pairs.entrySet()) {
                if(pair.getKey().equals(n) || pair.getValue().equals(n))
                    pairs.remove(pair);
            }
            return true;
        } catch (NoSuchElementException ne) {
            return false;
        }
    }
    public void setConnectionWeight(TYPE n1, TYPE n2,int newWeight) {
        if (newWeight<0)
            throw new IllegalArgumentException();
        NeuronPair np = getNeuronPair(n1, n2);
        Edge e = getEdgeBetween(np);
        e.setWeight(newWeight);
    }

    public NeuronPair findEdge(Object o){
        Edge find = (Edge)o;
        for(Map.Entry<NeuronPair,Edge> e : pairs.entrySet()){
            if(e.getValue().equals(find)){
                return e.getKey();
                //showEditEdgeDialog(null,e.getKey());
            }
        }
        return null;
    }
}
