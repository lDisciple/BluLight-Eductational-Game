/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomComponents;

import UndefinedLibrary.Utilities.Calculator;
import java.awt.Font;
import java.awt.Image;
import java.util.LinkedHashMap;

/**
 *
 * @author Jonathan Botha
 */
public class CalculatorTextfield extends BasicTextfield{

    public CalculatorTextfield(int xPos, int yPos, Image i, Font font) {
        super(xPos, yPos, i, font);
    }

    public CalculatorTextfield(int xPos, int yPos, Image i, Font font, int rightTextOffset, int leftTextOffset, int topTextOffset, int bottomTextOffset) {
        super(xPos, yPos, i, font, rightTextOffset, leftTextOffset, topTextOffset, bottomTextOffset);
    }
    
    @Override
    public Image getImage(){
        String oldText = text;
        text = text.replace("s", "sin(");//Based on symbols in calculator class
        text = text.replace("c", "acos(");//Based on symbols in calculator class
        text = text.replace("d", "cos(");//Based on symbols in calculator class
        text = text.replace("f", "tan(");//Based on symbols in calculator class
        text = text.replace("z", "asin(");//Based on symbols in calculator class
        text = text.replace("v", "atan(");//Based on symbols in calculator class
        Image img = super.getImage();
        text = oldText;
        return img;
    }    
    
    public double calculate(LinkedHashMap<String,String> binds){
        String calcText = text;
        calcText = calcText.replace("s", "s(");//Based on symbols in calculator class
        calcText = calcText.replace("d", "d(");//Based on symbols in calculator class
        calcText = calcText.replace("f", "f(");//Based on symbols in calculator class
        calcText = calcText.replace("z", "z(");//Based on symbols in calculator class
        calcText = calcText.replace("c", "c(");//Based on symbols in calculator class
        calcText = calcText.replace("v", "v(");//Based on symbols in calculator class
        for (String key : binds.keySet()) {
            calcText = calcText.replace(key, binds.get(key));
        }
        return Double.parseDouble(Calculator.calculateStringEquation(calcText));
    }
    
    public double calculate(){
        String calcText = text;
        calcText = calcText.replace("s", "s(");//Based on symbols in calculator class
        calcText = calcText.replace("d", "d(");//Based on symbols in calculator class
        calcText = calcText.replace("f", "f(");//Based on symbols in calculator class
        calcText = calcText.replace("z", "z(");//Based on symbols in calculator class
        calcText = calcText.replace("c", "c(");//Based on symbols in calculator class
        calcText = calcText.replace("v", "v(");//Based on symbols in calculator class
        return Double.parseDouble(Calculator.calculateStringEquation(calcText));
    }
}
