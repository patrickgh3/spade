package spade;

/** Beats the level and saves when picked up.
 *
 * @author Patrick
 */
public class DiodeItem extends Item {
    
    int type;
    static final int TYPE_1 = 1;
    static final int TYPE_2 = 2;
    boolean real;
    
    public DiodeItem(int xpos, int ypos, int type, boolean real, Game game) {
        this.xpos = xpos+4;
        this.ypos = ypos+4;
        this.game = game;
        this.type = type;
        this.real = real;
        if (real) sprite = Art.entities.get("diode");
        else sprite = Art.entities.get("diode ghost");
        spriteXo = spriteYo = -4;
    }
    
    public void pickup() {
        if (real) {
            SaveData.diodeGet(game.levelindex,type-1);
            game.entities.add(new TextEntity(xpos-32,ypos-24,"DIODE GET!",TextEntity.EASE_UP,game));
            game.whiteFadeEffect();
        }
        //game.whiteFadeEffect();
        Sound.obtain.play();
        game.entities.remove(this);
    }
    
}
