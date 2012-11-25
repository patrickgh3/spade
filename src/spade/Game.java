package spade;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Iterator;
import java.util.Scanner;

/** Class that handles all the game logic.
 * 
 * @author Patrick
 */
public class Game {
    
    InputHandler inputhandler;
    GameCanvas canvas;
    Tile[][] level;
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Entity> HUDentities = new ArrayList<>();
    
    MenuSelector menuSelector;
    Player player;
    Entity mainSpike;
    ScrollTemp scrollTemp;
    
    int state; // literally what the game is doing right now
    public static int STATE_MENU = 1;
    public static int STATE_INGAME = 2;
    public static int STATE_PAUSED = 3;
    
    int gamemode; // determines various settings for different level types, such as score, etc.
    public static int MODE_NORMAL = 1;
    public static int MODE_HORIZ_AUTOSCROLL = 2;
    public static int MODE_VERTICAL_AUTOSCROLL = 3;
    public static int MODE_RUN = 4;
    public static int MODE_CLIMB = 5;
    
    boolean scroll_respawning = false; // if the player is respawning, we scroll back to his checkpoint.
    int scrollrespawncount = 0;
    double respawnXtrans;
    double respawnYtrans;
    boolean autoscroll_ended = false;
    int deathrespawncount = 0; // if the player is respawning but shouldn't return to his checkpoint, when the timer runs out we just restart the level.
    public static final int deathrespawntime = 150;
    boolean death_respawning = false;
    
    int effecttime = 0;
    int effectcount = 0;
    boolean timefreeze = false;
    
    int tickcount; // used for spawning entities e.g. projectiles, homingtarget
    int lasthprojy = 0;
    int lastvprojx = 0;
        
    boolean whitefade_effect = false;
    int whitefade_count = 0;
    int whitefade_time = 30;
    float whitefade_alpha = (float)1.0;
    
    int w,h; // window width and height
    int Xtrans = 0;
    int Ytrans = 0;
    int levelindex = 0;
    int score = 0;
    double textspeed = TypeTextEntity.SLOW;
    boolean playsoundbreak = false;
    
    public Game(InputHandler i,GameCanvas c, int w, int h) {
        this.w = w;
        this.h = h;
        inputhandler = i;
        canvas = c;
        menuSelector = new MenuSelector(this);
        startMenu();
    }
    
    
    
    public void startMenu() {
        state = STATE_MENU;
        
        entities.clear();
        resetEffects();
        Art.setTileset("alpha");
        score = 0;
        timefreeze = false;
        menuSelector.setState(MenuSelector.Menu.TITLE);
    }
    
    public void changeLevel(int levelindex) {
        state = STATE_INGAME;
        this.levelindex = levelindex;
        Art.setTileset(LevelData.tilesets[levelindex]);
        this.gamemode = LevelData.gamemodes[levelindex];
        SaveData.saveArray();
        
        entities.clear();
        HUDentities.clear();
        resetEffects();
        scroll_respawning = false;
        autoscroll_ended = false;
        death_respawning = false;
        deathrespawncount=0;
        tickcount = 0;
        if (gamemode==MODE_NORMAL) loadLevel(Art.loadLevel(levelindex));
        else if (gamemode==MODE_HORIZ_AUTOSCROLL) loadLevel(Art.loadLevelScrollRight(levelindex));
        else if (gamemode==MODE_VERTICAL_AUTOSCROLL) loadLevel(Art.loadLevelScrollUp(levelindex));
    }
    
