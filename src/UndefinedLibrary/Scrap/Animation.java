/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap;

import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author JBotha
 */
public class Animation {
    private final ArrayList scenes;
    private int sceneIndex;
    private long movieTime;
    private long totalTime;
//constructor
    public Animation(){
        scenes = new ArrayList();
        totalTime = 0;
        start();
    }
//Add and create scene with time and mage

    /**
     *Adds a scene/image to the animation.
     * @param i Image to add.
     * @param t Time it will remain visible.
     */
        public synchronized void addScene(Image i,long t){
        totalTime += t;
        scenes.add(new Scene(i, totalTime));
    }

//start scenes from begin
    private void start() {
        movieTime = 0;
        sceneIndex = 0;
    }
//change scenes

    /**
     *Updates the animation to the current time.
     * @param timePassed Time change.
     */
        public synchronized void update(long timePassed){
        if (scenes.size() > 1) {
            movieTime += timePassed;
            if (movieTime >= totalTime) {
                movieTime = 0;
                sceneIndex = 0;
            }
            while(movieTime > getScene(sceneIndex).endTime){
                sceneIndex ++;
            }
        }
    }
    
//Get current scene

    /**
     *Gets the current image of the animation relative to the current time.
     * @return Animations current image
     */
        public synchronized Image getImage(){
        if (scenes.isEmpty()) {
            return null;
        }else{
            return getScene(sceneIndex).pic;
        }
    }

    private Scene getScene(int x){
        return (Scene) scenes.get(x);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Private Class Scene">
    private class Scene{
        Image pic;
        long endTime;
        
        public Scene(Image i, long et){
            this.pic = i;
            this.endTime = et;
        }
    }
    //</editor-fold>
}
