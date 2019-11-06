
package LazarusGame;

import GameEngine.*;
import static java.awt.Color.BLUE;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

public class Lazarus extends Player implements Observer {
    private int x,y,speed;
    private int left,right;
    private boolean isSquished;
    private boolean jumpLeft;
    private boolean jumpRight;
    private Image lazImage;
    
    //set sizes to these arrays
    private Image[] lazSquish=new Image[11];
    private Image[] lazRightJump=new Image[7];
    private Image[] lazLeftJump=new Image[7];
    private Image[] lazMoveLeft=new Image[7];
    private Image[] lazMoveRight=new Image[7];
    private Image[] lazScared = new Image[10];
    
    int moveCount=0;
    int squishCount=0;
    boolean isLeft,isRight,isLeftJump,isRightJump;
    
    //different Image Strips
    Image LazStand, LazLeft,LazRight,LazLJ,LazRJ,LazS, LazScared;
    
    Rectangle lazRect;
    
    //falling "gravity"
    private boolean hasLanded;
    
    //for rectangles to detect nearby boxes
    private boolean u,d,l,r,uL,uR,dL,dR;
    Rectangle upRect, downRect, leftRect,rightRect,upLeftRect,upRightRect,downLeftRect,downRightRect;
    
    //Game sounds - only movement sound and squished
    Sound moveSound;
    String moveName;
    Sound squishSound;
    String squishName;
    Sound screamSound;
    
    //to hold current positions and subtract them when image pics get bigger at movement
    int xPos,yPos;
    
    public Lazarus(Image img,int x, int y,int speed,int left,int right){
        super(img,0,0,0,x,y,speed,0,0,left,right,0);
        this.x=x;
        this.y=y;
        this.speed=speed;
            this.left = left;
            this.right = right;
        lazImage=img;
        try{
            LazStand = img;
            LazLeft = ImageIO.read(new File("Resources(Lazarus)/MoveLeft.png"));
            LazRight = ImageIO.read(new File("Resources(Lazarus)/MoveRight.png"));
            LazLJ = ImageIO.read(new File("Resources(Lazarus)/JumpLeft.png"));
            LazRJ = ImageIO.read(new File("Resources(Lazarus)/JumpRight.png"));
            LazS = ImageIO.read(new File("Resources(Lazarus)/Squished.png"));
            LazScared = ImageIO.read(new File("Resources(Lazarus)/Afraid.png"));
            super.split(LazLeft,lazMoveLeft);
            super.split(LazRight,lazMoveRight);
            super.split(LazLJ,lazLeftJump);
            super.split(LazRJ,lazRightJump);
            super.split(LazS,lazSquish);
            super.split(LazScared, lazScared);
            
            isSquished=false;
            
            lazRect=new Rectangle(x,y,LazStand.getHeight(null),LazStand.getHeight(null));
        
            upRect =  new Rectangle(x,y-40,40,40);
            downRect =  new Rectangle(x,y+40,40,40);
            leftRect = new Rectangle(x-40,y,40,40);
            rightRect = new Rectangle(x+40,y,40,40);
            upLeftRect = new Rectangle(x-40,y-40,40,40);
            upRightRect = new Rectangle(x+40,y-40,40,40);
            downLeftRect = new Rectangle(x-40,y+40,40,40);
            downRightRect = new Rectangle(x+40,y+40,40,40);
            
            this.moveName="Resources(Lazarus)/Move.wav";
            this.squishName="Resources(Lazarus)/Squished.wav";
            squishSound = new Sound(squishName,1);
            screamSound = new Sound("Resources(Lazarus)/Scream.wav",1);
        }catch(Exception e){
            System.out.println(e + ": Error loading images");
        }
        
        
    }
    
