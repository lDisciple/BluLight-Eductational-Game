/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Components;

import java.awt.Image;
import java.util.HashMap;

/**
 *
 * @author Jonathan Botha
 */
public abstract class ToggleButton implements Clickable{
    private boolean isVisible;
    private boolean toggled;
    
    private final HashMap<Byte,Image> imageMap;
    protected byte status;
    public byte BTN_NORMAL = 0;
    public byte BTN_TOGGLED = 1;
    protected int x,y;
    
    public ToggleButton(Image img, int xPos, int yPos, boolean isShowing){
        imageMap = new HashMap();
        imageMap.put((byte)-1,null);
        imageMap.put((byte)0,img);
        imageMap.put((byte)1,img);
        
        x = xPos;
        y = yPos;
        
        isVisible = isShowing;
        toggled = false;
        status = 0;
    }
    
    /**
     *Checks if the co-ordinates are touching the button
     * @param x X co-ordinate
     * @param y Y co-ordinate
     * @return Whether the button is touching the co-ordinates.
     */
    public boolean inBounds(int x,int y){
        return this.x < x && x < this.x + getWidth() && this.y < y && y < this.y + getHeight();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Image">
    /**
     *Gets buttons current image based on its status.
     * @return Button image
     */
    public Image getImage(){
        if (isVisible) {
            return imageMap.get(status);
        }else{
            return imageMap.get((byte)-1);
        }
    }
    /**
     *Sets the image that will appear when the button is toggled.
     * @param img Clicked image.
     */
    public void setToggledImage(Image img){
        imageMap.put((byte)1, img);
    }
    
    /**
     *Gets the image that appears when the button is toggled.
     * @return Clicked image.
     */
    public Image getToggledImage(){
        return imageMap.get((byte)1);
    }
    /**
     *Sets the image that will appear as default.
     * @param img Default image.
     */
    public void setDefaultImage(Image img){
        imageMap.put((byte)0, img);
    }
    
    /**
     *Gets the image that appears as default.
     * @return Default image.
     */
    public Image getDefaultImage(){
        return imageMap.get((byte)0);
    }
    
//</editor-fold>
    
        /**
     *Sets button's visibility.
     * @param b Button visibility
     * */
    public void setVisible(boolean b){
        isVisible = b;
    }
    
    /**
     *Checks if button is visible.
     * @return Button visibility
     */
    public boolean isVisible(){
        return isVisible;
    }
    
    /**
     *Sets whether the object is toggled.
     * @param  newToggled new toggled
     * */
public void setToggled (boolean newToggled){
	toggled = newToggled;
        if (toggled) {
            status = 1;
            toggleEvent();
        }else{
            status = 0;
            untoggleEvent();
        }
}

/**
     *Gets whether the object is toggled.
     * @return  The object's toggled.
     * */
public boolean isToggled (){
	return toggled;
}
    
    protected abstract  void toggleEvent();
    protected abstract  void untoggleEvent();
    
    public void setStatus(byte s){
        status = s;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Positioning">

    /**
     *Get button's integer X position
     * @return X position of sprite.
     */
        public int getXPosition(){
        return  x;
    }
    
    /**
     *Get button's integer Y position
     * @return Y position of sprite.
     */
    public int getYPosition(){
        return y;
    }
    
    /**
     *Sets the button's X position to a new value.
     * @param newX new X position
     */
    public void setXPosition(int newX){
        x = newX;
    }
    /**
     *Sets the button's Y position to a new value.
     * @param newY new Y position
     */
    public void setYPosition(int newY){
        y = newY;
    }
    
    
    /**
     *Gets the button's current image width according to the costume index and time interval.
     * @return button's current image width
     */
    public int getWidth(){
        return imageMap.get(status).getWidth(null);
    }
    /**
     *Gets the button's current image height according to the costume index and time interval.
     * @return button's current image height
     */    
    public int getHeight(){
        return imageMap.get(status).getHeight(null);
    }
//</editor-fold>

    @Override
    public void mouseClicked(int mX,int mY) {
        if (inBounds(mX,mY) && isVisible) {
            toggled = !toggled;
            if (toggled) {
                status = 1;
                toggleEvent();
            }else{
                status = 0;
                untoggleEvent();
            }
        }
    }

    @Override
    public void mouseMoved(int mX,int mY) {
    }

    @Override
    public void mouseReleased(int mX, int mY) {
    }
    
    

}
