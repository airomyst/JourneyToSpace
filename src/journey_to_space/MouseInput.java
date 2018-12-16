package journey_to_space;

import entities.Bullet;
import entities.Shuttle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class MouseInput extends MouseAdapter {
    private boolean paused = false;
    private ObjectHandler objectHandler;

    private Thread thread = new Thread(() -> {
        while(true) {
            if(!paused && CONSTANTS.state==STATE.GAME) {
                objectHandler.addBullet(new Bullet(objectHandler.getShuttle().getX() + 44,
                        objectHandler.getShuttle().getY() - 25));
                try {
                    Thread.sleep(220);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } else {
                //do nothing
                if(CONSTANTS.state!=STATE.GAME) paused=true;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public MouseInput(ObjectHandler objectHandler){
        this.objectHandler = objectHandler;
    }

    public void mouseMoved(MouseEvent e){
        Shuttle shuttle = objectHandler.getShuttle();

        if(!(e.getY()>CONSTANTS.HEIGHT-shuttle.getHeight()))
            shuttle.setY(e.getY());
        else
            shuttle.setY(CONSTANTS.HEIGHT-shuttle.getHeight());

        if(!(e.getX()>CONSTANTS.WIDTH-shuttle.getWidth()))
            shuttle.setX(e.getX());
        else
            shuttle.setX(CONSTANTS.WIDTH-shuttle.getWidth());
    }

    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }

    public void mousePressed(MouseEvent e){
        if(thread.getState()==Thread.State.NEW)
            thread.start();
        else
            paused = false;
    }

    public void mouseReleased(MouseEvent e){
        paused = true;
    }
}
