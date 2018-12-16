package views;

import journey_to_space.Game;
import journey_to_space.STATE;
import journey_to_space.CONSTANTS;

import java.awt.*;
import javax.swing.*;


public class Menu extends JFrame {

    private HallOfFame hallOfFame;

    public Menu(Game game, GameWindow gameWindow, GraphicsDevice vc) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new GridLayout(4, 1, 0, 10));

        hallOfFame = new HallOfFame(this, vc);
        hallOfFame.LoadScores();

        Font fnt0 = new Font("arial", Font.BOLD, 50);
        Font fnt1 = new Font("arial", Font.BOLD, 30);

        JLabel lblTitle = new JLabel("Journey To Space", SwingConstants.CENTER);
        lblTitle.setFont(fnt0);
        JButton btnPlay = new JButton("Play");
        btnPlay.setFont(fnt1);
        btnPlay.setBackground(Color.BLACK);
        btnPlay.setForeground(Color.white);
        btnPlay.setFocusPainted(false);
        JButton btnHall = new JButton("Hall of fame");
        btnHall.setFont(fnt1);
        btnHall.setBackground(Color.BLACK);
        btnHall.setForeground(Color.white);
        btnHall.setFocusPainted(false);
        JButton btnQuit = new JButton("Quit");
        btnQuit.setFont(fnt1);
        btnQuit.setBackground(Color.BLACK);
        btnQuit.setForeground(Color.white);
        btnQuit.setFocusPainted(false);

        pnlButtons.add(lblTitle);
        pnlButtons.add(btnPlay);
        pnlButtons.add(btnHall);
        pnlButtons.add(btnQuit);

        add(pnlButtons, BorderLayout.CENTER);

        btnPlay.addActionListener(arg0 -> {
            CONSTANTS.state = STATE.GAME;
            vc.setFullScreenWindow(gameWindow);
            setVisible(false);
            game.start();
        });

        btnHall.addActionListener(arg0 -> {
            vc.setFullScreenWindow(hallOfFame);
            setVisible(false);
        });

        btnQuit.addActionListener(e -> System.exit(0));
    }

    public void updateHallOfFame(){
        hallOfFame.saveNewBestScore();
    }
}