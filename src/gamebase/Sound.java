/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Sonar
 */
public class Sound{
    SoundThread st;
    public Sound(String fname){
        st=new SoundThread(fname);
        st.start();
    }
    public void play(){
        st.playing=false;
        while(st.busy){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        st.playing=true;
    }
    public void stop(){
        st.playing=false;
    }
    public void setVolume(float vol){
        st.setVolume(vol);
    }
    public void setPan(float pan){
        st.setPan(pan);
    }
    public void setCutoff(float cut){
        st.setCutoff(cut);
    }
}
class SoundThread extends Thread{
    
    String fname;
    private byte[] buffer;
    private float volume,pan,cutoff;
    private AudioInputStream stream;
    private AudioFormat format=null;
    private SourceDataLine line=null;
    private Mixer mixer=null;
    boolean playing,busy;
    public SoundThread(String fname){
        this.fname=fname;
        volume=1;
        pan=0;
        cutoff=1;
        playing=false;
    }
    public void createInput(File file) throws UnsupportedAudioFileException, IOException{
        stream=AudioSystem.getAudioInputStream(file);
        format=stream.getFormat();
    }
    public void createOutput(int bufferSize) throws LineUnavailableException{
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
        line=(SourceDataLine) AudioSystem.getLine(info);
        if(!AudioSystem.isLineSupported(info)){
            System.out.println("Line does not support"+info);
            System.exit(0);
        }
        line.open(format,bufferSize);
        buffer=new byte[line.getBufferSize()];
    }
    @Override
    public void run(){
        try {
            while(true){
                createInput(new File(fname));
                createOutput(8000);
                int numRead=0;
                buffer=new byte[line.getBufferSize()];
                float pvol=volume;
                float pcut=cutoff;
                float lvol=Math.min(1, 1-pan);
                float rvol=Math.min(1, pan+1);
                float pl=lvol,pr=rvol;
                int offset;
                line.start();
                while(!playing){
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(SoundThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                busy=true;
                short lprev, rprev;
                lprev=rprev=0;
                while(playing&&(numRead=stream.read(buffer,0,buffer.length))>=0){
                    float q,l,r,c;
                    short lval,rval;
                    for(int i=0; i<buffer.length; i+=4){
                        q=((float)i/(float)buffer.length);
                        lvol=Math.min(1, 1-pan);
                        rvol=Math.min(1, pan+1);
                        l=volume*lvol*q+pvol*pl*(1-q);
                        r=volume*rvol*q+pvol*pr*(1-q);
                        c=cutoff*q+pcut*(1-q);
                        lval=bytesToShort(buffer[i],buffer[i+1]);
                        rval=bytesToShort(buffer[i+2],buffer[i+3]);
                        lval=(short)((float)lval*l);
                        rval=(short)((float)rval*r);
                        lval=(short)((float)lval*c+(float)lprev*(1-c));
                        rval=(short)((float)rval*c+(float)rprev*(1-c));
                        lprev=lval;
                        rprev=rval;
                        setBytes(lval,i);
                        setBytes(rval,i+2);
                    }
                    pvol=volume;
                    pl=lvol;
                    pr=rvol;
                    pcut=cutoff;
                    offset=0;
                    while(offset<numRead){
                        offset+=line.write(buffer, offset, numRead-offset);
                    }
                }
                line.drain();
                line.stop();
                line.close();
                playing=false;
                busy=false;
            }
    
        } catch (IOException ex) {
            //Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setVolume(float volume){
        this.volume=Math.min(1,Math.max(volume,0));
    }
    public void setPan(float pan){
        this.pan=Math.min(1,Math.max(pan,-1));
    }
    public void setCutoff(float cutoff){
        this.cutoff=Math.min(1,Math.max(cutoff,0));
        this.cutoff*=this.cutoff;
    }
    short bytesToShort(byte b1, byte b2){
        if(format.isBigEndian()){
            return (short)(((short)b1<<8)|(b2&0xFF));
        }
        else{
            return (short)(((short)b2<<8)|(b1&0xFF));
        }
    }
    void setBytes(short value, int pointer){
        if(format.isBigEndian()){
            buffer[pointer]=(byte)((value>>8)&0xFF);
            buffer[pointer+1]=(byte)(value&0xFF);
            //buffer[pointer+1]=0;
        }
        else{
            buffer[pointer+1]=(byte)(value>>8);
            buffer[pointer]=(byte)((value)&0xFF);
            //buffer[pointer]=0;
        }
    }
}
