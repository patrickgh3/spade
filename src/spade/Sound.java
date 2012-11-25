package spade;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/** Database and player of sounds in the game. credit = notch, prelude of the chambered
 * 
 * @author Patrick
 */
public class Sound {
    public static Sound jump = loadSound("res/sound/jump.wav");
    public static Sound brickbreak = loadSound("res/sound/break.wav");
    public static Sound break2 = loadSound("res/sound/break2.wav");
    public static Sound die = loadSound("res/sound/die.wav");
    public static Sound redglow = loadSound("res/sound/redglow.wav");
    public static Sound flame = loadSound("res/sound/flame.wav");
    public static Sound blip = loadSound("res/sound/blip.wav");
    public static Sound blip2 = loadSound("res/sound/blip2.wav");
    public static Sound explode2 = loadSound("res/sound/explode2.wav");
    public static Sound target_acquired = loadSound("res/sound/target_acquired.wav");
    public static Sound obtain = loadSound("res/sound/obtain.wav");
    public static Sound land = loadSound("res/sound/land.wav");
    public static Sound steps = loadSound("res/sound/steps.wav");
    public static Sound text = loadSound("res/sound/text.wav");
    public static Sound checkpoint = loadSound("res/sound/checkpoint.wav");
    
    static boolean mute = false;
    static boolean musicmute = false;
    
    public static final HashMap<String,Sound> list = new HashMap<>();
    
    static {
        put("jump");
        put("break");
        put("break2");
        put("die");
        put("redglow");
        put("flame");
        put("blip");
        put("blip2");
        put("explode2");
        put("target_acquired");
        put("obtain");
        put("land");
        put("steps");
    }
    
    private static void put(String key) {
        list.put(key,loadSound("res/sound/"+key));
    }
    
    public static Sound loadSound(String fileName) {
        Sound sound = new Sound();
        try {
            InputStream audioSrc = Sound.class.getClassLoader().getResourceAsStream(fileName);
            //InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc);
            // note for the above line: I changed Sound.class to Sound.class.getClassLoader(). maybe it's different because this is an application and poc is an applet. who knows, i'm just guessing.
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sound.clip = clip;
        } catch (Exception e) {
            System.out.println(e);
        }
        return sound;
    }
    
    public static void play(String key) {
        list.get(key).play();
    }
    
    private Clip clip;
    
    public void play() {
        if (mute) return;
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void loop() {
        if (musicmute) return;
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void stop() {
        clip.stop();
    }
    
    public static void switchMute() {
        mute = !mute;
    }
    
    public static void switchMusicMute() {
        musicmute = !musicmute;
    }
    
    public static void activate() {}
}
