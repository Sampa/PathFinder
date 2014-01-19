import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Sampa on 2013-12-26.
 */
public class Neuron extends JComponent{
    private static int selectedNeuronCount;
    public static ArrayList<Neuron> selectedNeurons = new ArrayList();
    private int posX,posY;
    private String name;
    private boolean selected = false;
    private JPanel panel;
    private JLabel label;
    private LinkedList<?> edges = new LinkedList();
    private HooverListener hl;
    private ClickListener cl;

    public Neuron(int posX, int posY, String name,JFrame pf) {
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        hl = new HooverListener();
        cl = new ClickListener(pf);
        setLayout(new BorderLayout());
        panel = new JPanel();
        label = new JLabel(name);
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        panel.setPreferredSize(new Dimension(20, 20));
        panel.addMouseListener(hl);
        label.setForeground(Color.RED);
        label.setBackground(Color.WHITE);
        add(panel, BorderLayout.WEST);
        add(label, BorderLayout.SOUTH);

        panel.addMouseListener(cl);

        setBounds(posX, posY, 50, 35);
        addMouseListener(hl);
    }

    public void removeListerners(){
        try{
            //remove Listener(hl);
            this.panel.removeMouseListener(cl);
        }catch (NullPointerException npe){
            System.out.print("darn");
        }
    }
    public LinkedList getEdges() {
        return edges;
    }
    public void setEdges(LinkedList<?> edges) {
        this.edges = edges;
    }
    @Override
    public String toString() {

        String info;

        return name;
    }
    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    public JPanel getPanel() {
        return panel;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    public JLabel getLabel() {
        return label;
    }
    public void setLabel(JLabel label) {
        this.label = label;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
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

        if (posX != neuron.posX) return false;
        if (posY != neuron.posY) return false;
        if (!edges.equals(neuron.edges)) return false;
        if (!name.equals(neuron.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = posX;
        result = 31 * result + posY;
        result = 31 * result + name.hashCode();
        result = 31 * result + edges.hashCode();
        return result;
    }
    private class ClickListener extends MouseAdapter{
        private JFrame win;
        private ClickListener(JFrame pf) {
            win = pf;
        }
        public void mouseClicked(MouseEvent e) {
            int neuronCount = getSelectedNeuronCount();
            if (neuronCount > 1 && !Neuron.this.selected) {
                JOptionPane.showMessageDialog(Neuron.this, "Du har redan valt två platser");
                return;
            }
            if (Neuron.this.selected) {
                Neuron.this.panel.setBackground(Color.BLUE);
                Neuron.this.panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                setSelectedNeuronCount(neuronCount - 1);
                selectedNeurons.remove(Neuron.this);
                if (getSelectedNeuronCount() < 2) {
                    PathFinder.disableAllStateItems();
                }
            } else {
                panel.setBackground(Color.ORANGE);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                setSelectedNeuronCount(neuronCount + 1);
                selectedNeurons.add(Neuron.this);
                if (getSelectedNeuronCount() == 2) {
                    PathFinder.enableAllStateItems();
                }

            }
            Neuron.this.selected = !Neuron.this.selected;
        }
    }

    private class HooverListener extends MouseAdapter {
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
