//import myUtil.BgImage;
import com.sun.javafx.geom.*;
import graphs.*;
import graphs.Edge;
import sun.awt.image.PixelConverter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static myUtil.Printing.*;
/**
 * Created by Sampa on 2013-12-23.
 *
 * Blev lite tidsoptimist när jag klanta arslet av mig och fick göra om två tunga saker,
 * så koden är inte till 100 procent så snyggt uppdelat i metoder och  prestandaeffektiv som jag önskat
 * my ba
 */
public class PathFinder extends JFrame implements Serializable{
    private static ArrayList<JComponent> hasStateComponents = new ArrayList<JComponent>();
    public static PathFinder win;
    public int width = 580;
    public int height = 95;
    public boolean hasChanges;
    private FileNameExtensionFilter filter;
    private boolean loadedFromFile,allowNewNeuron;
    private JMenuBar menuBar;
    private JMenu arMenu,opMenu;
    private JPanel menuPanel;
    private JLayeredPane layerPanel;
    private String savedFilePath,findPathLabel,editPathLabel,newPathLabel,newNeuronLabel,showPathLabel;
    private JMenuItem open,newMap,exit,save,saveAs,findPath,editPath,newPath,showPath,newNeuron;
    private JButton findPathButton,editPathButton,newPathButton,showPathButton,newNeuronButton;
    private Cursor crosshairCursor,defaultCursor;
    private BgImage bg;
    private HashMap<Line, Boolean> lines;
    private HashSet<Neuron> visited;
    private ListGraph<Neuron> neuronListGraph = new ListGraph();
    private MouseListener mListener;
    private NewPathListener newPathL;
    private NewNeuronListener newNeuronL;
    private ShowPathListener spl;
    private EditPathListener epl;
    private FindPathListener fpl;
    private SaveListener saveL;
    private WindowListener ml;
    private OpenListener openL;
    private NewMapListener mpl;
    private ExitListener el;


