package spade;

import java.awt.Color;

/** Entity that displays text and times out.
 *
 * @author Patrick
 */
public class TextEntity extends Entity {
    
    int timeout;
    int count = 0;
    int scale = 1;
    int type;
    int targety; // used for EASE_UP
    String source;
    
    public static int POINTS = 1;
    public static int STATIC = 2;
    public static int STATICSMALL = 3;
    public static int STATICSMALL_COLOR = 4;
    public static int EASE_UP = 5;
    public static int FOLLOW_PLAYER = 6;
    
    public TextEntity(int xpos, int ypos, String source, int type, Game game) {
        this.xpos = xpos;
        this.ypos = ypos;
        xabs = xpos;
        yabs = ypos;
        this.source = source;
        this.game = game;
        this.type = type;
        meltsTime = true;
        int rgb_main = -1;
        int rgb_dark = -16777216;
        int rgb_back = 0;
        if (type==POINTS) {
            yspeed = -0.2;
            timeout = 50;
        }
        else if (type==STATIC) {
            timeout = -1;
            scale = 2;
        }
        else if (type==STATICSMALL) {
            timeout = -1;
        }
        else if (type==STATICSMALL_COLOR) {
            timeout = -1;
            rgb_main = Art.tileset.rgb_font_main;
            rgb_dark = Art.tileset.rgb_font_dark;
            rgb_back = Art.tileset.rgb_font_back;
        }
        else if (type==EASE_UP) {
            timeout = 100;
            targety = ypos;
            yabs+=40;
            ypos = (int)yabs;
        }
        else if (type==FOLLOW_PLAYER) {
            timeout = 100;
        }
        sprite = Art.scaleImage(Art.getString(source,rgb_main,rgb_dark,rgb_back),scale);
        width = sprite.getWidth();
        height = sprite.getHeight();
    }
    
    public void tick() {
        //if (count%5==0) setColor((int)(Math.random()*-1000000000));  // in case anyone ever reads this, de-comment this line for fun times
        if (type==EASE_UP) {
            yabs -= (ypos-targety)*0.08;
            ypos = (int)yabs;
            //xabs = xpos = game.player.getXpos()-32;
        }
        if (type==FOLLOW_PLAYER) {
            xabs = xpos = game.player.getXpos()+8-4*source.length();
            yabs = ypos = game.player.getYpos()-16;
        }
        xabs+=xspeed;
        yabs+=yspeed;
        xpos = (int)xabs;
        ypos = (int)yabs;
        if (count<timeout) count++;
        else if (count == timeout) game.entities.remove(this);
    }
    
    public void setColor(Color c) {
        int rgb = c.getRGB();
        sprite = Art.scaleImage(Art.getString(source,rgb),scale);
    }
    public void setColor(int rgb) {
        sprite = Art.scaleImage(Art.getString(source,rgb),scale);
    }
    public void setColors(Color text, Color shadow, Color bg) {
        
    }
    
    // Idk where this belongs
    public static String padZeroes(String src, int final_length) {
        String padded = src;
        while (padded.length()<final_length) {
            padded = "0"+padded;
        }
        return padded;
    }
    
}