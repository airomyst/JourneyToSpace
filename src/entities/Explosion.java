package entities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Explosion extends GameObject{

    private static BufferedImage Image;
    static {
        try {
            Image = ImageIO.read(new File("./res/objects/explosionSheet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int RowNum, ColNum; // marks last sprite from sprite sheet
    private long lastSpriteTime;

    public Explosion(int x, int y) {
        super(x,y, 77, 77);
        lastSpriteTime = System.currentTimeMillis();
        setHeight(77);
        setWidth(77);
    }

    @Override
    public void update() {
    }

    public void draw(Graphics g) {
        BufferedImage subImage = Image.getSubimage(ColNum*128,RowNum*128,128,128);
        g.drawImage(getAdjustedImage(subImage), getX(), getY(), null);

        if (System.currentTimeMillis() - lastSpriteTime > 30) {
            lastSpriteTime = System.currentTimeMillis();
            if(ColNum==3) RowNum = (RowNum+1)%4;
            ColNum = (ColNum+1)%4;
        }
    }

    public int getRowNum(){
        return RowNum;
    }

    public int getColNum(){
        return ColNum;
    }

}