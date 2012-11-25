package spade;

/**
 *
 * @author Patrick
 */
public class TimeFreezeItem extends Item {
    
    public TimeFreezeItem(int xpos, int ypos, Game game) {
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
        timeout = 150;
        regentype = RegenBar.TIMEFREEZE;
        sprite = Art.entities.get("timefreeze item");
    }
    
    public void activate() {
        game.timefreeze = true;
        game.effecttime = timeout;
        game.effectcount = 0;
    }
    
}
