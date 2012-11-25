package spade;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.awt.Rectangle;

/** User-controlled character in the game.
 * 
 * @author Patrick
 */
public class Player extends Entity {
    
    // 0.2 and 3.675
    // 0.15 and 3.17
    // 0.15 and 3.9
    
    double yaccel = 0.15;
    final double YA_NORMAL = 0.15;
    final double YA_SLOW = 0.01;
    double jumpSpeed = 3.17;
    double runSpeed = 0.9;
    int xsnap = 10; // number of pixels away you can be to snap into an opening above when jumping
    int digcharge = 0;
    int digtime = 20;
    boolean shovel;
    int itemcount = 0;
    int itemtime = 0;
    boolean strongshovel = false;
    boolean alive = true;
    boolean passedScrollPoint = false;
    int maxX = 0;
    int minY = 0;
    
    boolean respawning = false;
    double targetx; // where to catch the player when respawning
    int respawncount;
    
    boolean walking = false; // used for walking animation
    int walkcount = 0;
    
    HashMap<String,Selector> selectors = new HashMap<>(4);
    RegenBar regenBar;
    RegenBar powerBar;
    Item reserveItem = null;
    Checkpoint checkpoint;
    
    public Player(int xpos, int ypos, boolean shovel, Game game) {
        super(xpos,ypos,game);
        meltsTime = true;
        maxX = (int)(xpos/16);
        minY = (int)(ypos/16)-1;
        sprite = Art.player.get("idle");
        selectors.put("top",new Selector(Selector.TOP,this));
        selectors.put("bottom",new Selector(Selector.BOTTOM,this));
        selectors.put("left",new Selector(Selector.LEFT,this));
        selectors.put("right",new Selector(Selector.RIGHT,this));
        game.entities.add(selectors.get("top"));
        game.entities.add(selectors.get("bottom"));
        game.entities.add(selectors.get("left"));
        game.entities.add(selectors.get("right"));
        regenBar = new RegenBar(RegenBar.BELOW,game);
        powerBar = new RegenBar(RegenBar.ABOVE,game);
        this.shovel = shovel;
    }
    
