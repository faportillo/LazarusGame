
package LazarusGame;

import GameEngine.*;
import java.awt.Image;
import java.awt.Rectangle;

public class Button extends GameObject{
    private Image buttonImg;
    
    public Button(Image img, int x, int y) {
        super(img, x, y,0);
        try{
            buttonImg=img;
    
            
        }catch(Exception e){
            System.out.println(e+": Error in wall class");
        }
    }
}
