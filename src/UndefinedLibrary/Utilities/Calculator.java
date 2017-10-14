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
public class Calculator {
    /*
    s = sin
    d = cos
    f = tan
    x = asin
    c = acos
    v = atan
    */
    
    public static String calculateStringEquation(String equation){
        //"(17*(8-7/7))^2+7-3*-6*s90"
        //Validity Check
        String numberSymbols = "-.0123456789";
        String operators = "*/+^√sdfzcv";
        
        //Replace alternate symbols
        equation = equation.replace(",", ".");
        equation = equation.replace("x", "*");
        equation = equation.replace("%", "/100");
        equation = equation.replace("\\", "/");
        
        //Insert + in front of -
        int c = 1;
        String newEquation = "" + equation.charAt(0);//Doesnt need to add + to begin
        while (c < equation.length()) {
            if (equation.charAt(c) == '-' && !operators.contains(""+equation.charAt(c-1))) {
                newEquation += "+";
            }
            newEquation += equation.charAt(c);
            c++;
        }
        equation = newEquation;
        



            //Convert everything to double (eg 2 -> 2.0)
            c = 0;
            newEquation = "";
            boolean inNum = false;
            while (c < equation.length()) {
                if (numberSymbols.indexOf(equation.charAt(c)) != -1) {
                    if (!inNum) {//Doesnt re read when going over more difficult numbers eg 2342
                        newEquation += findNextNumber(equation, c-1);//opIndex is the index before next
                        inNum = true;
                    }
                }else{
                    inNum = false;
                    newEquation += equation.charAt(c);
                }
                c++;
            }
            equation = newEquation;
            //Brackets
            while(equation.contains("(") || equation.contains(")")) {
                int openIndex = equation.indexOf("(");
                int openBrackets = 1;
                int closedBrackets = 0;
                int closeIndex = openIndex + 1;

                while (openBrackets != closedBrackets) {
                    if (equation.charAt(closeIndex) == '(') {
                        openBrackets++;
                    }
                    if (equation.charAt(closeIndex) == ')') {
                        closedBrackets++;
                    }
                    closeIndex++;
                }

                //Rewrite equation and sub in new value
                newEquation = "";//Reused
                if (openIndex != 0) {
                    newEquation += equation.substring(0, openIndex);
                }
                newEquation += calculateStringEquation(equation.substring(openIndex+1, closeIndex-1));
                if (closeIndex != equation.length()-1) {
                    newEquation += equation.substring(closeIndex);
                }
                equation = newEquation;
            }
            
        //Check order
            boolean test = checkStringFormula(equation);
        if (checkStringFormula(equation)) {
            //Trig (Typically in brackets)
            while (equation.contains("s")) {
                double num1 = findNextNumber(equation,equation.indexOf("s"));
                equation = equation.replace("s" + num1, "" + (Math.sin(Math.toRadians(num1))));
            }
            while (equation.contains("d")) {
                double num1 = findNextNumber(equation,equation.indexOf("d"));
                equation = equation.replace("d" + num1, "" + (Math.cos(Math.toRadians(num1))));
            }
            while (equation.contains("f")) {
                double num1 = findNextNumber(equation,equation.indexOf("f"));
                equation = equation.replace("f" + num1, "" + (Math.tan(Math.toRadians(num1))));
            }
            while (equation.contains("z")) {
                double num1 = findNextNumber(equation,equation.indexOf("z"));
                equation = equation.replace("z" + num1, "" + Math.toDegrees(Math.asin(num1)));
            }
            while (equation.contains("c")) {
                double num1 = findNextNumber(equation,equation.indexOf("c"));
                equation = equation.replace("c" + num1, "" + Math.toDegrees(Math.acos(num1)));
            }
            while (equation.contains("v")) {
                double num1 = findNextNumber(equation,equation.indexOf("v"));
                equation = equation.replace("v" + num1, "" + Math.toDegrees(Math.atan(num1)));
            }
            //Exponents
            while (equation.contains("^")) {
                int opIndex = equation.indexOf("^");
                double num1,num2;
                num1 = findPreviousNumber(equation,opIndex);//Previous number is answer before ^
                num2 = findNextNumber(equation,opIndex);
                double ans = num1;//Need num1 for replace

                if (num2 == 0) {
                    ans = 1;
                }else{
                    for (int i = 0; i < num2-1; i++) {
                        ans*=ans;
                    }
                    if (num2 < 0) {
                        ans = 1/ans;
                    }
                }
                String oldNum = "" + num1 + equation.charAt(opIndex) + num2;
                String newNum = ""+ans;
                equation = equation.replace(oldNum, newNum);
            }
            //Roots
            while (equation.contains("√")) {
                int opIndex = equation.indexOf("√");
                double num1,num2;
                num1 = findPreviousNumber(equation,opIndex);//Previous number is answer before ^
                num2 = findNextNumber(equation,opIndex);
                double ans = num1;//Need num1 for replace

                if (num2 == 0) {
                    throw new ArithmeticException("Math error");
                }else{
                    ans = Math.pow(num2, 1/num1);
                }
                String oldNum = "" + num1 + equation.charAt(opIndex) + num2;
                String newNum = ""+ans;
                equation = equation.replace(oldNum, newNum);
            }
            //Division
            while (equation.contains("/")) {
                int opIndex = equation.indexOf("/");
                double num1,num2;
                num1 = findPreviousNumber(equation,opIndex);//Previous number is answer before ^
                num2 = findNextNumber(equation,opIndex);
                double ans = num1;//Need num1 for replace

                if (num2 == 0) {
                    throw new java.lang.ArithmeticException();
                }else{
                    ans = num1/num2;
                }
                String oldNum = "" + num1 + equation.charAt(opIndex) + num2;
                String newNum = ""+ans;
                equation = equation.replace(oldNum, newNum);
            }
            //Multiplication
            while (equation.contains("*")) {
                int opIndex = equation.indexOf("*");
                double num1,num2;
                num1 = findPreviousNumber(equation,opIndex);//Previous number is answer before ^
                num2 = findNextNumber(equation,opIndex);
                double ans;//Need num1 for replace

                ans = num1*num2;
                String oldNum = "" + num1 + equation.charAt(opIndex) + num2;
                String newNum = ""+ans;
                equation = equation.replace(oldNum, newNum);
            }
            //Addition
            while (equation.contains("+")) {
                int opIndex = equation.indexOf("+");
                double num1,num2;
                num1 = findPreviousNumber(equation,opIndex);//Previous number is answer before ^
                num2 = findNextNumber(equation,opIndex);
                double ans;//Need num1 for replace

                ans = num1+num2;
                String oldNum = "" + num1 + equation.charAt(opIndex) + num2;
                String newNum = ""+ans;
                equation = equation.replace(oldNum, newNum);
            }

            return equation;
        }else{
            throw new AssertionError("Incorrect Format");
        }
    }
    
