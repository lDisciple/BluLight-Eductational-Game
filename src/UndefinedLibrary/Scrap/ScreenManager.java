/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author JBotha
 */
public class ScreenManager {
    private GraphicsDevice vc;
    
    /**
     *Creates a new ScreenManager Object
     * @param gd Usable screen device
     */
    public ScreenManager(GraphicsDevice gd){
        vc = gd;
    }
    /**
     *Creates a new ScreenManager Object
     * Uses default screen device.
     */
    public ScreenManager(){
        this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
    }
    
    /**
     *Gets current screen device's compatible display modes.
     * @return Compatible display modes.
     */
    public DisplayMode[] getcompatibleDisplayModes(){
        return vc.getDisplayModes();
    }
    
    /**
     *Gets current screen device's compatible display modes.
     * @return Compatible display modes.
     */
    public static DisplayMode[] getDefaultCompatibleDisplayModes(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();
    }
    
    /**
     *Gets current screen device's compatible display modes.
     * @return Compatible display modes.
     */
    public static DisplayMode getDefaultDisplayMode(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    }
    
    /**
     *Compares inputted display mode to compatible display modes.
     * @param modes Inputted display mode array
     * @return First compatible display mode
     */
    public DisplayMode findFirstDisplayCompatibleMode(DisplayMode[] modes){
        DisplayMode[] goodModes = vc.getDisplayModes();
        for (DisplayMode mode : modes) {
            for (DisplayMode goodMode : goodModes) {
                if (displayModesMatch(mode, goodMode)) {
                    return mode;
                }
            }
        }
        return null;
    }

    /**
     *Gets the current display mode.
     * @return current display mode
     */
    public DisplayMode getCurrentDisplayMode (){
        return vc.getDisplayMode();
    }
    
    private boolean displayModesMatch(DisplayMode dm1, DisplayMode dm2) {
        if (dm1.getWidth() != dm2.getWidth() || dm1.getHeight() != dm2.getHeight()) {
            return false;
        }
        if (dm1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && dm2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && dm1.getBitDepth() != dm2.getBitDepth()) {
            return false;
        }
        return !(dm1.getRefreshRate() != dm2.getRefreshRate() && dm1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN&& dm2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN);
    }
    
    /**
     *Sets frame fullscreen
     * @param dm display mode to use.
     */
    public void setFullscreen(DisplayMode dm){
        JFrame f = new JFrame();
        f.setUndecorated(true);
        f.setIgnoreRepaint(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);
        
        if (dm != null && vc.isDisplayChangeSupported()) {
            try{
                vc.setDisplayMode(dm);
            }catch(Exception e){
                
            }
        }
        
        f.createBufferStrategy(2);
        
    }
    /**
     *Sets frame fullscreen
     * @param dm display mode to use
     * @param f Frame to be used
     */
    public void setFullscreen(DisplayMode dm, JFrame f){
        f.setUndecorated(true);
        f.setIgnoreRepaint(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);
        
        if (dm != null && vc.isDisplayChangeSupported()) {
            try{
                vc.setDisplayMode(dm);
            }catch(Exception e){
                
            }
        }
        Window w = vc.getFullScreenWindow();
        w.createBufferStrategy(2);
        
    }
    
    /**
     *Get the current frame's graphics.
     * @return graphics of frame
     */
    public Graphics2D getGraphics(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            return (Graphics2D) s.getDrawGraphics();
        }else{
            return null;
        }
    }
    
    /**
     *Updates the buffer.
     */
    public void update(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            if (!s.contentsLost()) {
                s.show();
            }
        }
    }
    
    /**
     *Gets the window that is fullscreen.
     * @return fullscreen window
     */
    public Window getFullscreenWindow(){
        return vc.getFullScreenWindow();
    }
    
    /**
     *Gets the fullscreen window's width
     * @return window's width
     */
    public int getWidth(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getWidth();
        }else{
            return 0;
        }
    }
    /**
     *Gets the fullscreen window's height
     * @return window's height
     */
    public int getHeight(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getHeight();
        }else{
            return 0;
        }
    }
    
    /**
     *Restores screen to windowed mode.
     */
    public void restoreScreen(){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        vc.setFullScreenWindow(null);
    }
    
//create compatible image

    /**
     *Creates a compatible image with the fullscreen window.
     * @param width width of the image
     * @param height height of the image
     * @param transparency transparency of the image
     * @return Compatible image
     */
        public BufferedImage createCompatibleImage(int width, int height, int transparency){
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            GraphicsConfiguration gc = w.getGraphicsConfiguration();
            return gc.createCompatibleImage(width, height, transparency);
        }else{
            return null;
        }
    }
}
