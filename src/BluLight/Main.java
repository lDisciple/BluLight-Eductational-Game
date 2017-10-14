package BluLight;

import Database.DatabaseManager;
import Entities.UserProfileEntity;
import UndefinedLibrary.Scrap.ScreenManager;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
String combo
mac = mac.substring(mac.length()-17).replace("-", "").trim() in  Main.class
curTextResID.substring(36).trim() + " by " + curTextResID.substring(0,36).trim().substring(12).trim()
newUser.getId().substring(12).trim().toUpperCase().equals("BLULIGHT") at Save button Reg form

sorting at BluLight/RefractionScene/checkGame
 */
/**
 *
 * @author Jonathan Botha
 */
public class Main{
    public static String curMAC;
    public static String curPCUsername;
    public static UserProfileEntity curProfile;
    public static DatabaseManager dbm;
    public static int paneX,paneY;
    
    
    public static void main(String[] args){
        System.setProperty("derby.stream.error.field", "Database.DerbyUtil.DEV_NULL");
        //<editor-fold defaultstate="collapsed" desc="Look and feel">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    
                    //break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Splash screen">
        JFrame splash = new JFrame(){
            @Override
            public void paint(Graphics g){
                g.setColor(Color.black);
                g.fillRect(0, 0, 400, 300);
                g.drawImage(resizeImage(new ImageIcon(Main.class.getResource("/Images/Splash.png")).getImage(),400,300,BufferedImage.TYPE_4BYTE_ABGR), 0, 0, null);
            }
        };
        splash.setBounds(ScreenManager.getDefaultDisplayMode().getWidth()/2-200,ScreenManager.getDefaultDisplayMode().getHeight()/2-150,400,300);
        splash.setUndecorated(true);
        splash.setResizable(false);
        splash.setVisible(true);
        
//</editor-fold>
        
        //Loads driver
        try {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Setup
        paneX = ScreenManager.getDefaultDisplayMode().getWidth()/2;
        paneY = ScreenManager.getDefaultDisplayMode().getHeight()/4;
        curMAC = getMAC();
        System.out.println(curMAC);
        curPCUsername = System.getenv("USERNAME");
        loadFonts();
        dbm = new DatabaseManager(); 
        dbm.generatePersonalisedResearch();
        //Check monitor compatibility
        if (checkMonitor()) {
            //Check users exist.
            splash.dispose();
            if (dbm.profileList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Welcome to BluLight!\nWe see that you are new, to get started:\n"
                        + "1. Register.\n"
                        + "2. Setup your settings in the launcher.\n"
                        + "3. Launch BluLight.", "Welcome!", JOptionPane.PLAIN_MESSAGE, null);
                new RegistrationForm().setVisible(true);
            }else{
                curProfile = dbm.profileList.get(0);
                new LoginForm().setVisible(true);
            }
        }
    }
    
    public static String getMAC(){
        String mac = "";
        try {
            //Only works with windows
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("ipconfig /all");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            boolean atEthernet = false;
            boolean atMAC = false;
            while ((line = br.readLine()) != null && atMAC == false) {
                if (line.toLowerCase().contains("ethernet adapter")) {
                    atEthernet = true;
                }
                if (line.toLowerCase().contains("physical address") && atEthernet) {
                    atMAC = true;
                }
                mac = line;
            }
            
            mac = mac.substring(mac.length()-17).replace("-", "").trim();
            Long.parseLong(mac,16);//Tests whether the String is hex.
            
        } catch (IOException | NumberFormatException ex) {
            mac = "************";
        }
        return mac.trim();
    }
    
    private static boolean checkMonitor(){
        ArrayList<String> resList = new ArrayList();
        for (DisplayMode dm : ScreenManager.getDefaultCompatibleDisplayModes()) {
            if (dm.getHeight() >= 600 && dm.getWidth() >= 800) {
                return true;
            }
        }
        return false;
    }
    
    public static void loadFonts(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/Fonts/DBXLNEWI.TTF")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/Fonts/DSMAN.TTF")));
        } catch (FontFormatException | IOException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Fonts not loaded", "Error", JOptionPane.ERROR_MESSAGE, null);
            System.err.println(ex);
            System.exit(0);
        }
    }
    
    
    public static Image resizeImage(Image img, int width, int height, int type){
        BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImg.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();
        return newImg;
    }
    
    public static Image createBasicTextfieldImage(int width, int height, Color c){
            BufferedImage tempImg = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) tempImg.getGraphics();
            g.setColor(c);
            for (int i = 0; i < 5; i++) {
                g.drawRect(i, i, tempImg.getWidth()-2*i, tempImg.getHeight()-2*i);
            }
            return tempImg;
        }
    
    public static Image getTextImage(String text, int width, Color textColor, Font font,boolean centre){
        text = text.replace("-", "­");//DBLX does not contain -
        ArrayList<String> newText = new ArrayList();
            Scanner scnText = new Scanner(text);
            BufferedImage tempImg = new BufferedImage(1,1,BufferedImage.TYPE_USHORT_GRAY);//Used to get font metrics
            Graphics2D g = tempImg.createGraphics();
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            //Breaks lines to fit into the image
            while (scnText.hasNextLine()) {
                String line = scnText.nextLine();
                Scanner scnLine = new Scanner(line);
                //120 +20
                String newLine = "";
                boolean addedLine = false;
                while(scnLine.hasNext()){
                    addedLine = true;
                String next = "";
                    while (fm.getStringBounds(newLine + next, g).getWidth()+10 < width && scnLine.hasNext()) {
                        next = scnLine.next();
                        newLine += next + " ";
                    }
                newText.add(newLine);
                newLine = "";
                }
                if (!addedLine) {
                    newText.add(" ");
                }
            }
            //Draw string to image
            tempImg = new BufferedImage(width,(fm.getAscent()+5)*newText.size() + font.getSize(),BufferedImage.TYPE_4BYTE_ABGR);
            g = tempImg.createGraphics();
            g.setColor(textColor);
            g.setFont(font);
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            for (int i = 1; i < newText.size() +1; i++) {
                if (centre) {
                    g.drawString(newText.get(i-1) , (int)(width- fm.getStringBounds(newText.get(i-1),g).getWidth())/2, i*(fm.getAscent()+5));
                }else{
                    g.drawString(newText.get(i-1) , 0, i*(fm.getAscent()+5));
                }
            }
            g.dispose();
            return tempImg;
    }
}
