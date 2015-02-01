/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class creates a window containing a GameLoop
 * How To Use: new GameFrame().Run(GameLoop yourGame, String caption, int fps)
 * 
 * @author Peter Cowal
 */
public class GameFrame implements Runnable{
    int fps,targetFPS;
    int width,height;
    boolean fsDialogue,fullscreen;
    String windowName;
    GameLoop gl;
    public GameFrame(GameLoop gl,String windowName,int fps){
        this.gl=gl;
        this.windowName=windowName;
        this.targetFPS=fps;
        fsDialogue=true;
        width=height=-1;
    }
    public void setFullScreen(boolean fs){
        fsDialogue=false;
        fullscreen=fs;
    }
    public void setSize(int width, int height){
        this.width=width;
        this.height=height;
    }
    /**
     * Initializes the window and your game
     * @param gl an instance of your GameLoop
     * @param windowName the caption of the window
     * @param fps the target FPS
     */
    public void Run(){
        this.run();
    }
    /**
     * USE THE OTHER RUN METHOD. THIS WILL NOT WORK.
     */
    @Override
    public void run() {
        if(fsDialogue){
            fullscreen=false;
            Object[] windowOptions = { "Windowed","Fullscreen"};
            int i=(int)JOptionPane.showOptionDialog(null, "Choose a display mode:", windowName,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, windowOptions, windowOptions[0]);
            if(i==1){
                fullscreen=true;
            }
        }
        JFrame frame = new JFrame(windowName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gl.setBackground(Color.BLACK);
        if(fullscreen){
            frame.setUndecorated(true);
            frame.setResizable(false);
            Toolkit tk=Toolkit.getDefaultToolkit();
            frame.setSize(tk.getScreenSize());
        }
        else{
            if(width<0){
            frame.setSize(gl.getWidth()+16,gl.getHeight()+38);
            }
            else{
                frame.setSize(width+16,height+38);
            }
            
        }
        
        
        frame.setContentPane(gl);
        gl.setFocusable(true);
        frame.setVisible(true);
        //frame.setResizable(false);
        long targetTime;
        long fpstimer=System.currentTimeMillis();
        int framecounter=0;
        gl.Initialize();
        fps=0;
        long dt;
        try{
        while(true){
            if(gl.keysPressed[KeyEvent.VK_ESCAPE])System.exit(0);
            //targetTimer=System.currentTimeMillis();
            gl.scale=Math.max(1, Math.min(frame.getWidth()/gl.width, frame.getHeight()/gl.height));
            gl.mouse=MouseInfo.getPointerInfo();
            int frameOffsetX=(frame.getWidth()-16-(gl.scale*gl.width))/2;
            int frameOffsetY=(frame.getHeight()-38-(gl.scale*gl.height))/2;
            if(fullscreen){
                frameOffsetX=(frame.getWidth()-(gl.scale*gl.width))/2;
                frameOffsetY=(frame.getHeight()-(gl.scale*gl.height))/2;
            }
            if(fullscreen){
                gl.mouseX=(int)(gl.mouse.getLocation().getX()-frame.getX()-frameOffsetX)/gl.scale;
                gl.mouseY=(int)(gl.mouse.getLocation().getY()-frame.getY()-frameOffsetY)/gl.scale;
                gl.update(frame.getX()+frameOffsetX,frame.getY()+frameOffsetY,fps);
            }
            else{
                gl.mouseX=(int)(gl.mouse.getLocation().getX()-8-frame.getX()-frameOffsetX)/gl.scale;
                gl.mouseY=(int)(gl.mouse.getLocation().getY()-30-frame.getY()-frameOffsetY)/gl.scale;
                gl.update(frame.getX()+frameOffsetX+8,frame.getY()+frameOffsetY+30,fps);
            }

            
            gl.render();
            
            Graphics2D g2=(Graphics2D)gl.getGraphics();
            AffineTransform at;
            if(fullscreen){
                at=AffineTransform.getTranslateInstance(frameOffsetX,frameOffsetY);
            }
            else{
                at=AffineTransform.getTranslateInstance(frameOffsetX,frameOffsetY);
            }
            at.scale(gl.scale,gl.scale);
            g2.drawImage(gl.screenBuffer, at, null);
            g2.dispose();
            
            targetTime=fpstimer+(framecounter+1)*1000/targetFPS;
            long time=System.currentTimeMillis();
            if(time<targetTime){
                Thread.sleep(targetTime-time);
            }
            else{
                Thread.sleep(1);
            }
            if(System.currentTimeMillis()>fpstimer+1000){
                fps=framecounter;
                fpstimer+=1000;
                //System.out.println(framecounter);
                framecounter=0;
            }
            framecounter++;
        }
        }
        catch(InterruptedException ex){
            
        }
    }
}
