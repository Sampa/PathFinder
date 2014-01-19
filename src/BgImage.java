import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

class BgImage extends JPanel{
    private Image bg;
    private int width;
    private int height;
    private String path;

    public String getPath() {
        return path;
    }

    public BgImage(String path){
        setLayout(null);
        try {
            this.path = path;
            BufferedImage bimg = ImageIO.read(new File(path));
            width = bimg.getWidth();
            height= bimg.getHeight();
            bg=bimg.getScaledInstance(width,height,0);
        }catch (Exception error) {
           System.out.print(error.getMessage() + "");
        }

        setBounds(0, 37, width, height);
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0,0,width,height,this);
    }

}
