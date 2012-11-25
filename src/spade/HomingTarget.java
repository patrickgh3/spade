package spade;

/** Targets the player and detonates an explosive.
 *
 * @author Patrick
 */
public class HomingTarget extends Entity {
    
    int state = 1;
    int count = 0;
    
    //int maxhspeed = 1;
    //int maxvspeed = 1;
    
    static int FOLLOW = 1;
    static int WAIT = 2;
    static int FIRING = 3;
    
    static int followtime = 70;
    static int waittime = 40;
    static int firetime = 10;
    
    public HomingTarget(Game game) {
        this.game = game;
    }
    
    public void tick() {
        if (state==FOLLOW && count==followtime) {
            state = WAIT;
            count = 0;
            Sound.target_acquired.play();
        }
        else if (state==WAIT && count==waittime) {
            state = FIRING;
            count = 0;
            harmful = true;
            game.entities.add(new AnimEntity(xpos,ypos,AnimEntity.Type.EXPLODE2,game));
            sprite = null;
            Sound.explode2.play();
            width = height = 12;
            xpos+=2;
            ypos+=2;
        }
        else if (state==FIRING && count==firetime) {
            game.entities.remove(this);
        }
        else count++;
        
        if (state==FOLLOW) {
            xpos = (int)((game.player.getXpos()+8)/16)*16;
            ypos = (int)((game.player.getYpos()+8)/16)*16;
            //xpos = (int)((game.player.getXpos()+4)/8)*8;
            //ypos = (int)((game.player.getYpos()+4)/8)*8;
            //xpos = game.player.getXpos();
            //ypos = game.player.getYpos();
            if (count%20==0) sprite = Art.entities.get("homing1");
            else if ((count+10)%20==0) sprite = Art.entities.get("homing2");
        }
        else if (state==WAIT) {
            if (count%10==0) sprite = Art.entities.get("homing3");
            else if ((count+5)%10==0) sprite = Art.entities.get("homing4");
        }
    }
}
