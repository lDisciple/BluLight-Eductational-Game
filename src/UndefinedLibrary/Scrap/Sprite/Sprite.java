/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Sprite;

import UndefinedLibrary.Scrap.Animation;
import UndefinedLibrary.Scrap.Sprite.Attachments.Attachment;
import UndefinedLibrary.Utilities.Vector;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author JBotha
 */
public class Sprite {
    protected ArrayList<Animation> costumes;
    protected double x,y;
    protected double vx,vy;
    protected float accX,accY;
    protected short size;
    protected short drawSize;
    protected byte curCostume;
    protected ArrayList<Attachment> attachmentList;
    
    /**
     *Creates a new Sprite object.
     * @param a Default costume.
     * @param x X position
     * @param y Y position
     * @param vx X velocity
     * @param vy Y velocity
     * @param size Sprite size
     */
    public Sprite(Animation a,double x,double y,double vx,double vy,short size){
        costumes = new ArrayList();
        costumes.add(a);
        attachmentList = new ArrayList();
        curCostume = 0;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        accX = 0;
        accY = 0;
        
        this.size = size;
        drawSize = 100;
    }
    /**
     *Creates a new Sprite object.
     * Position: (0,0,0)
     * Direction: 90
     * Velocity: none
     * @param a Default animation.
     */
    public Sprite(Animation a){
        costumes = new ArrayList();
        attachmentList = new ArrayList();
        costumes.add(a);
        curCostume = 0;
        x = 0;
        y = 0;
        vx = 0;
        vy = 0;
        accX = 0;
        accY = 0;
        
        size = 100;
        drawSize = 100;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Functional">

    /**
     * Update sprite's position and properties according to time passed.
     * @param timePassed Change in time in milliseconds
     */
        public void update (long timePassed){
        int oldX,oldY;
        oldX = (int)x;
        oldY = (int)y;
        
        vx += accX*timePassed;
        vy += accY*timePassed;
        
        x += vx * timePassed;
        y += vy * timePassed;
        
        customUpdate(timePassed);
        
        for (Attachment a : attachmentList.toArray(new Attachment[0])) {
            a.update(timePassed, this, oldX, oldY);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Direction">

    /**
     *Set the direction of the sprite
     * @param dir New direction.
     */
    public void setDirection(float dir){
            
        dir = Math.abs((dir%360));
        double speed = Vector.getResultantMagnitude(vx, vy*-1);
        vx = (float) Math.cos(Math.toRadians(dir)*speed);
        vy = (float) Math.sin(Math.toRadians(dir)*speed)*-1;
    }
    
    /**
     *Change the direction of the sprite.
     * @param d Change in direction in degrees.
     */
    public void changeDirection(float d){
        d = Math.abs(((d + Vector.getVectorDirection(vx, vy*-1))%360));
        double speed = Vector.getResultantMagnitude(vx, vy*-1);
        vx = (float) Math.cos(Math.toRadians(d)*speed);
        vy = (float) Math.sin(Math.toRadians(d)*speed)*-1;
    }
    
    /**
     *Change direction of the sprite by 180 degrees.
     */
    public void bounce(){
        vx *= -1;
        vy *= -1;
    }
    
    /**
     *Get the direction of the sprite in degrees.
     * @return Direction in degrees.
     */
    public float getDirection(){
        return Vector.getVectorDirection(vx, vy*-1);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Costumes">

    /**
     *Adds a new costume to sprite's costume list.
     * @param a Animation for costume.
     */
        public void addCostume(Animation a){
        costumes.add(a);
    }
        
    /**
     *Set the costume index of list.
     * @param i index of costume list
     */
    public void setCostume(byte i){
        if (i < costumes.size() && i >= 0) {
            curCostume = i;
        }else{
            System.err.println("Invalid integer entered.");
        }
    }
    
    /**
     *Set costume index to the index of matching Animation.
     * @param a Animation to compare
     */
    public void setCostume (Animation a){
        for (int i = 0; i < costumes.size(); i++) {
            if (costumes.get(i).equals(a)) {
                curCostume = (byte)i;
            }
        }
    }
    
    /**
     *Get the amount of costumes the sprite has.
     * @return Costume amount
     */
    public int getCostumeAmount(){
        return costumes.size();
    }
    
    /**
     *Get an array of all costumes the sprite has.
     * @return Animation array of costumes.
     */
    public Animation[] getCostumeList(){
        return (Animation[])costumes.toArray();
    }
    
    /**
     *Clears the costume list and adds a single new costume.
     * @param a The single new costume.
     */
    public synchronized void resetCostumeList(Animation a){
        costumes.clear();
        costumes.add(a);
        curCostume = 0;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Positioning">

    /**
     *Get sprite's integer X position
     * Rounded down from true X position.
     * @return X position of sprite.
     */
        public int getXPosition(){
        return (int) x;
    }
    
    /**
     *Get sprite's integer Y position
     * Rounded down from true Y position.
     * @return Y position of sprite.
     */
    public int getYPosition(){
        return (int)y;
    }
    
    /**
     *Get sprite's true X position
     * @return X position of sprite.
     */
    public double getTrueXPositionF(){
        return x;
    }
    /**
     *Get sprite's true Y position
     * @return Y position of sprite.
     */
    public double getTrueYPosition(){
        return y;
    }
    
    /**
     *Sets the sprite's X position to a new value.
     * @param newX new X position
     */
    public void setXPosition(double newX){
        x = newX;
    }
    /**
     *Sets the sprite's Y position to a new value.
     * @param newY new Y position
     */
    public void setYPosition(double newY){
        y = newY;
    }
    
    /**
     *Changes the X position of the sprite.
     * @param num Change of position
     */
    public void changeXPosition(float num){
        x = num + x;
    }
    /**
     *Changes the Y position of the sprite.
     * @param num Change of position
     */
    public void changeYPosition(float num){
        y = num + y;
    }
    
    
    /**
     *Gets the sprite's current image width according to the costume index and time interval.
     * @return Sprite's current image width
     */
    public int getWidth(){
        return costumes.get(curCostume).getImage().getWidth(null);
    }
    /**
     *Gets the sprite's current image height according to the costume index and time interval.
     * @return Sprite's current image height
     */
    public int getHeight(){
        return costumes.get(curCostume).getImage().getHeight(null);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Movement">

    /**
     *Gets the sprite's X velocity.
     * @return sprite's X velocity
     */
        public double getXVelocity(){
        return vx;
    }
    /**
     *Gets the sprite's Y velocity.
     * @return sprite's Y velocity
     */
    public double getYVelocity(){
        return vy;
    }
    
    /**
     *Sets the sprite's X velocity.
     * @param xVelocity new velocity
     */
    public void setXVelocity(double xVelocity){
        vx = xVelocity;
    }
    /**
     *Sets the sprite's Y velocity.
     * @param yVelocity new velocity
     */
    public void setYVelocity(double yVelocity){
        vy = yVelocity;
    }
    
    /**
     *Sets the sprite's acceleration that increases the sprite's X velocity
     * @param acc Acceleration amount
     */
    public void setXAcceleration(float acc){
        accX = acc;
    }
    
    /**
     *Returns the sprite's x acceleration
     * @return
     */
    public float getXAcceleration(){
        return accX;
    }
    
    /**
     *Sets the sprite's acceleration that increases the sprite's Y velocity
     * @param acc Acceleration amount
     */
    public void setYAcceleration(float acc){
        accY = acc;
    }
    
    /**
     *Returns the sprite's Y acceleration
     * @return
     */
    public float getYAcceleration(){
        return accY;
    }
    
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Sizes">
    
    /**
     *Sets the size to draw the sprite
     * @param size Size to set the sprite.
     */
    public void setDrawSize(short size){
        drawSize = size;
    }
    
    /**
     *Returns the current drawSize of the sprite.
     * @return drawSize of sprite.
     */
    public short getDrawSize(){
        return drawSize;
    }
    
    
    /**
     *Sets the overall size of the sprite
     * @param size Size to set the sprite.
     */
    public void setSize(short size){
        this.size = size;
    }
    
    /**
     *Returns the current overall size of the sprite.
     * @return Size of sprite.
     */
    public short getSize(){
        return size;
    }
    
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Attachments">

    /**
     *Returns an attachment that has the corresponding name.
     * @param name Name of attachment
     * @return The attachment with that name
     */
    public Attachment getAttachment(String name){
        for (Attachment a : attachmentList) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }
        
    /**
     *Adds an attachment to the sprite.
     * If a attachment with the same name exists the method will return false and the attachment will not be added.
     * @param att The new attachment
     * @return Boolean of whether or not the attachment was added
     */
    public boolean addAttachment(Attachment att){
         boolean exists = false;
         for (Attachment a : attachmentList) {
             if (a.getName().equals(att.getName())) {
                 exists = true;
             }
         }
         if (exists) {
             return false;
         }else{
             attachmentList.add(att);
             return true;
         }
     }   
     
    /**
     *Returns a list of attachments that contain the specified tag.
     * @param tag A class specific tag to identify attachments.
     * @return List of attachments.
     */
    public Attachment[] getAttachmentsByTag(String tag){
         Attachment[] aList;
         int c = 0;
         for (Attachment a : attachmentList) {
             if (a.getTag().equals(tag)) {
                 c++;
             }
         }
         aList = new Attachment[c];
         //reset counter and add parts
         c = 0;
         for (Attachment a : attachmentList) {
             if (a.getTag().equals(tag)) {
                 aList[c] = a;
                 c++;
             }
         }
         return aList;
     }
    
    public Attachment getFirstAttachmentByTag(String tag){
        for (Attachment a : attachmentList) {
            if (a.getTag().equals(tag)) {
                return a;
            }
        }
        return null;
    }
//</editor-fold>
    
    /**
     *Gets the sprite's current image.
     * @return Sprite's current image.
     */
    public Image getSpriteImage(){
            Image img = costumes.get(curCostume).getImage();
            int newHeight = (int) (img.getHeight(null)*(drawSize/100d));
            int newWidth = (int) (img.getWidth(null)*(drawSize/100d));
        if (newHeight <= 0 || newWidth <= 0) {
            return null;
        }else{
            img = resizeImage(img, newWidth, newHeight, Image.SCALE_FAST);

            return img;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Utilities">
    private Image resizeImage(Image img, int width, int height, int type){
        BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImg.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();
        return newImg;
    }
    
    public Sprite clone(){
        Sprite newSprite = new Sprite(costumes.get(0),x,y,vx,vy,size);
        for (int i = 1; i < costumes.size(); i++) {
            newSprite.addCostume(costumes.get(i));
        }
        newSprite.setCostume(curCostume);
        
        for (Attachment att : attachmentList) {
            newSprite.addAttachment(att);
        }
        return newSprite;
    }
//</editor-fold>

    /**
     *User-coded update method.
     * @param timePassed Time in milliseconds passed
     */
    protected void customUpdate(long timePassed){}
}
