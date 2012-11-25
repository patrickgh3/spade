package spade;

/** Turns on and off. When it's on, it fills the area surrounding it with RedGlow. When it's off, all RedGlows disappear.
 *
 * @author Patrick
 */
public class RedGlowEnemy extends Entity {
    
    private final int chargetime = 100;
    private final int glowtime = 20;
    
    private int count = 0;
    public boolean[][] level;
    
    public RedGlowEnemy(int xpos, int ypos, Game game) {
        super(xpos,ypos,game);
        sprite = Art.redglow.get("charge1");
        level = new boolean[game.level.length][game.level[0].length];
    }
    
    public void tick() {
        count++;
        if (count<chargetime) {
            // charging
            int sp = (int)(((double)count/(double)chargetime)*5)+1;
            sprite = Art.redglow.get("charge"+sp);
        }
        else if (count==chargetime) {
            if (isOnscreen()) Sound.redglow.play();
            // create guys
            for (int x=0;x<game.level.length;x++) {
                for (int y=0;y<game.level[0].length;y++) {
                    if (!game.level[x][y].isSolid() && !game.level[x][y].isSafezone()) level[x][y] = false;
                    else level[x][y] = true;
                }
            }
            int x = (int)(xpos/16);
            int y = (int)(ypos/16);
            for (int n=0;n<4;n++) {
                int xx,yy;
                if (n<2) {
                    yy=0;
                    if (n==0) xx=-1;
                    else xx=1;
                }
                else {
                    xx=0;
                    if (n==2) yy=-1;
                    else yy=1;
                }
                if (!level[x+xx][y+yy]) {
                    level[x+xx][y+yy] = true;
                    game.entities.add(new RedGlow(16*(x+xx),16*(y+yy),glowtime,this,game));
                }
            }
        }
        else if (count<chargetime+glowtime) {
            // animate
            if ((count/2)%2==0) sprite = Art.redglow.get("active light");
            else sprite = Art.redglow.get("active dark");
        }
        else if (count==chargetime+glowtime) {
            count = 0;
        }
    }
    
    public void setLevel(int x, int y, boolean val) {
        level[x][y] = val;
    }
    
}
