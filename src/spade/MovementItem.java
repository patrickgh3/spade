package spade;

/**
 *
 * @author Patrick
 */
public class MovementItem extends Item {
    
    public MovementItem(int xpos, int ypos, Game game) {
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
        timeout = 300;
        regentype = RegenBar.MOVEMENT;
        sprite = Art.entities.get("movement item");
    }
    
    public void activate() {
        game.player.runSpeed = 1.5;
        game.player.jumpSpeed = 3.9;
    }
}
