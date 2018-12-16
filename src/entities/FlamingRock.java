package entities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class FlamingRock extends GameObject {

    private static BufferedImage Image;
    static {
        try {
            Image = ImageIO.read(new File("./res/objects/flamingRocks.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long lastSpriteTime;
    private int ColNum=0, RowNum=0; // marks last sprite from sprite sheet
    public static final int defaultDimension=75;

    public FlamingRock(int x, int y) {
        super(x, y, 75 ,75);
        lastSpriteTime = System.currentTimeMillis();
        setVelY(3);
    }

    @Override
    public void update() {
        setY(getY()+getVelY());
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage subImage = Image.getSubimage(ColNum*252+30,RowNum*252+30,222,222);
        g.drawImage(getAdjustedImage(subImage), getX(), getY(), null);

        if (System.currentTimeMillis() - lastSpriteTime > 55) {
            lastSpriteTime = System.currentTimeMillis();
            if(ColNum==3) RowNum = (RowNum+1)%4;
            ColNum = (ColNum+1)%4;
        }
    }
}
