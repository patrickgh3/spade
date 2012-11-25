package spade;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.HashMap;

/** Database of graphics in the game. credit = notch, prelude of the chambered
 * 
 * @author Patrick
 */
public abstract class Art {
    
    // general images
    public static final BufferedImage icon = loadImage("icon.png");
    public static final BufferedImage font = loadImage("font.png");
    public static final String chars = "0123456789 @%"+
                                       "ABCDEFGHIJKLM"+
                                       "NOPQRSTUVWXYZ"+
                                       ".,:;-!?_)('\" "+
                                       "rlud+/i#     "+
                                       "^v>";
    
    // source images
    public static final BufferedImage srcPlayer = loadImage("entities/player.png");
    public static final BufferedImage srcTiles = loadImage("tiles/common.png");
    public static final BufferedImage srcSpeaker = loadImage("entities/speaker.png");
    public static final BufferedImage srcCheckpoint = loadImage("entities/checkpoint.png");
    public static final BufferedImage srcMenuSelector = loadImage("entities/menuselector.png");
    public static final BufferedImage srcEdgeTracer = loadImage("entities/edgetracer.png");
    public static final BufferedImage srcRedGlow = loadImage("entities/redglow.png");
    public static final BufferedImage srcFlash = loadImage("entities/flash.png");
    public static final BufferedImage srcExplode = loadImage("entities/explode.png");
    //public static final BufferedImage srcPaceEnemy = loadImage("entities/paceenemy.png");
    public static final BufferedImage srcProjectile = loadImage("entities/projectile.png");
    public static final BufferedImage srcHoming = loadImageWhiteBG("entities/homing.png");
    public static final BufferedImage srcExplode2 = loadImageWhiteBG("entities/explode2.png");
    public static final BufferedImage srcItem = loadImage("entities/item.png");
    
    // tilesets
    public static Tileset tileset; // current tileset in use
    public static final HashMap<String,Tileset> tilesets = new HashMap<>();
    
    // hashmaps for groupings of individual images
    public static final HashMap<String,BufferedImage> commontiles = new HashMap<>();
    public static final HashMap<String,BufferedImage> player = new HashMap<>();
    public static final HashMap<String,BufferedImage> player2 = new HashMap<>();
    public static final HashMap<String,BufferedImage> entities = new HashMap<>();
    public static final HashMap<String,BufferedImage> checkpoint = new HashMap<>();
    public static final HashMap<String,BufferedImage> speakers = new HashMap<>();
    public static final HashMap<String,BufferedImage> redglow = new HashMap<>();
    public static final HashMap<String,BufferedImage> paceenemy = new HashMap<>();
    public static final HashMap<String,BufferedImage> projectile = new HashMap<>();
    
    // animations
    public static final BufferedImage[] bloodsplat = {srcExplode.getSubimage(0,0,16,16),srcExplode.getSubimage(17,0,16,16),srcExplode.getSubimage(34,0,16,16)};
    public static final BufferedImage[] flash = {srcFlash.getSubimage(0,0,16,16),srcFlash.getSubimage(16,0,16,16),srcFlash.getSubimage(32,0,16,16)};
    public static final BufferedImage[] menuselector = {srcMenuSelector.getSubimage(0,0,16,16),srcMenuSelector.getSubimage(16,0,16,16),srcMenuSelector.getSubimage(32,0,16,16),srcMenuSelector.getSubimage(16,0,16,16)};
    public static final BufferedImage[] playerenter = {srcPlayer.getSubimage(0,32,16,16),srcPlayer.getSubimage(16,32,16,16),srcPlayer.getSubimage(32,32,16,16)};
    public static final BufferedImage[] checkpointopen = {srcCheckpoint.getSubimage(0,16,16,16),srcCheckpoint.getSubimage(16,16,16,16),srcCheckpoint.getSubimage(32,16,16,16),srcCheckpoint.getSubimage(16,16,16,16),srcCheckpoint.getSubimage(0,16,16,16)};
    public static BufferedImage[] explode2 = new BufferedImage[7];
    
