import graphs.Graph;
import graphs.ListGraph;
import graphs.NeuronPair;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;


/**
 * Created by Sampa on 2014-01-05.
 */
public class Line extends JComponent implements Serializable {
    private int startX,startY,endX,endY,maxX,maxY,minX,minY,width,height;
    private Color lineColor;
    private PointerInfo pointer;
    private Robot robot;
    private Point coord;
    private JFrame surface;
    private boolean allowClick;
    private String invokeOnClick;
    private boolean defaultStartCorner;
    public NeuronPair neuronPair;
    public Graph graph;
    public PathFinder win;

    public Line(PathFinder surface,String invokeOnClick, Color lineColor, Graph graph,NeuronPair np) {
        this.neuronPair = np;
        this.graph = graph;
        this.win = surface;
        Neuron one = (Neuron)np.getN1();
        Neuron two = (Neuron)np.getN2();
        startX = one.getX();
        startY = one.getY();
        endX = two.getX();
        endY = two.getY();
        this.surface = surface;
        this.lineColor = lineColor;
        this.invokeOnClick = invokeOnClick;
        this.allowClick = false;
        this.defaultStartCorner = true;
        //must find min values to be able to draw correctly
        minX = Math.min(startX,endX);
        minY = Math.min(startY, endY);
        width = (Math.max(startX,endX))-minX;
        height =(Math.max(startY, endY))-minY;
        //must check it to draw from right corner
        if((startX<endX)&&(startY>endY))
            defaultStartCorner = false;
        if((endX<startX)&&(endY>startY))
            defaultStartCorner = false;
        //else we can risk drawing lines extremly thin or even 0px height/width if nodes are close in X/Y angle
        width = Math.max(5,width);
        height = Math.max(5,height);
        setBounds(minX, minY, width, height);

        //for listeners
        pointer = MouseInfo.getPointerInfo();
        coord = pointer.getLocation();
        coord = pointer.getLocation();
        addMouseListener(new lineMouseListener());
        addMouseMotionListener(new lineMouseListener());
    }

    public void setPointer(PointerInfo pointer) {
        this.pointer = pointer;
    }
    public void prepareForSave() {
        pointer = null;
        robot = null;
        coord = null;
    }

    public void performAction() throws NoSuchMethodException {
       PathFinder.invokeOnLineClick(neuronPair,win);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(lineColor);
        if(defaultStartCorner) {
            g2.drawLine(0, 0,getWidth(), getHeight());
        }
        else {
            g2.drawLine(getWidth()-1, 0, 0, getHeight() - 1);
        }
    }
    private class lineMouseListener extends MouseInputAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            checkPixel(e);
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if(allowClick){
                try {
                    performAction();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
            }
        }
        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }
        private void checkPixel(MouseEvent e){
            /* kopierad (ej egenskriven) kod */
            try {
                robot = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
            coord = MouseInfo.getPointerInfo().getLocation();
            Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getY());
            /*Slut kopierad kod*/
            if(color.getRGB()==lineColor.getRGB()) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                allowClick = true;
            }else{
                setCursor(Cursor.getDefaultCursor());
                allowClick = false;
            }
            robot.delay(100);
        }
    }
}