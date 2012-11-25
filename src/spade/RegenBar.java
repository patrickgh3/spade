package spade;

/** Outline of the bar that appears when the player digs/activates item.
 * 
 * @author Patrick
 */
public class RegenBar extends Entity {
    
    int timeout;
    int count = 0;
    int position;
    RegenFill filler;
    
    public static int ABOVE = 1;
    public static int BELOW = 2;
    
    public static int DIG = 1;
    public static int MOVEMENT = 2;
    public static int DIGFAST = 3;
    public static int STRONGSHOVEL = 4;
    public static int TIMEFREEZE = 5;
    
    public RegenBar(int position, Game game) {
        this.position = position;
        this.game = game;
        filler = new RegenFill(game,this);
        //game.entities.add(filler);
        meltsTime = true;
    }
    
    public void tick() {
        if (count < timeout) {
            count++;
        }
        else if (count==timeout) {
            sprite = null;
            count++;
        }
    }
    
    // called in player's tick function
    public void setPosition() {
        xpos = game.player.getXpos();
        if (position == BELOW) ypos = game.player.getYpos()+20;
        else ypos = game.player.getYpos()-8;
        filler.setPosition();
    }
    
    public void activate(int status, int timeout) {
        this.timeout = timeout;
        count = 0;
        sprite = Art.entities.get("regen bar");
        width = sprite.getWidth();
        height = sprite.getHeight();
        filler.activate(status,timeout);
    }
    
    public void die() {
        game.entities.remove(filler);
        game.entities.remove(this);
    }
    public void revive() {
        game.entities.add(filler);
        game.entities.add(this);
    }
    
}