    static {
        
        commontiles.put("spike up",srcTiles.getSubimage(0,0,16,16));
        commontiles.put("spike down",srcTiles.getSubimage(16,0,16,16));
        commontiles.put("spike left",srcTiles.getSubimage(32,0,16,16));
        commontiles.put("spike right",srcTiles.getSubimage(48,0,16,16));
        commontiles.put("memory off",srcTiles.getSubimage(0,16,16,16));
        
        player.put("idle",srcPlayer.getSubimage(0,0,16,16));
        player.put("jump",srcPlayer.getSubimage(16,0,16,16));
        player.put("fall",srcPlayer.getSubimage(32,0,16,16));
        player.put("walk1",srcPlayer.getSubimage(48,0,16,16));
        player.put("walk2",srcPlayer.getSubimage(64,0,16,16));
        
        player2.put("idle",srcPlayer.getSubimage(0,16,16,16));
        player2.put("jump",srcPlayer.getSubimage(16,16,16,16));
        player2.put("fall",srcPlayer.getSubimage(32,16,16,16));
        player2.put("walk1",srcPlayer.getSubimage(48,16,16,16));
        player2.put("walk2",srcPlayer.getSubimage(64,16,16,16));
        
        //entities.put("dig outline",loadImage("res/entities/outline.png"));
        entities.put("regen bar",loadImage("entities/regen_bar.png"));
        entities.put("regen dig",loadImage("entities/regen_dig.png"));
        //entities.put("regen movement",loadImage("res/entities/regen_movement.png"));
        //entities.put("regen digfast",loadImage("res/entities/regen_digfast.png"));
        //entities.put("regen strongshovel",loadImage("res/entities/regen_strongshovel.png"));
        //entities.put("regen timefreeze",loadImage("res/entities/regen_timefreeze.png"));
        
        //entities.put("item box",loadImage("res/entities/itembox.png"));
        //entities.put("points item",loadImage("res/entities/item.png"));
        //entities.put("movement item",loadImage("res/entities/item_movement.png"));
        //entities.put("digfast item",loadImage("res/entities/item_digfast.png"));
        //entities.put("strongshovel item",loadImage("res/entities/item_strongshovel.png"));
        //entities.put("timefreeze item",loadImage("res/entities/item_timefreeze.png"));
        entities.put("shovel item",srcItem.getSubimage(0,0,16,16));
        entities.put("diode",srcItem.getSubimage(0,16,16,16));
        entities.put("diode ghost",srcItem.getSubimage(16,16,16,16));
        
        checkpoint.put("inactive",srcCheckpoint.getSubimage(0,0,16,16));
        checkpoint.put("active",srcCheckpoint.getSubimage(16,0,16,16));
        checkpoint.put("opening1",srcCheckpoint.getSubimage(0,16,16,16));
        checkpoint.put("opening2",srcCheckpoint.getSubimage(16,16,16,16));
        checkpoint.put("open",srcCheckpoint.getSubimage(32,16,16,16));
        
        speakers.put("wall 1",srcSpeaker.getSubimage(0,0,16,16));
        
        entities.put("bounce enemy",loadImage("entities/bounce.png").getSubimage(0,0,16,16));
        
        entities.put("edge trace test",srcEdgeTracer);
        
        redglow.put("charge1",srcRedGlow.getSubimage(0,0,16,16));
        redglow.put("charge2",srcRedGlow.getSubimage(16,0,16,16));
        redglow.put("charge3",srcRedGlow.getSubimage(32,0,16,16));
        redglow.put("charge4",srcRedGlow.getSubimage(48,0,16,16));
        redglow.put("charge5",srcRedGlow.getSubimage(64,0,16,16));
        redglow.put("active light",srcRedGlow.getSubimage(0,16,16,16));
        redglow.put("active dark",srcRedGlow.getSubimage(16,16,16,16));
        redglow.put("light",srcRedGlow.getSubimage(0,32,16,16));
        redglow.put("dark",srcRedGlow.getSubimage(16,32,16,16));
        
        //paceenemy.put("idle",srcPaceEnemy.getSubimage(0,0,16,16));
        //paceenemy.put("walk1",srcPaceEnemy.getSubimage(16,0,16,16));
        //paceenemy.put("walk2",srcPaceEnemy.getSubimage(32,0,16,16));
        
        projectile.put("v1",srcProjectile.getSubimage(0,0,16,16));
        projectile.put("v2",srcProjectile.getSubimage(16,0,16,16));
        projectile.put("vwait1",srcProjectile.getSubimage(0,16,16,16));
        projectile.put("vwait2",srcProjectile.getSubimage(16,16,16,16));
        projectile.put("h1",srcProjectile.getSubimage(0,32,16,16));
        projectile.put("h2",srcProjectile.getSubimage(16,32,16,16));
        projectile.put("hwait1",srcProjectile.getSubimage(0,48,16,16));
        projectile.put("hwait2",srcProjectile.getSubimage(16,48,16,16));
        
        entities.put("homing1",srcHoming.getSubimage(0,0,16,16));
        entities.put("homing2",srcHoming.getSubimage(16,0,16,16));
        entities.put("homing3",srcHoming.getSubimage(0,16,16,16));
        entities.put("homing4",srcHoming.getSubimage(16,16,16,16));
        
        for (int n=0;n<7;n++) {
            explode2[n] = srcExplode2.getSubimage(n*17,0,16,16);
        }
        
        tilesets.put("alpha",new Tileset(loadImage("tiles/alpha.png")));
        tilesets.put("beta",new Tileset(loadImage("tiles/beta.png")));
        tilesets.put("sheet",new Tileset(loadImage("tiles/sheet.png")));
    }
    
