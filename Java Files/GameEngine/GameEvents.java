package GameEngine;

import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;


public class GameEvents extends Observable{
    public int type;
    final int keyE = 1;
    final int collision = 2;
    int collisionDamage;
    public Object event;
    public KeyAdapter key;
    public void setValue(KeyEvent e) {
        type = keyE;
	// let's assume this means key input.
        //Should use CONSTANT value for this when you program
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this); 
    }
    public void setValue(String msg) {
        type = collision;
        event = msg;
        setChanged();
        // trigger notification
        notifyObservers(this); 
    }

    public int getType(){
        return this.type;
    }
  
    public Object getEvent(){
        return this.event;
    }
    public KeyAdapter getKey(){
        return this.key;
    }

}
