package spade;

/** Object that the player respawns at upon death.
 *
 * @author Patrick
 */
public class Checkpoint extends Entity {
    
    public static final int RESPAWNTIME = 100; // makes sense for this to go here, right?
    
    boolean active = false;
    int count = 0;
    boolean counting = false;
    
    public Checkpoint(int xpos, int ypos, Game game) {
        super(xpos,ypos,game);
        blockprotected = true;
        sprite = Art.checkpoint.get("inactive");
    }
    
    public void tick() {
        if (counting) count++;
        if (count==RESPAWNTIME-24) game.entities.add(new AnimEntity(xpos,ypos,AnimEntity.Type.CHECKPOINTOPEN,game));
    }
    
    public void setActive(boolean active) {
        this.active = active;
        if (active) sprite = Art.checkpoint.get("active");
        else sprite = Art.checkpoint.get("inactive");
    }
    
    public void startTimer() {
        counting = true;
        count = 0;
    }
    public void startTimerInit() {
        counting = true;
        count = RESPAWNTIME-32;
    }
    
    public int getXcenter() {
        return xpos+width/2;
    }
    public int getYcenter() {
        return ypos+height/2;
    }
    
}
