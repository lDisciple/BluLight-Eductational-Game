/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Jonathan Botha
 */
public class DerbyUtil {
    public static final OutputStream DEV_NULL = new OutputStream(){

        @Override
        public void write(int b) throws IOException {
            System.out.print("\u001B[34m" + (char)b);//Print what derby does in blue
            System.out.print("\u001B[30m");
        }
        
    };
}