    private static double findNextNumber(String eq,int opIndex){
        String numberSymbols = "-.0123456789";
        int endIndex = opIndex+2;
        boolean cont = true;//Used to prevent AIOB
        while (cont) {
            if (endIndex >= eq.length()) {
                cont = false;
            }else{
                if (numberSymbols.indexOf(eq.charAt(endIndex)) == -1) {
                    cont = false;
                }else{
                    endIndex++;
                }
            }
        }
        double answer;
        if (eq.charAt(opIndex+1) == '-') {
            if (endIndex == eq.length()-1) {
                answer = Double.parseDouble(eq.substring(opIndex+2));
            }else{
                answer = Double.parseDouble(eq.substring(opIndex+2, endIndex));
            }
            answer *=-1;
        }else{
                answer = Double.parseDouble(eq.substring(opIndex+1, endIndex));
            
        }
        return answer;
    }
    
    private static double findPreviousNumber(String eq,int opIndex){
        String numberSymbols = "-.0123456789";
        int openIndex = opIndex-1;
        while (numberSymbols.indexOf(eq.charAt(openIndex)) != -1 && openIndex != 0) {
            openIndex--;
        }
        if (openIndex != 0) {
            openIndex++;
        }
        return Double.parseDouble(eq.substring(openIndex, opIndex));
    }

    public static boolean checkStringFormula(String eq) {
        int openBrackets = 0;
        int closedBrackets = 0;
        String operators = "*/+^√sdfzcv";
        boolean onOp = true;//cant start with operator
        
        for (int i = 0; i < eq.length(); i++) {
            if ("()".indexOf(eq.charAt(i)) == -1) {
                if (operators.contains(""+eq.charAt(i))) {
                    if (onOp && operators.indexOf(eq.charAt(i)) <= 4) {// <= 3 is single operators
                        return false;
                    }else{
                        onOp = true;
                    }
                }else{
                    try{
                        i += (""+findNextNumber(eq,i-1)).length()-1;
                        onOp = false;
                    }catch(NumberFormatException x){ // if not a number throws NFE
                        x.printStackTrace(System.err);
                        return false;
                    }
                }
            }else{
                if (eq.charAt(i) == '(') {
                    openBrackets++;
                }else{
                    closedBrackets++;
                }
            }
        }
        return openBrackets == closedBrackets;
    }
}
