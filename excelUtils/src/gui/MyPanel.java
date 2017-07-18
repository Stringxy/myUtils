package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xy on 2017/7/17.
 */
public class MyPanel extends JPanel {
    @Override
    public void paintComponent(Graphics gs) {
        Graphics2D g = (Graphics2D) gs;
        super.paintComponent(g);

        InputStream in;
        try {
            String src = "/background.jpg";
            // 画背景图片
            // Image image =
            // Toolkit.getDefaultToolkit().getImage(getClass().getResource("sk.png"));
            Image image =ImageIO.read(getClass().getResource(src));
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),
                    this);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
