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
public abstract class Attachment {
    protected String name;
    protected String tag;
    
    public Attachment(){
        tag = "DefaultAttachmentTag";
    }
    
    public abstract void update(long timePassed, Sprite s, float oldX, float oldY);
    
    public String getName(){
        return name;
    } 
    
    public void setName(String newName){
        this.name = newName;
    } 
    
    public String getTag(){
        return tag;
    }

}
