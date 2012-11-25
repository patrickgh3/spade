package spade;

import java.io.*;

/** Manages the player's saved game data. Game is saved upon loading a level.
 * Credit: http://www.vbforums.com/showthread.php?308145-JAVA-saving-loading-an-array-to-from-a-file
 *
 * @author Patrick
 */
public abstract class SaveData {
    static String directory = "save";
    static int[][] data = new int[120][2];
    static File checker = new File(directory);
    static int num_diodes;
    static boolean displaynumberonspawn = true;
    
    static final int progress = 100; // not yet implemented. what does 'progress' even mean? I forgot why this is here
    static final int num_jumps = 101;
    static final int num_created = 102;
    static final int num_destroyed = 103;
    static final int num_deaths = 104;
    static final int playtime = 105; // not yet implemented
    
    public static boolean levelPassed(int levelindex) {
        return data[levelindex][0]==1;
    }
    public static boolean levelObj_2(int levelindex) {
        return data[levelindex][1]==1;
    }
    public static boolean areaComplete(int levelindex) {
        // TODO much later: display diode over area door if all diodes are obtained in that area.
        return false;
    }
    public static int getDeaths() {
        return data[num_deaths][0];
    }
    public static int getJumps() {
        return data[num_jumps][0];
    }
    public static int getCreated() {
        return data[num_created][0];
    }
    public static int getDestroyed() {
        return data[num_destroyed][0];
    }
    
    
    
    // TODO: Announce milestones?
    public static void diodeGet(int level, int y) {
        num_diodes++;
        data[level][y] = 1;
        saveArray();
    }
    public static void playerDeath() {
        data[num_deaths][0]++;
        displaynumberonspawn = true;
    }
    public static void playerJump() {
        data[num_jumps][0]++;
    }
    public static void create() {
        data[num_created][0]++;
    }
    public static void destroy() {
        data[num_destroyed][0]++;
    }
    
    
    
    public static void saveArray() {
        //System.out.println("saved array");
        try {
            FileOutputStream fos = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(data);
            out.flush();
            out.close();
        } catch (IOException e) {
        System.out.println(e); 
        }
    }
    
    public static void loadArray() {
        //System.out.println("loaded array");
        if (!checker.exists()) {
            saveNewArray();
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(directory);
            ObjectInputStream in = new ObjectInputStream(fis);
            int[][] read = (int[][])in.readObject();
            in.close();
            data = read;
        } catch (Exception e) {
          System.out.println(e);
        }
        num_diodes = 0;
        for (int y=0;y<2;y++) {
            for (int x=0;x<100;x++) {
                if (data[x][y]==1) num_diodes++;
            }
        }
    }
    
    public static void saveNewArray() {
        //System.out.println("erased and saved new array");
        data = new int[120][2];
        saveArray();
        num_diodes = 0;
    }
    
}
