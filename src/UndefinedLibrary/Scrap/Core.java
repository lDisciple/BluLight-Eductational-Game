/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;

/**
 *
 * @author JBotha
 */
public abstract class Core {
    /*private static final DisplayMode[] modes = {
    new DisplayMode(800,600,32,0),
    new DisplayMode(800,600,24,0),
    new DisplayMode(800,600,16,0),
    new DisplayMode(640,480,32,0),
    new DisplayMode(640,480,24,0),
    new DisplayMode(640,480,16,0),
    };*/
    protected boolean running;
    protected ScreenManager s;
    
    /**
     *Stops the looping of the program
     */
    public void stop(){
        running = false;
    }
    
    /**
     *Begins the initiation and looping of the program
     */
    public void run(){
        try{
            init();
        gameloop();
        }finally{
            s.restoreScreen();
        }
    }
    
    /**
     *Core initiation
     * @param f Frame to be used
     */
    public void init(JFrame f){
        s = new ScreenManager();
        DisplayMode dm = s.getCurrentDisplayMode();
        
        customInit(f);
        
        s.setFullscreen(dm,f);
        Window w = s.getFullscreenWindow();
        w.setBackground(Color.black);
        w.setForeground(Color.GREEN);
        running = true;
    }
    
    /**
     * initialization for implementations
     * @param f The frame used for the Core
     */
    protected abstract void customInit(JFrame f);
    
    /**
     *Core initiation
     */
    public void init(){
        JFrame f = new JFrame();
        init(f);
    }
    
    /**
     *Main loop of the Core
     * Use after init if core stopped.
     */
    public void gameloop(){
        long startingTime = System.currentTimeMillis();
        long totTime = startingTime;
        
        while(running){
            long timePassed = System.currentTimeMillis() - totTime;
            totTime += timePassed;
            update(timePassed);
                Graphics2D g = s.getGraphics();
            if (s.getFullscreenWindow().isShowing()) {
                draw(g);
            }
                g.dispose();
                s.update();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }
        
    public abstract void update(long timePassed);
    
    /**
     *Add a KeyListener to the frame
     * @param a KeyListener
     */
    public void addKeyListener(KeyListener a){
        Window w = s.getFullscreenWindow();
        w.addKeyListener(a);
    }
    /**
     *Removes a KeyListener to the frame
     * @param a KeyListener to remove
     */
    public void removeKeyListener(KeyListener a){
        Window w = s.getFullscreenWindow();
        w.removeKeyListener(a);
    }
    /**
     *Add a MouseListener to the frame
     * @param a MouseListener
     */
    public void addMouseListener(MouseListener a){
        Window w = s.getFullscreenWindow();
        w.addMouseListener(a);
    }
    /**
     *Removes a MouseListener to the frame
     * @param a MouseListener to remove
     */
    public void removeMouseListener(MouseListener a){
        Window w = s.getFullscreenWindow();
        w.removeMouseListener(a);
    }
    /**
     *Add a MouseMotionListener to the frame
     * @param a MouseMotionListener
     */
    public void addMouseMotionListener(MouseMotionListener a){
        Window w = s.getFullscreenWindow();
        w.addMouseMotionListener(a);
    }
    /**
     *Removes a MouseMotionListener to the frame
     * @param a MouseMotionListener to remove
     */
    public void removeMouseMotionListener(MouseMotionListener a){
        Window w = s.getFullscreenWindow();
        w.removeMouseMotionListener(a);
    }
    /**
     *Add a MouseWheelListener to the frame
     * @param a MouseWheelListener
     */
    public void addMouseWheelListener(MouseWheelListener a){
        Window w = s.getFullscreenWindow();
        w.addMouseWheelListener(a);
    }
    /**
     *Removes a MouseWheelListener to the frame
     * @param a MouseWheelListener to remove
     */
    public void removeMouseWheelListener(MouseWheelListener a){
        Window w = s.getFullscreenWindow();
        w.removeMouseWheelListener(a);
    }
    
    /**
     *Removes all event listeners.
     */
    public void removeAllListeners(){
        Window w = s.getFullscreenWindow();
        for (MouseListener ml : w.getMouseListeners()) {
            w.removeMouseListener(ml);
        }
        for (MouseMotionListener mml : w.getMouseMotionListeners()) {
            w.removeMouseMotionListener(mml);
        }
        for (MouseWheelListener mwl : w.getMouseWheelListeners()) {
            w.removeMouseWheelListener(mwl);
        }
        for (KeyListener kl : w.getKeyListeners()) {
            w.removeKeyListener(kl);
        }
    }
    
    public abstract void draw(Graphics2D g);
    
}
