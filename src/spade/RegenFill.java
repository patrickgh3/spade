package spade;

import java.awt.image.BufferedImage;

/** Animated filling of the regen bar.
 * 
 * @author Patrick
 */
public class RegenFill extends Entity {
    
    int timeout;
    int count;
    boolean inverted = false;
    RegenBar parent;
    BufferedImage source;
    
    public RegenFill(Game game, RegenBar parent) {
        this.game = game;
        this.parent = parent;
        meltsTime = true;
    }
    
    public void tick() {
        if (count < timeout) {
            count++;
            int width = 8;
            if (!inverted) {
                width = (int)(16*count/timeout+1);
                if (width>16) width = 16;
            }
            else if (inverted) {
                width = (int)(16-16*count/timeout);
                if (width<1) width=1;
            }
            sprite = source.getSubimage(source.getWidth()-width,0,width,source.getHeight());
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }
        else if (count==timeout) {
            sprite = null;
            count++;
        }
    }
    
    public void setPosition() {
        xpos = parent.getXpos();
        ypos = parent.getYpos()+1;
    }
    
    public void activate(int status, int timeout) {
        this.timeout = timeout;
        count = 0;
        inverted = true;
        if (status==RegenBar.DIG) {
            source = Art.entities.get("regen dig");
            inverted = false;
        }
        else if (status==RegenBar.MOVEMENT) {
            source = Art.entities.get("regen movement");
        }
        else if (status==RegenBar.DIGFAST) {
            source = Art.entities.get("regen digfast");
        }
        else if (status==RegenBar.STRONGSHOVEL) {
            source = Art.entities.get("regen strongshovel");
        }
        else if (status==RegenBar.TIMEFREEZE) {
            source = Art.entities.get("regen timefreeze");
        }
    }
}