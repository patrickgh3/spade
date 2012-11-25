package spade;

/** Megaman-style memory blocks.
 * @see http://youtu.be/Tc9K9M7LClE?t=13s
 * 
 * @author Patrick
 */
public class MemoryBlock extends Entity {
    
    int ontime;
    int offtime;
    int count;
    
    int x;
    int y;
    
    public MemoryBlock(int xpos, int ypos, int type, Game game) {
        super(xpos,ypos,game);
        x = xpos/16;
        y = ypos/16;
        if (type==1) {
            ontime = 100;
            offtime = 100;
            count = 0;
        }
        else if (type==2) {
            ontime = 100;
            offtime = 100;
            count = 100;
        }
        if (count>=ontime) {
            sprite = Art.tileset.get("memory off");
        }
        else {
            sprite = Art.tileset.get("memory on");
            game.level[x][y].memoryBlockActivate();
        }
        blockprotected = true;
    }
    
    public void tick() {
        count++;
        if (count==ontime) {
            for (int n=0;n<4;n++) {
                game.entities.add(new TileParticle(sprite,game,x,y));
            }
            game.level[x][y].memoryBlockActivate();
            sprite = Art.tileset.get("memory off");
            if (isOnscreen()) game.playSoundBreak();
        }
        else if (count==ontime+offtime) {
            sprite = Art.tileset.get("memory on");
            if (isOnscreen()) game.playSoundBreak();
            count = 0;
            game.level[x][y].memoryBlockActivate();
            for (int n=0;n<4;n++) {
                game.entities.add(new TileParticle(sprite,game,x,y));
            }
            game.player.memoryBlockShove(this);
        }
        
    }
    
    public boolean isOn() {
        return !(count>ontime);
    }
    
}