    public void tick() {
        boolean jump = game.inputhandler.getTyped(KeyEvent.VK_W);
        boolean left = game.inputhandler.keys[KeyEvent.VK_A];
        boolean right = game.inputhandler.keys[KeyEvent.VK_D];
        boolean digup = game.inputhandler.getTyped(KeyEvent.VK_UP);
        boolean digdown = game.inputhandler.getTyped(KeyEvent.VK_DOWN);
        boolean digleft = game.inputhandler.getTyped(KeyEvent.VK_LEFT);
        boolean digright = game.inputhandler.getTyped(KeyEvent.VK_RIGHT);
        boolean use = game.inputhandler.getTyped(KeyEvent.VK_SPACE);
        boolean enterdoor = game.inputhandler.getTyped(KeyEvent.VK_S);
        
        // use item
        if (use && reserveItem!=null) {
            resetItems();
            reserveItem.activate();
            powerBar.activate(reserveItem.getRegenType(),reserveItem.getTimeout());
            itemtime = reserveItem.getTimeout();
            itemcount = -1;
            reserveItem = null;
        }
        // item timeout
        if (itemcount<itemtime) itemcount++;
        else if (itemcount==itemtime) {
            resetItems();
            itemtime = -1;
        }
        
        // dig
        boolean dug = false;
        String dir = "";
        if (digup) dir = "top";
        else if (digdown) dir = "bottom";
        else if (digleft) dir = "left";
        else if (digright) dir = "right";
        // later if necessary player dig can return status of digging.
        if (!dir.equals("") && digcharge == 0 && shovel) dug = game.playerDig(selectors.get(dir).getX(),selectors.get(dir).getY());
        if (dug) {
            digcharge = digtime;
            regenBar.activate(RegenBar.DIG,digtime);
        }
        else if (digcharge>0) digcharge--;
        
        // set x and y speed
        if (right && !left) xspeed = runSpeed;
        else if (left && !right) xspeed = -runSpeed;
        else xspeed = 0;
        int x,y,xa,xb;
        x = (int)(xpos/16);
        y = (int)(ypos/16);
        int xdir = (int)(xspeed/Math.abs(xspeed));
        int ydir = (int)(yspeed/Math.abs(yspeed));
        if (jump && ypos == y*16) {
            if (xpos != x*16 && (game.level[x][y+1].isSolid() || game.level[x+1][y+1].isSolid())) {
                yspeed = -jumpSpeed;
                if (!game.level[x][y-1].isSolid() && !game.level[x+1][y-1].isSolid()) {
                    Sound.jump.play();
                    SaveData.playerJump();
                }
                // shift right
                if (game.level[x+1][y+1].isSolid() && game.level[x][y-1].isSolid() && !game.level[x+1][y-1].isSolid() && x != (int)((xpos+xsnap)/16)) {
                    xpos = (int)((xpos+xsnap)/16)*16;
                    xabs = xpos + xdir/2;
                    xspeed = 0;
                }
                // shift left
                if (game.level[x][y+1].isSolid() && game.level[x+1][y-1].isSolid() && !game.level[x][y-1].isSolid() && x != (int)((xpos-xsnap)/16)) {
                    xpos = (int)(xpos/16)*16;
                    xabs = xpos + xdir/2;
                    xspeed = 0;
                }
            }
            x = (int)(xpos/16);
            if (xpos == x*16 && game.level[x][y+1].isSolid()) {
                yspeed = -jumpSpeed;
                if (!game.level[x][y-1].isSolid()) {
                    Sound.jump.play();
                    SaveData.playerJump();
                }
            }
        }
        if (yspeed>-0.1 && yspeed<0 && yaccel!=YA_SLOW) yaccel = YA_SLOW;
        else if (yaccel==YA_SLOW && (yspeed>0 || yspeed<-0.1)) yaccel = YA_NORMAL;
        if (yspeed == 0) yspeed = 0.5;
        else yspeed += yaccel;
        
        boolean falling = (yspeed>2);
        boolean up = (yspeed<-2);
        // movement algorithm
        move();
        if (falling && yspeed==0) Sound.land.play();
        //else if (up && yspeed==0) Sound.land.play();
        
        if (!passedScrollPoint && game.gamemode==Game.MODE_HORIZ_AUTOSCROLL && xpos>16*7) passedScrollPoint = true;
        if (!passedScrollPoint && game.gamemode==Game.MODE_VERTICAL_AUTOSCROLL && ypos<(game.level[0].length-4)*16) passedScrollPoint = true;
        
        // score by position (+10 for every tile)
        if (game.gamemode==game.MODE_RUN && (int)(xpos/16)>maxX) {
            maxX++;
            game.addScore(10);
            game.entities.add(new TextEntity(xpos,ypos-8,"10",TextEntity.POINTS,this.game));
        }
        else if (game.gamemode==game.MODE_CLIMB && (int)((ypos-0.07)/16)<minY) {
            minY--;
            game.addScore(10);
            game.entities.add(new TextEntity(xpos,ypos-8,"10",TextEntity.POINTS,this.game));
        }
        
        
        // check for various entity collisions
        for (int n=0;n<game.entities.size();n++) {
            Entity e = game.entities.get(n);
            Rectangle me = new Rectangle((int)xpos,(int)ypos,width,width);
            Rectangle him = new Rectangle(e.getXpos(),e.getYpos(),e.getWidth(),e.getHeight());
            // playerkillers
            if (e.isHarmful() && me.intersects(him)) {
                die();
            }
            // pick up items
            else if (e instanceof Item && me.intersects(him)) {
                if (reserveItem!=null) {
                    game.addScore(50);
                    game.entities.add(new TextEntity(xpos,ypos-8,"50",TextEntity.POINTS,game));
                }
                ((Item)e).pickup();
            }
            // activate/deactivate checkpoints
            else if (e instanceof Checkpoint && checkpoint != e && me.intersects(him)) {
                checkpoint.setActive(false);
                checkpoint = (Checkpoint)e;
                checkpoint.setActive(true);
                Sound.checkpoint.play();
            }
            // enter doors
            else if (e instanceof Door && ((Door)e).isOpen() && me.intersects(him) && enterdoor) {
                xabs = xpos = e.getXpos();
                yabs = ypos = e.getYpos();
                ((Door)e).startTimer();
                game.entities.remove(this);
                game.entities.add(new AnimEntity(e.getXpos(),e.getYpos(),AnimEntity.Type.PLAYERENTER,game));
                Sound.steps.play();
            }
            // temp memory block kill
            //else if (e instanceof MemoryBlock && ((MemoryBlock)e).isOn() && me.intersects(him)) die();
        }
        
        // check for harmful tile collisions
        x = (int)(xpos/16);
        y = (int)(ypos/16);
        if (ypos != (int)(ypos/16)*16 && (game.level[x][y+1].isHarmful())) die();
        else if (game.level[x][y].isHarmful()) die();
        else if (xpos != (int)(xpos/16)*16) {
            if (game.level[x+1][y].isHarmful()) die();
            else if (ypos != (int)(ypos/16)*16 && ((game.level[x][y+1].isHarmful()) || (game.level[x+1][y+1].isHarmful()))) die();
        }
        
        // tick entities that should tick after the player
        regenBar.setPosition();
        powerBar.setPosition();
        
        // animate
        x = (int)(xpos/16);
        y = (int)(ypos/16);
        boolean xall = (xpos==x*16); // x alligned
        boolean yall = (ypos==y*16); // y alligned
        boolean standing = (xall && game.level[x][y+1].isSolid())  ||  (!xall && (game.level[x][y+1].isSolid() || game.level[x+1][y+1].isSolid()));
        String ref = "null";
        // idle
        if (standing && xspeed==0) {
            ref = "idle";
        }
        // jumping/falling
        else if (!standing) {
            if (yspeed<0) ref = "jump";
            else ref = "fall";
        }
        
        // walking
        if (walking && !(standing && xspeed!=0)) walking = false;
        else if (!walking && (standing && xspeed!=0)) {
            walking = true;
            walkcount = 0;
        }
        if (walking && (xpos==x*16 && ypos==y*16 && (game.level[x+1][y].isSolid()|| game.level[x-1][y].isSolid()))) {
            // walking into a wall
            walking = false;
            ref = "idle";
        }
        if (walking) {
            if (walkcount<8) ref = "walk1";
            else if (walkcount<=15) ref = "walk2";
            if (walkcount==15) walkcount = 0;
            walkcount++;
        }
        
        if (shovel) sprite = Art.player.get(ref);
        else sprite = Art.player2.get(ref);
        if (respawning) sprite = null;
        // flip sprite if walking
        if (xspeed>0) spriteXFlip = 1;
        if (xspeed<0) spriteXFlip = -1;
        
    }
    
