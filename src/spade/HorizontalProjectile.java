package spade;

/** Projectile that files from the right side of the screen.
 *
 * @author Patrick
 */
public class HorizontalProjectile extends Entity {
    
    double xspeed = 0;
    int count = 0;
    public static int waittime = 40;
    public static double speed = 2.5;
    boolean rightside = true;
    
    public HorizontalProjectile(int ypos, Game game) {
        yabs = this.ypos = ypos;
        if (game.gamemode==Game.MODE_VERTICAL_AUTOSCROLL) {
            if (Math.random()>0.5) {
                rightside = true;
                xabs = xpos = -game.Xtrans+game.w-4;
            }
            else {
                rightside = false;
                xabs = xpos = -12;
                spriteXFlip = -1;
            }
        }
        harmful = true;
        height = 12;
        spriteYo=-2;
        this.game = game;
        sprite = Art.projectile.get("hwait1");
    }
    
    public void tick() {
        count++;
        if (count==waittime) {
            if (rightside) xspeed = -speed;
            else xspeed = speed;
            Sound.flame.play();
        }
        if (xspeed==0) {
            //xabs = xpos = -game.Xtrans+game.w-4;
            if (count%10<5) sprite = Art.projectile.get("hwait1");
            else sprite = Art.projectile.get("hwait2");
        }
        else {
            if (count%10<5) sprite = Art.projectile.get("h1");
            else sprite = Art.projectile.get("h2");
        }
        xabs+=xspeed;
        xpos = (int)xabs;
        if (xpos<-game.Xtrans-32 || xpos>-game.Xtrans+game.w+32) game.entities.remove(this);
    }
    
    // because mainspike may be ticked after this entity (?), sometiems the position of the entity is off.
    // we correct this by setting its position to the newest and most accurate state, so it doesn't lag one tick behind.
    public void setPosition() {
        if (game.gamemode==Game.MODE_HORIZ_AUTOSCROLL && xspeed==0) {
            xabs = game.mainSpike.getXpos()+game.w-4;
            xpos = (int)xabs;
        }
    }
}
