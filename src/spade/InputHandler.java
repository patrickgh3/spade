package spade;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Handles the mouse and keyboard input.
 *
 * @author Patrick
 */
public class InputHandler extends MouseAdapter implements KeyListener {
    public boolean[] keys = new boolean[65536];
    private boolean[] typed = new boolean[65536];
    public Point mouse = new Point();
    public boolean mouseL = false;
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); 
        if (code>0 && code<keys.length) {
            keys[code] = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode(); 
        if (code>0 && code<keys.length) {
            keys[code] = typed[code] = false;
        }
    }
    
    public boolean getTyped(int code) {
        // return true the first instant the key is down
        // only return true again once the key is released and pressed again.
        if (keys[code] && !typed[code]) {
            typed[code] = true;
            return true;
        }
        else return false;
    }
    
    public void keyTyped(KeyEvent e) {}
    
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) mouseL = true;
    }
    
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) mouseL = false;
    }
    
    public void mouseClicked(MouseEvent e) {
        
    }
    
    public void mouseMoved (MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
        //mouseMoved.setLocation(  (int)((mouse.x/GameCanvas.scaleX - GameCanvas.xOffset))  ,  (int)((mouse.y/GameCanvas.scaleY - GameCanvas.yOffset))  );
        //mouseScaled.setLocation(  (int)(mouse.x/GameCanvas.scaleX)  ,  (int)(mouse.y/GameCanvas.scaleY)  );
    }
    
    public void mouseDragged(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
    }
    
}
