package GameEngine;

import LazarusGame.Box;
import java.awt.Rectangle;
import java.util.ArrayList;
/*
    Extend in game packages
*/
public class DetectCollisions {  
    GameEvents gameEvent1, gameEvent2;    
    public DetectCollisions(GameEvents ge1, GameEvents ge2){
        this.gameEvent1 = ge1;
        this.gameEvent2 = ge2;
    }
   
    public boolean doesCollide(GameObject o1,GameObject o2){       
        
        Rectangle o1_box = new Rectangle(o1.getX(), o1.getY(), o1.getWidth(), o1.getHeight());
        Rectangle o2_box = new Rectangle(o2.getX(), o2.getY(), o2.getWidth(), o2.getHeight());
        
        if(o1_box.intersects(o2_box)){
            return true;
        }
        else 
            return false;
    }

    public boolean doesCollide(Rectangle r, GameObject o2) {
        Rectangle o2_box = new Rectangle(o2.getX(), o2.getY(), o2.getWidth(), o2.getHeight());
        
        if(r.intersects(o2_box)){
            return true;
        }
        else 
            return false;
    }
 }