    private static BufferedImage loadImage(String fileName) {
        fileName = "res/"+fileName;
        try {
            BufferedImage img = ImageIO.read(Art.class.getClassLoader().getResource(fileName));
            // if pixels are pink, make them transparent
            for (int x=0;x<img.getWidth();x++) {
                for (int y=0;y<img.getHeight();y++) {
                    if (img.getRGB(x, y)==-65408) img.setRGB(x,y,0);
                }
            }
            return img;
        } catch (Exception e) {
            System.out.println("Cannot find image at: "+fileName);
        }
        return null;
    }
    
    private static BufferedImage loadImageWhiteBG(String fileName) {
        fileName = "res/"+fileName;
        try {
            BufferedImage img = ImageIO.read(Art.class.getClassLoader().getResource(fileName));
            // if pixels are WHITE, make them transparent
            for (int x=0;x<img.getWidth();x++) {
                for (int y=0;y<img.getHeight();y++) {
                    if (img.getRGB(x, y)==-1) img.setRGB(x,y,0);
                }
            }
            return img;
        } catch (Exception e) {
            System.out.println("Cannot find image at: "+fileName);
        }
        return null;
    }
    
    private static BufferedImage[] loadImage(String[] source) {
        BufferedImage[] animation = new BufferedImage[source.length];
        for (int n=0;n<source.length;n++) {
            animation[n] = loadImage(source[n]);
        }
        return animation;
    }
    
    // given a string, return an image with that text written on it. note: 'n' means a new line
    public static BufferedImage getString(String source) {
        int height = 1;
        int maxlength = 0;
        int length = 0;
        for (int i=0;i<source.length();i++) {
            length++;
            if (source.charAt(i)=='n') {
                height++;
                if (length>maxlength) maxlength = length;
                length = 0;
            }
        }
        if (length>maxlength) maxlength = length;
        
        BufferedImage comb = new BufferedImage(maxlength*8,height*8,BufferedImage.TYPE_4BYTE_ABGR);
        int xx = -1;
        int yy = 0;
        for (int i=0;i<source.length();i++) {
            xx++;
            if (source.charAt(i)=='n') {
                xx = -1;
                yy ++;
                continue;
            }
            BufferedImage addme = getChar(source.charAt(i));
            comb.getRaster().setRect(xx*8, yy*8, addme.getRaster());
        }
        // if pixels are pink, make them transparent
        // TODO maybe, if pixels are black set them to be slightly transparent
        for (int x=0;x<comb.getWidth();x++) {
            for (int y=0;y<comb.getHeight();y++) {
                if (comb.getRGB(x, y)==-65408) comb.setRGB(x,y,0);
            }
        }
        return comb;
    }
    
