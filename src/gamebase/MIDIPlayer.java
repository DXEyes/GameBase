/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * A class for handling and playing MIDI files.
 * @author Peter Cowal
 */
public class MIDIPlayer {
    Sequence sequence;
    Sequencer sequencer;
    
    /**
     * Initializes the sequencer and such.
     */
    public MIDIPlayer(){
        try {
            
            sequencer=MidiSystem.getSequencer();
            sequencer.open();
            
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Loads and plays a MIDI file.
     * @param fileName the path to the MIDI file
     * @param loop whether or not the MIDI file will loop
     */
    public void play(String fileName,boolean loop){
        try {
            sequencer.stop();
            sequence=MidiSystem.getSequence(new File(fileName));
            sequencer.setSequence(sequence);
            if(loop) {
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            }
            sequencer.start();
            
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stops the sequencer from playing.
     */
    public void stop(){
        sequencer.stop();
    }
}
