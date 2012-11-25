package spade;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Prototype for a 2D action platformer game.
 * 
 * @author Patrick
 */
public class GameCanvas extends Canvas {
    
    public static final int w = 12;
    public static final int h = 8;
    public static final int W = w*16;
    public static final int H = h*16;
    
    private JPanel panel;
    private JFrame frame; //nomal game window
    private BufferStrategy bs;
    private Graphics2D g;
    private InputHandler inputHandler = new InputHandler();
    
    boolean gameRunning = true;
    boolean ticked = false;
    long passedTime;
    int tickCount = 0;
    Game game;
    int scaleX = 6;
    int scaleY = 6;
    int width = W*scaleX;
    int height = H*scaleY;
    double FPS = 60.0;
    
    public GameCanvas() {
        Dimension size = new Dimension(width,height);
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        
        panel = new JPanel(new BorderLayout());
        panel.add(this, BorderLayout.CENTER); // adding in this argument gets rid of the weird border.
        
        frame = new JFrame("game engine test");
        frame.setIconImage(Art.icon);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-width)/2, (Toolkit.getDefaultToolkit().getScreenSize().height - height)/2);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        
        game = new Game(inputHandler,this,W,H);
    }
    
    public void gameLoop() {
        double unprocessedSeconds = 0;
        long lastTime = System.nanoTime();
        double secondsPerTick = 1 / FPS;
        requestFocus();
        
        while (gameRunning) {
            ticked = false;
            long now = System.nanoTime();
            
            passedTime = now - lastTime;
            lastTime = now;
            if (passedTime < 0) passedTime = 0;
            if (passedTime > 100000000) passedTime = 100000000;
            
            unprocessedSeconds += passedTime / 1000000000.0;
            while (unprocessedSeconds > secondsPerTick) {
                unprocessedSeconds -= secondsPerTick;
                tick();
                ticked = true;
            }
            if (ticked) {
                render();
                ticked = false;
            }// else {
            //    try {
            //        Thread.sleep(10);
            //    } catch (InterruptedException e) {
            //        e.printStackTrace();
            //    }
            //}
        }
    
    }
    
    public void tick() {
        tickCount++;
        game.tick();
    }
        
    public void render() {
        bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
        g.scale(scaleX,scaleY);
        game.render(g);
        //g.setColor(Color.BLACK);
        //g.drawString(Integer.toString(tickCount),5,10);
        g.dispose();
        bs.show();
    }
    
    public void changeResolution() {
        scaleX += 1;
            if (scaleX > 8) scaleX = 1;
            scaleY = scaleX;
            width = W*scaleX;
            height = H*scaleY;
            Dimension size = new Dimension(width,height);
            setSize(size);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-width-20)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-height-40)/2);
            frame.pack();
    }
    
    public static void main(String[] args) {
        //LevelData.activate();
        Sound.activate();
        Art.activate();
        GameCanvas canvas = new GameCanvas();
        SaveData.loadArray();
        canvas.gameLoop();
    }
    
}
