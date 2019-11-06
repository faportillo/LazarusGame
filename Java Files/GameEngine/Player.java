package GameEngine;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Observer;
import java.util.Observable;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Player extends GameObject implements Observer{
        private int life,ID, score;
    	private int health,damage,bulletDamage;
       	private int forward,backward,left,right,fire;
       	private int lifeCount;
       	private boolean boom;
       	
       	private Image [] healthBars, healthImg;
       	private GameObject healthbar;
 	String soundFileName;
	//SoundPlayer sp;
   	
        public Player(Image img,int life, int id, int damage,int x, int y, int Yspeed,int forward, int backward, int left, int right, int fire) {
            super(img,x,y,Yspeed);
            health = 200;
            this.damage = damage;
            this.bulletDamage = 4;
            this.forward = forward;
            this.backward = backward;
            this.left = left;
            this.right = right;
            this.fire = fire;
            
            this.healthBars = new Image[4];
            
            this.lifeCount = life;
           
    	}

	public int getDamage(){
            return this.damage;
     	}

     

     	public void reduceHealth(int d){
            if(health < d)
            this.isDead();
            this.health -= d;
        }

     	public void addHealth(int h){
            this.health += h;
     	}

     	public void isDead(){
       
            if(this.lifeCount >1){
                lifeCount --;
                this.health = 200;
                this.x = 200;
                this.y = 360;
            }
            else{
                this.x = 480;
                this.y = 500;
                boom = true;
            }
        }

     	public void draw(Graphics g, ImageObserver obs){
            if(!boom){
                g.drawImage(img, x, y, obs);
                if(this.health >= 150){
                        healthbar = new GameObject(healthBars[0],x,y+height,Yspeed);
                        healthbar.draw(g, obs);
                }
                if(this.health < 150 && this.health >=100){
                        healthbar = new GameObject(healthBars[1],x,y+height,Yspeed);
                        healthbar.draw(g, obs);
                }
                if(this.health < 100 && this.health >=50){
                        healthbar = new GameObject(healthBars[2],x,y+height,Yspeed);
                        healthbar.draw(g, obs);
                }
                if(health < 50){
                        healthbar = new GameObject(healthBars[3],x,y+height,Yspeed);
                        healthbar.draw(g, obs);
                }
                /*for(int i = 0; i < lifeCount;i++){
                *****PLACE IN TANKGAME PACKAGE
                        GameObject lifeObj = new GameObject(this.lifeImg,x+i*lifeImg.getWidth(null)-15,y+height+healthbar.getHeight(),Yspeed);
                        lifeObj.draw(g, obs);
                }*/
            }       
        }
   
        public void update(Observable obj, Object arg) {
            GameEvents ge = (GameEvents) arg;
            if(ge.type == 1) {
                KeyEvent e = (KeyEvent) ge.event;
                int keyCode = e.getKeyCode();
                //System.out.println(keyCode);
                //System.out.println(left + " " + right + " "+ up + " "+ down+ " "+fire);
                if( keyCode == left) {
                        if(x > 0)
                                x -= Yspeed;
                }
                else if(keyCode == right){
                            if(x < 570)
                                    x += Yspeed;
                }
                else if(keyCode == forward){
                            if(y > 0)
                            y -= Yspeed;
                }
                else if(keyCode == backward){
                            if(y < 400)
                            y += Yspeed;
                }
                else if(keyCode == fire) {
                   
                }
            }
            else if(ge.type == 2) {
                String msg = (String)ge.event;
                String[] msgArray = new String[2];
                StringTokenizer st = new StringTokenizer(msg);	
                int i = 0;
                while (st.hasMoreTokens()) {
                        msgArray[i] = st.nextToken();
                        i++;
                }
                if(msgArray[0].equals("Collision")) {
                    int y = Integer.parseInt(msgArray[1]);
                    this.reduceHealth(y);
                  

                }
            }

       }
        
    public int getScore(){
        return this.score;
    }
    public int getPlayerID(){
        return this.ID;
    }
    public boolean isAlive(){
        //return !myPlane.getBoom();
        return true;
    }
    public void addScore(int s){
        this.score += s;
    }

 }
    
        

