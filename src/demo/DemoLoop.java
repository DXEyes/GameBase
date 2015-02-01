/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import gamebase.GameLoop;
import gamebase.Sound;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author vcowal
 */
public class DemoLoop extends GameLoop{
    int ax,ay,bx,by,cx,cy;
    int color;
    int x,y;
    long time;
    public DemoLoop(int width, int height){
        super(width,height);
    }

    @Override
    public void Initialize() {
        backgroundColor=0;
        time=System.currentTimeMillis();
    }

    @Override
    public void Update() {
        double angle=(double)mouseX/60;
        double dist=(double)(mouseY+mouseWheel*40-height/2)/4;
        ax=(int)(Math.cos(angle)*dist);
        ay=(int)(-Math.sin(angle)*dist);
        angle+=2*Math.PI/3;
        bx=(int)(Math.cos(angle)*dist);
        by=(int)(-Math.sin(angle)*dist);
        angle+=2*Math.PI/3;
        cx=(int)(Math.cos(angle)*dist);
        cy=(int)(-Math.sin(angle)*dist);
        float v=1-((float)mouseY/(float)height);
        float p=-1+2*((float)mouseX/(float)width);
        //throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public void Render() {
        blendMode=BM_NORMAL;
        color=Color.BLACK.getRGB();
        drawRectangle(0,0,160,120,color,false);
        drawRectangle(160,120,320,240,color,false);
        color=Color.WHITE.getRGB();
        drawRectangle(160,0,320,120,color,false);
        drawRectangle(0,120,160,240,color,false);
        
        blendMode=BM_NORMAL;
        x=80;
        y=60;
        
        int size=16;
        if(((System.currentTimeMillis()-time)*128)%60000<10000)size=20;
        color=new Color(255,0,0,128).getRGB();
        drawRectangle(x+ax-size,y+ay-size,x+ax+size,y+ay+size,color,false);
        color=new Color(0,255,0,128).getRGB();
        drawRectangle(x+bx-size,y+by-size,x+bx+size,y+by+size,color,false);
        color=new Color(0,0,255,128).getRGB();
        drawRectangle(x+cx-size,y+cy-size,x+cx+size,y+cy+size,color,false);
        
        blendMode=BM_ADD;
        x=240;
        y=180;
        color=new Color(255,0,0).getRGB();
        drawRectangle(x+ax-size,y+ay-size,x+ax+size,y+ay+size,color,false);
        color=new Color(0,255,0).getRGB();
        drawRectangle(x+bx-size,y+by-size,x+bx+size,y+by+size,color,false);
        color=new Color(0,0,255).getRGB();
        drawRectangle(x+cx-size,y+cy-size,x+cx+size,y+cy+size,color,false);
        
        blendMode=BM_MULTIPLY;
        x=240;
        y=60;
        color=new Color(255,0,128).getRGB();
        drawRectangle(x+ax-size,y+ay-size,x+ax+size,y+ay+size,color,false);
        color=new Color(128,255,0).getRGB();
        drawRectangle(x+bx-size,y+by-size,x+bx+size,y+by+size,color,false);
        color=new Color(0,128,255).getRGB();
        drawRectangle(x+cx-size,y+cy-size,x+cx+size,y+cy+size,color,false);
        
        blendMode=BM_DIFFERENCE;
        x=80;
        y=180;
        color=new Color(255,0,128).getRGB();
        drawRectangle(x+ax-size,y+ay-size,x+ax+size,y+ay+size,color,false);
        color=new Color(128,255,0).getRGB();
        drawRectangle(x+bx-size,y+by-size,x+bx+size,y+by+size,color,false);
        color=new Color(0,128,255).getRGB();
        drawRectangle(x+cx-size,y+cy-size,x+cx+size,y+cy+size,color,false);
        
        if(keysPressed[KeyEvent.VK_SPACE]){
            color=Color.WHITE.getRGB();
            drawRectangle(0,0,320,240,color,false);
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseWheelRotated(int dir) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
