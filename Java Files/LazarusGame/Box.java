
package LazarusGame;
import GameEngine.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

public class Box extends GameObject {
    /*
        **Remember that the y value is always at -50 so it appears
          above the gamescreen and then falls down as 
          opposed to appearing at the top of the gamescreen
    
        **Remember that the x value has to be the same value as 
        Lazarus
    
        **Remember that a new one can't appear until the previous one
        lands
    */
    
    /*
        0-cardboard
        1-wood
        2-metal
        3-stone
        4-mesh
    */
    int type; 
    int speed;
    public boolean hasLanded;
    Image boxImage;
    Rectangle boxRect;
    
    public Box(Image img, int x, int y, int Yspeed, int type) {
        super(img, x, y, Yspeed);
        boxImage = img;
        this.type=type;
        this.speed=Yspeed;
        boxRect=new Rectangle(x,y,boxImage.getHeight(null),boxImage.getHeight(null));
    }
    
    public void update(){
        if(hasLanded==false){
            y+=speed;
        }
        else{
            y=y;
        }
    }
    
    public void draw(Graphics g, ImageObserver obs){
        g.drawImage(boxImage,x,y,obs);
    }
    
    public int getType(){
        return type;
    }
    
    public boolean isLanded(){
        return hasLanded;
    }
    public void setLanded(boolean newL){
        hasLanded=newL;
    }
    public Rectangle getBoxRect(){
        return boxRect;
    }
    
}
