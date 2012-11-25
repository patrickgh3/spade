package spade;

/**
 *
 * @author Patrick
 */
public class DigFastItem extends Item {
    
    public DigFastItem(int xpos, int ypos, Game game) {
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
        timeout = 300;
        regentype = RegenBar.DIGFAST;
        sprite = Art.entities.get("digfast item");
    }
    
    public void pickup() {
        super.pickup();
    }
    
    public void activate() {
        game.player.digtime = 5;
    }
    
}
