package spade;

/** Door the player can enter that changes the level, etc.
 *
 * @author Patrick
 */
public class Door extends Entity {
    
    int levelref; // level this door will take you to.
    int timeout = 40; // how long it takes for player to enter the door.
    int count = 0;
    boolean counting = false;
    boolean open = false;
    
    static String symbols = "HTABCD";
    
    public Door(int xpos, int ypos, int levelref, Game game) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.levelref = levelref;
        this.game = game;
        blockprotected = true;
        
        // TEMP
        if (levelref==0 || SaveData.num_diodes>=LevelData.diode_requirements[levelref]) open = true;
        
        if (levelref>=34 && levelref <= 39) open = false;
        else if (levelref>=24 && levelref <= 29) open = false;
        else if (levelref==4 || levelref==5) open = false;
        
        if (open) sprite = Art.tileset.get("door open");
        else sprite = Art.tileset.get("door closed");
        
        String text1=" "; // Level Number
        String text2=" "; // Diodes Collected, or Diodes Needed
        int x = xpos+8;
        int y1 = ypos-8;
        int y2 = ypos-16;
        
        // text1
        if (game.levelindex>=10) text1=" ";
        else if (SaveData.num_diodes<LevelData.diode_requirements[levelref]) {
            text1 = ">"+LevelData.diode_requirements[levelref];
            if (text1.equals(">99")) text1 = ">i";
        }
        else if (levelref>=10) text1 = symbols.charAt((levelref-levelref%10)/10)+""+Integer.toString(levelref-(levelref-levelref%10));
        else if (levelref==0) text1 = "HUB";
        else if (levelref==1) text1 = "TUTORIAL";
        else {
            text1 = Character.toString(symbols.charAt(levelref));
        }
        
        // text2
        if (game.levelindex>=10) text2=" ";
        else if (SaveData.num_diodes<LevelData.diode_requirements[levelref]) text2 = " ";
        else if (levelref>=10) {
            text2 = getDiode();
        }
        
        game.entities.add(new TextEntity(x-4*text1.length(),y1,text1,TextEntity.STATICSMALL_COLOR,game));
        game.entities.add(new TextEntity(x-4*text2.length(),y2,text2,TextEntity.STATICSMALL_COLOR,game));
    }
    
    private String getDiode() {
        char a = 'v';
        if (SaveData.levelPassed(levelref)) a = '^';
        return Character.toString(a);
    }
    
    private String getDiode2() {
        char a = 'v';
        char b = 'v';
        if (SaveData.levelPassed(levelref)) a = '^';
        if (SaveData.levelObj_2(levelref)) b = '^';
        return Character.toString(a)+Character.toString(b);
    }
    
    private String getLocked() {
        char b = 'A';
        return ">-"+LevelData.diode_requirements[levelref];
    }
    
    public void tick() {
        if (counting) count++;
        if (count==timeout) {
            game.changeLevel(levelref);
        }
    }
    
    public void startTimer() {
        counting = true;
    }
    
    public boolean isOpen() {
        return open;
    }
    
}
