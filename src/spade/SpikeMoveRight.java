package spade;

/** Spike that slowly moves right in Horizontal Autoscroll levels.
 *
 * @author Patrick
 */
public class SpikeMoveRight extends Entity {
    
    double xspeed;
    double xaccel;
    int count; // the hack-ish way that we can tell when the right-side spikes should reverse direction from left to right at the very start.
    boolean mainspike = false;
    
    public SpikeMoveRight(int ypos, double xspeed, boolean mainspike, Game game) {
        // Note that there are 2 'types' of this entity: one that stays on the left of the screen, and one that stays on the right.
        // We differentiate in this constructor based on the positive or negative value passed in for xspeed.
        this.game = game;
        this.ypos = ypos;
        this.xspeed = xspeed;
        xaccel=(double)((double)game.deathrespawntime/Math.abs(xspeed));
        xaccel = 0.004;
        // TODO: calculae xaccel manually.
        if (xspeed>0) xabs = xpos = -16;
        else xabs = xpos = 192;
        harmful = true;
        //blockprotected = true;
        if (xspeed>0) sprite = Art.tileset.get("spike right");
        else sprite = Art.tileset.get("spike left");
        this.mainspike = mainspike;
    }
    
    public void tick() {
        if (game.autoscroll_ended && xspeed!=0) {
            xspeed = 0;
            xabs+=8;
            xabs = xpos = (int)(xabs/16)*16;
        }
        if (!game.player.isAlive() && xspeed>0) xspeed -= xaccel;
        if (xspeed!=0 && game.player.hasPassedScrollPoint()) {
            xabs += xspeed;
            xpos = (int)xabs;
        }
        if (game.player.hasPassedScrollPoint() && xspeed<0) count++;
        if (xspeed<0 && count>=16/-xspeed) {
            xspeed*=-1;
            xabs = xpos = 176; // just to make sure it is in exactly the right position, to avoid jittering due to scrolling. ?
        }
    }
    
    public void setPosition() {
        if (!mainspike && xspeed>0) {
            if (xpos-20<-game.Xtrans) xpos = game.mainSpike.getXpos();
            else xpos = game.mainSpike.getXpos()+game.w-16;
        }
    }
    
}