    PathFinder(){
        super("Pathfinder");
        Dimension lpd = new Dimension(getWidth(),getHeight()-100);
        mListener = new myMouseListener();
        layerPanel = new JLayeredPane();
        layerPanel.setOpaque(true);
        layerPanel.setBounds(0, 0, lpd.width, lpd.height - 100);
        layerPanel.setMaximumSize(lpd);
        visited = new HashSet<Neuron>();
        defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        editPathLabel = "Ändra förbindelse";
        newPathLabel = "Ny förbindelse";
        newNeuronLabel = "Ny plats";
        showPathLabel = "Visa förbindelse";
        findPathLabel = "Hitta väg";
        menuPanel = new JPanel();
        menuBar = new JMenuBar();
        allowNewNeuron = true;
        loadedFromFile = false;
        hasChanges = false;
        filter = new FileNameExtensionFilter("PathFinder projekt","drini");
        lines = new HashMap<Line, Boolean>();
        visited = new HashSet<Neuron>();
        buildMenu();
        addListeners();
        setMinimumSize(new Dimension(width, height));
        setSize(width, height);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());
        add(layerPanel, BorderLayout.CENTER);
        changeAllStateComponents(false);
        pack();
        repaint();
    }
    public static void main(String[] args) {
        win = new PathFinder();
       // win.loadFromFile("test.drini");
        /*    Neuron newestNeuron = new Neuron(50,249,"sthlm",win);
            Neuron newestNeuron2 = new Neuron(250,350,"polen",win);
            Neuron newestNeuron3 = new Neuron(350,140,"Helsinki",win);
            Neuron newestNeuron4 = new Neuron(400,240,"TAllin",win);
            win.layerPanel.add(newestNeuron,new Integer(1));
            win.layerPanel.add(newestNeuron2,new Integer(2));
            win.layerPanel.add(newestNeuron3,new Integer(3));
            win.layerPanel.add(newestNeuron4,new Integer(4));
            win.neuronListGraph.add(newestNeuron);
            win.neuronListGraph.add(newestNeuron2);
            win.neuronListGraph.add(newestNeuron3);
            win.neuronListGraph.add(newestNeuron4);
            win.neuronListGraph.connect(newestNeuron, newestNeuron2, "flyg", 10);
            win.neuronListGraph.connect(newestNeuron2, newestNeuron3, "båt", 20);
            win.neuronListGraph.connect(newestNeuron, newestNeuron4, "båt", 20);
            win.neuronListGraph.connect(newestNeuron3, newestNeuron4, "båt", 20);
            win.addLine("invokeOnLineClick", new Color(60, 118, 61), win.neuronListGraph, win.neuronListGraph.getNeuronPair(newestNeuron, newestNeuron2));
            win.addLine("invokeOnLineClick", new Color(60, 118, 61), win.neuronListGraph, win.neuronListGraph.getNeuronPair(newestNeuron2, newestNeuron3));
            win.addLine("invokeOnLineClick", new Color(60, 118, 61), win.neuronListGraph,win.neuronListGraph.getNeuronPair(newestNeuron,newestNeuron4));
            win.addLine("invokeOnLineClick", new Color(60, 118, 61), win.neuronListGraph,win.neuronListGraph.getNeuronPair(newestNeuron3, newestNeuron4));
    //        win.saveToFile("test.drini");*/
        win.pack();
        win.repaint();

    }
    private boolean saveToFile(String filePath) {
        //todo se om jag kan få bort neuronernas listeners också
        try {
            FileOutputStream saveFile = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(saveFile);
            // BufferedOutputStream save = new BufferedOutputStream(oos);
           oos.writeObject(bg.getPath());
        //   oos.writeObject(win);
         //  oos.writeObject(neuronListGraph.getAllNeurons());
          // oos.writeObject(neuronListGraph.getPairs());
        }catch(FileNotFoundException fnfe){
            JOptionPane.showMessageDialog(win,"Kunde inte hitta filen, eller så har den fel rättigheter");
        }catch (IOException ioe){

        }
        hasChanges = false;
        return true;
    }
    private void loadFromFile(String fileName){
        FileInputStream openFile = null;
        ObjectInputStream ois = null;
        try {
            openFile = new FileInputStream(fileName);
            ois = new ObjectInputStream(openFile);
            String bgImagePath = (String)ois.readObject();
            setBg(bgImagePath);
          //  ois.close();
//            win = (PathFinder)ois.readObject();

           // print(neuronListGraph.toString());
          //  ListGraph<Object> lista = (
//            println(neuronListGraph.getPairs().size());
//              println(lista.toString());
          /*  HashMap<Object,List<graphs.Edge>> allNeurons = (HashMap)ois.readObject();
            HashMap<NeuronPair, graphs.Edge> pairs = (HashMap)ois.readObject();
            neuronListGraph.setAllNeurons(allNeurons);
            neuronListGraph.setPairs(pairs);
            println(allNeurons.toString());
            println(pairs.toString());*/

        } catch (ClassNotFoundException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void invokeOnLineClick(NeuronPair neuronPair){
        win.editPath(neuronPair);
    }
    private void addLine(String invokeOnClick, Color lineColor,Graph graph, NeuronPair np) {
        Neuron n2 = (Neuron)np.getN2();
        Neuron n1 = (Neuron)np.getN1();
        Line newLine = new Line(win,invokeOnClick,lineColor,graph,np);
        lines.put(newLine,true);
        layerPanel.add(newLine);
        layerPanel.moveToFront(newLine);
        validate();
        pack();
        repaint();
    }
    private Neuron getFirstSelectedNeuron(){
        return getSelectedNeurons().get(0);
    }
    private Neuron getLastSelectedNeuron(){
        return getSelectedNeurons().get(1);
    }
    private ArrayList<Neuron> getSelectedNeurons(){
        return Neuron.selectedNeurons;
    }
    private NeuronPair getSelectedNeuronPair(){
        return neuronListGraph.getNeuronPair(getFirstSelectedNeuron(),getLastSelectedNeuron());
    }
    private boolean save(){
        boolean saved = false;
        if (loadedFromFile) {
            saveToFile(savedFilePath);
        } else {
            saved = saveAs();
        }
        hasChanges = !saved; //if save is true then there is no unsaved changes
        return saved;
    }
    private boolean saveAs() {
        // The name of the file to open.
        String str = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(str);
        fileChooser.setFileFilter(filter);
        int code = fileChooser.showSaveDialog(win);
        if(code == JFileChooser.APPROVE_OPTION) {
            String filePath;
            try{
                File file = fileChooser.getSelectedFile();
                // The name of the file to open.
                filePath = file.getAbsolutePath();

                saveToFile(filePath);
            }catch(Exception ex) {
                JOptionPane.showMessageDialog(win, "Kunde inte spara");
                return false;
            }
        }
        return true;
    }
    public static  void disableAllStateItems() {
        changeAllStateComponents(false);
    }
    private static void changeAllStateComponents(Boolean newState ) {
        for (int i = 0; i < hasStateComponents.size(); i++) {
            hasStateComponents.get(i).setEnabled(newState);
        }
        try {
            boolean newNeuronState = false;
            if (win.bg != null) {
                newNeuronState = true;
            }
            win.newNeuron.setEnabled(newNeuronState);
            win.newNeuronButton.setEnabled(newNeuronState);
        } catch (NullPointerException ne) {}
    }
    public static ArrayList<JComponent> getHasStateComponents() {
        return hasStateComponents;
    }
    public static void setHasStateComponents(ArrayList<JComponent> hasStateComponents) {
        hasStateComponents = hasStateComponents;
    }
    public static void enableAllStateItems() {
        changeAllStateComponents(true);
    }
    private void buildArchieve()  {
        arMenu = new JMenu("Arkiv");
        open = new JMenuItem("Öppna");
        newMap = new JMenuItem("Ny karta");
        exit = new JMenuItem("Avsluta");
        save = new JMenuItem("Spara");
        saveAs = new JMenuItem("Spara som");
        arMenu.add(open);
        arMenu.add(newMap);
        arMenu.add(save);
        arMenu.add(saveAs);
        arMenu.add(exit);
        menuBar.add(arMenu);
        arMenu.setMnemonic(KeyEvent.VK_A);
        open.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(214));
        newMap.setMnemonic(KeyEvent.VK_N);
        exit.setMnemonic(KeyEvent.VK_U);
        save.setMnemonic(KeyEvent.VK_S);
        save.setActionCommand("save");
        saveAs.setMnemonic(KeyEvent.VK_P);
        saveAs.setActionCommand("saveAs");
    }
    private void buildOperations(){
        opMenu = new JMenu("Operationer");
        findPath = new JMenuItem(findPathLabel);
        editPath = new JMenuItem(editPathLabel);
        newPath = new JMenuItem(newPathLabel);
        showPath = new JMenuItem(showPathLabel);
        newNeuron = new JMenuItem(newNeuronLabel);
        opMenu.add(findPath);
        opMenu.add(editPath);
        opMenu.add(newPath);
        opMenu.add(showPath);
        opMenu.add(newNeuron);
        opMenu.setMnemonic(KeyEvent.VK_O);
        findPath.setMnemonic(KeyEvent.VK_H);
        editPath.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(196));
        newPath.setMnemonic(KeyEvent.VK_N);
        showPath.setMnemonic(KeyEvent.VK_V);
        newNeuron.setMnemonic(KeyEvent.VK_P);
        menuBar.add(opMenu);
    }
    private void editPath(NeuronPair np){
        try{
            neuronListGraph.showEditEdgeDialog(win,np);
        }catch(Exception editPathError){
            JOptionPane.showMessageDialog(PathFinder.this.win, "Du kan bara editera om du valt två platser som det existerar en förbindelse mellan");
        }
    }
    private void editPath() {
        NeuronPair temp = new NeuronPair(getFirstSelectedNeuron(),getLastSelectedNeuron());
        editPath(temp);
    }
    private void buildMenu(){
        buildArchieve();
        buildOperations();
        setJMenuBar(menuBar);
        buildButtonPanel();
        setHasStateComponents();
    }
    private void buildButtonPanel() {
        add(menuPanel,BorderLayout.NORTH);
        findPathButton = new JButton(findPathLabel);
        editPathButton = new JButton(editPathLabel);
        newPathButton = new JButton(newPathLabel);
        showPathButton = new JButton(showPathLabel);
        newNeuronButton  = new JButton(newNeuronLabel);
        findPathButton.setMnemonic(KeyEvent.VK_H);
        editPathButton.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(196));
        newPathButton.setMnemonic(KeyEvent.VK_N);
        showPathButton.setMnemonic(KeyEvent.VK_V);
        newNeuronButton.setMnemonic(KeyEvent.VK_P);
        menuPanel.setLayout(new FlowLayout());
        menuPanel.add(findPathButton);
        menuPanel.add(editPathButton);
        menuPanel.add(newPathButton);
        menuPanel.add(showPathButton);
        menuPanel.add(newNeuronButton);
        Dimension mpD  = new Dimension(getWidth(),150);
        menuPanel.setMinimumSize(mpD);
