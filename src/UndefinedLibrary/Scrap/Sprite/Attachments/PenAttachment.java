/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Sprite.Attachments;


import UndefinedLibrary.Scrap.Pen;
import UndefinedLibrary.Scrap.Sprite.Sprite;

/**
 *
 * @author Jonathan Botha
 */
public class PenAttachment extends Attachment{
    private Pen pen;
    
    public PenAttachment(String name, Pen pen){
        this.name = name;
        this.tag = "Pen";
        
        this.pen = pen;
    }
    
    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {
        pen.update(s.getXPosition(), s.getYPosition());
    }
    
    /**
    *Gets the sprite's current pen
    * @return Sprite's pen.
    */
    public Pen getPen(){
        return pen;
    }
    /**
     *Sets the sprite's current pen.
     * @param p New Pen
     */
    public void setPen(Pen p){
        pen = p;
    }
}
