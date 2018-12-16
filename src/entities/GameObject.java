package entities;

import java.awt.*;
import java.awt.image.BufferedImage;


public abstract class GameObject {

    private int velY;
    private Rectangle objBounds = new Rectangle();

    public GameObject(int x, int y, int width, int height){
        objBounds.x=x;
        objBounds.y=y;
        objBounds.width = width;
        objBounds.height = height;
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    public void setX(int x) {
        objBounds.x = x;
    }

    public void setY(int y){
        objBounds.y=y;
    }

    public int getX(){
        return objBounds.x;
    }

    public int getY(){
        return objBounds.y;
    }

    public void setVelY(int velY){
        this.velY=velY;
    }

    public int getVelY(){
        return velY;
    }

    public int getWidth() {
        return objBounds.width;
    }

    public int getHeight(){
        return objBounds.height;
    }

    public void setWidth(int width){
        objBounds.width =width;
    }

    public void setHeight(int height){
        objBounds.height=height;
    }

    public Rectangle getBounds(){
        return objBounds;
    }

    public BufferedImage getAdjustedImage(BufferedImage imageToResize){
        BufferedImage resizedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(imageToResize, 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
        return resizedImage;
    }
}
