package spade;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/** HashMap that holds tiles. Made as a separate class because all tilesets will have the same indexes.
 *
 * @author Patrick
 */
public class Tileset {
    
    HashMap<String,BufferedImage> images = new HashMap<>();
    HashMap<String,Point> positions = new HashMap<>();
    ArrayList<SmallTile> _8by8 = new ArrayList<>();
    int rgb_font_main, rgb_font_dark, rgb_font_back;
    
    public Tileset(BufferedImage src) {
        // 8x8 tiles
        _8by8.add(new SmallTile("topleft",0,0));
        _8by8.add(new SmallTile("topright",3,0));
        _8by8.add(new SmallTile("bottomleft",0,3));
        _8by8.add(new SmallTile("bottomright",3,3));
        _8by8.add(new SmallTile("top1",1,0));
        _8by8.add(new SmallTile("top2",2,0));
        _8by8.add(new SmallTile("bottom1",1,3));
        _8by8.add(new SmallTile("bottom2",2,3));
        _8by8.add(new SmallTile("right1",3,1));
        _8by8.add(new SmallTile("right2",3,2));
        _8by8.add(new SmallTile("left1",0,1));
        _8by8.add(new SmallTile("left2",0,2));
        _8by8.add(new SmallTile("insidecorner1",4,0));
        _8by8.add(new SmallTile("insidecorner2",5,0));
        _8by8.add(new SmallTile("insidecorner3",4,1));
        _8by8.add(new SmallTile("insidecorner4",5,1));
        _8by8.add(new SmallTile("center1",1,1));
        _8by8.add(new SmallTile("center2",2,1));
        _8by8.add(new SmallTile("center3",1,2));
        _8by8.add(new SmallTile("center4",2,2));

        for (int n=0;n<_8by8.size();n++) {
            SmallTile e = _8by8.get(n);
            images.put(e.index,src.getSubimage(9*e.x,9*e.y,8,8));
        }
        
        // 16x16 tiles
        images.put("break",src.getSubimage(0,36,16,16));
        images.put("bg",src.getSubimage(16,36,16,16));
        images.put("memory on",src.getSubimage(32,36,16,16));
        images.put("door open",src.getSubimage(0,58,16,16));
        images.put("door closed",src.getSubimage(16,58,16,16));
        
        //font colors
        rgb_font_main = src.getRGB(0,53);
        rgb_font_dark = src.getRGB(4,53);
        rgb_font_back = 0;
    }
    
    public BufferedImage get(String ref) {
        if (images.containsKey(ref)) return images.get(ref);
        else return Art.commontiles.get(ref);
    }
    
}