    private static BufferedImage getChar(char c) {
        int ch = chars.indexOf(c);
        if (ch==-1) return getChar('%');
        int xi = ch % 13;
        int yi = ch / 13;
        return font.getSubimage(xi*8,yi*8,8,8);
    }
    
    // used in menus, since we only need the main color to be changed.
    public static BufferedImage getString(String source, int rgb1) {
        return getString(source,rgb1,-16777216,0);
    }
    
    // rgb1-main color. rgb2-shadow color. rgb3-background color.
    public static BufferedImage getString(String source, int rgb1, int rgb2, int rgb3) {
        BufferedImage col = getString(source);
        for (int x=0;x<col.getWidth();x++) {
            for (int y=0;y<col.getHeight();y++) {
                // pixels are white
                if (col.getRGB(x,y)==-1) col.setRGB(x,y,rgb1);
                // pixels are black
                if (col.getRGB(x,y)==-16777216) col.setRGB(x,y,rgb2);
                // pixels are transparent
                if (col.getRGB(x,y)==0) col.setRGB(x,y,rgb3);
            }
        }
        return col;
    }
    
    // scale an image larger by an integer multiple
    public static BufferedImage scaleImage(BufferedImage source, int scale) {
        BufferedImage scaled = new BufferedImage(source.getWidth()*scale,source.getHeight()*scale,BufferedImage.TYPE_4BYTE_ABGR);
        for (int x=0;x<source.getWidth();x++) {
            for (int y=0;y<source.getHeight();y++) {
                for (int xi=0;xi<scale;xi++) {
                    for (int yi=0;yi<scale;yi++) {
                        scaled.setRGB(scale*x+xi,scale*y+yi,source.getRGB(x,y));
                    }
                }
            }
        }
        return scaled;
    }
    
    public enum Type {
        EMPTY (255,255,255),
        BLOCK (195,195,195),
        BLOCK_50 (235,235,235),
        STRONG (127,127,127),
        UNBREAKABLE (0,0,0),
        SPIKEUP (255,0,0),
        SPIKEDOWN (255,1,0),
        
        PLAYER (0,0,250),
        SPEAKER (0,255,0),
        CHECKPOINT (0,0,255),
        CHUNK_BOUNDARY (0,0,255),
        DOOR (0,0,200),
        DOOR2 (0,0,201),
        
        ITEM_SHOVEL (100,100,255),
        ITEM_DIODE_1 (101,100,255),
        ITEM_DIODE_2 (102,100,255),
        
        ENEMY_TRACE (100,0,0),
        ENEMY_TRACE_CC (101,0,0),
        ENEMY_REDGLOW (110,0,0),
        MEMORYBLOCK (120,0,0),
        MEMORYBLOCK_2 (121,0,0),
        ENEMY_PACE (130,0,0),
        ENEMY_BOUNCE_V1 (140,0,0),
        ENEMY_BOUNCE_V2 (140,1,0),
        ENEMY_BOUNCE_H1 (141,0,0),
        ENEMY_BOUNCE_H2 (141,1,0),
        ;
        
        int val;
        Type(int r, int g, int b) {
            Color c = new Color(r,g,b);
            val = c.getRGB();
        }
    }
    
    public static int[][] loadLevel(int levelref) {
        BufferedImage src = loadImage("level/level_"+levelref+".png");
        int[][] code = new int[src.getWidth()][src.getHeight()];
        for (int x=0;x<code.length;x++) {
            for (int y=0;y<code[0].length;y++) {
                code[x][y] = src.getRGB(x,y);
                if ((code[x][y]==Type.BLOCK.val || code[x][y]==Type.EMPTY.val) && (x==0 || y==0 || x==code.length-1 || y==code[0].length)) code[x][y] = Type.UNBREAKABLE.val; // edges are unbreakable even if not specified in the image.
            }
        }
        return code;
    }
    