    public void loadLevel(int[][] code) {
        // set level and add entities
        level = new Tile[code.length][code[0].length];
        int speakerindex = 0;
        int doorindex = 0;
        if (levelindex==0) doorindex++;
        for (int x=0;x<code.length;x++) {
            for (int y=code[0].length-1;y>=0;y--) {
                int type = code[x][y];
                // tiles
                if (type==Art.Type.EMPTY.val && (code[x][y+1]==Art.Type.DOOR.val || code[x][y+1]==Art.Type.DOOR.val)) level[x][y] = new Tile(Tile.FENCE);
                else if (type==Art.Type.EMPTY.val) level[x][y] = new Tile(Tile.EMPTY);
                else if (type==Art.Type.BLOCK.val) level[x][y] = new Tile(Tile.BLOCK);
                else if (type==Art.Type.BLOCK_50.val) {
                    if (Math.random()>0.5) level[x][y] = new Tile(Tile.EMPTY);
                    else level[x][y] = new Tile(Tile.BLOCK);
                }
                else if (type==Art.Type.STRONG.val) level[x][y] = new Tile(Tile.STRONG);
                else if (type==Art.Type.UNBREAKABLE.val) level[x][y] = new Tile(Tile.UNBREAKABLE);
                else if (type==Art.Type.SPIKEUP.val) level[x][y] = new Tile(Tile.SPIKEUP);
                else if (type==Art.Type.SPIKEDOWN.val) level[x][y] = new Tile(Tile.SPIKEDOWN);
                // special - entities
                else {
                    level[x][y] = new Tile(Tile.EMPTY);
                    int xx = 16*x;
                    int yy = 16*y;
                    if (type==Art.Type.PLAYER.val) {
                        level[x][y] = new Tile(Tile.FENCE);
                        boolean shovel = false;
                        if (LevelData.shovel[levelindex]==1) shovel = true;
                        else if (LevelData.shovel[levelindex]==2) shovel = (SaveData.levelPassed(12));
                        player = new Player(xx,yy,shovel,this);
                        Checkpoint c = new Checkpoint(xx,yy,this);
                        entities.add(c);
                        c.setActive(true);
                        player.setCheckpoint(c);
                        
                        scroll_respawning = true;
                        scrollrespawncount=Checkpoint.RESPAWNTIME-32;
                        c.startTimerInit();
                        scrollTemp = new ScrollTemp(player.getXcenter()+1,player.getYcenter()+1,player.getXcenter()+1,player.getYcenter()+1);
                        entities.add(scrollTemp);
                    }
                    else if (type==Art.Type.SPEAKER.val) entities.add(new Speaker(xx,yy,levelindex,speakerindex++,this));
                    else if (type==Art.Type.CHECKPOINT.val) {
                        level[x][y] = new Tile(Tile.FENCE);
                        entities.add(new Checkpoint(xx,yy,this));
                    }
                    else if (type==Art.Type.ITEM_SHOVEL.val) entities.add(new ShovelItem(xx,yy,this));
                    else if (type==Art.Type.ITEM_DIODE_1.val) entities.add(new DiodeItem(xx,yy,DiodeItem.TYPE_1,!SaveData.levelPassed(levelindex),this));
                    else if (type==Art.Type.ITEM_DIODE_2.val) entities.add(new DiodeItem(xx,yy,DiodeItem.TYPE_2,!SaveData.levelObj_2(levelindex),this));
                    else if (type==Art.Type.DOOR.val) {
                        if (levelindex>=10) entities.add(new Door(xx,yy,levelindex/10,this));
                        else if (levelindex<10) entities.add(new Door(xx,yy,levelindex*10+doorindex++,this));
                    }
                    else if (type==Art.Type.DOOR2.val) entities.add(new Door(xx,yy,0,this));
                    else if (type==Art.Type.ENEMY_TRACE.val) entities.add(new EdgeTraceEnemy(xx,yy,EdgeTraceEnemy.CLOCKWISE,this));
                    else if (type==Art.Type.ENEMY_TRACE_CC.val) entities.add(new EdgeTraceEnemy(xx,yy,EdgeTraceEnemy.COUNTERCLOCKWISE,this));
                    else if (type==Art.Type.ENEMY_REDGLOW.val) {
                        level[x][y] = new Tile(Tile.UNBREAKABLE_INVISIBLE);
                        entities.add(new RedGlowEnemy(xx,yy,this));
                    }
                    else if (type==Art.Type.MEMORYBLOCK.val) entities.add(new MemoryBlock(xx,yy,1,this));
                    else if (type==Art.Type.MEMORYBLOCK_2.val) entities.add(new MemoryBlock(xx,yy,2,this));
                    else if (type==Art.Type.ENEMY_PACE.val) entities.add(new PaceEnemy(xx,yy,this));
                    else if (type==Art.Type.ENEMY_BOUNCE_V1.val) entities.add(new BounceEnemy(xx,yy,0,0.7,this));
                    else if (type==Art.Type.ENEMY_BOUNCE_V2.val) entities.add(new BounceEnemy(xx,yy,0,1.7,this));
                    else if (type==Art.Type.ENEMY_BOUNCE_H1.val) entities.add(new BounceEnemy(xx,yy,0.7,0,this));
                    else if (type==Art.Type.ENEMY_BOUNCE_H2.val) entities.add(new BounceEnemy(xx,yy,1.4,0,this));
                    else System.out.println("invalid tyle type.");
                }
            }
        }
        // add additional entities/etc. depending on game mode
        if (gamemode==MODE_HORIZ_AUTOSCROLL) {
            mainSpike = new SpikeMoveRight(0,LevelData.scrollspeeds[levelindex],true,this);
            entities.add(mainSpike);
            for (int n=0;n<8;n++) {
                entities.add(new SpikeMoveRight(n*16,LevelData.scrollspeeds[levelindex],false,this));
                entities.add(new SpikeMoveRight(n*16,-LevelData.scrollspeeds[levelindex],false,this));
            }
        }
        else if (gamemode==MODE_VERTICAL_AUTOSCROLL) {
            mainSpike = new SpikeMoveUp(0,-LevelData.scrollspeeds[levelindex],true,this);
            entities.add(mainSpike);
            for (int n=0;n<12;n++) {
                entities.add(new SpikeMoveUp(n*16,-LevelData.scrollspeeds[levelindex],false,this));
                entities.add(new SpikeMoveUp(n*16,LevelData.scrollspeeds[levelindex],false,this));
            }
        }
        // set graphics of certain blocks (i.e. strong and unbreakable blocks)
        boolean[][] corners = new boolean[level.length*2+2][level[0].length*2+2]; // array of 8x8 tiles, padded on the edges.
        for (int x=0;x<corners.length;x++) {
            for (int y=0;y<corners[0].length;y++) {
                boolean type = false;
                int num = 0;
                int lx = (int)((x-1)/2);
                int ly = (int)((y-1)/2);
                // case 1 - corners
                if ((x==0 || x==corners.length-1) && (y==0 || y==corners[0].length-1)) type = true;
                // case 2 - edges
                else if (x==0)                  num = level[0][ly].getType();
                else if (x==corners.length-1)   num = level[level.length-1][ly].getType();
                else if (y==0)                  num = level[lx][0].getType();
                else if (y==corners[0].length-1)   num = level[lx][level[0].length-1].getType();
                // case 3 - middles
                else {
                    if (level[lx][ly].getType()==Tile.STRONG || level[lx][ly].getType()==Tile.UNBREAKABLE) type = true;
                    else type = false;
                }
                if (num!=0) {
                    if (num==Tile.STRONG || num==Tile.UNBREAKABLE) type = true;
                    else type = false;
                }
                corners[x][y] = type;
            }
        }
        // print the array - used for testing purposes
//        for (int y=0;y<corners[0].length;y++) {
//            for (int x=0;x<corners.length;x++) {
//            
//                System.out.print(Boolean.toString(corners[x][y])+" ");
//            }
//            System.out.println();
//        }
        for (int x=0;x<level.length;x++) {
            for (int y=0;y<level[0].length;y++) {
                level[x][y].updateSprite(corners,x,y);
            }
        }
    }
    
