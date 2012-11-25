package spade;

/** Non-moving(?) entity that displays text when the player is near it
 *
 * @author Patrick
 */
public class Speaker extends Entity {
    
    String source;
    TypeTextEntity text;
    boolean activated = false;
    
    public Speaker(int xpos, int ypos, int level, int index, Game game) {
        super(xpos,ypos,game);
        source = LevelData.speakertext[level][index];
        // TODO: different sprite support. logic can be different based on sprite/type.
        sprite = Art.speakers.get("wall 1");
        blockprotected = true;
    }
    
    public void tick() {
        if (!activated && game.player.getRect().intersects(this.getRect())) {
            activated = true;
            text = new TypeTextEntity(xpos,ypos-16,source,game.textspeed,game);
            game.entities.add(text);
        }
        else if (activated && !game.player.getRect().intersects(this.getRect())) {
            activated = false;
            game.entities.remove(text);
        }
    }
}