//        menuPanel.setBounds(0,0,mpD.height,mpD.width);
//        win.layerPanel.add(menuPanel);
//        win.layerPanel.moveToFront(menuPanel);
    }
    private void setHasStateComponents(){
        hasStateComponents.add(findPath);
        hasStateComponents.add(editPath);
        hasStateComponents.add(newPath);
        hasStateComponents.add(showPath);
        hasStateComponents.add(newNeuron);
        hasStateComponents.add(newNeuronButton);
        hasStateComponents.add(findPathButton);
        hasStateComponents.add(editPathButton);
        hasStateComponents.add(newPathButton);
        hasStateComponents.add(showPathButton);
        changeAllStateComponents(false);
    }
    private void setBg(String path) {
        bg = new BgImage(path);
        hasChanges = true;
        width = bg.getWidth() + 15;
        height = bg.getHeight() + 99;//fixa marginalerna
        setSize(width, height);//fixa marginalerna
        setMinimumSize(new Dimension(width, height));
        repaint();
        disableAllStateItems(); //make sure all is false but not newNeuron actions
        setVisible(true);
        layerPanel.add(bg);
        layerPanel.moveToBack(bg);
    }
    private void createNewPath() {
        JTextField edgeName = new JTextField(20);
        JTextField edgeWeight = new JTextField(20);
        JPanel newEdgePanel;
        newEdgePanel = new JPanel(new GridLayout(3,1,5,2));
        newEdgePanel.add(new JLabel("Vad ska förbindelsen heta?"));
        newEdgePanel.add(edgeName);
        newEdgePanel.add(new JLabel("Hur lång tid tar det?"));
        newEdgePanel.add(edgeWeight);
        Neuron n1 = getFirstSelectedNeuron();
        Neuron n2 = getLastSelectedNeuron();
        String title = String.format("Skapa förbindelse mellan %s och %s", n1.getName(), n2.getName());
        //"complex" just to be able to set focus
        boolean doLoop = true;
        while(doLoop){
            int result = JOptionPane.showOptionDialog(win, newEdgePanel,
                    title,JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"OK", "Cancel"}, edgeName);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    if(edgeName.getText().length()<1){
                        JOptionPane.showMessageDialog(win, "Ett Namn,bitte,tack,thankyou!");
                    }else{
                        neuronListGraph.connect(n1, n2, edgeName.getText(), Integer.parseInt(edgeWeight.getText()));
                        doLoop = false;
                    }
                } catch (NullPointerException nullE) {
                    JOptionPane.showMessageDialog(win, "Du fyllde inte i alla fällt");
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(win,"Tid får bara vara heltal!");
                }

            }else{
                doLoop= false;
            }
        }
