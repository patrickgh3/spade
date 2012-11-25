package spade;

/** Entity that just sits there, e.g. instructions graphic.
 * @author Patrick
 */
public class IdleEntity extends Entity {
    
    public static int INSTRUCTIONS = 1;
    
    public IdleEntity(int xpos,int ypos,int type) {
        this.xpos = xpos;
        this.ypos = ypos;
        if (type==INSTRUCTIONS) {
            sprite = Art.entities.get("instructions");
        }
        sprite = Art.entities.get("instructions");
    }
    
    
}
