package entities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Rock extends GameObject implements Attackable {

    private static BufferedImage Image;
    static {
        try {
            Image = ImageIO.read(new File("./res/objects/rocks_rotated.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int HP=3;
    private int ColNum, RowNum; // marks last sprite from sprite sheet
    private long lastSpriteTime;
    public static final int defaultDimension=63;

    public void applyDmg(){
        HP--;
    }

    public int getHP(){
        return HP;
    }

    public void setHP(int HP){
        this.HP = HP;
    }

    public Rock(int x, int y) {
        super(x, y, defaultDimension, defaultDimension);
        lastSpriteTime = System.currentTimeMillis();
        setVelY(3);
    }

    @Override
    public void update() {
        setY(getY()+getVelY());
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage subImage = Image.getSubimage(ColNum*256+37,RowNum*256+37,180,190);
        g.drawImage(getAdjustedImage(subImage), getX(), getY(), null);
        if (System.currentTimeMillis() - lastSpriteTime > 70) {
            lastSpriteTime = System.currentTimeMillis();
            if(ColNum==7) RowNum = (RowNum+1)%4;
            ColNum = (ColNum+1)%8;
        }
    }
}