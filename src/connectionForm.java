import javax.swing.*;
import java.awt.*;

/**
 * Created by Sampa on 2014-01-01.
 */
public class connectionForm extends JFrame {
    private JLabel connectionNameLabel;
    private JTextField connectionName;
    private JTextField connectionWeight;
    private JLabel connectionWeightLabel;
    private JLabel infoText;

    connectionForm() {
        Dimension d = new Dimension(400,250);
        setMinimumSize(d);
        setSize(d);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        add(infoText);
        add(connectionNameLabel);
        add(connectionName);
        pack();
        setVisible(true);
    }

    public static void man(String[] args) {

    }
}
