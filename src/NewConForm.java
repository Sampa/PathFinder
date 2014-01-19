import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sampa on 2014-01-02.
 */
public class NewConForm<TYP> extends JFrame{
    private JPanel panel1;
    private JTextField name;
    private JLabel weightLabel;
    private JLabel nameLabel;
    private JTextField weight;
    private JButton save;
    private JButton cancel;
    private JLabel info;
    private String edgeName;
    private int edgeWeight;

    public String getEdgeName() {
        return edgeName;
    }

    public int getEdgeWeight() {
        return edgeWeight;
    }

    public NewConForm() {
        runForm();
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               NewConForm.this.edgeName = name.getText();
               NewConForm.this.edgeWeight = Integer.parseInt(weight.getText());
               System.out.print("hahah");

               NewConForm.this.dispose();

            }
        });

    }

    public static void main(String[] args) {


    }

    public JPanel getPanel1() {
        return panel1;
    }

    private void runForm() {
        JFrame frame = new JFrame("NewConForm");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
