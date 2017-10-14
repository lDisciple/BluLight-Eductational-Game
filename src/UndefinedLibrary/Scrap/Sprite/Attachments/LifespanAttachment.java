/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Sprite.Attachments;

import UndefinedLibrary.Scrap.Sprite.Sprite;

/**
 *
 * @author Jonathan Botha
 */
public class LifespanAttachment extends Attachment{
    private long time;
    
    public LifespanAttachment(String name, int lifeTime){
        this.name = name;
        this.tag = "Lifespan";
        
        time = lifeTime;
    }

    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {
        time -= timePassed;
    }
    
    /**
     *Gets the time remaining on the Sprite.
     * @return the time in milliseconds.
     */
    public long getTimeRemaining(){
        return time;
    }
    
    /**
     *Add time to the Sprite's remaining lifespan.
     * @param addTime The time to add.
     */
    public void addTime(long addTime){
        time+= addTime;
    }
  
    /**
     *Set the time of the Sprite's remaining lifespan.
     * @param newTime The new time.
     */
    public void setTime(long newTime){
        time = newTime;
    }
    
    public boolean isDead(){
        return time <= 0;
    }
}
