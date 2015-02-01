/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;

/**
 * The abstract base for your game.  
 * Automatically implements a runtime loop as well as some graphical operations
 * @author Peter Cowal
 */
public abstract class GameLoop extends JPanel implements MouseListener,KeyListener,MouseWheelListener{
    
    /**
     * An array containing booleans representing which keys are down.
     * True=pressed, False=released.
     * To check whether or not a key is pressed, use the keycode as an index
     * For example:
     *  if(keysPressed[KeyEvent.VK_UP]||keysPressed[(int)'W']{
     *  moveUp()
     * }
     */
    public boolean[] keysPressed;
    BufferedImage screenBuffer;
    /**
     * An integer array containing all the color-data for the display.
     * It's probably a better idea to modify this indirectly (with setPixel() or similar)
     */
    public int[] screen;
    
    public int mouseWheel;
    
    /**
     * The in-game x-coordinate of the cursor
     */
    public int mouseX;
    
    /**
     * The in-game y-coordinate of the cursor
     */
    public int mouseY;
    /**
     * An array containing booleans representing which keys are down.
     * True=pressed, False=released.
     * To check whether or not a key is pressed, use the buttoncode as an index
     */
    public boolean[] mbPressed;
    /**
     * The width of the screen.
     */
    public int width;
    /**
     * The height of the screen.
     */
    public int height;
    /**
     * The current blending mode for drawSprite and drawSpriteStretched.
     */
    public int blendMode;

    /**
     * The integer representation (ARGB) for the background color.
     * Set alpha to less than 255 for a fading effect.
     */
    public int backgroundColor;
    int color,newcolor;
    
