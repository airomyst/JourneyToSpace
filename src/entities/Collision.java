package entities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Collision extends GameObject{

    //defines the collision between the shuttle and objects
    private static BufferedImage Image;
    static {
        try {
            Image = ImageIO.read(new File("./res/objects/explosionSheet.png")).getSubimage(0,0,128,128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collision(int x, int y) {
        super(x-32,y-40,128,128);
    }

    @Override
    public void update() {
    }

    public void draw(Graphics g) {
        g.drawImage(Image, getX(), getY(), null);
    }
}