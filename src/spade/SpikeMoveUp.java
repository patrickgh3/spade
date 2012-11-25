package spade;

/** Spike that slowly moves up in 'Climb Mode'
 *
 * @author Patrick
 */
public class SpikeMoveUp extends Entity {
    
    double yspeed = -.15;
    double yaccel = .002;
    int count = 0;
    boolean mainspike = false;
    
    public SpikeMoveUp(int xpos, double yspeed, boolean mainspike, Game game) {
        //
        //
        this.game = game;
        this.xpos = xpos;
        this.yspeed = yspeed;
        
        
        // TODO: calculate yaccel manually.
        if (yspeed>0) yabs =ypos = 16*game.level[0].length-game.h-16;
        else yabs = ypos = 16*game.level[0].length;
        harmful = true;
        //blockprotected = true;
        if (yspeed<0) sprite = Art.tileset.get("spike up");
        else sprite = Art.tileset.get("spike down");
        this.mainspike = mainspike;
    }
    
    public void tick() {
        if (game.autoscroll_ended && yspeed!=0) {
            yspeed = 0;
            yabs+=8;
            yabs = ypos = (int)(yabs/16)*16;
        }
        if (!game.player.isAlive() && yspeed<0) yspeed += yaccel;
        if (yspeed!=0 && game.player.hasPassedScrollPoint()) {
            yabs += yspeed;
            ypos = (int)yabs;
        }
        if (game.player.hasPassedScrollPoint() && yspeed>0) count++;
        if (yspeed>0 && count>=16/yspeed) {
            yspeed*=-1;
            //yabs = ypos = 176; // just to make sure it is in exactly the right position, to avoid jittering due to scrolling. ?
        }
    }
    
    public void setPosition() {
        if (!mainspike && yspeed<0) {
            if (ypos-20<-game.Ytrans) ypos = game.mainSpike.getYpos()-game.h+16;
            else ypos = game.mainSpike.getYpos();
        }
    }
    
}