    public void menuSelection(int index) {
        MenuSelector.Menu menu = menuSelector.getMenu();
        if (menu == MenuSelector.Menu.TITLE) {
            entities.remove(menuSelector);
            if (index==0) {
                if (SaveData.num_diodes>=2) changeLevel(0);
                else if (SaveData.num_diodes>=1) changeLevel(1);
                else changeLevel(10);
            }
            else if (index==1) menuSelector.setState(MenuSelector.Menu.OPTIONS);
            else if (index==2) menuSelector.setState(MenuSelector.Menu.STATS);
            else if (index==3) menuSelector.setState(MenuSelector.Menu.CREDITS_1);
            else if (index==4) System.exit(0);
        }
        else if (menu == MenuSelector.Menu.PAUSE) {
            if (index==0) {
                state = STATE_INGAME;
                menuSelector.removeAll();
            }
            else if (index==1) {
                menuSelector.setState(MenuSelector.Menu.OPTIONS);
            }
            else if (index==2) {
                menuSelector.setState(MenuSelector.Menu.HELP_1);
            }
            else if (index==3) {
                menuSelector.removeAll();
                // this buffer is here to reset effects(timefreeze), scroll(respawnscroll), etc.
                changeLevel(9);
                startMenu();
            }
        }
        else if (menu == MenuSelector.Menu.PAUSE_LEVEL) {
            if (index==0) {
                state = STATE_INGAME;
                menuSelector.removeAll();
            }
            else if (index==1) {
                menuSelector.setState(MenuSelector.Menu.OPTIONS);
            }
            else if (index==2) {
                menuSelector.setState(MenuSelector.Menu.HELP_1);
            }
            else if (index==3) {
                menuSelector.removeAll();
                int oldindex = levelindex;
                changeLevel(9);
                changeLevel(oldindex/10);
            }
        }
        else if (menu==MenuSelector.Menu.HELP_1) {
            menuSelector.setState(MenuSelector.Menu.HELP_2);
        }
        else if (menu==MenuSelector.Menu.HELP_2) {
            menuSelector.setState(menuSelector.previous);
        }
        else if (menu==MenuSelector.Menu.OPTIONS) {
            if (index==0) {
                canvas.changeResolution();
                menuSelector.updateOptions();
            }
            else if (index==1) {
                Sound.switchMute();
                menuSelector.updateOptions();
            }
            else if (index==2) {
                Sound.switchMusicMute();
                menuSelector.updateOptions();
            }
            else if (index==3) menuSelector.setState(menuSelector.previous);
        }
        else if (menu==MenuSelector.Menu.CREDITS_1) {
            menuSelector.setState(MenuSelector.Menu.CREDITS_2);
        }
        else if (menu==MenuSelector.Menu.CREDITS_2) {
            menuSelector.setState(MenuSelector.Menu.CREDITS_3);
        }
        else if (menu==MenuSelector.Menu.CREDITS_3) {
            menuSelector.setState(MenuSelector.Menu.TITLE);
        }
        else if (menu==MenuSelector.Menu.STATS) {
            if (index==0) menuSelector.setState(MenuSelector.Menu.TITLE);
            else if (index==1) menuSelector.setState(MenuSelector.Menu.STATS_2);
        }
        else if (menu==MenuSelector.Menu.STATS_2) {
            if (index==1) {
                SaveData.saveNewArray();
                menuSelector.setState(MenuSelector.Menu.TITLE);
            }
            else if (index==0) menuSelector.setState(MenuSelector.Menu.TITLE);
        }
        Sound.blip2.play();
    }
    