    public static int[][] loadLevelScrollRight(int levelref) {
        BufferedImage src = loadImage("level/level_"+levelref+".png");
        // cycle through and see how many chunks we have
        int chunks = -1;
        for (int x=0;x<src.getWidth();x++) {
            if (src.getRGB(x,0)==Type.CHUNK_BOUNDARY.val) chunks++;
        }
        // now, cycle through and determine where chunks start and how long they are, for reference.
        int[] csize = new int[chunks];
        int[] cstart = new int[chunks];
        int size = 0;
        int cindex = -1;
        for (int x=0;x<src.getWidth();x++) {
            if (src.getRGB(x,0)==Type.CHUNK_BOUNDARY.val) {
                if (cindex>=0) {
                    csize[cindex] = size+1;
                }
                size = 0;
                cindex++;
                if (cindex<chunks) cstart[cindex] = ++x;
            }
            else size++;
        }
        // next, add all necesarry elements to code[].
        int[][] code = new int[src.getWidth()-chunks-1][src.getHeight()];
        // starting area
        for (int x=0;x<=cstart[0]-2;x++) {
            for (int y=0;y<code[0].length;y++) {
                code[x][y] = src.getRGB(x,y);
            }
        }
        // finish area
        for (int x=cstart[chunks-1]+csize[chunks-1]+1;x<src.getWidth();x++) {
            for (int y=0;y<code[0].length;y++) {
                code[x-chunks-1][y] = src.getRGB(x,y);
            }
        }
        // create a random order to add the chunks in
        int[] order = new int[chunks];
        for (int x=0;x<chunks;x++) {
            order[x] = (int)(Math.random()*chunks);
            for (int c=0;c<=x-1;c++) {
                if (order[c] == order[x]) x--;
            }
        }
        // add them to code[].
        int xcount = cstart[0]-1;
        for (int n=0;n<chunks;n++) {
            cindex = order[n];
            for (int x=cstart[cindex];x<cstart[cindex]+csize[cindex];x++) {
                for (int y=0;y<code[0].length;y++) {
                    code[xcount][y] = src.getRGB(x,y);
                }
                xcount++;
            }
        }
        return code;
    }
    
    public static int[][] loadLevelScrollUp(int levelref) {
        // Remember that as opposed to loadLevelScrollRight, the finish area is at the top of the level (start of the image) and the starting area is at the bottom of the level (end of the image).
        BufferedImage src = loadImage("level/level_"+levelref+".png");
        // cycle through and see how many chunks we have
        int chunks = -1;
        for (int y=0;y<src.getHeight();y++) {
            if (src.getRGB(0,y)==Type.CHUNK_BOUNDARY.val) chunks++;
        }
        // now, cycle through and determine where chunks start and how long they are, for reference.
        int[] csize = new int[chunks];
        int[] cstart = new int[chunks];
        int size = 0;
        int cindex = -1;
        for (int y=0;y<src.getHeight();y++) {
            if (src.getRGB(0,y)==Type.CHUNK_BOUNDARY.val) {
                if (cindex>=0) {
                    csize[cindex] = size+1;
                }
                size = 0;
                cindex++;
                if (cindex<chunks) cstart[cindex] = ++y;
            }
            else size++;
        }
        // next, add all necesarry elements to code[].
        int[][] code = new int[src.getWidth()][src.getHeight()-chunks-1];
        // finish area
        for (int y=0;y<cstart[0]-1;y++) {
            for (int x=0;x<code.length;x++) {
                code[x][y] = src.getRGB(x,y);
            }
        }
        // starting area
        for (int y=cstart[chunks-1]+csize[chunks-1]+1;y<src.getHeight();y++) {
            for (int x=0;x<code.length;x++) {
                code[x][y-chunks-1] = src.getRGB(x,y);
            }
        }
        // create a random order to add the chunks in
        int[] order = new int[chunks];
        for (int x=0;x<chunks;x++) {
            order[x] = (int)(Math.random()*chunks);
            for (int c=0;c<=x-1;c++) {
                if (order[c] == order[x]) x--;
            }
        }
        // add them to code[].
        int ycount = cstart[0]-1;
        for (int n=0;n<chunks;n++) {
            cindex = order[n];
            for (int y=cstart[cindex];y<cstart[cindex]+csize[cindex];y++) {
                for (int x=0;x<code.length;x++) {
                    code[x][ycount] = src.getRGB(x,y);
                }
                ycount++;
            }
        }
        return code;
    }
    
    public static void setTileset(String ref) {
        Art.tileset = Art.tilesets.get(ref);
    }
    
    public static void activate() {}
    
}