    public void memoryBlockShove(MemoryBlock mb) {
        if (getRect().intersects(mb.getRect())) die();
    }
    
    private void die() {
        Sound.die.play();
        game.entities.add(new AnimEntity(xpos,ypos,AnimEntity.Type.BLOODSPLAT,game));
        digcharge = 0;
        regenBar.die();
        powerBar.die();
        alive = false;
        game.entities.remove(this);
        xspeed = yspeed = 0;
        if (game.gamemode==Game.MODE_NORMAL) {
            game.setScroll(checkpoint.getXcenter(),checkpoint.getYcenter(),100);
            checkpoint.startTimer();
            xabs = checkpoint.getXpos();
            yabs = checkpoint.getYpos();
            xpos = (int)xabs;
            ypos = (int)yabs;
        }
        else if (game.gamemode==Game.MODE_HORIZ_AUTOSCROLL || game.gamemode==Game.MODE_VERTICAL_AUTOSCROLL) {
            game.startRespawnCount();
        }
        SaveData.playerDeath();
    }
    
    public void revive() {
        game.entities.add(this);
        regenBar.revive();
        powerBar.revive();
        if (SaveData.displaynumberonspawn) {
            String text = Integer.toString(SaveData.getDeaths());
            while (text.length()<5) {
                text = "0"+text;
            }
            game.entities.add(new TextEntity(-100,-100,"#"+text,TextEntity.FOLLOW_PLAYER,game));
            SaveData.displaynumberonspawn = false;
        }
    }
    
    private void resetItems() {
        digtime = 20;
        runSpeed = 0.9;
        jumpSpeed = 3.17;
        strongshovel = false;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public int getXcenter() {
        return xpos+8;
    }
    
    public int getYcenter() {
        return ypos + 8;
    }
    public boolean hasPassedScrollPoint() {
        return passedScrollPoint;
    }
    public Item getReserveItem() {
        return reserveItem;
    }
    public void setReserveItem(Item i) {
        reserveItem = i;
    }
    public void setCheckpoint(Checkpoint c) {
        checkpoint = c;
    }
    
    
}
