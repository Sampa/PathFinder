import graphs.NeuronPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
public class Neuron extends JComponent implements Serializable{
    private static int selectedNeuronCount;
    public static ArrayList<Neuron> selectedNeurons = new ArrayList<>();
    private int posX,posY;
    private String name;
    private boolean selected;
    private JPanel panel;
    private JLabel label;
    private LinkedList<?> edges = new LinkedList();
    private ClickListener cl;
    private PathFinder win;
    public Neuron(int posX, int posY, String name,PathFinder pf) {
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        win = pf;
        selected = false;
        cl = new ClickListener();
        setLayout(new BorderLayout());
        panel = new JPanel();
        label = new JLabel(name);
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        panel.setPreferredSize(new Dimension(20, 20));
        label.setForeground(Color.RED);
        label.setBackground(Color.WHITE);
        add(panel, BorderLayout.WEST);
        add(label, BorderLayout.SOUTH);
        panel.addMouseListener(cl);
        setBounds(posX, posY, 50, 35);
    }

    public void removeListerner(){
        try{
            this.panel.removeMouseListener(cl);
        }catch (NullPointerException npe){
            System.out.print("darn");
        }
    }
    public void addListerner(){
        try{
            this.panel.addMouseListener(new ClickListener());
        }catch (NullPointerException npe){
            System.out.print("darn");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public static int getSelectedNeuronCount() {
        return selectedNeuronCount;
    }
    public static void setSelectedNeuronCount(int selectedNeuronCount) {
        Neuron.selectedNeuronCount = selectedNeuronCount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neuron)) return false;

        Neuron neuron = (Neuron) o;

        return posX == neuron.posX && posY == neuron.posY && edges.equals(neuron.edges) && name.equals(neuron.name);

    }
    @Override
    public int hashCode() {
        int result = posX;
        result = 31 * result + posY;
        result = 31 * result + name.hashCode();
        result = 31 * result + edges.hashCode();
        return result;
    }

    public void setWin(PathFinder win) {
        this.win = win;
    }

    public void select(){
        int neuronCount = getSelectedNeuronCount();
        selected = true;
        panel.setBackground(Color.ORANGE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setSelectedNeuronCount(neuronCount + 1);
        selectedNeurons.add(this);
        if (getSelectedNeuronCount() == 2) {
            win.enableAllStateItems();
        }
    }

    public void deselect(){
        int neuronCount = getSelectedNeuronCount();
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        setSelectedNeuronCount(neuronCount - 1);
        selectedNeurons.remove(this);
        if (getSelectedNeuronCount() < 2) {
            win.disableAllStateItems();
        }
        selected = false;
    }
    public static void selectNeuronPair(NeuronPair<Neuron> np){
          if(deselectAll()){
              np.getN1().select();
              np.getN2().select();
          }
    }
    public static boolean deselectAll(){
        //lite onödigt stor men failsafe med loop och try catch iaf
        try {
            for (Neuron selectedNeuron : selectedNeurons)
                selectedNeuron.deselect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setCl(ClickListener o) {
        cl = o;
    }

    private class ClickListener extends MouseAdapter implements Serializable{
        public void mouseClicked(MouseEvent e) {
            int neuronCount = getSelectedNeuronCount();
            if (neuronCount > 1 && !Neuron.this.selected) {
                JOptionPane.showMessageDialog(Neuron.this, "Du har redan valt två platser");
                return;
            }
            if(Neuron.this.isSelected()){
                Neuron.this.deselect();
            }else{
                Neuron.this.select();
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
