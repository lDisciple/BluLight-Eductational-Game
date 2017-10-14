/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap;

import UndefinedLibrary.Utilities.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author Jonathan Botha
 */
public class Pen {
    protected Graphics2D penGraphics;
    protected Color color;
    protected short penSize;
    protected boolean penDown;
    protected int x;
    protected int y;
    
    /**
     *Creates a pen.
     * @param g Graphics for pen to use.
     * @param xPos X position of pen
     * @param yPos Y position of pen
     * @param size Size of pen
     * @param color Color of pen
     */
    public Pen(Graphics2D g, int xPos, int yPos, int size, Color color){
        this.penSize = (short)size;
        penGraphics = g;
        this.color = color;
        x = xPos;
        y = yPos;
    }
    /**
     *Creates a pen.
     * @param g Graphics for pen to use.
     * @param xPos X position of pen
     * @param yPos Y position of pen
     * @param size Size of pen
     */
    public Pen(Graphics2D g, int xPos, int yPos, int size){
        this(g, size, xPos, yPos, Color.black);
    }
    /**
     *Creates a pen.
     * @param g Graphics for pen to use.
     * @param xPos X position of pen
     * @param yPos Y position of pen
     */
    public Pen(Graphics2D g,int xPos, int yPos){
        this(g, xPos, yPos, 1, Color.black);
    }
    
    /**
     *Updates the pen to a new position.
     * @param newX new X position
     * @param newY new Y position
     */
        public void update(int newX,int newY){
        update(newX,newY,Vector.getDirectionOfLine(x,y*-1,newX,newY*-1));
    }
    /**
     *Updates the pen to a new position with direction included.
     * Direction must correspond with the change in coordinates.
     * @param newX new X position
     * @param newY new Y position
     * @param direction direction of pen
     */
    public void update(int newX,int newY, float direction){
            int oldX = x;
            int oldY = y;
            x = newX;
            y = newY;
        if (penDown && penGraphics != null) {
            penGraphics.setColor(color);
            
            int p1x = (int) ((Math.cos(Math.toRadians(direction +90))*penSize));
            int p1y = (int) ((Math.sin(Math.toRadians(direction +90))*penSize))*-1;
            int p2x = (int) ((Math.cos(Math.toRadians(direction -90))*penSize));
            int p2y = (int) ((Math.sin(Math.toRadians(direction -90))*penSize))*-1;
            
            penGraphics.fillPolygon(
                new int[]{oldX+p1x, oldX+p2x, (int)x+p2x, (int)x+p1x}, 
                new int[]{oldY+p1y, oldY+p2y, (int)y+p2y, (int)y+p1y},
                4);
        }
    }
        
    /**
     *Set a new graphics for the Pen
     * @param g new Graphics2D
     */
    public void setGraphics(Graphics2D g){
        penGraphics = g;
    }
    
    /**
     *Gets the pen's current graphics.
     * @return Pen's graphics
     */
    public Graphics2D getGraphics(){
        return penGraphics;
    }
    
    /**
     *Set the pen's color
     * @param c New color
     */
    public void setColor (Color c){
        color = c;
    }
    
    /**
     *Gets the pen's current color.
     * @return Pen's color
     */
    public Color getColor(){
        return color;
    }
    
    /**
     *Set the pen's size.
     * @param size New size
     */
    public void setSize(short size){
        this.penSize = size;
    }
    /**
     *Gets the pen's current size.
     * @return Pen's size
     */
    public short getSize(){
        return penSize;
    }
    
    /**
     *Changes the pen's current size.
     * @param num delta/change of value
     */
    public void changeSize(short num){
        penSize += num;
    }
    
    /**
     *Sets the status of the pen.
     * If pen is down then it will draw.
     * @param b penDown
     */
    public void setPenDown (boolean b){
        penDown = b;
    }
    
    /**
     *Get whether the pen is down or not.
     * @return pen status
     */
    public boolean isPenDown(){
        return penDown;
    }
    
    /**
     *Pen draws the image at its current position.
     * Image is drawn from top-left corner.
     * @param img Image to stamp
     */
    public void Stamp(Image img){
        penGraphics.drawImage(img, (int)x, (int)y, null);
    }
    