    int frameX,frameY;
    /**
     * The current frames per second the game is running at.
     */
    public int fps;
    /**
     * An integer multiplier for the size of the screen.
     */
    public int scale;
    PointerInfo mouse;
    Robot mouseController;
    /**
     * The MIDIPlayer for playing background music and the like.
     */
    public MIDIPlayer midiPlayer;
    Sprite cursorSprite;
    /**
     * (color/a) returns the alpha value of the color.
     */
    public static final int a=256*256*256;
    /**
     * (color/r)%256 returns the red channel of the color.
     */
    public static final int r=256*256;
    /**
     * (color/g)%256 returns the green channel of the color.
     */
    public static final int g=256;
    /**
     * Creates a GameLoop with the desired dimensions
     * @param width The width of your game (when it isn't resized)
     * @param height The height of your game (when it isn't resized)
     */
    public GameLoop(int width, int height){
        super();
        addKeyListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        this.setSize(width,height);
        keysPressed=new boolean[1000];
        mbPressed=new boolean[4];
        screenBuffer=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        scale=1;
        this.width=width;
        this.height=height;
        //this.add(new JLabel(new ImageIcon(screenBuffer)));
        backgroundColor=a*255+r*255+g*255+255;
        screen=((DataBufferInt) screenBuffer.getRaster().getDataBuffer()).getData();
        //System.out.println(screen.length);
        mouse=MouseInfo.getPointerInfo();
        mouseX=(int)(mouse.getLocation().getX()-8)/scale;
        mouseY=(int)(mouse.getLocation().getY()-30)/scale;
        try {
            //screen2=new int[width*height*3];
            mouseController=new Robot();
        } catch (AWTException ex) {
            //Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
        cursorSprite=null;
        blendMode=BM_NORMAL;
        midiPlayer=new MIDIPlayer();
    }
    /**
     * Put anything you want to happen at the beginning of the game here.
     */
    public abstract void Initialize();

    void update(int fx, int fy, int fps){
        frameX=fx;
        frameY=fy;
        this.fps=fps;

        
        
        Update();
        
    }
    public abstract void Update();
    void render(){
        blendMode=BM_NORMAL;
        //screen=((DataBufferInt) screenBuffer.getRaster().getDataBuffer()).getData();
        /*for(int i=0; i<screen.length; i++){
            screen[i]=bg[i%3];        
        }
        */
        //System.out.println(backgroundColor);
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                
                setPixel(i,j,backgroundColor);
            }
        }
        Render();
        if(cursorSprite!=null){
            drawSprite(cursorSprite,0,mouseX,mouseY);
        }
        //System.out.println(screen[10000]);
        //screenImage=createImage(new MemoryImageSource(width, height, screen, 0, width));
        
        
        
    }
    public abstract void Render();
    
    
    //handle all the key events

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode()<1000);
        keysPressed[ke.getKeyCode()]=true;
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode()<1000);
        keysPressed[ke.getKeyCode()]=false;
    }
    
    //handle all the mouse events
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getButton()<4)
        mbPressed[e.getButton()]=true;
    }
    @Override
    public void mouseReleased(MouseEvent e){
        if(e.getButton()<4)
        mbPressed[e.getButton()]=false;
    }
    
    
    //not using these mouse events
    @Override
    public void mouseEntered(MouseEvent e){
        //do nothing (for now)
    }
    @Override
    public void mouseExited(MouseEvent e){
        //do nothing (for now)
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(e.getWheelRotation()<0){
            this.mouseWheel--;
            mouseWheelRotated(-1);
        }
        if(e.getWheelRotation()>0){
            this.mouseWheel++;
            mouseWheelRotated(1);
        }
    }
    public abstract void mouseWheelRotated(int dir);
    /**
     * Moves the cursor to the given position in in-game coordinates
     * @param x the in-game x-coordinate
     * @param y the in-game y-coordinate
     */
    public void moveMouse(int x, int y){
        mouseController.mouseMove(frameX+x*scale+scale/2, frameY+y*scale+scale/2);
    }
    /**
     * Sets the cursor to the given sprite.  Can be used to hide the cursor entirely.
     * @param newCursor The sprite for the cursor. If set to null, the cursor will be hidden
     */
    public void setCursorSprite(Sprite newCursor){
        cursorSprite=newCursor;
        
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        
        Cursor blank=Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank cursor");
        this.setCursor(blank);
    }
     /**
     * Sets the pixel at (x,y) to the given color (in ARGB integer format) multiplied by the filter values
     * Depending on blendMode, it will act accordingly.
     * Supports alpha transparency, unless blendMode==BM_MULTIPLY.
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param color an integer (ARGB) representation of the desired color
     * @param aFilter the filter value for alpha
     * @param rFilter the filter value for red
     * @param gFilter the filter value for green
     * @param bFilter the filter value for blue
     */
    public void setPixelFiltered(int x, int y, int color, int aFilter,int rFilter, int gFilter, int bFilter){
        if(x>=0&&x<width&&y>=0&&y<height){
            int alpha=(color>>24)&0xff;
            alpha=(alpha*aFilter)/255;
            if (true) {
                
                if (alpha == 255) {
                    newcolor=getPixel(x,y);
                    int red=((rFilter)*((color>>16)&0xff))>>8;
                    int green=((gFilter)*((color>>8)&0xff))>>8;
                    int blue=((bFilter)*(color&0xff))>>8;
                    screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;
                } else if (alpha > 0) {

                    newcolor = getPixel(x, y);
                    int red = (((newcolor >> 16) & 0xff) * (~alpha & 0xff) + ((color >> 16) & 0xff) * alpha)>>8;
                    
                    int green = (((newcolor >> 8) & 0xff) * (~alpha & 0xff) + ((color >> 8) & 0xff) * alpha)>>8;
                    int blue = ((newcolor&0xff) * (~alpha&0xff)+ (color&0xff) * alpha)>>8;
                    red=((red*rFilter)>>8);
                    green=((green*gFilter)>>8);
                    blue=((blue*bFilter)>>8);
                    screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;
                }
            }
            else if (blendMode == BM_ADD){
                addPixel(x,y,color);
            }
            else if (blendMode == BM_MULTIPLY){
                multiplyPixel(x,y,color);
            }
            else if (blendMode == BM_DIFFERENCE){
                differencePixel(x,y,color);
            }
        }
    }
    public void setPixelDirect(int index,int color){
        if(0<=index && index<screen.length){
            screen[index]=color;
        }
    }
    /**
     * Sets the pixel at (x,y) to the given color (in ARGB integer format).
     * Depending on blendMode, it will act accordingly.
     * Supports alpha transparency, unless blendMode==BM_MULTIPLY.
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param color an integer (ARGB) representation of the desired color
     */
    public void setPixel(int x, int y, int color){
        
        if(x>=0&&x<width&&y>=0&&y<height){
            int alpha=(color>>24)&0xff;
            if (blendMode == BM_NORMAL) {
                
                if (alpha == 255) {
                    screen[y * width + x] = color;
                } else if (alpha > 0) {

                    newcolor = getPixel(x, y);
                    int red = (((newcolor >> 16) & 0xff) * (~alpha & 0xff) + ((color >> 16) & 0xff) * alpha)>>8;
                    
                    int green = (((newcolor >> 8) & 0xff) * (~alpha & 0xff) + ((color >> 8) & 0xff) * alpha)>>8;
                    int blue = ((newcolor&0xff) * (~alpha&0xff)+ (color&0xff) * alpha)>>8;
                    screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;
                }
            }
            else if (blendMode == BM_ADD){
                addPixel(x,y,color);
            }
            else if (blendMode == BM_MULTIPLY){
                multiplyPixel(x,y,color);
            }
            else if (blendMode == BM_DIFFERENCE){
                differencePixel(x,y,color);
            }
        }
    }
    public int blendColor(int color1, int color2, int amount) {

        if (amount == 255) {
            return color2;
        } else if (amount > 0) {
            int red = (((color1 >> 16) & 0xff) * (~amount & 0xff) + ((color2 >> 16) & 0xff) * amount) >> 8;

            int green = (((color1 >> 8) & 0xff) * (~amount & 0xff) + ((color2 >> 8) & 0xff) * amount) >> 8;
            int blue = ((color1 & 0xff) * (~amount & 0xff) + (color2 & 0xff) * amount) >> 8;
            return (red << 16) + (green << 8) + blue + 0xff000000;
        } else {
            return color1;
        }
    }
    public int getPixel(int index){
        if(0<=index && index<screen.length){
            return screen[index];
        }
        return 0;
    }
    /**
     * Returns an integer representing the color of the screen at (x,y).
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return integer (ARGB) representing the color of the screen at (x,y)
     */
    public int getPixel(int x, int y){
        if(x>=0&&x<width&&y>=0&&y<height){
            if(screen[y*width+x]>0){
                return screen[y*width+x];
            }
            else{
                return screen[y*width+x]+a;
            }
            
        }
        else{
            return 0;
        }
    }
    /**
     * Adds the RGB values of the given color to the pixel at (x,y).
     * Supports alpha transparency
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param color an integer (ARGB) representation of the color to add
     */
    public void addPixel(int x, int y, int color){
        if(x>=0&&x<width&&y>=0&&y<height){
            int alpha=(color>>24)&0xff;
            newcolor=getPixel(x,y);
            int red=Math.min(((newcolor>>16)&0xff)+((((color>>16)&0xff)*alpha)>>8),255);
            int green=Math.min(((newcolor>>8)&0xff)+((((color>>8)&0xff)*alpha)>>8),255);
            int blue=Math.min((newcolor&0xff)+(((color&0xff)*alpha)>>8),255);
            screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;
            
        }
    }
    /**
     * Multiplies the RGB values of the pixel at (x,y) by the given color (ARGB).
     * Ignores alpha transparency
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param color an integer (ARGB) representation of the color to multiply by
     */
    public void multiplyPixel(int x, int y, int color){
        if(x>=0&&x<width&&y>=0&&y<height){
            newcolor=getPixel(x,y);
            int red=(((newcolor>>16)&0xff)*((color>>16)&0xff))>>8;
            int green=(((newcolor>>8)&0xff)*((color>>8)&0xff))>>8;
            int blue=((newcolor&0xff)*(color&0xff))>>8;
            screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;

        }
    }
    /**
     * Subtracts the RGB values of the given color from the pixel at (x,y).
     * Can be used to create a "negative" effect
     * Supports alpha transparency
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param color an integer (ARGB) representation of the color to subtract
     */
    public void differencePixel(int x, int y, int color){
        if(x>=0&&x<width&&y>=0&&y<height){
            newcolor=getPixel(x,y);
            int alpha=(color>>24)&0xff;
            int red=Math.abs(((newcolor>>16)&0xff)-((((color>>16)&0xff)*alpha)>>8));
            int green=Math.abs(((newcolor>>8)&0xff)-((((color>>8)&0xff)*alpha)>>8));
            int blue=Math.abs((newcolor&0xff)-(((color&0xff)*alpha)>>8));
            
            screen[y * width + x] =0x01000000+(red<<16)+(green<<8)+blue;
        }
    }
    /**
     * Draws the (frame)th frame of the given sprite at (x,y).
     * @param sprite The sprite to draw
     * @param frame The frame of the sprite to draw
     * @param x The x-coordinate of the top left corner of the sprite
     * @param y The y-coordinate of the top left corner of the sprite
     */
    public void drawSprite(Sprite sprite, int frame, int x, int y) {

        for (int i = 0; i < sprite.width; i++) {
            for (int j = 0; j < sprite.height; j++) {
                setPixel(x + i, y + j, sprite.getPixel(frame, i, j));
            }
        }


    }
        /**
     * Draws the (frame)th frame of the given sprite at (x,y).
     * @param sprite The sprite to draw
     * @param frame The frame of the sprite to draw
     * @param x The x-coordinate of the top left corner of the sprite
     * @param y The y-coordinate of the top left corner of the sprite
     */
    public void drawSpriteFiltered(Sprite sprite, int frame, int x, int y, int a, int r, int g, int b) {

        for (int i = 0; i < sprite.width; i++) {
            for (int j = 0; j < sprite.height; j++) {
                setPixelFiltered(x + i, y + j, sprite.getPixel(frame, i, j),a,r,g,b);
            }
        }


    }
    /**
     * Draws the given frame of the given sprite stretched from (x,y) to (x2,y2).
     * @param sprite The sprite to draw
     * @param frame The frame of the sprite to draw
     * @param x The x-coordinate of the top left corner of the sprite
     * @param y The y-coordinate of the top left corner of the sprite
     * @param x2 The x-coordinate of the bottom right corner of the sprite
     * @param y2 The y-coordinate of the bottom right corner of the sprite
     */
    public void drawSpriteScaled(Sprite sprite, int frame, int x, int y, int x2, int y2){
        int w = x2 - x;
        int h = y2 - y;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                setPixel(x + i, y + j, sprite.getPixel(frame, i * sprite.width / w, j * sprite.height / h));
            }
        }

    }
    
    /**
     * Draws a rectangle from (x1,y1) to (x2,y2) of a certain color.
     * @param x1 The x-coordinate of the top left corner of the rectangle
     * @param y1 The y-coordinate of the top left corner of the rectangle
     * @param x2 The x-coordinate of the bottom right corner of the rectangle
     * @param y2 The y-coordinate of the bottom right corner of the rectangle
     * @param color The color of the rectangle in integer (ARGB) format
     * @param outline If true, only an outline of the rectangle will be drawn.  Otherwise, it will be filled.
     */
    public void drawRectangle(int x1, int y1, int x2, int y2, int color, boolean outline){
        //clip the rectangle to the screen
        int xx1=x1;
        if(x1<0)xx1=-1;
        else if(x1>width)xx1=width+1;
        int xx2=x2;
        if(x2<0)xx2=-1;
        else if(x2>width)xx2=width+1;
        int yy1=y1;
        if(y1<0)yy1=-1;
        else if(y1>height)yy1=height+1;
        int yy2=y2;
        if(y2<0)yy2=-1;
        else if(y2>height)yy2=height+1;
        if(outline){
            //draw an outline of the rectangle
            for(int i=xx1; i<xx2; i++){
                setPixel(i,yy1,color);
                setPixel(i,yy2-1,color);
            }
            for(int i=yy1; i<yy2; i++){
                setPixel(xx1,i,color);
                setPixel(xx2-1,i,color);
            }
        }
        else{
            for(int i=xx1; i<xx2; i++){
                for(int j=yy1; j<yy2; j++){
                    setPixel(i,j,color);
                }
            }
        }
    }
    /**
     * Draws the string in the given font at (x1,y1).
     * @param text The string to draw
     * @param font The sprite containing the font. Each character should be
     * contained within one frame of the sprite, so fixed-width fonts are
     * required.  The first frame of the sprite should contain the "!" character,
     * and it should go all the way through "~" (in ASCII order).
     * @param x1 The x-coordinate of the top left corner of the string
     * @param y1 The y-coordinate of the top left corner of the string
     */
    public void drawText(String text, Sprite font, int x1, int y1){
        int x=x1;
        for(int i=0; i<text.length(); i++){
            if((int)text.charAt(i)>32){
                drawSprite(font,(int)text.charAt(i)-33,x,y1);
            }
            x+=font.width;
        }
    }
    /**
     * Useful for most, if not all, of your drawing operations.
     */
    public static final int BM_NORMAL=0;
    /**
     * Use this for glowing or flamey effects if you want.
     */
    public static final int BM_ADD=1;
    /**
     * Useful for adding a gradient overlay or making everything red or something.
     */
    public static final int BM_MULTIPLY=2;
    /**
     * Useful for burn effects or negative effects, maybe.
     */
    public static final int BM_DIFFERENCE=3;
}
