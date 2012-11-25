package spade;

/** Ball that bounces, destroying destructible blocks it hits.
 *
 * @author Patrick
 */
public class BounceEnemy extends Entity {
    
    public BounceEnemy(int xpos, int ypos, double xspeed, double yspeed, Game game) {
        super(xpos, ypos, game);
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        harmful = true;
        blockprotected = true;
        sprite = Art.entities.get("bounce enemy");
    }
    
    public void tick() {
        // this is a similar algorithm to Entity.move() but we don't need bounce enemy to slip into one-block gaps.
        // so, we just do x movement and y movement each in one fell swoop, without checking for the other each pixel.
        int x,y,xdir,ydir,xa,ya;
        
        int oldx = (int)(xpos/16);
        xabs+=xspeed;
        xpos = (int)xabs;
        x = (int)(xpos/16);
        y = (int)(ypos/16);
        xdir = (int)(xspeed/Math.abs(xspeed));
        xa = (int)((xdir+1)/2); // 1 if right, 0 if left.
        if (oldx!=x) {
            if ((  ypos == y*16 && game.level[x+xa][y].isSolid())  ||  (ypos != y*16 && (game.level[x+xa][y].isSolid() || game.level[x+xa][y+1].isSolid()))  ) {
                xabs = (int)((xpos+8)/16)*16;
                xpos = (int)xabs;
                xspeed*=-1;
                if (isOnscreen()) Sound.break2.play();
                // break blocks it hits
                int ax = xpos/16;
                int by = 1;
                if (ypos==y*16) by--;
                while (by>=0) {
                    if (game.level[ax+xdir][y+by].getType()==Tile.BLOCK) {
                        //Sound.break2.play();
                        game.level[ax+xdir][y+by].playerDig(false);
                        for (int n=0;n<4;n++) {
                            game.entities.add(new TileParticle(game.level[ax+xdir][y+by].getSprite(),game,x+xa,y+by));
                        }
                    }
                    by--;
                }
            }
        }
        
        int oldy = (int)(ypos+16);
        yabs+=yspeed;
        ypos = (int)yabs;
        x = (int)(xpos/16);
        y = (int)(ypos/16);
        ydir = (int)(yspeed/Math.abs(yspeed));
        ya = (int)((ydir+1)/2);
        if (oldy!=y) {
            if (  (xpos == x*16 && game.level[x][y+ya].isSolid())  ||  (xpos != x*16 && (game.level[x][y+ya].isSolid() || game.level[x+1][y+ya].isSolid()))  ) {
                yabs = (int)((ypos+8)/16)*16;
                ypos = (int)yabs;
                yspeed*=-1;
                if (isOnscreen()) Sound.break2.play();
                // break blocks it hits
                int ay = ypos/16;
                int bx = 1;
                if (xpos==x*16) bx--;
                while (bx>=0) {
                    if (game.level[x+bx][ay+ydir].getType()==Tile.BLOCK) {
                        //Sound.break2.play();
                        game.level[x+bx][ay+ydir].playerDig(false);
                        for (int n=0;n<4;n++) {
                            game.entities.add(new TileParticle(game.level[x+bx][ay+ydir].getSprite(),game,x+bx,ay+ydir));
                        }
                    }
                    bx--;
                }
            }
        }
    }
    
}
