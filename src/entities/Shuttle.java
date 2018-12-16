package entities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Shuttle extends GameObject implements Attackable {

    private static BufferedImage Image;
    static{
        try {
            Image = ImageIO.read(new File("./res/objects/index1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int HP;

    public Shuttle(int x, int y){
        super(x,y, 96, 96);
    }

    public void update() {
    }

    public void applyDmg(){
        HP--;
    }

    public int getHP(){
        return HP;
    }

    public void setHP(int HP){
        this.HP = HP;
    }

    public void increaseHP(){
        if(HP<3) HP++;
    }

    public void draw(Graphics g){
        g.drawImage(getAdjustedImage(Image),getX(),getY(),null);
    }
}
