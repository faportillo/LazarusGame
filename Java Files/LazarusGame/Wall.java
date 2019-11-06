/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LazarusGame;
import GameEngine.*;
import java.awt.Image;
import java.awt.Rectangle;
/**
 *
 * @author FelixA
 */
public class Wall extends GameObject {
    Image wallImg;
    Rectangle wallRect;
    
    public Wall(Image img, int x, int y) {
        super(img, x, y, 0);
        wallImg=img;
        wallRect=new Rectangle(x,y,wallImg.getHeight(null),wallImg.getHeight(null));
    }
    
    public Rectangle getWallRect(){
        return wallRect;
    }
}
