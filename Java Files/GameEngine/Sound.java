package GameEngine;

import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FelixA
 */
public class Sound {
    
    /*
        For explosions, screams, just any sort of game effect
    */
    private AudioInputStream soundStream;
    private String soundFile;
    private Clip clip;
    private int type; //0 for continuous sounds, 1 for play once

    public Sound(String soundFile, int type){
        this.soundFile = soundFile;
        this.type=type;
        

        if(soundFile != null){
            try{
                soundStream = AudioSystem.getAudioInputStream(new File(soundFile));
                //System.out.println("Gets here!");//test print!
                clip = AudioSystem.getClip(); 
                clip.open(soundStream);
            }
            catch(Exception e){
                System.out.println(e.getMessage() + " No sound documents are fouond");
            }
        }//end if try sound

        if(this.type == 0){ 
          Runnable myRunnable = new Runnable(){
               public void run(){
                   while(true){
                       clip.start();
                       clip.loop(clip.LOOP_CONTINUOUSLY);
                       try {
                          Thread.sleep(10000);
                       } catch (InterruptedException ex) {
                           Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    }
                }
           };
           Thread thread = new Thread(myRunnable);
           thread.start();
       }//end if type 1

    }
    
    public void play(){
       clip.start();
   }
    
   public void stop(){
       clip.stop();
   }

}
