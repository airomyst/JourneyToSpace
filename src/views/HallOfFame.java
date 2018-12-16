package views;

import journey_to_space.CONSTANTS;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HallOfFame extends JFrame {

    private JTextArea scoresTxtArea = new JTextArea();
    private static ArrayList<Integer> bestScores = new ArrayList<>();

    public HallOfFame(Menu menu, GraphicsDevice gd) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        Font fnt1 = new Font("arial", Font.BOLD, 30);
        setLayout(new GridLayout(3, 1));

        scoresTxtArea.setEnabled(false);
        scoresTxtArea.setFont(fnt1);
        scoresTxtArea.setDisabledTextColor(Color.black);
        JScrollPane scrollPane = new JScrollPane(scoresTxtArea);
        add(scrollPane);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(fnt1);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.white);
        btnBack.setFocusPainted(false);
        add(btnBack);

        btnBack.addActionListener(actionEvent -> {
            gd.setFullScreenWindow(menu);
            setVisible(false);
        });
    }

    public static String readFile(String path, Charset encoding){
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
        return new String(encoded, encoding);
    }

    public void LoadScores() {
        File f = new File("Score.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String scores = readFile("Score.txt", StandardCharsets.UTF_8);
            for (String score : scores.split(System.lineSeparator())) {
                try {
                    bestScores.add(Integer.parseInt(score));
                } catch (NumberFormatException ignored){
                }
            }
            try {
                CONSTANTS.BestScore = bestScores.get(0);
            } catch (IndexOutOfBoundsException ignored){
            }
        }
        updateScoresTxtArea();
    }

    private void updateScoresTxtArea(){
        scoresTxtArea.setText("Highest Scores:\n");
        for (int i = 0; i <bestScores.size(); i++) {
            if (i == 0) {
                this.scoresTxtArea.setText(scoresTxtArea.getText()+ "1st:" +bestScores.get(i));
            } else if (i == 1) {
                this.scoresTxtArea.setText(scoresTxtArea.getText() + "\n" + "2nd:" +bestScores.get(i));
            } else if (i == 2) {
                this.scoresTxtArea.setText(scoresTxtArea.getText() + "\n" + "3rd:" +bestScores.get(i));
            } else {
                this.scoresTxtArea.setText(scoresTxtArea.getText() + "\n" + (i + 1) + "th:" + bestScores.get(i));
            }
        }
    }

    public void saveNewBestScore() {
        if (CONSTANTS.SCORE > CONSTANTS.BestScore) {
            CONSTANTS.BestScore = CONSTANTS.SCORE;
            bestScores.add(CONSTANTS.SCORE);
            bestScores.sort(Collections.reverseOrder());
        } else {
            return;
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter("Score.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter pw = new PrintWriter(fw, false);
        for (Integer bestScore : bestScores)
            pw.println(bestScore);
        pw.close();
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateScoresTxtArea();
    }
}
