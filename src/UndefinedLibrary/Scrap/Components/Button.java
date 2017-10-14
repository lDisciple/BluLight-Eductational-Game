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
public abstract class Button implements Clickable{
    private boolean isVisible;
    
    private final HashMap<Byte,Image> imageMap;
    protected byte status;
    public byte BTN_NORMAL = 0;
    public byte BTN_CLICKED = 1;
    public byte BTN_HOVER = 2;
    protected int x,y;
    
    public Button(Image img, int xPos, int yPos, boolean isShowing){
        imageMap = new HashMap();
        imageMap.put((byte)-1,null);
        imageMap.put((byte)0,img);
        imageMap.put((byte)1,img);
        imageMap.put((byte)2,img);
        
        x = xPos;
        y = yPos;
        
        isVisible = isShowing;
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
        return imageMap.get(status);
    }
    
    /**
     *Sets the image that will appear when the mouse hovers over the button.
     * @param img Hover image.
     */
    public void setHoverImage(Image img){
        imageMap.put((byte)2, img);
    }
    
    /**
     *Gets the image that appears when the mouse hovers over the button.
     * @return Hover image.
     */
    public Image getHoverImage(){
        return imageMap.get((byte)2);
    }
    /**
     *Sets the image that will appear when the button is clicked.
     * @param img Clicked image.
     */
    public void setClickedImage(Image img){
        imageMap.put((byte)1, img);
    }
    
    /**
     *Gets the image that appears when the button is clicked.
     * @return Clicked image.
     */
    public Image getClickedImage(){
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
    
    protected abstract  void clickEvent();
    
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
            status = 1;
            clickEvent();
        }else{
            status = 0;
        }
    }

    @Override
    public void mouseMoved(int mX,int mY) {
        if (inBounds(mX,mY) && isVisible) {
            status = 2;
        }else{
            status = 0;
        }
    }

    @Override
    public void mouseReleased(int mX, int mY) {
        if (inBounds(mX,mY) && isVisible) {
            status = 2;
        }else{
            status = 0;
        }
    }
    
    

}
