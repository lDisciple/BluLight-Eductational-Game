/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomComponents;

import UndefinedLibrary.Scrap.Components.Clickable;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jonathan Botha
 */
public class BasicTextfield implements Clickable{
    protected int x,y;
    private boolean visible;
    private boolean focused,editable;
    private Image img;
    private int rightOffset, leftOffset, topOffset, bottomOffset;
    protected String text;
    private Font f;

    
    public BasicTextfield(int xPos, int yPos,Image i,Font font,int rightTextOffset,int leftTextOffset,int topTextOffset,int bottomTextOffset){
        x = xPos;
        y = yPos;
        img = i;
        f = font;
        rightOffset = rightTextOffset;
        leftOffset = leftTextOffset;
        topOffset = topTextOffset;
        bottomOffset = bottomTextOffset;
        text = "";
        visible = true;
        editable = true;
    }    
    
    public BasicTextfield(int xPos, int yPos,Image i,Font font){
        x = xPos;
        y = yPos;
        img = i;
        f = font;
        rightOffset = 0;
        leftOffset = 0;
        topOffset = 0;
        bottomOffset = 0;
        text = "";
        visible = true;
        editable = true;
    }
    
    public void append(String in){
        text += in;
    }
    
    public void typed(char c){
        if ( editable && focused) {
            append(""+c);
        }
    }
    
    public void backspace(){
        if (text.length() > 0) {
            text = text.substring(0, text.length()-1);
        }
    }
    
    public void setText(String t){
        text = t;
    }
    
    public String getText(){
        return text;
    }
    
    /**
     *Checks if the co-ordinates are touching the textfield
     * @param x X co-ordinate
     * @param y Y co-ordinate
     * @return Whether the textfield is touching the co-ordinates.
     */
    public boolean inBounds(int x,int y){
        if (visible) {
            return this.x < x && x < this.x + getWidth() && this.y < y && y < this.y + getHeight();
        }else{
            return false;
        }
    }
    
        /**
     *Checks if textfield is visible.
     * @return Button visibility
     */
    public boolean isVisible(){
        return visible;
    }
    
            /**
     *Sets textfield's visibility.
     * @param b Button visibility
     * */
    public void setVisible(boolean b){
        visible = b;
    }
    
    /**
     *Sets whether the object is focused.
     * @param  newFocused new focused
     * */
public void setFocused (boolean newFocused){
	focused = newFocused;
}

/**
     *Gets whether the object is focused.
     * @return  The object's focused.
     * */
public boolean isFocused (){
	return focused;
}
    
    /**
     *Sets the object's font.
     * @param  newFont new font
     * */
public void setFont (Font newFont){
	f = newFont;
}

/**
     *Gets the object's font.
     * @return  The object's font.
     * */
    public Font getFont (){
            return f;
    }
    
    /**
     *Sets the object's rightOffset.
     * @param  newRightOffset new rightOffset
     * */
public void setRightOffset (int newRightOffset){
	rightOffset = newRightOffset;
	}

/**
     *Gets the object's rightOffset.
     * @return  The object's rightOffset.
     * */
public int getRightOffset (){
	return rightOffset;
}

/**
     *Sets the object's leftOffset.
     * @param  newLeftOffset new leftOffset
     * */
public void setLeftOffset (int newLeftOffset){
	leftOffset = newLeftOffset;
	}

/**
     *Gets the object's leftOffset.
     * @return  The object's leftOffset.
     * */
public int getLeftOffset (){
	return leftOffset;
}

/**
     *Sets the object's topOffset.
     * @param  newTopOffset new topOffset
     * */
public void setTopOffset (int newTopOffset){
	topOffset = newTopOffset;
	}

/**
     *Gets the object's topOffset.
     * @return  The object's topOffset.
     * */
public int getTopOffset (){
	return topOffset;
}

/**
     *Sets the object's bottomOffset.
     * @param  newBottomOffset new bottomOffset
     * */
public void setBottomOffset (int newBottomOffset){
	bottomOffset = newBottomOffset;
	}

/**
     *Gets the object's bottomOffset.
     * @return  The object's bottomOffset.
     * */
public int getBottomOffset (){
	return bottomOffset;
}

/**
     *Sets whether the object is editable.
     * @param  newEditable new editable
     * */
public void setEditable (boolean newEditable){
	editable = newEditable;
}

/**
     *Gets whether the object is editable.
     * @return  The object's editable.
     * */
public boolean isEditable (){
	return editable;
}
    
    /**
     *Gets textfield's current image based on its status.
     * @return Button image
     */
    public Image getImage(){
        BufferedImage image = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) image.createGraphics();
        g.drawImage(img,0,0,null);
        
        BufferedImage textImage = new BufferedImage(img.getWidth(null)- rightOffset - leftOffset,img.getHeight(null) - topOffset - bottomOffset,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gt = (Graphics2D) textImage.createGraphics();
        gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gt.setFont(f);
        FontMetrics fm = gt.getFontMetrics();
        int textWidth = (int)fm.getStringBounds(text, g).getWidth();
        if (textWidth > img.getWidth(null)-rightOffset - leftOffset) {
            gt.drawString(text, (textWidth - img.getWidth(null))*-1, fm.getAscent());
        }else{
            gt.drawString(text, 0, fm.getAscent());
        }
        gt.dispose();
        g.drawImage(textImage, leftOffset, topOffset, null);
        textImage.flush();
        g.dispose();
        return image;
    }
    
    /**
     *Sets the textfield's current image based on its status.
     * @param i New image
     */
    public void setImage(Image i){
        img = i;
    }
    
        //<editor-fold defaultstate="collapsed" desc="Positioning">

    /**
     *Get textfield's integer X position
     * @return X position of field.
     */
        public int getXPosition(){
        return  x;
    }
    
    /**
     *Get textfield's integer Y position
     * @return Y position of field.
     */
    public int getYPosition(){
        return y;
    }
    
    /**
     *Sets the textfield's X position to a new value.
     * @param newX new X position
     */
    public void setXPosition(int newX){
        x = newX;
    }
    /**
     *Sets the textfield's Y position to a new value.
     * @param newY new Y position
     */
    public void setYPosition(int newY){
        y = newY;
    }
    
    
    /**
     *Gets the textfield's current image width according to the time interval.
     * @return textfield's current image width
     */
    public int getWidth(){
        return img.getWidth(null);
    }
    /**
     *Gets the textfield's current image height according to the time interval.
     * @return textfield's current image height
     */    
    public int getHeight(){
        return img.getHeight(null);
    }
//</editor-fold>

    
    @Override
    public void mouseClicked(int mX, int mY) {
        focused = inBounds(mX,mY);
    }

    @Override
    public void mouseReleased(int mX, int mY) {
        focused = inBounds(mX,mY);
    }

    @Override
    public void mouseMoved(int mX, int mY) {
    }
}
    