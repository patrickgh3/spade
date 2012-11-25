package spade;

/** Enemy that follows the edges of blocks, turning either clockwise or CC at junctions.
 *
 * @author Patrick
 */
public class EdgeTraceEnemy extends Entity {
    
    // found out about enums here. but, I chose to use magic integers instead because I don't wnant to typeout Spin.whatever every time. I don't need to sanitize my inputs here.
    // also, I can use numbers to my advantage here. I could do that using enums, but whatevs.
    public static int CLOCKWISE = 1;
    public static int COUNTERCLOCKWISE = 2;
    
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int RIGHT = 3;
    public static final int DOWN = 4;
    public static final int STILL = 5; // when trapped
    
    int spin;
    int dir;
    double speed = 0.7;
    
    public EdgeTraceEnemy(int xpos, int ypos, int spin, Game game) {
        super(xpos,ypos,game);
        this.spin = spin;
        dir = UP;
        yspeed = -speed;
        blockprotected = true;
        harmful = true;
        sprite = Art.entities.get("edge trace test");
    }
    
    public void tick() {
        // TODO: does this work with >1 speeds? use Entity.move()?
        int oldx = (int)(xpos/16);
        int oldy = (int)(ypos/16);
        xabs+=xspeed;
        yabs+=yspeed;
        xpos = (int)xabs;
        ypos = (int)yabs;
        if (oldx!=(int)(xpos/16) || oldy!=(int)(ypos/16) || dir==STILL) {
            // we have just crossed into a possible junction space.
            int x = (int)((xpos+8)/16);
            int y = (int)((ypos+8)/16);
            boolean left = game.level[x-1][y].isSolid();
            boolean right = game.level[x+1][y].isSolid();
            boolean up = game.level[x][y-1].isSolid();
            boolean down = game.level[x][y+1].isSolid();
            
            // preferred direction: turn, then straight, then turn opposite, then turn around.
            int chosen = STILL; // chosen new direction. will be still unless there is a free place to move.
            int[] pref;
            if (dir==STILL) {
                pref = new int[]{LEFT,RIGHT,UP,DOWN};
            }
            else if (spin==CLOCKWISE) {
                pref = new int[]{getRight(dir),dir,getLeft(dir),getRight(getRight(dir))};
            }
            else {
                pref = new int[]{getLeft(dir),dir,getRight(dir),getLeft(getLeft(dir))};
            }
            //System.out.print(pref[0]);
            //System.out.print(pref[1]);
            //System.out.print(pref[2]);
            //System.out.println(pref[3]);
            for (int n=0;n<pref.length;n++) {
                if ((pref[n]==LEFT && left) || (pref[n]==RIGHT && right) || (pref[n]==UP && up) || (pref[n]==DOWN && down)) continue;
                chosen = pref[n];
                break;
            }
            
            if (dir!=chosen) {
                // we have changed directions, so we have to update speed and position.
                dir = chosen;
                xabs = x*16;
                yabs = y*16;
                if (dir==LEFT) xabs-=1;
                else if (dir==RIGHT) xabs+=1;
                else if (dir==UP) yabs-=1;
                else if (dir==DOWN) yabs+=1;
                xpos = (int)xabs;
                ypos = (int)yabs;
                xspeed = yspeed = 0;
                if (dir==UP) yspeed = -speed;
                else if (dir==DOWN) yspeed = speed;
                else if (dir==LEFT) xspeed = -speed;
                else if (dir==RIGHT) xspeed = speed;
            }
        }
    }
    
    private int getRight(int givendir) {
        int rdir = givendir+1;
        if (rdir>4) rdir = 1;
        return rdir;
    }
    
    private int getLeft(int givendir) {
        int ldir = givendir-1;
        if (ldir<1) ldir = 4;
        return ldir;
    }
    
}
