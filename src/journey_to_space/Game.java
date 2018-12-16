package journey_to_space;

import views.Menu;
import entities.Rock;
import entities.Shuttle;
import views.GameWindow;
import entities.GameObject;
import entities.FlamingRock;

import java.awt.*;
import java.io.File;
import java.util.Random;
import javax.swing.Timer;
import java.util.ArrayList;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;


public class Game extends Canvas implements Runnable {

    private static ArrayList<BufferedImage> BackgroundImages = new ArrayList<>();
    static {
        try {
            for(int i = 0; i < 49; i++)
                BackgroundImages.add(ImageIO.read(new File("./res/background/"+i+".jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int counter;
    private Thread thread;
    private Timer RockTimer;
    private views.Menu menu;
    private GraphicsDevice gd;
    private long lastFrameTime;
    private int prevLvlScore=80;
    private GameWindow gameWindow;
    private Timer flamingRockTimer;
    private ObjectHandler objectHandler;
    private Random rand_gen = new Random();

    private Game(){
        setFocusable(true);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./res/sound/BGS.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = env.getDefaultScreenDevice();

        objectHandler = new ObjectHandler(this);
        objectHandler.setShuttle(new Shuttle(CONSTANTS.WIDTH/4,CONSTANTS.HEIGHT/2));
        gameWindow = new GameWindow("journey_to_space", this);
        menu = new Menu(this, gameWindow, gd);
        gd.setFullScreenWindow(menu);

        addKeyListener(new KeyInput());
        MouseInput mi = new MouseInput(objectHandler);
        addMouseListener(mi);
        addMouseMotionListener(mi);
    }

    public void initializeGame(){
        lastFrameTime = System.currentTimeMillis();
        RockTimer = new Timer(1500, e -> {
            int randomSPOT = rand_gen.nextInt();
            while(!isFreeSpot(randomSPOT, Rock.defaultDimension))
                randomSPOT=rand_gen.nextInt();
            objectHandler.addRock(new Rock(rand_gen.nextInt(CONSTANTS.WIDTH-100),0));
        });
        RockTimer.start();

        objectHandler.flushObjects();
        objectHandler.getShuttle().setHP(3);
        CONSTANTS.SCORE=0;
        CONSTANTS.LEVEL=0;
    }

    public synchronized void start(){
        initializeGame();
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        try {
            thread.interrupt();
            RockTimer.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double lastFrameTime = System.nanoTime();
        double currentTime;
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
        int lastSecond = (int) (lastFrameTime / 1000000000);
        while (true) {
            currentTime = System.nanoTime();
            update();
            draw();
            int thisSecond = (int) (lastFrameTime / 1000000000);
            if (thisSecond > lastSecond)
                lastSecond = thisSecond;

            lastFrameTime = currentTime;
            while (currentTime - lastFrameTime < TARGET_TIME_BETWEEN_RENDERS) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentTime = System.nanoTime();
            }
            if(CONSTANTS.state == STATE.MENU){
                gd.setFullScreenWindow(menu);
                gameWindow.setVisible(false);
                menu.updateHallOfFame();
                break;
            }
        }
        stop();
    }

    private void draw(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(BackgroundImages.get(counter),0,0, CONSTANTS.WIDTH, CONSTANTS.HEIGHT, null);

        if(System.currentTimeMillis()-lastFrameTime>90){
            counter = (counter+1)%49;
            lastFrameTime=System.currentTimeMillis();
        }

        objectHandler.draw(g);
        g.setColor(Color.gray);
        g.fillRect(10,25,180,32);
        g.setColor(Color.red);
        g.fillRect(10,25,objectHandler.getShuttle().getHP()*60,32);
        g.setColor(Color.white);
        g.drawString("Score: "+CONSTANTS.SCORE+"   Level: "+CONSTANTS.LEVEL,10,15);
        g.dispose();
        bs.show();
    }

    private void update(){
        objectHandler.update();

        if(CONSTANTS.SCORE>prevLvlScore){
            if(CONSTANTS.LEVEL<=15) {
                objectHandler.getShuttle().increaseHP();
                CONSTANTS.LEVEL++;
                prevLvlScore += 120;

                RockTimer.stop();
                int delay;
                if(CONSTANTS.LEVEL%2==0)
                    delay = 1300-(CONSTANTS.LEVEL-1)*12;
                else
                    delay = 1300-CONSTANTS.LEVEL*12;

                RockTimer = new Timer(delay, e -> {
                    int INCREASED_DIMENSION =
                            (CONSTANTS.LEVEL % 2)==0? (CONSTANTS.LEVEL-1) * 3 + Rock.defaultDimension :
                                    CONSTANTS.LEVEL*3+Rock.defaultDimension;

                    int randomSPOT = rand_gen.nextInt(CONSTANTS.WIDTH - INCREASED_DIMENSION);
                    while (!isFreeSpot(randomSPOT, INCREASED_DIMENSION))
                        randomSPOT = rand_gen.nextInt(CONSTANTS.WIDTH - INCREASED_DIMENSION);

                    Rock newRock = new Rock(randomSPOT, 0);
                    newRock.setVelY(newRock.getVelY() + CONSTANTS.LEVEL % 3);
                    newRock.setHeight(INCREASED_DIMENSION);
                    newRock.setWidth(INCREASED_DIMENSION);
                    newRock.setHP(newRock.getHP() + CONSTANTS.LEVEL / 3);
                    objectHandler.addRock(newRock);
                });
                RockTimer.setInitialDelay(0);
                RockTimer.start();
            }
            if((CONSTANTS.LEVEL>1 && CONSTANTS.LEVEL<5) || (CONSTANTS.LEVEL>6 && CONSTANTS.LEVEL<10) ||
                    (CONSTANTS.LEVEL>12 && CONSTANTS.LEVEL<=15)) {
                if(flamingRockTimer!=null)
                    flamingRockTimer.stop();

                flamingRockTimer = new Timer(2000-(CONSTANTS.LEVEL%2)*100, e -> {
                    int randomSPOT = rand_gen.nextInt(CONSTANTS.WIDTH- FlamingRock.defaultDimension);
                    while(!isFreeSpot(randomSPOT, FlamingRock.defaultDimension))
                        randomSPOT=rand_gen.nextInt(CONSTANTS.WIDTH-FlamingRock.defaultDimension);
                    FlamingRock newFlamingRock = new FlamingRock(rand_gen.nextInt(randomSPOT),0);
                    objectHandler.addFlamingRock(newFlamingRock);
                });
                flamingRockTimer.start();
            } else {
                if (flamingRockTimer != null && CONSTANTS.LEVEL<15)
                    flamingRockTimer.stop();
            }
        }
    }

    private boolean isOverlappedOn(GameObject go, int spot, int width){
        if((go.getX()>spot) && (go.getX()-spot<width))
            return true;
        else if((go.getX()<spot) && (spot-go.getX()<go.getWidth()))
            return true;
        return false;
    }

    private boolean isFreeSpot(int spot, int width){
        LinkedList<Rock> tmpRocks = objectHandler.getRocks();
        LinkedList<FlamingRock> tmpFrocks = objectHandler.getFlamingRocks();

        for (Rock tmpRock : tmpRocks)
            if (isOverlappedOn(tmpRock, spot, width))
                return false;
        for (FlamingRock tmpFrock : tmpFrocks)
            if (isOverlappedOn(tmpFrock, spot, width))
                return false;
        return true;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public static void main(String[] args) {
        new Game();
    }
}
