/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UndefinedLibrary.Utilities;

/**
 *
 * @author Jonathan Botha
 */
public class Vector {
    protected double vx,vy,vz;
    
    public Vector(double xVelocity,double yVelocity,double zVelocity){
        vx = xVelocity;
        vy = yVelocity;
        vz = zVelocity;
    }
    public Vector(float xVelocity,float yVelocity){
        this(xVelocity,yVelocity,0);
    }
    
    /**
     *Returns the X velocity of the vector.
     * @return X velocity.
     */
    public double getXVelocity(){
        return vx;
    }
    
    /**
     *Set the X velocity of the vector.
     * @param xVelocity new X Velocity
     */
    public void setXVelocity(double xVelocity){
        vx = xVelocity;
    }
    
    /**
     *Change the X velocity of the vector.
     * @param d Change in X velocity
     */
    public void changeXVelocity(double d){
        vx+=d;
    }
    
    /**
     *Returns the Y velocity of the vector.
     * @return Y velocity.
     */
    public double getYVelocity(){
        return vy;
    }
    
    /**
     *Set the Y velocity of the vector.
     * @param yVelocity new Y Velocity
     */
    public void setYVelocity(double yVelocity){
        vy = yVelocity;
    }
    
    /**
     *Change the Y velocity of the vector.
     * @param d Change in Y velocity
     */
    public void changeYVelocity(double d){
        vy+=d;
    }
    
    /**
     *Returns the Z velocity of the vector.
     * @return Z velocity.
     */
    public double getZVelocity(){
        return vz;
    }
    
    /**
     *Set the Z velocity of the vector.
     * @param zVelocity new Z Velocity
     */
    public void setZVelocity(double zVelocity){
        vz = zVelocity;
    }
    
    /**
     *Change the Z velocity of the vector.
     * @param d Change in Z velocity
     */
    public void changeZVelocity(double d){
        vz+=d;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Static">

    /**
     *Calculates and returns the direction of a line
     * consisting of 2 points in degrees.
     * @param x1 Point 1's X component.
     * @param y1 Point 1's Y component
     * @param x2 Point 2's X component.
     * @param y2 Point 2's Y component
     * @return Direction of the line in degrees.
     */
        public static float getDirectionOfLine(double x1,double y1,double x2,double y2){
        float dir;
        try{
            dir = (float) Math.toDegrees(Math.atan((y2-y1)/(x2-x1)));
            if (dir < 0) {
                dir += 360d;
            }
            if (x2 < x1) {
                dir += 180d; 
            }
            dir = dir%360f;
        }catch(ArithmeticException x){
            dir = 90;
        }
        return dir;
    }
    
    /**
     *Calculates and returns the direction of
     * a vector using its X and Y components.
     * Direction is in degrees.
     * @param yComp Y component of vector.
     * @param xComp X component of vector.
     * @return Direction in degrees.
     */
    public static float getVectorDirection(double yComp,double xComp){
        float dir;
        try{
            dir = (float) Math.toDegrees(Math.atan((yComp)/(xComp)));
            if (dir < 0) {
                dir += 360;
            }
            if ((xComp) < 0) {
                dir += 180; 
            }
            dir = dir%360f;
        }catch(ArithmeticException x){
            dir = 90;
        }
        return dir;
    }
    
    /**
     *Calculates and returns the magnitude of a vector
     * using its X and Y components.
     * @param vx X velocity
     * @param vy Y velocity
     * @return Magnitude of vector.
     */
    public static double getResultantMagnitude(double vx,double vy){
        return Math.sqrt(vx*vx + vy*vy);
    }
    
    /**
     *Returns the X component of the vector.
     * @param mag Vector's magnitude
     * @param dir Vector's direction
     * @return
     */
    public static double getXComponent(double mag,float dir){
        return Math.cos(Math.toRadians(dir))*mag;
    }
    
    /**
     *Returns the Y component of the vector.
     * @param mag Vector's magnitude
     * @param dir Vector's direction
     * @return
     */
    public static double getYComponent(double mag,float dir){
        return Math.sin(Math.toRadians(dir))*mag;
    }
    
//</editor-fold>
}
