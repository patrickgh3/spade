package spade;

/**
 *
 * @author Patrick
 */
public class StrongShovelItem extends Item {
    
    public StrongShovelItem(int xpos, int ypos, Game game) {
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
        timeout = 300;
        regentype = RegenBar.STRONGSHOVEL;
        sprite = Art.entities.get("strongshovel item");
    }
    
    public void activate() {
        game.player.strongshovel = true;
    }
}
