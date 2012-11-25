package spade;

/** Projectile that files from the top of the screen.
 *
 * @author Patrick
 */
public class VerticalProjectile extends Entity {
    
    double yspeed = 0;
    int count;
    public static int waittime = 40;
    public static double speed = 2.5;
    
    public VerticalProjectile(int xpos, Game game) {
        xabs = this.xpos = xpos;
        yabs = ypos = -12;
        harmful = true;
        width = 12;
        spriteXo=-2;
        this.game = game;
        sprite = Art.projectile.get("vwait1");
    }
    
    public void tick() {
        count++;
        if (count==waittime) {
            yspeed = speed;
            ypos = -16;
            Sound.flame.play();
        }
        if (yspeed==0) {
            if (count%10<5) sprite = Art.projectile.get("vwait1");
            else sprite = Art.projectile.get("vwait2");
        }
        else {
            if (count%10<5) sprite = Art.projectile.get("v1");
            else sprite = Art.projectile.get("v2");
        }
        yabs+=yspeed;
        ypos = (int)yabs;
        if (ypos>game.level[0].length*16) game.entities.remove(this);
    }
    
    public void setPosition() {
        if (game.gamemode==Game.MODE_VERTICAL_AUTOSCROLL && yspeed==0) {
            yabs = game.mainSpike.getYpos()-game.h+4;
            ypos = (int)yabs;
        }
    }
}
