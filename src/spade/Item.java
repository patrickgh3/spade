package spade;

import spade.Entity;

/** Item the player can pick up and possibly use later.
 * 
 * @author Patrick
 */
public class Item extends Entity {
    
    int hovercount = 0;
    int hovertime = 20;
    int timeout = 0;
    int regentype;
    
    public Item() {
        //blockprotected = true;
        width = height = 8;
        spriteXo = spriteYo = -2;
    }
    
    public Item(int xpos, int ypos, Game game) {
        super();
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
    }
    
    public void pickup() {
        // play sound?
        game.entities.remove(this);
        game.player.setReserveItem(this);
    }
    
    public void activate() {}
    
    public void tick() {
        hovercount++;
        if (hovercount==hovertime) ypos++;
        else if (hovercount==2*hovertime) {
            ypos--;
            hovercount=0;
        }
    }
    
    public int getTimeout() {
        return timeout;
    }
    public int getRegenType() {
        return regentype;
    }
    
}
