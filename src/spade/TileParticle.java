package spade;

import java.awt.Image;
import java.awt.image.BufferedImage;

/** Particle that appears upon breaking/creating a block. Inspired by Minecraft.
 * 
 * @author Patrick
 */
public class TileParticle extends Entity {
    
    int count = 0;
    int timeout = 25;
    double yaccel = 0.1;
    
    public TileParticle(BufferedImage source, Game game, int x, int y) {
        this.game = game;
        width = height = 5;
        sprite = source.getSubimage((int)(Math.random()*(source.getWidth()-width)+1),(int)(Math.random()*(source.getHeight()-height)+1),width,height);
        xspeed = (Math.random()-0.5)*0.9;
        yspeed = -(Math.random()*0.4+0.8);
        // TODO: is this right? I don't seem to see the top or left edges...
        xabs = xpos = 16*x+(int)(Math.random()*16)-width/2;
        yabs = ypos = 16*y+(int)(Math.random()*16)-height/2;
    }
    
    public void tick() {
        xpos = (int)(xabs+=xspeed);
        yspeed += yaccel;
        ypos = (int)(yabs+=yspeed);
        count++;
        if (count==timeout) game.entities.remove(this);
    }
    
    
}