    /**
     *Change the blue component of the pen's RGB color.
     * @param change Delta/change of value
     */
    public void changeBlue(short change){
        if (color.getBlue() + change > 255 || color.getBlue() + change < 0) {
            if (color.getBlue() + change > 255) {
                color = new Color(color.getRed(),
                        color.getGreen(),
                        255,
                        color.getAlpha());
            }else{
                color = new Color(color.getRed(),
                        color.getGreen(),
                        0,
                        color.getAlpha());
            }
        }else{
            color = new Color(color.getRed(),
                    color.getGreen(),
                    color.getBlue() + change,
                    color.getAlpha());
        }
    }
    /**
     *Change the red component of the pen's RGBA color.
     * @param change Delta/change of value
     */
    public void changeRed(short change){
        if (color.getRed() + change > 255 || color.getRed() + change < 0) {
            if (color.getRed() + change > 255) {
                color = new Color(255,
                        color.getGreen(),
                        color.getBlue(),
                        color.getAlpha());
            }else{
                color = new Color(0,
                        color.getGreen(),
                        color.getBlue(),
                        color.getAlpha());
            }
        }else{
            color = new Color(color.getRed() + change,
                    color.getGreen(),
                    color.getBlue(),
                    color.getAlpha());
        }
    }
    /**
     *Change the green component of the pen's RGBA color.
     * @param change Delta/change of value
     */
    public void changeGreen(short change){
        if (color.getGreen() + change > 255 || color.getGreen() + change < 0) {
            if (color.getGreen() + change > 255) {
                color = new Color(color.getRed(),
                        255,
                        color.getBlue(),
                        color.getAlpha());
            }else{
                color = new Color(color.getRed(),
                        0,
                        color.getBlue(),
                        color.getAlpha());
            }
        }else{
            color = new Color(color.getRed(),
                    color.getGreen()+change,
                    color.getBlue(),
                    color.getAlpha());
        }
    }
    /**
     *Change the alpha component of the pen's RGBA color.
     * @param change Delta/change of value
     */
    public void changeAlpha(short change){
        if (color.getAlpha() + change > 255 || color.getAlpha() + change < 0) {
            if (color.getAlpha() + change > 255) {
                color = new Color(color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        255);
            }else{
                color = new Color(color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        0);
            }
        }else{
            color = new Color(color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    color.getAlpha() + change);
        }
    }
    /**
     *Change the shade component of the pen's HSB color.
     * @param change Delta/change of value
     */
    public void changeShade(float change){
        short a = (short) color.getAlpha();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
        hsb[2] += change;
        while(hsb[2] > 1 || hsb[2] < 0){
            if (hsb[2] > 1) {
                hsb[2] -= 1;
            }else{
                hsb[2] += 1;
            }
        }
        color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        setAlpha(a);
    }
    /**
     *Change the saturation component of the pen's HSB color.
     * @param change Delta/change of value
     */
    public void changeSaturation(float change){
        short a = (short) color.getAlpha();
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
        hsb[1] += change;
        while(hsb[1] > 1 || hsb[1] < 0){
            if (hsb[1] > 1) {
                hsb[1] -= 1;
            }else{
                hsb[1] += 1;
            }
        }
        color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        setSaturation(a);
    }
    /**
     *Set the red component of the pen's RGBA color to a new value.
     * @param r new value
     */
    public void setRed(short r){
        color = new Color(r%256,
            color.getGreen(),
            color.getBlue(),
            color.getAlpha());
    }
    /**
     *Set the green component of the pen's RGBA color to a new value.
     * @param g new value
     */
    public void setGreen(short g){
        color = new Color(color.getRed(),
            g%256,
            color.getBlue(),
            color.getAlpha());
    }
    /**
     *Set the blue component of the pen's RGBA color to a new value.
     * @param b new value
     */
    public void setBlue(short b){
        color = new Color(color.getRed(),
            color.getGreen(),
            b%256,
            color.getAlpha());
    }
    /**
     *Set the alpha component of the pen's RGBA color to a new value.
     * @param a new value
     */
    public void setAlpha(short a){
        color = new Color(color.getRed(),
            color.getGreen(),
            color.getBlue(),
            a%256);
    }
    /**
     *Set the shade component of the pen's HSB color to a new value.
     * @param value new value
     */
    public void setShade(float value){
        short a = (short) color.getAlpha();
        while(value > 1 || value < 0){
            if (value > 1) {
                value -= 1;
            }else{
                value += 1;
            }
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
        hsb[2] = value %1.0000000001f;
        color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        setAlpha(a);
    }
    /**
     *Set the saturation component of the pen's HSB color to a new value.
     * @param value new value
     */
    public void setSaturation(float value){
        short a = (short) color.getAlpha();
        while(value > 1 || value < 0){
            if (value > 1) {
                value -= 1;
            }else{
                value += 1;
            }
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
        hsb[1] = value %1.0000000001f;
        color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        setAlpha(a);
    }
    
    /**
     *Draws a line from one point to another. Pen size is half the amount of pixels used.
     * @param g Graphics to be used
     * @param x1 X position of first point
     * @param y1 Y position of first point
     * @param x2 X position of second point
     * @param y2 Y position of second point
     * @param penSize Size of line
     */
    public static void drawLine(Graphics2D g, int x1, int y1, int x2, int y2,int penSize){
           
            float direction = Vector.getDirectionOfLine(x1,y1,x2,y2);
            
            int p1x = (int) ((Math.cos(Math.toRadians(direction +90))*penSize));
            int p1y = (int) ((Math.sin(Math.toRadians(direction +90))*penSize))*-1;
            int p2x = (int) ((Math.cos(Math.toRadians(direction -90))*penSize));
            int p2y = (int) ((Math.sin(Math.toRadians(direction -90))*penSize))*-1;
            
            g.fillPolygon(
                new int[]{x1+p1x, x1+p2x, (int)x2+p2x, (int)x2+p1x}, 
                new int[]{y1+p1y, y1+p2y, (int)y2+p2y, (int)y2+p1y},
                4);
    }
}
