
package LazarusGame;
import GameEngine.*;
import java.awt.*;
import java.util.*;
/**
 *
 * @author FelixA
 */

/*
    Used the doesCollide() method from the super class
*/
public class Collisions extends DetectCollisions {
    GameEvents gameEvent1,gameEvent2;
    
    Sound buttonSound;
    Sound crushSound;
    Sound meshSound;
    
    Graphics2D g2;
    public Collisions(GameEvents ge1,GameEvents ge2) {
        super(ge1, ge2);
        gameEvent1=ge1;
        gameEvent2=ge2;
    }
    
    public void boxVSbox(Box b1,Box b2){
        if(super.doesCollide(b1, b2)){
            //System.out.println("BOX COLLISION");
            if(b1.getType()<b2.getType()){ //if type is greater
                b1.setY(b1.getY()-10);
                b1.setLanded(true);
            }
            else if(b1.getType()==b2.getType()&&b1.getType()==0){
                //for when cardboard box collides with another cardboard box
                LazarusGameWorld.landList.remove(b2);
                System.out.println("Cardboard BOX COLLISION");//Test print
                crushSound= new Sound("Resources(Lazarus)/Crush.wav",1);
                crushSound.play();
            }
            else if(b1.getType()==b2.getType()&&b1.getType()!=0){
                b1.setY(b1.getY()-10);
                b1.setLanded(true);
                if(b1.getType()==4){//turn mesh into wall block
                   b1.boxImage=LazarusGameWorld.wallImg;
                   meshSound = new Sound("Resources(Lazarus)/Wall.wav",1);
                   meshSound.play();
                }
            }
            else if(b1.getType()>b2.getType()){
                LazarusGameWorld.landList.remove(b2);
                crushSound= new Sound("Resources(Lazarus)/Crush.wav",1);
                crushSound.play();
            }
            else{
                System.out.println("Check if I missed any collision type");
            }
        }
    }
    
    public void boxVSlazarus(Lazarus l,Box b){
        
        if(super.doesCollide(l, b) && b.getY()<l.getY()){
            l.isDead();
            //System.out.println("Squished!");//Test print
        }
        else if(super.doesCollide(l, b) && b.getY()==l.getY() && b.getX()==l.getX() && !l.wasSquished()){
            //System.out.println("Touch box");                   
            if(l.getUp()==false){
                l.setY(l.getY()-40);
            }           
            else if(l.getLeft()==false && l.getUp()==true && l.getIsRight()){
                l.setX(l.getX()-40);
            }
            else if(l.getRight()==false && l.getUp()==true && l.getIsLeft()){
                l.setX(l.getX()+40);
            }
        }
    }
    public void boxVSwall(Box b, Wall w){
        if(super.doesCollide(b, w)){
            b.setLanded(true);
            b.setY(b.getY()-10);
            if(b.getType()==4){//turn mesh into wall block
               b.boxImage=LazarusGameWorld.wallImg;
               meshSound = new Sound("Resources(Lazarus)/Wall.wav",1);
               meshSound.play();
            }
        }
    }
    public void lazarusVSbutton(Lazarus l,Button b){
        if(super.doesCollide(l, b)){
            buttonSound=new Sound("Resources(Lazarus)/Button.wav",1);
            buttonSound.play();
            if(LazarusGameWorld.level1==true && LazarusGameWorld.level2==false){
                LazarusGameWorld.landList.clear();
                LazarusGameWorld.wallList.clear();
                LazarusGameWorld.level1=false;
                LazarusGameWorld.level2=true;
                l.setX(120);l.setY(400);
            }
            else if(!LazarusGameWorld.level1==true && !LazarusGameWorld.level2==false){
                LazarusGameWorld.isGameOver=true;
            }
        }
    }
    public void lazarusVSwall(Lazarus l,Wall w){
        if(this.doesCollide(l, w) &&l.getX()<w.getX()+41 && l.getX()<=320){
            //System.out.println("Collision!");
            //System.out.println("Up: "+LazarusGameWorld.up+", Down: "+LazarusGameWorld.down +"\n");
            if(l.getX()>w.getX()-1 && l.getUp()==false){
                //l.setX(l.getX()+40);
                l.setY(l.getY()-40);
                //System.out.println(l.getX() + " , " + l.getY());
            }
            else if(l.getX()>w.getX()-1 && l.getUp()==true){
                l.setX(l.getX()+40);
            }
        }
        else if(this.doesCollide(l,w) && l.getX()>w.getX()-1){
            //System.out.println("Collision!");
            if(l.getX()<w.getX()+2 && l.getUp()==false){
               //System.out.println(l.getX());
               //l.setX(l.getX()-40); 
               l.setY(l.getY()-40);
            }
            else if(l.getX()<w.getX()+2 && l.getUp()==true){
               l.setX(l.getX()-40); 
            }
        }
       
    }
}
