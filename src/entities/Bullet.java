package entities;

import java.io.File;
import java.awt.Graphics;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Bullet extends GameObject {

    private static int counter;
    private static Clip[] clip = new Clip[5];
    private static BufferedImage Image;
    static {
        try {
            Image = ImageIO.read(new File("./res/objects/index2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for(int i=0;i<5;i++) {
                clip[i] = AudioSystem.getClip();
                clip[i].open(AudioSystem.getAudioInputStream(new File("./res/sound/shot.wav")));
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {
        }
    }

    public Bullet(int x, int y){
        super(x,y, Image.getWidth(), Image.getHeight());
        setVelY(-5);

        //in case of concurrent shots an array of clips is made to play more than one shot sound at once
        clip[counter].stop();
        clip[counter].setFramePosition(0);
        clip[counter].start();
        counter = (counter+1)%5;
    }

    public void update(){
        setY(getY()+getVelY());
    }

    public void draw(Graphics g){
        g.drawImage(Image,getX(),getY(),null);
    }
}
