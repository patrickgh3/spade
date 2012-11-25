package spade;

/** Item that grants the player ability to dig blocks.
 *
 * @author Patrick
 */
public class ShovelItem extends Item {

    public ShovelItem(int xpos, int ypos, Game game) {
        this.game = game;
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        spriteXo = spriteYo = -4;
        sprite = Art.entities.get("shovel item");
    }
    
    public void pickup() {
        game.player.shovel = true;
        game.entities.remove(this);
        Sound.obtain.play();
    }
    
}
