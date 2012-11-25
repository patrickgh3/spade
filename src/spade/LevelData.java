package spade;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Stores information about levels.
 *
 * @author Patrick
 */
public abstract class LevelData {
    
    //public static final int[] scrolltypes = new int[100];
    public static final int[] gamemodes = new int[100];
    public static final String[] tilesets = new String[100];
    public static final int[] shovel = new int[100];
    public static final String[][] speakertext = new String[100][5];
    public static final double[] scrollspeeds = new double[100];
    public static final int[] horizprojectiles = new int[100];
    public static final int[] verticalprojectiles = new int[100];
    public static final int[] homingtarget = new int[100];
    public static final String[] music = new String[100];
    public static final int[] diode_requirements = new int[100];
    
    public enum GameMode {
        DEFAULT (1);
        
        
        int val;
        GameMode(int val) {
            this.val = val;
        }
    }
    
    static {
        //Arrays.fill(speakertext,new String[]{"ASDF","ASDF","ASDF","ASDF","ASDF",}); // for some reason this line caused bugs later on. I don't get it
        String ref = "res/level/levelinfo.txt";
        
        try {
            URL url = LevelData.class.getClassLoader().getResource(ref);
            Scanner sc = new Scanner(url.openStream());
            //Pattern normal = sc.delimiter();
            //Pattern equals = Pattern.compile("=");
            //sc.useDelimiter(normal);
            while (sc.hasNext()) {
                sc.findWithinHorizon("Level ", 100);
                int level = sc.nextInt();
                //System.out.println("parsing level: "+level);
                
                sc.nextLine();
                sc.skip("diodereq=");
                diode_requirements[level] = sc.nextInt();
                
                sc.nextLine();
                sc.skip("tileset=");
                tilesets[level] = sc.next();
                
                //sc.nextLine();
                //sc.skip("music=");
                //music[level] = sc.next();
                
                sc.nextLine();
                sc.skip("gamemode=");
                String st = sc.next();
                if (st.equals("normal")) gamemodes[level] = Game.MODE_NORMAL;
                else if (st.equals("horiz_autoscroll")) gamemodes[level] = Game.MODE_HORIZ_AUTOSCROLL;
                else if (st.equals("vertical_autoscroll")) gamemodes[level] = Game.MODE_VERTICAL_AUTOSCROLL;
                else if (st.equals("run")) gamemodes[level] = Game.MODE_RUN;
                
                if (gamemodes[level]==Game.MODE_HORIZ_AUTOSCROLL || gamemodes[level]==Game.MODE_VERTICAL_AUTOSCROLL) {
                    sc.nextLine();
                    sc.skip("scrollspeed=");
                    scrollspeeds[level] = sc.nextDouble();
                    sc.nextLine();
                    sc.skip("hproj=");
                    horizprojectiles[level] = sc.nextInt();
                    sc.nextLine();
                    sc.skip("vproj=");
                    verticalprojectiles[level] = sc.nextInt();
                    sc.nextLine();
                    sc.skip("homing=");
                    homingtarget[level] = sc.nextInt();
                }
                
                sc.nextLine();
                sc.skip("shovel=");
                shovel[level] = sc.nextInt();
                
                sc.nextLine();
                if (sc.hasNext())
                while (!sc.hasNext("end")) {
                    sc.skip("message");
                    String s = sc.nextLine();
                    int index = Integer.parseInt(s.substring(0,1));
                    String msg = s.substring(2);
                    speakertext[level][index] = msg;
                }
                if (sc.hasNext()) {
                    sc.nextLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot find file at: "+ref);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
    
    public static void activate() {}
}
