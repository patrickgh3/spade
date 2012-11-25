package spade;

import java.awt.image.BufferedImage;

/** A 16x16 pixel tile in the level. It's more like a data holder, since it has barely any logic.
 * 
 * @author Patrick
 */
public class Tile {
    
    public static int EMPTY = 1;
    public static int BLOCK = 2;
    public static int STRONG = 3;
    public static int UNBREAKABLE = 4;
    public static int UNBREAKABLE_INVISIBLE = 5;
    public static int FENCE = 6;
    public static int SPIKEUP = 10;
    public static int SPIKEDOWN = 11;
    boolean solid = false;
    boolean strong = false;
    boolean harmful = false;
    boolean visible = true;
    boolean safezone = false;
    BufferedImage sprite;
    int type;
    
    public Tile() {}
    
    public Tile(int type) {
        this.type = type;
        if (type == EMPTY) {
            visible = false;
        }
        else if (type == BLOCK) {
            solid = true;
            sprite = Art.tileset.get("break");
        }
        else if (type == STRONG) {
            solid = true;
            strong = true;
            //sprite = Art.nullSprite;
        }
        else if (type==UNBREAKABLE) {
            solid = true;
            strong = true;
            //sprite = Art.nullSprite;
        }
        else if (type == UNBREAKABLE_INVISIBLE) {
            solid = true;
            //strong = true;
            visible = false;
        }
        else if (type == FENCE) {
            safezone = true;
            sprite = Art.tileset.get("fence");
        }
        else if (type == SPIKEUP) {
            harmful = true;
            sprite = Art.tileset.get("spike up");
        }
        else if (type==SPIKEDOWN) {
            harmful = true;
            sprite = Art.tileset.get("spike down");
        }
        else {
            System.out.println("invalid tile type at Tile level.");
            type = EMPTY;
            visible = false;
            sprite = Art.tileset.get("break");
        }
    }
    
    // update sprite based on type and blocks surrounding it.
    // @param level - array of the solidity of the 8x8 tiles in the level.
    // After going through MUCH BULLSHIT, I figured out this problem. If the image is "suspicious" (example = black and white x with border and no other colors) then when you try to change part of the raster using .getRaster().setRect() it gets all fucked up for no fucking reason. It's fucking bullshit. It took me like two or three hours to track down this fucking thing that didn't even seem like a problem at all. It didn't even seem like it could possibly be a problem. Fucking ass. /rant So the moral is, to in this codebase, not use black and white bufferedimages if you plan on making changes to their rasters.
    // another notable thing is that sprite.getRaster().setRect() in this case actually modifies the original image referenced by sprite. so don't do that. make a copy.
    public void updateSprite(boolean[][] level,int tilex, int tiley) {
        if (type!=STRONG && type != UNBREAKABLE) {
            //System.out.println("Returning at type: "+Integer.toString(type));
            return;
        }
        sprite = new BufferedImage(16,16,BufferedImage.TYPE_4BYTE_ABGR); // see above, making a copy
        for (int yy = 0;yy<2;yy++) {
            for (int xx = 0;xx<2;xx++) {
                int x = tilex*2+1+xx;
                int y = tiley*2+1+yy;
                boolean top = level[x][y-1];
                boolean bottom = level[x][y+1];
                boolean left = level[x-1][y];
                boolean right = level[x+1][y];
                boolean topleft = level[x-1][y-1];
                boolean topright = level[x+1][y-1];
                boolean bottomleft = level[x-1][y+1];
                boolean bottomright = level[x+1][y+1];
                String ref = "null";
                // outside corners
                if (!top && !left) ref = "topleft";
                else if (!top && !right) ref = "topright";
                else if (!bottom && !left) ref = "bottomleft";
                else if (!bottom && !right) ref = "bottomright";
                // edges
                else if (!top) {
                    if (xx==0) ref = "top1";
                    else ref = "top2";
                }
                else if (!bottom) {
                    if (xx==0) ref = "bottom1";
                    else ref = "bottom2";
                }
                else if (!right) {
                    if (yy==0) ref = "right1";
                    else ref = "right2";
                }
                else if (!left) {
                    if (yy==0) ref = "left1";
                    else ref = "left2";
                }
                // inside corners
                else if (!bottomright) ref = "insidecorner1";
                else if (!bottomleft) ref = "insidecorner2";
                else if (!topright) ref = "insidecorner3";
                else if (!topleft) ref = "insidecorner4";
                // center
                else if (top && bottom && left && right) {
                    if (yy==0) {
                        if (xx==0) ref = "center1";
                        else ref = "center2";
                    }
                    else {
                        if (xx==0) ref = "center3";
                        else ref = "center4";
                    }
                }
                sprite.getRaster().setRect(8*xx,8*yy,Art.tileset.get(ref).getRaster());
            }
        }
    }
    
    // could be type boolean or int - returns the status of what happened to
    // the Player that calls the function.
    // right now: returns true if block was changed. (so we can create particles.)
    public boolean playerDig(boolean strongshovel) {
        if (type == EMPTY) {
            type = BLOCK;
            solid = true;
            visible = true;
            sprite = Art.tileset.get("break");
            Sound.brickbreak.play();
            SaveData.create();
            return true;
        }
        else if (type == BLOCK) {
            type = EMPTY;
            solid = false;
            visible = false;
            Sound.brickbreak.play();
            SaveData.destroy();
            return true;
        }
        else if (type == STRONG && strongshovel) {
            type = EMPTY;
            solid = false;
            strong = false;
            visible = false;
            Sound.brickbreak.play();
            return true;
        }
        return false;
    }
    
    public void memoryBlockActivate() {
        if (solid) solid = false;
        else solid = true;
    }
    
    public BufferedImage getSprite() {
        return sprite;
    }
    
    public boolean isSolid() {
        return solid;
    }
    
    public boolean isHarmful() {
        return harmful;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public int getType() {
        return type;
    }
    
    public boolean isSafezone() {
        return safezone;
    }
    
}
