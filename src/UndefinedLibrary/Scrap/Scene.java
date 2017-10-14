/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Jonathan Botha
 */
public abstract class Scene implements KeyListener, MouseMotionListener, MouseWheelListener, MouseListener{
    protected byte newScene;//Changes between scenes
    private boolean isRunning;
    
    public Scene(){
        newScene = -1;//Does not change
        isRunning = false;
        init();
    }
    
    private void init(){
        customInit();
        isRunning = true;
    }
    
    protected abstract void customInit();
    
    public abstract void update(long timePassed);
    
    public abstract void draw(Graphics2D g);

    @Override
    public abstract void mouseClicked(MouseEvent e);

    @Override
    public abstract void mousePressed(MouseEvent e);
    @Override
    public abstract void mouseReleased(MouseEvent e);

    @Override
    public abstract void mouseEntered(MouseEvent e);

    @Override
    public abstract void mouseExited(MouseEvent e);
    
    /**
     *Gets whether the scene must change and to which index
     * returns -1 if scene must not change.
     * @return new scene index
     */
    public byte sceneStatus(){
        return newScene;
    }
    
    /**
     *Returns whether initialization is complete.
     * 
     * @return isRunning boolean
     */
    public boolean isRunning(){
        return isRunning;
    }
}
