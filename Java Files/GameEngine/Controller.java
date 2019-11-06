package GameEngine;
import java.awt.event.*;


public class Controller extends KeyAdapter {
    private GameEvents gameEvents;
  
    public Controller(){

    }

    public Controller(GameEvents ge){
        this.gameEvents = ge;
    }
   
    /*public void keyPressed(KeyEvent e) {
        gameEvents.setValue(e);
    }*/

    public void keyReleased(KeyEvent e){
        gameEvents.setValue(e);
    }

}
