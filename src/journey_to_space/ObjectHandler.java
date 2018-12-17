package journey_to_space;

import entities.*;

import java.awt.*;
import javax.swing.*;
import java.util.LinkedList;


public class ObjectHandler {

    private Game game;
    private boolean isHit;
    private Shuttle shuttle;
    private Collision collision;
    private LinkedList<Rock> rocks = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();
    private LinkedList<FlamingRock> frocks = new LinkedList<>();
    private LinkedList<Explosion> explosions = new LinkedList<>();

    public ObjectHandler(Game game){
        this.game = game;
    }

    public void update(){
        //check for a collision between the shuttle and a rock
        if(isHit){
            if(shuttle.getHP()!=0) {
                CONSTANTS.state = STATE.WARNING;
                JOptionPane.showInternalMessageDialog(game.getGameWindow().getContentPane(),
                        "Remaining lives: " + shuttle.getHP(), "", JOptionPane.ERROR_MESSAGE);
                CONSTANTS.state = STATE.GAME;
            } else {
                CONSTANTS.state = STATE.WARNING;
                JOptionPane.showInternalMessageDialog(game.getGameWindow().getContentPane(),
                         "Game over!", "", JOptionPane.ERROR_MESSAGE);
                CONSTANTS.state = STATE.MENU;
            }
            flushObjects();
        }

        //updates all the bullets
        for(int i =0;i<bullets.size();i++){
            Bullet tempBullet = bullets.get(i);
            if(tempBullet.getY() < -tempBullet.getHeight())
                removeBullet(tempBullet);
            else
                tempBullet.update();
        }

        shuttle.update(); //updates the shuttle

        //updates all the rocks
        for(int i=0;i<rocks.size();i++){
            Rock tempRock = rocks.get(i);
            for(int j=0;j<bullets.size();j++){
                Bullet tempBullet = bullets.get(j);
                if(tempBullet.getBounds().intersects(tempRock.getBounds())){
                    addExplosion(new Explosion(tempRock.getX(), tempBullet.getY()));
                    tempRock.applyDmg();
                    removeBullet(tempBullet);
                    CONSTANTS.SCORE++;
                }
            }

            if(tempRock.getY()>CONSTANTS.HEIGHT+tempRock.getHeight()) {
                removeRock(tempRock); //remove if out of screen
            } else if(tempRock.getHP()==0){
                removeRock(tempRock);
                CONSTANTS.SCORE+=tempRock.getWidth()/12;
            } else {
                tempRock.update();
            }
            applyCollisionIfHappened(tempRock.getBounds());
        }

        //updates all explosions
        for(int i=0;i<explosions.size();i++){
            Explosion tempExp = explosions.get(i);
            if(tempExp.getRowNum()==3 && tempExp.getColNum()==3)
                removeExplosion(tempExp);
        }

        //updates all the flaming rocks
        for(int i=0;i<frocks.size();i++){
            FlamingRock tempFrock = frocks.get(i);
            if(tempFrock.getY()>CONSTANTS.HEIGHT)
                removeFlamingRock(tempFrock);
            else
                tempFrock.update();
            applyCollisionIfHappened(tempFrock.getBounds());
        }
    }

    private void applyCollisionIfHappened(Rectangle bounds) {
        if(bounds.intersects(shuttle.getBounds())) {
            //applies the collision of an object with the shuttle
            Rectangle coordinates = bounds.intersection(shuttle.getBounds());
            isHit = true;
            shuttle.applyDmg();
            collision  = new Collision(coordinates.x, coordinates.y);
        }
    }

    public void draw(Graphics g){
        for(int i=0;i<bullets.size();i++)
            bullets.get(i).draw(g);
        for(int i=0;i<rocks.size();i++)
            rocks.get(i).draw(g);
        for(int i=0;i<explosions.size();i++)
            explosions.get(i).draw(g);
        for(int i=0;i<frocks.size();i++)
            frocks.get(i).draw(g);

        shuttle.draw(g);
        if(collision!=null) {
            collision.draw(g);
            game.getBufferStrategy().show();
        }
    }

    public void setShuttle(Shuttle shuttle){
        this.shuttle = shuttle;
    }

    public Shuttle getShuttle(){
        return shuttle;
    }

    public void addBullet(Bullet bullet){
        bullets.add(bullet);
    }

    public void removeBullet(Bullet bullet){
        bullets.remove(bullet);
    }

    public void addExplosion(Explosion explosion){
        explosions.add(explosion);
    }

    public void removeExplosion(Explosion explosion){
        explosions.remove(explosion);
    }

    public void addRock(Rock rock){
        rocks.add(rock);
    }

    public void removeRock(Rock rock){
        rocks.remove(rock);
    }

    public LinkedList<Rock> getRocks(){
        return rocks;
    }

    public LinkedList<FlamingRock> getFlamingRocks(){
        return frocks;
    }

    public void addFlamingRock(FlamingRock frock){
        frocks.add(frock);
    }

    public void removeFlamingRock(FlamingRock frock){
        frocks.remove(frock);
    }

    public void flushObjects(){
        rocks = new LinkedList<>();
        frocks = new LinkedList<>();
        isHit=false;
        collision=null;
    }
}