    public void tick() {
        // game entities logic
        if (!(state==STATE_PAUSED)) {
            for (int n=0;n<entities.size();n++) {
                if (!timefreeze || entities.get(n).meltsTime) entities.get(n).tick();
            }
            // effect logic
            if (effectcount<effecttime) effectcount++;
            else if (effectcount==effecttime) {
                resetEffects();
                effecttime = -1;
            }
        }
        for (int n=0;n<HUDentities.size();n++) {
            HUDentities.get(n).tick();
        }
        
        // level logic: create projectiles, etc.
        if (playsoundbreak) {
            playsoundbreak = false;
            Sound.break2.play();
        }
        if (whitefade_effect) {
            whitefade_count++;
            whitefade_alpha*=0.85;
        }
        if (whitefade_effect && whitefade_count==whitefade_time) whitefade_effect = false;
        if (state==STATE_INGAME && player.hasPassedScrollPoint() && !autoscroll_ended && (gamemode==MODE_HORIZ_AUTOSCROLL || gamemode==MODE_VERTICAL_AUTOSCROLL)) {
            tickcount++;
            if (gamemode==MODE_HORIZ_AUTOSCROLL) {
                if (LevelData.horizprojectiles[levelindex]!=0 && tickcount%LevelData.horizprojectiles[levelindex]==0) {
                    int y = lasthprojy;
                    while (y==lasthprojy) {
                        y = randInt(1,6);
                    }
                    lasthprojy=y;
                    entities.add(new HorizontalProjectile(16*y+2,this));
                }
                if (LevelData.verticalprojectiles[levelindex]!=0 && tickcount%LevelData.verticalprojectiles[levelindex]==0) {
                    int x = lastvprojx;
                    while (x==lastvprojx) {
                        x = (int)(     (randInt(-Xtrans+16,-Xtrans+w-40)+64/VerticalProjectile.speed+VerticalProjectile.waittime*LevelData.scrollspeeds[levelindex])     /16)*16+2;
                    }
                    lastvprojx = x;
                    entities.add(new VerticalProjectile(x,this));
                }
            }
            else if (gamemode==MODE_VERTICAL_AUTOSCROLL) {
                // h and v projectiles are sort of reversed here. also may need to add setPosition() to verticalprojectile as well.
                if (LevelData.verticalprojectiles[levelindex]!=0 && tickcount%LevelData.verticalprojectiles[levelindex]==0) {
                    int x = lastvprojx;
                    while (x==lastvprojx) {
                        x = randInt(1,10);
                    }
                    lastvprojx = x;
                    entities.add(new VerticalProjectile(16*x+2,this));
                }
                if (LevelData.horizprojectiles[levelindex]!=0 && tickcount%LevelData.horizprojectiles[levelindex]==0) {
                    int y = lasthprojy;
                    while (y==lasthprojy) {
                        y = (int)(       (randInt(-Ytrans+48,-Ytrans+h)-96/HorizontalProjectile.speed-HorizontalProjectile.waittime*LevelData.scrollspeeds[levelindex])      /16)*16+2;
                    }
                    lasthprojy=y;
                    entities.add(new HorizontalProjectile(y,this));
                }
            }
        }
        if (state==STATE_INGAME && LevelData.homingtarget[levelindex]!=0 && tickcount%LevelData.homingtarget[levelindex]==0 && player.hasPassedScrollPoint() && !autoscroll_ended) {
            entities.add(new HomingTarget(this));
        }
        for (int n=0;n<entities.size();n++) {
            Entity e = entities.get(n);
            if (e instanceof HorizontalProjectile ){
                ((HorizontalProjectile)e).setPosition();
            }
            else if (e instanceof VerticalProjectile) {
                ((VerticalProjectile)e).setPosition();
            }
            else if (e instanceof SpikeMoveRight) {
                ((SpikeMoveRight)e).setPosition();
            }
            else if (e instanceof SpikeMoveUp) {
                ((SpikeMoveUp)e).setPosition();
            }
        }
        
        // set translate amounts
        if (death_respawning) {
            if (state!=STATE_PAUSED) deathrespawncount++;
            if (deathrespawncount==deathrespawntime) {
                death_respawning = false;
                this.changeLevel(levelindex);
            }
        }
        if (scroll_respawning) {
            if (state!=STATE_PAUSED) scrollrespawncount++;
            Xtrans = (int)(-Math.min(Math.max(scrollTemp.getXpos()-w/2,0),16*level.length-w));
            Ytrans = (int)(-Math.min(Math.max(scrollTemp.getYpos()-h/2,0),16*level[0].length-h));
            if (scrollrespawncount==Checkpoint.RESPAWNTIME) {
                entities.remove(scrollTemp);
                player.revive();
                scroll_respawning = false;
            }
        }
        else if (state==STATE_INGAME || state==STATE_PAUSED) {
            if (gamemode == MODE_NORMAL) { //scrolltype == SCROLL_PLAYER
                Xtrans = (int)(-Math.min(Math.max(player.getXcenter()-w/2,0),16*level.length-w));
                Ytrans = (int)(-Math.min(Math.max(player.getYcenter()-h/2,0),16*level[0].length-h));
            }
            else if (gamemode == MODE_HORIZ_AUTOSCROLL) { //scrolltype == SCROLL_MAINSPIKE_HORIZONTAL
                Xtrans = (int)(-Math.min(Math.max(mainSpike.getXpos(),0),16*level.length-w));
                Ytrans = 0;
                if (!autoscroll_ended && Xtrans==-(16*level.length-w)) autoscroll_ended = true;
            }
            else if (gamemode == MODE_VERTICAL_AUTOSCROLL) { //scrolltype == SCROLL_MAINSPIKE_VERTICAL
                Xtrans = 0;
                Ytrans = (int)(-Math.max(Math.min(mainSpike.getYpos()-h+16,16*level[0].length-h),0));
                if (!autoscroll_ended && Ytrans==(0)) autoscroll_ended = true;
            }
        }
        else Xtrans = Ytrans = 0;
        
        // pause the game
        if (inputhandler.getTyped(KeyEvent.VK_ESCAPE) && state == STATE_INGAME) {
            state = STATE_PAUSED;
            if (levelindex<10) menuSelector.setState(MenuSelector.Menu.PAUSE);
            else menuSelector.setState(MenuSelector.Menu.PAUSE_LEVEL);
            Sound.blip2.play();
        }
    }
    
