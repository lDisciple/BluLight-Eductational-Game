/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Components;

/**
 *
 * @author Jonathan Botha
 */
public interface Clickable {
       /**
     *Used when mouse is clicked. Used in conjunction with MouseListener.
     * @param mX Mouse X position
     * @param mY Mouse Y position
     */
    public void mouseClicked(int mX,int mY);
       /**
     *Used when mouse is released. Used in conjunction with MouseListener.
     * @param mX Mouse X position
     * @param mY Mouse Y position
     */
    public void mouseReleased(int mX,int mY);

    /**
     *Used when mouse is moved. Used in conjunction with MouseMotionListener.
     * @param mX Mouse X position
     * @param mY Mouse Y position
     */
    public void mouseMoved(int mX,int mY);
}
