package spade;

/** Item that gives you points upon pickup.
 *
 * @author Patrick
 */
public class PointsItem extends Item {
    
    int value; // points to award upon touch
    
    public PointsItem(int xpos, int ypos, int value, Game game) {
        this.game = game;
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.value = value;
        sprite = Art.entities.get("points item");
    }
    
    @Override
    public void pickup() {
        game.addScore(value);
        game.entities.remove(this);
        // TODO: create text effect here
    }
    
}
