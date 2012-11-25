package spade;

/** Entity created by RedGlowEnemy that just stands still and hurts the player.
 *
 * @author Patrick
 */
public class RedGlow extends Entity {
    
    int count = 0;
    int timeout;
    
    public RedGlow(int xpos, int ypos, int timeout, RedGlowEnemy parent, Game game) {
        super(xpos,ypos,game);
        this.timeout = timeout;
        harmful = true;
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
                if (!parent.level[x+xx][y+yy]) {
                    parent.setLevel(x+xx,y+yy,true);
                    game.entities.add(new RedGlow(16*(x+xx),16*(y+yy),timeout,parent,game));
                }
            }
    }
    
    public void tick() {
        if (count==timeout) game.entities.remove(this);
        count++;
        if ((count/2)%2==0) sprite = Art.redglow.get("light");
        else sprite = Art.redglow.get("dark");
    }
    
}
