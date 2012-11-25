package spade;

/** Text that types itself out at a specified speed.
 *
 * @author Patrick
 */
public class TypeTextEntity extends TextEntity {
    
    int typecount = 0;
    double speed;
    int previouslength = 0;
    
    public static final double SLOW = 0.25;
    public static final double FAST = 2;
    
    public TypeTextEntity(int xpos, int ypos, String source, double speed, Game game) {
        super(xpos,ypos,source,STATICSMALL,game);
        this.speed = speed;
        // adjust position because of string length/height.
        int height = 1;
        int length = 0;
        int maxlength = 0;
        for (int i=0;i<source.length();i++) { 
            length++;
            if (source.charAt(i)=='n') {
                height++;
                if (length>maxlength) maxlength = length;
                length = 0;
            }
        }
        if (length>maxlength) maxlength = length;
        
        this.xpos = xpos - 4*(maxlength-2);
        this.ypos = ypos - 8*(height-1);
        xabs = this.xpos;
        yabs = this.ypos;
    }
    
    public void tick() {
        super.tick();
        if (previouslength<source.length()) typecount++;
        int length = (int)(typecount*speed+1);
        if (length<=source.length()) sprite = Art.getString(source.substring(0,length));
        // TODO sound when letter appears. Note: only one sound even if 2 letters appear.
        if (length>previouslength && !(source.charAt(length-1)=='n' && source.charAt(length-2)=='n')) Sound.text.play();
        previouslength = length;
    }
    
}
