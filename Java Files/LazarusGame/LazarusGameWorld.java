package LazarusGame;

import GameEngine.*;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class LazarusGameWorld extends JPanel implements Observer, Runnable,ActionListener {
    private Thread thread;
    
    private Image background;
    
    //for boxes
    private Image cardBoxImg,woodBoxImg,metalBoxImg,stoneBoxImg, meshBoxImg;
    static Box cardBox,woodBox,metalBox,stoneBox, meshBox;
    Box startBox;
    static ArrayList<Box> boxList = new ArrayList<Box>();
    Random boxR = new Random();
    int boxType;
    static ArrayList<Box> landList = new ArrayList<Box>();
    
    //for walls
    public static ArrayList<Wall> wallList = new ArrayList<Wall>();
    static Image wallImg;
    
    //for button
    private Image buttonImg;
    static Button button1,button2;
    
    int frameCount=0;
    static int mapWidth=640,mapHeight=480; 
    
    private Timer timer;
    private final int DELAY=150;
    
    Lazarus lazarus;
    Image lImage;
    
    GameEvents gameEvent,gameEvent2;
    Collisions c;
    
    //for levels
    static boolean level1,level2;
    
    //if box has landed
    boolean landed=false;
    Image nextBox;//to display next box
    
    //if game is over
    static boolean startMenu,isGameOver;
    
    
    //Game sounds
    Sound music;
    
    public void init(){
        this.setFocusable(true);
        setBackground(Color.white);
        try{
            background = ImageIO.read(new File("Resources(Lazarus)/Background.bmp"));
            
            //for box images
            cardBoxImg = ImageIO.read(new File("Resources(Lazarus)/CardBox.gif"));
            woodBoxImg = ImageIO.read(new File("Resources(Lazarus)/WoodBox.gif"));
            metalBoxImg = ImageIO.read(new File("Resources(Lazarus)/MetalBox.gif"));
            stoneBoxImg = ImageIO.read(new File("Resources(Lazarus)/StoneBox.gif"));
            meshBoxImg = ImageIO.read(new File("Resources(Lazarus)/Mesh.gif"));
            
            //for wall
            wallImg=ImageIO.read(new File("Resources(Lazarus)/Wall.gif"));
            
            //for button
            buttonImg=ImageIO.read(new File("Resources(Lazarus)/Button.gif"));
            
            //for lazarus player
            lImage = ImageIO.read(new File("Resources(Lazarus)/Lazarus_stand.png"));
            lazarus = new Lazarus(lImage,400,400,40,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT);
            gameEvent = new GameEvents();
            gameEvent2=new GameEvents();
            gameEvent.addObserver(lazarus);
            gameEvent2.addObserver(lazarus);
            Controller key1 = new Controller(gameEvent);
            addKeyListener(key1);
            
            //for staring box spawing
            //boxType = boxR.nextInt(4);
            startBox=new Box(cardBoxImg,400,-50,10,0);
            boxList.add(startBox);
            
            //for different levels
            level1=true;
            level2=false;
            
            //for testing 
            //level1=false;
            //level2=true;
            
            c=new Collisions(gameEvent,gameEvent2);
            //randome next box for starting
            boxType = boxR.nextInt(4);
            
            isGameOver=false;
            
            //Music
            music = new Sound("Resources(Lazarus)/Stayin_Alive.wav",0);
            music.play();
        }
        catch (Exception e) {
            System.out.print(e.getStackTrace() +" No resources are found");
        }

    }//end init
    
    public void drawStartMenu(Graphics g){
        
    }
    
    public void drawBackgroundOne(Graphics g){
	//Draw the background
        g.drawImage(background, 0,0, this);
        button1=new Button(buttonImg,0,200);
        button2=new Button(buttonImg,mapWidth-wallImg.getWidth(this), 200);
       	//add walls
        for(int a=240;a<mapHeight;a+=wallImg.getHeight(this)){
            wallList.add(new Wall(wallImg,0,a));
            wallList.add(new Wall(wallImg,mapWidth-wallImg.getWidth(this), a));
        }
        for(int b=40;b<(mapWidth);b+=wallImg.getWidth(this)){
            wallList.add(new Wall(wallImg,b,mapHeight-wallImg.getHeight(this)));
        }
        for(Wall w:wallList){
            w.draw(g, this);
            //g.drawRect(w.getX(),w.getY(),40,40);
            //c.lazarusVSwall(w);
            g.drawRect(w.getX(),w.getY(),w.getWallRect().width,w.getWallRect().height);
        }
    }
    public void drawBackgroundTwo(Graphics g){
	//Draw the background
        g.drawImage(background, 0,0, this);
        button1=new Button(buttonImg,-500,200);
        button2=new Button(buttonImg,mapWidth-wallImg.getWidth(this), 200);
        //Draw the walls
        for(int a=280;a<mapHeight;a+=wallImg.getHeight(this)){
            wallList.add(new Wall(wallImg,0,a));
            wallList.add(new Wall(wallImg,-40,a));
            wallList.add(new Wall(wallImg,mapWidth-wallImg.getWidth(this), a));
        }
        for(int b=40;b<(mapWidth);b+=wallImg.getWidth(this)){
            wallList.add(new Wall(wallImg,b,mapHeight-wallImg.getHeight(this)));
        }
        for(int a=240;a<mapHeight;a+=wallImg.getHeight(this)){
            wallList.add(new Wall(wallImg,320,a));
            landList.add(new Box(wallImg,320,a,0,3));
            wallList.add(new Wall(wallImg,mapWidth-wallImg.getWidth(this), a));
        }
        for(int a=360;a<mapHeight;a+=wallImg.getHeight(this)){
            landList.add(new Box(wallImg,160,a,0,4));
            landList.add(new Box(wallImg,480,a,0,4));
        }
        for(Wall w: wallList){
            w.draw(g, this);
            g.drawRect(w.getX(),w.getY(),w.getWallRect().width,w.getWallRect().height);
        }
    }
    
    public void drawGame(Graphics g){
        /*Call the drawing methods*/
        //drawBackground();
        if(level1&&!level2){
            //remember to clear lists for new level
            drawBackgroundOne(g);         
        }
        else if(level2&&!level1){
            drawBackgroundTwo(g);
        }
        g.setColor(GREEN);
        //draw buttons
        button1.draw(g, this);
        button2.draw(g, this);
        
        lazarus.detector(g);
        
        /*
            This is to determine if there are boxes above or below 
            In separate loops than the ones that detect player collision
             because they would detected different boxes at each iteration 
        */
        for(Box b: landList){
            Rectangle bBox = new Rectangle(b.getX(),b.getY(), 40,40);
            
            if(lazarus.getUpRect().intersects(bBox)){
                lazarus.setUp(true);
            }
            if(lazarus.getDownRect().intersects(bBox)){
                lazarus.setDown(true);
            }
            if(lazarus.getLeftRect().intersects(bBox)){
                lazarus.setLeft(true);
            }
            if(lazarus.getRightRect().intersects(bBox)){
                lazarus.setRight(true);
            }
            if(lazarus.getUpLeft().intersects(bBox)){
                lazarus.setUR(true);
            }
            if(lazarus.getDownLeft().intersects(bBox)){
                lazarus.setDL(true);
            }
            if(lazarus.getUpRight().intersects(bBox)){
                lazarus.setUR(true);
            }
            if(lazarus.getDownRight().intersects(bBox)){
                lazarus.setDR(true);
            }
        }
        for(Wall w: wallList){
            Rectangle wBox = new Rectangle(w.getX(),w.getY(), 40,40);
            
            if(lazarus.getUpRect().intersects(wBox)){
                lazarus.setUp(true);
            }
            if(lazarus.getDownRect().intersects(wBox)){
                lazarus.setDown(true);
            }
            if(lazarus.getLeftRect().intersects(wBox)){
                lazarus.setLeft(true);
            }
            if(lazarus.getRightRect().intersects(wBox)){
                lazarus.setRight(true);
            }
            if(lazarus.getUpLeft().intersects(wBox)){
                lazarus.setUR(true);
            }
            if(lazarus.getDownLeft().intersects(wBox)){
                lazarus.setDL(true);
            }
            if(lazarus.getUpRight().intersects(wBox)){
                lazarus.setUR(true);
            }
            if(lazarus.getDownRight().intersects(wBox)){
                lazarus.setDR(true);
            }
        }        
        //System.out.println("Up: "+lazarus.getUp()+", Down: "+lazarus.getDown() + ", Left: "+lazarus.getLeft()+", Right: "+lazarus.getRight()+"\n");
        //end
        
        //to show next box type based on the random # generator that is called when the box lands
	switch(boxType){
            case 0:
                nextBox=cardBoxImg;
                g.drawImage(nextBox, 0, mapHeight-40, this);
                break;
            case 1:
                nextBox=woodBoxImg;
                g.drawImage(nextBox, 0, mapHeight-40, this);
                break;
            case 2:
                nextBox=metalBoxImg;
                g.drawImage(nextBox, 0, mapHeight-40, this);
                break;
            case 3:
                nextBox=stoneBoxImg;
                g.drawImage(nextBox, 0, mapHeight-40, this);
                break;
            case 4:
                nextBox=meshBoxImg;
                g.drawImage(nextBox, 0, mapHeight-40, this);
                break;
        }
        for(Box b: boxList){
            b.draw(g, this);
            b.update();
            c.boxVSlazarus(lazarus, b);
            
            for(Wall w:wallList){
                c.boxVSwall(b, w);
            }
            
            if(b.isLanded()){
                switch(boxType){
                    case 0:
                        boxList.add(new Box(cardBoxImg,lazarus.getX(),-40,10,0));
                        break;
                    case 1:
                        boxList.add(new Box(woodBoxImg,lazarus.getX(),-40,10,1));
                        break;
                    case 2:
                        boxList.add(new Box(metalBoxImg,lazarus.getX(),-40,10,2));
                        break;
                    case 3:
                        boxList.add(new Box(stoneBoxImg,lazarus.getX(),-40,10,3));
                        break;
                    case 4:
                        boxList.add(new Box(meshBoxImg,lazarus.getX(),-40,10,4));
                }
                //System.out.println("Landed!");//test print
                landed=true;
                landList.add(boxList.get(boxList.indexOf(b)));
                boxList.remove(b);
                boxType = boxR.nextInt(5);
            }
        }
        
        //for box vs box collisions
        for(int a=0;a<landList.size();a++){
            c.boxVSbox(boxList.get(0), landList.get(a));
            c.boxVSlazarus(lazarus, landList.get(a));
        }
        
        //print boxes once they've landed
        for(Box b: landList){
            b.draw(g, this);
            //g.drawString(Integer.toString(landList.indexOf(b)), b.getX()+2, b.getY());//Test print
        }
       
        //wall collisions
        for(Wall w:wallList){
            c.lazarusVSwall(lazarus,w);
            g.setColor(GREEN);
            //g.drawString(Integer.toString(wallList.indexOf(w)), w.getX()+2, w.getY());//Test print
        }
        
        //for touching button
        c.lazarusVSbutton(lazarus, button1);
        c.lazarusVSbutton(lazarus, button2);
        
        frameCount++;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);             
        
        if(isGameOver==false){
            drawGame(g);     
        
            //drawing Lazarus
            lazarus.draw(g, this);
        }
        else{
            g.setColor(BLACK);
            g.fillRect(0, 0,496+96*10 , 496+96+96);
            g.setFont(new Font("Comic Sans MS", Font.BOLD,100));
            g.setColor(RED);
            g.drawString("Game Over!",640/12,480/2);
        }
        
        //clear wallList to save memory
        wallList.clear();
        lazarus.setUp(false);
        lazarus.setDown(false);
        lazarus.setLeft(false);
        lazarus.setRight(false);
    }
    
    private void initTimer(){
        //timer = new Timer(DELAY,this);
        //timer.start();
    }
    public Timer getTimer(){
        return timer;
    }
    
    public void start() {
        System.out.println();
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
     public void run() {   	
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                if(lazarus.wasSquished()){
                    thread.sleep(150);
                }
                else{
                    thread.sleep(50);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
      @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    //Initialize the game
    public static void main(String[] args){
        final LazarusGameWorld game = new LazarusGameWorld();
        game.init();
        JFrame f = new JFrame("Lazarus");
        f.addWindowListener(new WindowAdapter(){});
        f.setSize(655,525);
        f.getContentPane().add(game);
        f.setVisible(true);
        
        game.start();
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
    }

   

}
