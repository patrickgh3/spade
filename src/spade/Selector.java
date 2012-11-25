package spade;

/** Entity that controls where the player places or removes a block.
 * I made this an entity because I thought I might want to have a graphic showing the player where these spots are.
 * @author Patrick
 */
public class Selector extends Entity {
    
    Player player;
    int type;
    public static int TOP = 1;
    public static int BOTTOM = 2;
    public static int LEFT = 3;
    public static int RIGHT = 4;
    
    
    public Selector(int type,Player player) {
        //sprite = Art.entities.get("dig outline");
        this.player = player;
        this.type = type;
        meltsTime = true;
    }
    
    @Override
    public void tick() {
        if (type == TOP) {
            xpos = (int)((player.getXpos()+8)/16)*16;
            ypos = (int)(player.getYpos()/16-1)*16;
        }
        else if (type == BOTTOM) {
            xpos = (int)((player.getXpos()+8)/16)*16;
            ypos = (int)((player.getYpos()+31)/16)*16;
        }
        else if (type == LEFT) {
            xpos = (int)((player.getXpos()-16)/16)*16;
            ypos = (int)((player.getYpos()+8)/16)*16;
        }
        else if (type == RIGHT) {
            xpos = (int)((player.getXpos()+31)/16)*16;
            ypos = (int)((player.getYpos()+8)/16)*16;
        }
    }
    
    public int getX() {
        return (int)xpos/16;
    }
    public int getY() {
        return (int)ypos/16;
    }
    
}