    public void render(Graphics2D g) {
        g.translate(Xtrans,Ytrans);
        // draw level's tiles
        int xmin = -Xtrans/16-1;
        int xmax = xmin+14;
        if (state==STATE_INGAME || state==STATE_PAUSED)
        for (int n=0;n<level.length;n++) {
            for (int m=0;m<level[0].length;m++) {
                if (n<xmin || n>xmax)  continue;
                if (!level[n][m].isVisible() || !level[n][m].isSolid()) g.drawImage(Art.tileset.get("bg"),n*16,m*16,null);
                if (level[n][m].isVisible())
                g.drawImage(level[n][m].getSprite(),n*16,m*16,null);
            }
        }
        // draw entities
        for (int n=0;n<entities.size();n++) {
            Entity e = entities.get(n);
            if (e.getXpos()<-Xtrans-e.getWidth() || e.getXpos()>-Xtrans+w) continue;
            if (e.spriteXFlip==1 && e.spriteYFlip==1) g.drawImage(e.getSprite(),e.getXpos()+e.getXoffset(),e.getYpos()+e.getYoffset(),null);
            else {
                int x = e.getXpos()+e.getXoffset();
                int y = e.getYpos()+e.getYoffset();
                if (e.spriteXFlip()==-1) x+=e.getWidth();
                if (e.spriteYFlip()==-1) y+=e.getHeight();
                g.drawImage(e.getSprite(),x,y,e.getSprite().getWidth()*e.spriteXFlip(),e.getSprite().getHeight()*e.spriteYFlip(),null);
            }
        }
        
        // draw front entities
        if (entities.contains(player)) {
            if (player.spriteXFlip==1 && player.spriteYFlip==1) g.drawImage(player.getSprite(),player.getXpos()+player.getXoffset(),player.getYpos()+player.getYoffset(),null);
            else {
                int x = player.getXpos()+player.getXoffset();
                int y = player.getYpos()+player.getYoffset();
                if (player.spriteXFlip()==-1) x+=player.getWidth();
                if (player.spriteYFlip()==-1) y+=player.getHeight();
                g.drawImage(player.getSprite(),x,y,player.getWidth()*player.spriteXFlip(),player.getHeight()*player.spriteYFlip(),null);
            }
            for (int n=0;n<entities.size();n++) {
                if (entities.get(n) instanceof HomingTarget) {
                    Entity e = entities.get(n);
                    g.drawImage(e.getSprite(),e.getXpos(),e.getYpos(),null);
                }
            }
        }
        
        g.translate(-Xtrans,-Ytrans);
        
        // draw hud special stuff
        if (state==STATE_INGAME || state==STATE_PAUSED) {
            if (gamemode==MODE_RUN || gamemode==MODE_CLIMB) g.drawImage(Art.scaleImage(Art.getString(Integer.toString(score)),2),0,0,null);
            if (player.getReserveItem()!=null) g.drawImage(Art.entities.get("item box"),w/2-8,4,null);
            if (player.getReserveItem()!=null) g.drawImage(player.getReserveItem().getSprite(),w/2-6,6,null);
            if (levelindex<10) g.drawImage(Art.scaleImage(Art.getString("^:"+SaveData.num_diodes),1),8,8,null);
        }
        // darken if paused
        if (state==STATE_PAUSED) {
            g.setColor(Color.BLACK);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.7));
            g.fillRect(0, 0, GameCanvas.W, GameCanvas.H);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
        // draw hud
        for (int n=0;n<HUDentities.size();n++) {
            Entity e = HUDentities.get(n);
            g.drawImage(e.getSprite(),e.getXpos()+e.getXoffset(),e.getYpos()+e.getYoffset(),null);
        }
        
