/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Scrap.Sprite.Attachments;

import UndefinedLibrary.Scrap.Sprite.Sprite;
import java.awt.Point;


/**
 *
 * @author Jonathan Botha
 */
public class EdgeBehaviourAttachment extends Attachment{
    protected int edgeX,edgeY,edgeWidth,edgeHeight;
    protected byte edgeBehaviour;
    
    public final static byte EB_CONTINUE = 0;
    public final static byte EB_BOUNCE = 1;
    public final static byte EB_SLIDE = 2;
    public final static byte EB_STOP = 3;

    public EdgeBehaviourAttachment (String name, byte edgeBehaviour, int edgeX, int edgeY, int edgeWidth, int edgeHeight ){
        this.name = name;
        this.tag = "EdgeBehaviour";
        
        this.edgeBehaviour = edgeBehaviour;
        this.edgeX = edgeX;
        this.edgeY = edgeY;
        this.edgeWidth = edgeWidth;
        this.edgeHeight = edgeHeight;
    }
    
    

    /**
     *Gets the constant EB value of the sprite.
     *EB_CONTINUE = 0;
     *EB_BOUNCE = 1;
     *EB_SLIDE = 2;
     *EB_STOP = 3;
     * @return Edge behaviour constant
     */
        public byte getEdgeBehaviour(){
        return edgeBehaviour;
    }
    /**
     *Sets the constant EB value of the sprite.
     *EB_CONTINUE = 0;
     *EB_BOUNCE = 1;
     *EB_SLIDE = 2;
     *EB_STOP = 3;
     * @param eb EB Value
     */
    public void setEdgeBehaviour(byte eb){
        edgeBehaviour = eb;
    }
    /**
     *Gets the position of the Sprite's virtual space.
     * @return Point of virtual space
     */
    public Point getEdgePoint(){
        return new Point(edgeX,edgeY);
    }
    
    /**
     *Sets the position of the Sprite's virtual space.
     * @param x X position
     * @param y Y position
     */
    public void setEdgePoint(int x,int y){
        edgeX = x;
        edgeY = y;
    }
    
    /**
     *Retrieves the Sprite's virtual space edge width
     * @return Edge width
     */
    public int getEdgeWidth(){
        return edgeWidth;
    }
    
    /**
     *Sets the Sprite's virtual space edge width.
     * @param width Edge width
     */
    public void setEdgeWidth(int width){
        edgeWidth = width;
    }
    
    /**
     *Retrieves the Sprite's virtual space edge height
     * @return Edge height
     */
    public int getEdgeHeight(){
        return edgeHeight;
    }
    
    /**
     *Sets the Sprite's virtual space edge height.
     * @param height Edge height
     */
    public void setEdgeHeight(int height){
        edgeHeight = height;
    }

    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {      switch (edgeBehaviour) {
            case 0:
                //Continue
                break;
            case 1:
                //Bounce
                if (s.getXPosition() > edgeWidth + edgeX) {
                    s.setXVelocity(s.getXVelocity()*-1);
                    s.setXPosition(edgeWidth + edgeX);
                }
                if (s.getXPosition() < edgeX) {
                    s.setXVelocity(s.getXVelocity()*-1);
                    s.setXPosition(edgeX);
                }
                if (s.getYPosition() > edgeHeight + edgeY) {
                    s.setYVelocity(s.getYVelocity()*-1);
                    s.setYPosition(edgeHeight + edgeY);
                }
                if (s.getYPosition() < edgeY) {
                    s.setYVelocity(s.getYVelocity()*-1);
                    s.setYPosition(edgeY);
                }
                //Z bounce
                Attachment[] depthArr = s.getAttachmentsByTag("Depth");
                for (Attachment a : depthArr) {
                    if (a instanceof DepthAttachment) {
                        DepthAttachment d = (DepthAttachment) a;
                        if (d.getZPosition() >= d.getMaxDepth() || d.getZPosition() <= d.getMinDepth()) {
                            d.bounce();
                        }
                    }
                }
                break;
            case 2:
                //Slide
                if (s.getXPosition() > edgeWidth + edgeX) {
                    s.setXVelocity(0);
                    s.setXPosition(edgeWidth + edgeX);
                }
                if (s.getXPosition() < edgeX) {
                    s.setXVelocity(0);
                    s.setXPosition(edgeX);
                }
                if (s.getYPosition() > edgeHeight + edgeY) {
                    s.setYVelocity(0);
                    s.setYPosition(edgeHeight + edgeY);
                }
                if (s.getYPosition() < edgeY) {
                    s.setYVelocity(0);
                    s.setYPosition(edgeY);
                }
                break;
            case 3:
                if (s.getYPosition() < edgeY || s.getYPosition() > edgeHeight + edgeY || s.getXPosition() < edgeX || s.getXPosition() > edgeWidth + edgeX) {
                    if (s.getYVelocity() < 0) {
                        s.setYPosition(0);
                    }else{
                        s.setYPosition(edgeHeight + edgeY);
                    }
                    s.setYVelocity(0);
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    
    
}
