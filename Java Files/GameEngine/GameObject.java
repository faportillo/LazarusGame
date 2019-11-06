package GameEngine;


/*
    SHOULD HAVE MADE A GAME OBJECT INTERFACE INSTEAD SINCE
    I REUSE THE SAME METHOD NAMES BUT DOESN't HAVE THE SAME CODE

*/
/*
    Moved convertToBuffered and split into this class for better
    reusability
*/

import java.awt.*;
import java.util.*;
import java.awt.image.*;
/**
 *
 * @author FelixA
 */
public class GameObject{
    public int x, y, width, height, Yspeed;
 
    protected Image img;

    
    public GameObject(Image img, int x, int y, int Yspeed){
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        this.Yspeed = Yspeed;
    }

    private BufferedImage convertToBuffered(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        
        BufferedImage bimg = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
              
        Graphics2D g2 = bimg.createGraphics();
        
        g2.drawImage(img,0,0,null);
        g2.dispose();
        //System.out.println("Here 0");//Test Print
        return bimg;
    }
    
    public void split(Image img, Image[] iArray){
        try{
            BufferedImage buffPic;
            //System.out.println("Here 0");//Test Print
            buffPic=this.convertToBuffered(img);
            int width = buffPic.getHeight(); //width is same as height to get square 
            for(int i=0,j=0;i<buffPic.getWidth();i+=buffPic.getHeight(),j++){
                iArray[j]=buffPic.getSubimage(i,0,buffPic.getHeight(),buffPic.getHeight());                
            }
        }catch(Exception e){
            System.out.println(e+ ": Problem in Lazarus Class split image");
        }
    }
    
    public int getX(){
        return this.x;
    }
   
    public int getY(){
        return this.y;
    }
 
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }

    public void setX(int a){
        this.x = a;
   }

    public void setY(int b){
        this.y = b;
    }

     public void draw(Graphics g, ImageObserver obs){
         g.drawImage(img, x, y, obs); 
    }
    
    public int getSpeed(){
        return Yspeed;
    }
     
     
}