        // white fade effect
        if (whitefade_effect) {
            g.setColor(Color.WHITE);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, whitefade_alpha));
            g.fillRect(0,0,GameCanvas.W, GameCanvas.H);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
        
    }
    
    public boolean playerDig(int x, int y) {
        boolean success = false;
        if (isTileFree(x,y)) {
            if (level[x][y].playerDig(player.strongshovel)) {
                entities.add(new AnimEntity(16*x,16*y,AnimEntity.Type.FLASH,this));
                success = true;
            }
            if (level[x][y].getSprite()!=null)
            for (int n=0;n<4;n++) {
                entities.add(new TileParticle(level[x][y].getSprite(),this,x,y));
            }
            //if (!success) Sound.break2.play();
        }
        return success;
    }
    
    private boolean isTileFree(int xcheck, int ycheck) {
        boolean free = true;
        for (int n=0;n<entities.size();n++) {
            Entity e = entities.get(n);
            if (e.isBlockProtected()) {
                int x = (int)(e.getXpos()/16);
                int y = (int)(e.getYpos()/16);
                
                if (x==xcheck && y==ycheck) free = false;
                if (e.getXpos() != x*16) {
                if (x+1==xcheck && y==ycheck) free = false;
                if (e.getYpos() != y*16 && x+1==xcheck && y+1==ycheck) free = false;
                }
                if (e.getYpos() != y*16 && x==xcheck && y+1==ycheck) free = false;
            }
        }
        return free;
    }
    
    public void startRespawnCount() {
        death_respawning = true;
    }
    // smoothly slide-scroll to a point.
    public void setScroll(int xpos, int ypos, int time) {
        scroll_respawning = true;
        scrollrespawncount = 0;
        int startx,starty,targetx,targety;
        startx = Math.max(Math.min(player.getXcenter(),16*level.length-w/2),w/2);
        starty = Math.max(Math.min(player.getYcenter(),16*level[0].length-h/2),h/2);;
        targetx = Math.max(Math.min(xpos,16*level.length-w/2),w/2);
        targety = Math.max(Math.min(ypos,16*level[0].length-h/2),h/2);
        scrollTemp = new ScrollTemp(startx,starty,targetx,targety);
        entities.add(scrollTemp);
    }
    
    public void playSoundBreak() {
        if (!playsoundbreak) playsoundbreak = true;
    }
    
    public void whiteFadeEffect() {
        whitefade_effect = true;
        whitefade_count = 0;
        whitefade_alpha = (float)1.0;
    }
    
    public void playSound(Sound sound) {
        
    }
    
    private void resetEffects() {
        timefreeze = false;
    }
    // player accesses these to know when to stop at the border. TEMP
    public int getXmin() {
        return Xtrans;
    }
    public int getXmax() {
        return -Xtrans+w-16;
    }
    public int randInt(int min, int max) {
        return (int)(Math.random()*(max-min+1))+min;
    }
    public int probInt(int[] type, double[] prob) {
        for (int n=1;n<prob.length;n++) {
            prob[n] += prob[n-1];
        }
        double r = Math.random();
        for (int n=0;n<prob.length;n++) {
            if (r<prob[n]) return type[n];
        }
        return -1;
    }
    public int chooseInt(int x, int y) {
        if (randInt(0,1)==1) return x;
        else return y;
    }
    public void addScore(int amount) {
        score+=amount;
    }
    public int getXtrans() {
        return Xtrans;
    }
            
    
}
