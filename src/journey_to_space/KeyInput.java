package journey_to_space;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


public class KeyInput extends KeyAdapter {

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            CONSTANTS.state = STATE.MENU;
    }
}

