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
public class DepthAttachment extends Attachment{
    private float z,vz;
    private int minDepth,maxDepth;
    
    /**
     *Creates a depth attachment for the sprite class which creates a z-axis for the sprite.
     * Tag: Depth
     * @param name Name of attachment
     * @param z Starting Z position
     * @param vz Starting Z velocity
     * @param minDepth Minimum Z Value
     * @param maxDepth Maximum Z Value
     */
    public DepthAttachment(String name, float z, float vz, int minDepth, int maxDepth ){
        this.name = name;
        this.tag = "Depth";
        
        this.z = z;
        this.vz = vz;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
        
    }
    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {
        //updates sprites z position
        if ((z+vz * timePassed) > minDepth && (z+vz * timePassed)< maxDepth) {
            z += vz * timePassed;
        }else{
            if ((z+vz * timePassed) < minDepth) {
                z = minDepth;
            }else{
                z = maxDepth;
            }
        }
        //Work out draw size of sprite
        short drawSize;
        if (!(minDepth >= maxDepth)) {
            //drawSize = (short)((size/100d)*(z/maxDepth-minDepth));
            if (z < (maxDepth+minDepth)/2) {
                drawSize = (short)(((s.getSize()/100d)*((minDepth - z)/minDepth))*100);
                drawSize = (short) ((drawSize/2) +50);
            }else{
                drawSize = (short)(((s.getSize()/100d)*(1+((z)/maxDepth)))*100);
            }
        }else{
            drawSize = s.getSize();
        }
        s.setDrawSize(drawSize);
    }
    
    /**
     *Get sprite's true Z position
     * @return Z position of sprite.
     */
    public float getTrueZPosition(){
        return z;
    }
    /**
     *Get sprite's integer Z position
     * Rounded down from true Z position.
     * @return Z position of sprite.
     */
    public int getZPosition(){
        return (int)z;
    }
    
    /**
     *Changes the Z position of the sprite.
     * @param num Change of position
     */
    public void changeZPosition(float num){
        z = num + z;
    }
    
    /**
     *Sets the sprite's Z position to a new value.
     * @param newZ new Z position
     */
    public void setZPosition(float newZ){
        z = newZ;
    }
    
    /**
     *Sets the sprite's Z velocity.
     * @param zVelocity new velocity
     */
    public void setZVelocity(float zVelocity){
        vz = zVelocity;
    }
    
    /**
     *Gets the sprite's Z velocity.
     * @return sprite's Z velocity
     */
    public float getZVelocity(){
        return vz;
    }
    
    /**
     *Inverts the Z velocity
     */
    public void bounce(){
        vz *= -1;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Depth">

    /**
     *Sets the maximum and minimum Z values.
     * @param min minimum Z value
     * @param max maximum Z value
     */
        public void setDepth(int min,int max){
        minDepth = min;
        maxDepth = max;
    }
    /**
     *Gets the minimum Z value of the sprite.
     * @return minimum Z value
     */
    public int getMinDepth(){
        return minDepth;
    } 
    /**
     *Gets the maximum Z value of the sprite.
     * @return maximum Z value
     */
    public int getMaxDepth(){
        return maxDepth;
    } 
//</editor-fold>
}