//        win.addLine("invokeOnLineClick",new Color(60,118,61),newestNeuron.getPosX(),newestNeuron.getPosY(),newestNeuron3.getPosX(),newestNeuron3.getPo

        win.addLine("invokeOnLineClick", new Color(60, 118, 61), neuronListGraph, neuronListGraph.getNeuronPair(n1, n2));
        validate();
        repaint();
    }
    private void addListeners() {
        saveL = new SaveListener();
        save.addActionListener(saveL);
        saveAs.addActionListener(saveL);

        ml = new MyWindowListener();
        addWindowListener(ml);

        el = new ExitListener();
        exit.addActionListener(el);

        openL = new OpenListener();
        open.addActionListener(openL);

        mpl = new NewMapListener();
        newMap.addActionListener(mpl);

        newPathL = new NewPathListener();
        newPath.addActionListener(newPathL);
        newPathButton.addActionListener(newPathL);

        newNeuronL = new NewNeuronListener();
        newNeuron.addActionListener(newNeuronL);
        newNeuronButton.addActionListener(newNeuronL);

        spl = new ShowPathListener();
        showPathButton.addActionListener(spl);
        showPath.addActionListener(spl);

        epl = new EditPathListener();
        editPath.addActionListener(epl);
        editPathButton.addActionListener(epl);

        fpl = new FindPathListener();
        findPath.addActionListener(fpl);
        findPathButton.addActionListener(fpl);

    }
    private void removeListeners() {
        //-todo inte alla listeners raderas här, bad practices då vissa försvinner i saveToFile pga referensproblem(tidsbrist)
        save.removeActionListener(saveL);
        saveAs.removeActionListener(saveL);
        removeWindowListener(ml);
        exit.removeActionListener(el);
        open.removeActionListener(openL);
        newMap.removeActionListener(mpl);
        newPath.removeActionListener(newPathL);
        newPathButton.removeActionListener(newPathL);
        newNeuron.removeActionListener(newNeuronL);
        newNeuronButton.removeActionListener(newNeuronL);
        showPathButton.removeActionListener(spl);
        showPath.removeActionListener(spl);
        editPath.removeActionListener(epl);
        editPathButton.removeActionListener(epl);
        findPath.removeActionListener(fpl);
        findPathButton.removeActionListener(fpl);
    }
    private class OpenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = System.getProperty("user.dir");
            JFileChooser fileChooser = new JFileChooser(str);
            fileChooser.setFileFilter(filter);
            int code = fileChooser.showOpenDialog(win);
            if(code == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // The name of the file to open.
                String fileName = file.getAbsolutePath();
                loadFromFile(fileName);
            /*    // This will reference one line at a time
                String line = null;
                try {

                    FileInputStream saveFile = new FileInputStream(fileName);
                    ObjectInputStream restore = new ObjectInputStream(saveFile);
                    try {
                        String bgPath = (String) restore.readObject();
                        //win.neuronListGraph = (ListGraph)restore.readObject();
                        setBg(bgPath);
                        JOptionPane.showMessageDialog(win,"Laddat in");
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    loadedFromFile = true;
                    savedFilePath = fileName;
//                    bufferedReader.close();
                }catch(FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(win,"Kunde inte hitta filen'" +fileName + "'");
                }catch(IOException ex) {
                    JOptionPane.showMessageDialog(win, "Kunde inte läsa från '" + fileName + "'");
                }*/
            }
        }
    }
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionName = e.getActionCommand();
            if(actionName.equals("save"))
                save();
            if (actionName.equals("saveAs"))
                saveAs();
        }
    }
    private class NewMapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (bg != null) {
                if (hasChanges)
                    confirmExit(win);
                //reset everything
                remove(bg);
            }
            String str = System.getProperty("user.dir");
            JFileChooser fileChooser = new JFileChooser(str);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Bilder","jpg","gif","png");
            fileChooser.setFileFilter(filter);
            int code = fileChooser.showOpenDialog(win);
            if(code == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                setBg(file.getAbsolutePath());
            }
        }
    }
    private class myMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (win.allowNewNeuron) {
                try{
                    win.allowNewNeuron = false;
                    setCursor(defaultCursor);
                    int posX = e.getX()-15;
                    int posY = e.getY()-75;
                    if(posX < bg.getX() || posY < bg.getY()){
                        JOptionPane.showMessageDialog(win, "Du måste klicka på kartan!");
                    }else{
                        String neuronName = JOptionPane.showInputDialog(win,"Platsens namn","Ny plats",JOptionPane.PLAIN_MESSAGE);
                        if ((neuronName !=null) & (neuronName.length() > 0)) {


                            Neuron newestNeuron = new Neuron(posX,posY,neuronName,win);
                            layerPanel.add(newestNeuron);
                            layerPanel.moveToFront(newestNeuron);
                            neuronListGraph.add(newestNeuron);
                            hasChanges =  true;
                            allowNewNeuron = true;
                            validate();
                            repaint();
                            pack();
                        }
                    }
                }catch(NullPointerException n){
                    JOptionPane.showMessageDialog(win, "Du måste ange ett namn!");
                    allowNewNeuron = true;
                }
            }
            addMouseListener(mListener);
        }
    }
    private class NewNeuronListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(allowNewNeuron){
                setCursor(crosshairCursor); //setCursor(int) is depricated
                addMouseListener(mListener);
            }
        }
    }
    private class MyWindowListener extends WindowAdapter implements ActionListener {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            handleExit();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handleExit();
        }
        private void handleExit() {
            if(!hasChanges){
                System.exit(0);
            }
            Boolean[] result = confirmExit(win);
            if (result[1]) { //användaren ville spara
                try {
                    save();
                } catch (Exception saveError) {
                    JOptionPane.showMessageDialog(win,saveError,"Något gick fel",JOptionPane.ERROR_MESSAGE);
                    result[0]=false;
                }
            }
            if (result[0]) {  //användaren ville exita
                System.exit(0);
            }
        }
    }
    private class EditPathListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            editPath();
        }
    }
    private class ShowPathListener implements ActionListener{
        public void alert(Neuron n1,Neuron n2){
            int answer = JOptionPane.showConfirmDialog(win,
                    "Finns ingen förbindelse mellan,vill du skapa en?", "Ooops!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                createNewPath();
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Neuron n1 = null;
            Neuron n2 = null;
            try{
                NeuronPair pair = getSelectedNeuronPair();
                n1 = (Neuron)pair.getN1();
                n2 = (Neuron)pair.getN2();
                pair = neuronListGraph.getNeuronPair(n1,n2);
                neuronListGraph.showViewEdgeDialog(win, pair);
            }catch (NoSuchElementException nee){
                alert(n1,n2);
            }catch(NullPointerException npe){
                alert(n1,n2);
            }
            //todo måste prova söka väg

            return;
        }
    }
    private class FindPathListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!graphs.GraphMethods.pathExists(getFirstSelectedNeuron(),getLastSelectedNeuron(),neuronListGraph)){
                JOptionPane.showMessageDialog(win,"Finns tyvärr ingen väg");
                return;
            }
            displayPath();
        }
    }
    private Object[] convertListToArray(List<?> path){
        Object[] arr = new Object[path.size()];
        for (int i = 0; i < path.size(); i++) {
            Object o = path.get(i);
            arr[i] = o;
        }
        return arr;
    }
    @Override
    public void repaint(long time, int x, int y, int width, int height) {

        super.repaint(time, x, y, width, height);
    }
    private void displayPath() {
        //Fetches a list of Edges really but we neither cant(not allowed due to specs) or need to cast them here anyway
        List<?> path = GraphMethods.getPath(getFirstSelectedNeuron(), getLastSelectedNeuron(), neuronListGraph);

        //for the jlist we need them in an object array
        Object[] arr = convertListToArray(path);
        //since we are not allowed to cast to edges here we have to send it to the graphmethods that does the work for us
        int totalTime = GraphMethods.getTotalPathTime(path);
        //init components
        JList list = new JList(arr);
        JScrollPane listScroller = new JScrollPane(list);
        JButton editThisPath = new JButton("Ändra vald förbindelse");
        JDialog dialog = new JDialog(win,"Snabbaste vägen tog "+totalTime,true); //I just happend to think JDialogs are the cutest option

        list.clearSelection();//just to make sure if some randow bug could appear
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//only one selected is allowed
        //styles, because default is boring right?
        list.setBackground(new Color(0, 136, 204));
        list.setForeground(new Color(230, 230, 230));
        list.setFont(new Font("Serif", Font.PLAIN, 20));
        list.setMinimumSize(new Dimension(dialog.getWidth(), 100));

        editThisPath.setBackground(new Color(98, 196, 98));
        editThisPath.setForeground(Color.WHITE);

        dialog.add(listScroller, BorderLayout.PAGE_START);//kompass options etc is "depricated" but still working old style so I _try_ to remember that everywher
        dialog.add(editThisPath, BorderLayout.CENTER);
        editThisPath.addActionListener(new ListPathListener(dialog, path, list));
        dialog.pack();
        dialog.setVisible(true);
    }
    private class ListPathListener implements ActionListener {
        private JList list;
        private List<?> path;
        private JDialog dialog;

        private ListPathListener(JDialog dialog,List<?> path,JList list) {
            this.list = list;
            this.path = path;
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            for (int i = 0; i < path.size(); i++) {
                if(index==i){
                    NeuronPair found = neuronListGraph.findEdge(path.get(i));
                    if(found != null){
                        neuronListGraph.showEditEdgeDialog(win, found);
                        dialog.pack();
                    }
                }
            }

        }
    }
    private class NewPathListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            createNewPath();
        }
    }
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            confirmExit(win,true);
        }
    }
}
