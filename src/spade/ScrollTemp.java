package spade;

import java.lang.Math;

/** Entity that game temporarily scrolls to when the player is respawning.
 *
 * @author Patrick
 */
public class ScrollTemp extends Entity {
    
    int targetx;
    int targety;
    int xdir;
    int ydir;
    int count = 0;
    
    public ScrollTemp(int xpos, int ypos, int targetx, int targety) {
        xabs = xpos;
        yabs = ypos;
        this.xpos = xpos;
        this.ypos = ypos;
        this.targetx = targetx;
        this.targety = targety;
        if (targetx>xpos) xdir = 1;
        else xdir = -1;
        if (targety>ypos) ydir = 1;
        else ydir = -1;
        //sprite = Art.entities.get("test enemy");
    }
    
    public void tick() {
        count++;
        //xabs+=xspeed;
        //yabs+=yspeed;
        xabs += (targetx - xabs)*0.05;
        yabs += (targety - yabs)*0.05;
        if (Checkpoint.RESPAWNTIME-count<24) {
            xabs = targetx;
            yabs = targety;
            xdir = 0;
            ydir = 0;
        }
        xpos = (int)xabs+xdir;
        ypos = (int)yabs+ydir;
    }
    
}
