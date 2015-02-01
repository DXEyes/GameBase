/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class for handling images with multiple frames.
 * @author Peter Cowal
 */
public class Sprite {
    int[] frames;
    public int width,height,length;
    /**
     * Creates a sprite from a file with a given number of frames.
     * @param fname the path to the sprite file
     * @param numFrames the number of frames the sprite has
     * @throws IOException if the sprite file is missing
     */
    public Sprite(String fname, int numFrames) throws IOException{
        BufferedImage spriteImage = ImageIO.read(new File(fname));
        int type = spriteImage.getType();
        if(type!=BufferedImage.TYPE_INT_ARGB){
            BufferedImage tempImage= new BufferedImage(spriteImage.getWidth(),spriteImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = tempImage.createGraphics();
            g.drawImage(spriteImage,0,0,null);
            g.dispose();
            spriteImage=tempImage;
        }
        if(numFrames>0){
            this.length=numFrames;
        }
        else{
            this.length=0;
            String s=fname;
            for(int i=0; i<s.length()-8; i++){
                if(s.substring(i,i+6).equals("_strip")){
                    i+=6;
                    while((int)s.charAt(i)>=48&&(int)s.charAt(i)<=57){
                        length*=10;
                        length+=(int)s.charAt(i)-48;
                        i++;
                    }
                    i=100000;
                }
            }
            if(length==0)length=1;
        }
        frames=new int[spriteImage.getWidth()*spriteImage.getHeight()];
        width=spriteImage.getWidth()/length;
        height=spriteImage.getHeight();
        int[] color=new int[4];
        for(int n=0; n<length; n++){
            for(int i=0; i<width; i++){
                for(int j=0; j<height; j++){
                    spriteImage.getRaster().getPixel(i+n*width, j, color);
                    frames[(n*height+j)*width+i]=color[3]*256*256*256+color[0]*256*256+color[1]*256+color[2]; 
                    
                }
            }
        }
    }
    /**
     * Creates a sprite from a file with a given number of frames.
     * @param file the image file
     * @param numFrames the number of frames the sprite has
     * @throws IOException if the sprite file is missing
     */
    public Sprite(File file, int numFrames) throws IOException{
        BufferedImage spriteImage = ImageIO.read(file);
        int type = spriteImage.getType();
        if(type!=BufferedImage.TYPE_INT_ARGB){
            BufferedImage tempImage= new BufferedImage(spriteImage.getWidth(),spriteImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = tempImage.createGraphics();
            g.drawImage(spriteImage,0,0,null);
            g.dispose();
            spriteImage=tempImage;
        }
        if(numFrames>0){
            this.length=numFrames;
        }
        else{
            this.length=0;
            String s=file.getName();
            for(int i=0; i<s.length()-8; i++){
                if(s.substring(i,i+6).equals("_strip")){
                    i+=6;
                    while((int)s.charAt(i)>=48&&(int)s.charAt(i)<=57){
                        length*=10;
                        length+=(int)s.charAt(i)-48;
                        i++;
                    }
                    i=100000;
                }
            }
            if(length==0)length=1;
        }
        frames=new int[spriteImage.getWidth()*spriteImage.getHeight()];
        width=spriteImage.getWidth()/length;
        height=spriteImage.getHeight();
        int[] color=new int[4];
        for(int n=0; n<length; n++){
            for(int i=0; i<width; i++){
                for(int j=0; j<height; j++){
                    spriteImage.getRaster().getPixel(i+n*width, j, color);
                    frames[(n*height+j)*width+i]=color[3]*256*256*256+color[0]*256*256+color[1]*256+color[2]; 
                    
                }
            }
        }
    }
    /**
     * Returns an integer representation of the sprite's color at (x,y)
     * @param frame the frame of the sprite
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return the color of the pixel in integer (ARGB) format
     */
    public int getPixel(int frame, int x, int y){
        return frames[(((frame%length)*height+y)*width+x)];
    }
}
