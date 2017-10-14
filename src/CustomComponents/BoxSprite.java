/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomComponents;

import UndefinedLibrary.Scrap.Animation;
import UndefinedLibrary.Scrap.Sprite.Attachments.Attachment;
import UndefinedLibrary.Scrap.Sprite.Sprite;
import java.awt.Point;

/**
 *
 * @author Jonathan Botha
 */
public class BoxSprite extends Sprite{
    private Point ptTarget;
    private double targetX;
    private int minBound,maxBound;
    
    public BoxSprite(Animation a, double x, double y, double vx, double vy, short size) {
        super(a, x, y, vx, vy, size);
    }
    
    public BoxSprite(Animation a) {
        super(a);
    }

    public void update(long timePassed){
        
        double oldX,oldY;
        oldX = x;
        oldY = y;
        for (int i = 0; i < timePassed; i++) {//Very accurate method
            vx += accX;
            vy += accY;
            
            x += vx;
            y += vy;
            if (y <= ptTarget.y) {
                targetX = x;
            }
            if (x <= minBound) {
                x = minBound;
                vx = 0;
            }
            if (x >= maxBound) {
                x = maxBound;
                vx = 0;
            }
        }
        
        customUpdate(timePassed);
        
        for (Attachment a : attachmentList) {
            a.update(timePassed, this, (float)oldX, (float)oldY);
        }
    }
    
    public void setTargetPoint(Point p){
        ptTarget = p;
    }
    
    public double getTargetX(){
        return targetX;
    }
        /**
         *Sets the object's minBound.
         * @param  newMinBound new minBound
         * */
    public void setMinBound (int newMinBound){
            minBound = newMinBound;
    }

    /**
         *Gets the object's minBound.
         * @return  The object's minBound.
         * */
    public int getMinBound (){
            return minBound;
    }
    
    /**
     *Sets the object's maxBound.
     * @param  newMaxBound new maxBound
     * */
    public void setMaxBound (int newMaxBound){
            maxBound = newMaxBound;
    }

    /**
         *Gets the object's maxBound.
         * @return  The object's maxBound.
         * */
    public int getMaxBound (){
            return maxBound;
    }
}