    @Override
    public void update(Observable obj, Object arg){
        GameEvents ge = (GameEvents)arg;
        if(ge.type==1){
            KeyEvent e = (KeyEvent) ge.event;
            int keyCode=e.getKeyCode();
            
            if(keyCode==left){
                this.x-=speed;
                isLeft=true;
                isRight=false;
                System.out.println(x);
            }
            if(keyCode==right){
                isLeft=false;
                isRight=true;
                this.x+=speed;
            }        
            moveSound= new Sound(moveName,1);
            moveSound.play();
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
        //lazImage=LazStand;
        moveCount=0;
        xPos=x;yPos=y;
    }
    
    /*
        This method is for updating the detection rectangles
    */
    public void detector(Graphics g){
        upRect =  new Rectangle(x,y-40,40,40);
        downRect =  new Rectangle(x,y+40,40,40);
        leftRect = new Rectangle(x-40,y,40,40);
        rightRect = new Rectangle(x+40,y,40,40);
        upLeftRect = new Rectangle(x-40,y-40,40,40);
        upRightRect = new Rectangle(x+40,y-40,40,40);
        downLeftRect = new Rectangle(x-40,y+40,40,40);
        downRightRect = new Rectangle(x+40,y+40,40,40);
        
        /*g.setColor(BLUE);
        g.fillRect((int) upRect.getX(), (int) upRect.getY(), 40,40);
        g.fillRect((int) downRect.getX(), (int) downRect.getY(), 40,40);
        g.fillRect((int) leftRect.getX(), (int) leftRect.getY(), 40,40);
        g.fillRect((int) rightRect.getX(), (int) rightRect.getY(), 40,40);*/
    }
    
    @Override
    public void draw(Graphics g, ImageObserver obs){    
        if((isLeft && u==true&&r==false) || (isRight&&u==true&&l==false) && moveCount==0){ //if against wall
            //System.out.println("Should stand");
            lazImage=LazStand;
            g.drawImage(lazImage, this.x, this.y, obs);
            isLeft=false;
            isRight=false;      
        }
        else if(d==true&&l==true&&r==true&&uL==true&&uR==true && moveCount<lazScared.length-1){//if trapped, then be scared
            lazImage = lazScared[moveCount];
            g.drawImage(lazImage, this.x, this.y, obs);
            isLeft=false;
            isRight=false;
        }
        else if(isLeft && (d==true) && moveCount<lazMoveLeft.length-1){ //Move left
            lazImage=lazMoveLeft[moveCount];
            //System.out.println("Gets move left");
            g.drawImage(lazImage, this.x, this.y-40, obs);
            moveCount++; 
        }
        else if(isRight && d==true && moveCount<lazMoveRight.length-1){ //Move right
            lazImage=lazMoveRight[moveCount];
            //System.out.println("Gets here");
            g.drawImage(lazImage, this.x-40, this.y-40, obs);
            moveCount++;     
        }
        else if(isLeft && (d==false) && moveCount<lazLeftJump.length-1){ //Jump left
            lazImage=lazLeftJump[moveCount];
            //System.out.println("Gets Jump Left");
            if(dR==true && d==false){
                g.drawImage(lazImage, this.x, this.y-40, obs);   
            }
            moveCount++;
        }
        else if(isRight && d==false && moveCount<lazRightJump.length-1){//Jump right
            lazImage=lazRightJump[moveCount];
            if(dL==true){
                g.drawImage(lazImage, this.x-40, this.y-40, obs); 
            }
            moveCount++;
        }
        else if(isSquished && squishCount<lazSquish.length-1){ //Squished
            squishSound.play();
            screamSound.play();
            lazImage=lazSquish[squishCount];
            squishCount++;
            g.drawImage(lazImage, this.x, this.y, obs);
            if(squishCount==lazSquish.length-1){
                LazarusGameWorld.isGameOver=true;
            }
        }
        else{ //default standing
            if(d==false){ //Fall down after animations are completed
                y+=40;
            }
            if(d==false && dL==false && dR==false){
                y+=40;
            }
            lazImage=LazStand;
            g.drawImage(lazImage, this.x, this.y, obs);
        }
        //g.drawImage(lazImage, this.x, this.y, obs);
    }
    
    public void isDead(){
        //System.out.println("is squished");
        isSquished=true;
    }
    
    //for rectangles to detect nearby obstacles
    public Rectangle getUpRect(){
        return upRect;
    }
    public Rectangle getDownRect(){
        return downRect;
    }
    public Rectangle getLeftRect(){
        return leftRect;
    }
    public Rectangle getRightRect(){
        return rightRect;
    }
    public Rectangle getUpLeft(){
        return upLeftRect;
    }
    public Rectangle getUpRight(){
        return upRightRect;
    }
    public Rectangle getDownLeft(){
        return downLeftRect;
    }
    public Rectangle getDownRight(){
        return downRightRect;
    }
    public boolean getUp(){
        return u;
    }
    public void setUp(boolean newU){
        u=newU;
    }
    public boolean getDown(){
        return d;
    }
    public void setDown(boolean newD){
        d=newD;
    }
    public boolean getLeft(){
        return l;
    }
    public void setLeft(boolean newL){
        l=newL;
    }
    public boolean getRight(){
        return r;
    }
    public void setRight(boolean newR){
        r=newR;
    }
    public boolean getUL(){
        return uL;
    }
    public void setUL(boolean newUL){
        uL=newUL;
    }
    public boolean getUR(){
        return uR;
    }
    public void setUR(boolean newUR){
        uR=newUR;
    }
    public boolean getDL(){
        return dL;
    }
    public void setDL(boolean newDL){
        dL=newDL;
    }
    public boolean getDR(){
        return dR;
    }
    public void setDR(boolean newDR){
        dR=newDR;
    }
    
   
    public void setX(int newX){
        this.x=newX;
    }
    public int getX(){
        return this.x;
    }
    public void setY(int newY){
        this.y=newY;
    }
    public int getY(){
        return this.y;
    }
    
    public boolean wasSquished(){
        return this.isSquished;
    }
    public boolean isLanded(){
        return hasLanded;
    }
    public void setLanded(boolean newL){
        hasLanded=newL;
    }
    
    //to detect which keys are pressed
    public boolean getIsLeft(){
        return isLeft;
    }
    public boolean getIsRight(){
        return isRight;
    }
    
}
