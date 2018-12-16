package views;

import journey_to_space.Game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;


public class GameWindow extends JFrame{

    public GameWindow(String title, Game game) {
        setTitle(title);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");
        setCursor(invisibleCursor);
        add(game);
    }
}
