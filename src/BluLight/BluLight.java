/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BluLight;

import CircuitClasses.CircuitLink;
import CustomComponents.BasicTextfield;
import CustomComponents.BoxSprite;
import CustomComponents.CalculatorTextfield;
import Entities.PersonalResearchEntity;
import Entities.ResearchEntity;
import UndefinedLibrary.Scrap.Animation;
import UndefinedLibrary.Scrap.Components.Button;
import UndefinedLibrary.Scrap.Components.Clickable;
import UndefinedLibrary.Scrap.Components.ToggleButton;
import UndefinedLibrary.Scrap.Core;
import UndefinedLibrary.Scrap.Pen;
import UndefinedLibrary.Scrap.Scene;
import UndefinedLibrary.Scrap.ScreenManager;
import UndefinedLibrary.Scrap.Sprite.Attachments.Attachment;
import UndefinedLibrary.Scrap.Sprite.Attachments.EdgeBehaviourAttachment;
import UndefinedLibrary.Scrap.Sprite.Attachments.LifespanAttachment;
import UndefinedLibrary.Scrap.Sprite.Sprite;
import UndefinedLibrary.Utilities.Vector;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan Botha
 */
public class BluLight extends Core implements Runnable{
    private Scene curScene;
    private float widthAdj,heightAdj;
    
    @Override
    public void init(JFrame f){
        //Basic initiaition overrided to allow the resolution to be changed.
        f.setIconImage(new ImageIcon(LoginForm.class.getResource("/Images/Logo.png")).getImage());
        s = new ScreenManager();
        DisplayMode dm;
        try{
        dm = new DisplayMode(Integer.parseInt(Main.curProfile.getSettingresolution().split("x")[0]),//Uses res from the database
                Integer.parseInt(Main.curProfile.getSettingresolution().split("x")[1]),
                32,0);
        }catch(NumberFormatException x){
            dm = new DisplayMode(800,600,32,0);
        }
        s.setFullscreen(dm,f);
        
        customInit(f);
        
        Window w = s.getFullscreenWindow();
        w.setBackground(Color.black);
        w.setForeground(Color.GREEN);
        running = true;
        
    }

    @Override
    protected void customInit(JFrame f) {
        //New ratios for adjusting screen to resolution
        widthAdj = s.getWidth()/800f;
        heightAdj = s.getHeight()/600f;
        
        updateScene(0);//Goes to main menu
    }

    @Override
    public void update(long timePassed) {
        if (curScene.isRunning()) {
            curScene.update(timePassed);
        }
        if (curScene.sceneStatus() != -1) {
            updateScene(curScene.sceneStatus());
        }
    }
    
    public void updateScene(int scene){
        switch (scene) {//Polymorphism: Implements the subclass of scene that is entered
            case 0://MainMenu
                curScene = new MainMenuScene();
                break;
            case 1://Freefall
                curScene = new FreefallScene();
                break;
            case 2://Refraction
                curScene = new RefractionScene();
                break;
            case 3://Research
                curScene = new ResearchScene();
                break;
            default:
        }
        //Replace listeners
        removeAllListeners();
        addMouseMotionListener(curScene);
        addMouseWheelListener(curScene);
        addKeyListener(curScene);
        addMouseListener(curScene);
    }

    @Override
    public void draw(Graphics2D g) {
        curScene.draw(g);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Scenes">
    private class MainMenuScene extends Scene{
        private ArrayList<Button> buttonList;
        private ArrayList<Sprite> spriteList;
        private Image title;
        private Point ptMouse;
        private int connectDist;
        private boolean mouseDrag;
        private Image aboutImage;
        private Image aboutText;
        private int aboutPos;
        private boolean aboutShowing;
        
        @Override
        public void customInit() {
        //init variables
            buttonList = new ArrayList();
            spriteList = new ArrayList();
            ptMouse = new Point();
            connectDist = 150;
            mouseDrag = false;
            
            aboutShowing = false;
        // Create Title
            title = new ImageIcon(getClass().getResource("/Images/MainMenu/Title.png")).getImage();
            
            //<editor-fold defaultstate="collapsed" desc="Menu button creation">
            //Freefall
            Image tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/FreeFallNormal.png")).getImage();
            Button tempBtn = new Button(tempImg,(s.getWidth()-tempImg.getWidth(null))/2,(int)(175*heightAdj),true){
                @Override
                protected void clickEvent() {
                    transistion((byte)1);
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/FreeFallHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            
            //Refraction
            tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/RefractionNormal.png")).getImage();
            tempBtn = new Button(tempImg,(s.getWidth()-tempImg.getWidth(null))/2,(int)(250*heightAdj),true){
                @Override
                protected void clickEvent() {
                    transistion((byte)2);
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/RefractionHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            
            //Research
            tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/ResearchNormal.png")).getImage();
            tempBtn = new Button(tempImg,(s.getWidth()-tempImg.getWidth(null))/2,(int)(325*heightAdj),true){
                @Override
                protected void clickEvent() {
                    transistion((byte)3);
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/ResearchHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            
            //About
            tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/AboutNormal.png")).getImage();
            tempBtn = new Button(tempImg,(s.getWidth()-tempImg.getWidth(null))/2,(int)(400*heightAdj),true){
                @Override
                protected void clickEvent() {
                    aboutShowing = true;
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/AboutHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            
            int btnWidth = tempImg.getWidth(null);//Used to align in sign out and exit buttons
            //Sign out
            tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/SignOutNormal.png")).getImage();
            tempBtn = new Button(tempImg,(s.getWidth()-btnWidth)/2,(int)(475*heightAdj),true){
                @Override
                protected void clickEvent() {
                    transistion();
                    stop();
                    new LoginForm().setVisible(true);
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/SignOutHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            //Exit
            tempImg = new ImageIcon(getClass().getResource("/Images/MainMenu/ExitNormal.png")).getImage();
            tempBtn = new Button(tempImg,(s.getWidth()+btnWidth)/2 - tempImg.getWidth(null),(int)(475*heightAdj),true){
                @Override
                protected void clickEvent() {
                    transistion();
                    stop();
                }
            };
            tempBtn.setHoverImage(new ImageIcon(getClass().getResource("/Images/MainMenu/ExitHover.png")).getImage());
            buttonList.add(tempBtn);
            tempImg.flush();
            
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="About screen">
//Create the about image that fits screen.
            BufferedImage tempBImg = new BufferedImage(s.getWidth()-200, s.getHeight()-200,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) tempBImg.createGraphics();
            g.setColor(new Color(255, 113, 17));
            g.fillRect(0, 0, tempBImg.getWidth(null), tempBImg.getHeight(null));
            g.setColor(Color.black);
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            FontMetrics fm = g.getFontMetrics(new Font("DBXLNightfever",Font.PLAIN,36));
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,36));
            g.drawString("About", (int)(tempBImg.getWidth(null)/2 - fm.getStringBounds("About", g).getCenterX()), (int)(30+fm.getStringBounds("About", g).getMaxY()));
            g.drawLine(15, 40, tempBImg.getWidth(null)-15, 40);
            g.dispose();
            aboutImage = tempBImg;
            tempBImg.flush();
//Loads the project notes file
            InputStream fr = BluLight.class.getResourceAsStream("/BluLight/ProjectNotes.txt");
            int ch;
            String fileContents = "";
            try {
                while ((ch = fr.read()) != -1) {
                    fileContents += (char)ch;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: File not found.\n Jar file may be corrupted.\nRedownload BluLight to fix.", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
//Draw text for the about to an image that will be scrollable
            aboutText = Main.getTextImage(
                "Developer: Jonathan Botha\n" +
                "Email: Jonathan.botha.com@gmail.com\n" +
                "-\n" +
                "Typefaces:\n" +
                "DBLX Nightfever by Donald Beekman (studio@dbxl.demon.nl)\n" +
                "DS MAN by Iconian fonts (iconian@aol.com)\n" +
                "-\n" +
                "This software forms part of the CAPS Grade 11 syllabus that I am partaking in. The theme is educational software which we had to design and create as single developers in a short time span.\n" +
                "-\n" +
                "I hope you enjoy the program and feel free to contact me about any problems or project suggestions.\n--------------------------------\n"
                + "Project notes:\n--------------------------------\n"
                + fileContents
                    , s.getWidth()-250, Color.black, new Font("Arial",Font.PLAIN,16),true);
//</editor-fold>
            
        //Sprite setup
            //Sprite animation
            Animation a = new Animation();
            tempImg = new BufferedImage(3,3,BufferedImage.TYPE_INT_ARGB);
            g = (Graphics2D)tempImg.getGraphics();
            g.rotate(Math.toRadians(45));
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, 3, 3);
            g.dispose();
            a.addScene(tempImg, 10);
            tempImg.flush();
            //EdgeBehaviour (Bounce around)
            EdgeBehaviourAttachment spriteEB = new EdgeBehaviourAttachment("globalEB",EdgeBehaviourAttachment.EB_BOUNCE,0,0,s.getWidth(),s.getHeight());
            
//Sprite init
            for (int i = 0; i < 100* Main.curProfile.getSettingparticleamount(); i++) {
                Sprite sp = new Sprite(a,
                        (float)Math.random()*s.getWidth(),
                        (float)Math.random()*s.getHeight(),
                        (float)Math.random()*0.05f -0.025f,
                        (float)Math.random()*0.05f -0.025f,
                        (short) 100);
                sp.addAttachment(spriteEB);
                spriteList.add(sp);
            }
        }
        
        @Override
        public void update(long timePassed) {
            for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                sp.update(timePassed);
            }
        }
        
        private void transistion(){
//Replaces buttons with identical sprites that move upwards
            for (Button btn : buttonList.toArray(new Button[0])) {
                Animation a = new Animation();
                a.addScene(btn.getDefaultImage(), 10);
                Sprite btnSprite = new Sprite(a,btn.getXPosition(),btn.getYPosition(),0,0,(short)100);
                spriteList.add(btnSprite);
                btn.setVisible(false);
            }
//Replaces the title with an identical sprite that move upwards
            Animation a = new Animation();
            a.addScene(title, 10);
            Sprite titleSprite = new Sprite(a,
                    
                    (s.getWidth()-title.getWidth(null))/2,
                    (int)(100*heightAdj)-title.getHeight(null),
                    0,0,(short)100);
            spriteList.add(titleSprite);
            buttonList.clear();
            title = null;
//Makes all sprites move up and off screen
            for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                for (Attachment att : sp.getAttachmentsByTag("EdgeBehaviour")) {
                    ((EdgeBehaviourAttachment)att).setEdgeBehaviour(EdgeBehaviourAttachment.EB_CONTINUE);
                }
                sp.setYAcceleration(-0.001f);
            }
            try {
                Thread.sleep(1200);//Give the sprites time to get off the screen
            } catch (InterruptedException ex) {}
        }
        
        private void transistion(byte scene){
//Replaces buttons with identical sprites that move upwards
            for (Button btn : buttonList.toArray(new Button[0])) {
                Animation a = new Animation();
                a.addScene(btn.getDefaultImage(), 10);
                Sprite btnSprite = new Sprite(a,btn.getXPosition(),btn.getYPosition(),0,0,(short)100);
                spriteList.add(btnSprite);
                btn.setVisible(false);
            }
//Replaces the title with an identical sprite that move upwards
            Animation a = new Animation();
            a.addScene(title, 10);
            Sprite titleSprite = new Sprite(a,
                    (s.getWidth()-title.getWidth(null))/2,
                    (int)(100*heightAdj)-title.getHeight(null),
                    0,0,(short)100);
            spriteList.add(titleSprite);
            buttonList.clear();
            title = null;
//Makes all sprites move up and off screen
            for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                for (Attachment att : sp.getAttachmentsByTag("EdgeBehaviour")) {
                    ((EdgeBehaviourAttachment)att).setEdgeBehaviour(EdgeBehaviourAttachment.EB_CONTINUE);
                }
                sp.setYAcceleration(-0.001f);
            }
            try {
                Thread.sleep(1200);//Give the sprites time to get off the screen
            } catch (InterruptedException ex) {}
            newScene = scene;
        }

        @Override
        public void draw(Graphics2D g) {
            g.setColor(Color.black);
            g.fillRect(0, 0, s.getWidth(), s.getHeight());//Background prevents flashing
//Sprite connections
            ArrayList<Sprite> connectList = new ArrayList();
            for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                if (ptMouse.distance(sp.getXPosition(), sp.getYPosition()) < connectDist*widthAdj) {
                    connectList.add(sp);
                }
            }
            for (int i = 0; i < connectList.size()-1; i++) {
                for (int j = i+1; j < connectList.size(); j++) {
                    if (Point.distance(connectList.get(i).getXPosition(), connectList.get(i).getYPosition(),
                            connectList.get(j).getXPosition(), connectList.get(j).getYPosition()) <= 50*widthAdj) {
                        
                        if (ptMouse.distance(connectList.get(i).getXPosition(), connectList.get(i).getYPosition()) < 75*widthAdj) {
                            g.setColor(new Color(0,0,200,100)); 
                        }else{
                            g.setColor(new Color(0,0,200,50));
                        }
                        
                        g.drawLine(connectList.get(i).getXPosition(), connectList.get(i).getYPosition(),
                            connectList.get(j).getXPosition(), connectList.get(j).getYPosition());
                    }
                }
            }
//Draws light rays
            if (mouseDrag) {
                g.setColor(new Color(0,0,200,100)); 
                for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                    g.drawLine(sp.getXPosition(),sp.getYPosition(),s.getWidth()/2,0);
                }
            }
//Draws sprites
            for (Sprite sp : spriteList.toArray(new Sprite[0])) {
                g.drawImage(sp.getSpriteImage(), sp.getXPosition(), sp.getYPosition(), null);
            }
//Draw title
            if (title != null) {
                g.drawImage(title, (s.getWidth()-title.getWidth(null))/2, (int)(100*heightAdj)-title.getHeight(null), null);
            }
//Draw buttons
            for (Button btn : buttonList.toArray(new Button[0])) {//Avoids concurrent exception
                if (btn.isVisible()) {
                    g.drawImage(btn.getImage(), btn.getXPosition(), btn.getYPosition(), null);
                }
            }
//Draw about screen if showing
            if (aboutShowing) {
                g.drawImage(aboutImage, 100, 100, null);
                BufferedImage tempImg = new BufferedImage(aboutImage.getWidth(null),aboutImage.getHeight(null)-50,BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = tempImg.createGraphics();
                g2.drawImage(aboutText, 25, aboutPos, null);
                g2.dispose();
                g.drawImage(tempImg,100,150, null);
                tempImg.flush();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && !aboutShowing) {
                for (Button btn : buttonList.toArray(new Button[0])) {//Avoids concurrent exception
                    btn.mouseClicked(e.getX(), e.getY());
                }
            }
            
            
            if (e.getXOnScreen() < 100 || e.getXOnScreen() > s.getWidth()-100 ||
                    e.getYOnScreen() < 100 || e.getYOnScreen() > s.getWidth() -100) {
                aboutShowing = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseDrag = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDrag = false;
            if (e.getButton() == MouseEvent.BUTTON1) {
                for (Button btn : buttonList.toArray(new Button[0])) {//Avoids concurrent exception
                    btn.mouseReleased(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseDrag = true;
//About popup text scrolling. The Position changes relative to the mouse
            if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                if (e.getXOnScreen() > 100 && e.getXOnScreen() < s.getWidth()-100 &&
                    e.getYOnScreen() > 100 && e.getYOnScreen() < s.getWidth() -100) {
                    aboutPos += e.getYOnScreen() - ptMouse.y;
                    if (aboutText.getHeight(null) < aboutImage.getHeight(null)-50) {
                        aboutPos = 0;
                    }else{
                        if (aboutPos > 0) {
                            aboutPos = 0;
                        }
                        if (aboutPos + aboutText.getHeight(null) < aboutImage.getHeight(null)-50) {
                            aboutPos = (aboutImage.getHeight(null))-aboutText.getHeight(null)-50;
                        }
                    }
                    
                }
            }
            //update mouse
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
            for (Button btn : buttonList.toArray(new Button[0])) {//Avoids concurrent exception
                btn.mouseMoved(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (connectDist <= s.getWidth() && connectDist >= 0) {
                connectDist += e.getWheelRotation()*-10;//Up is - so *-10 to make up increase.
            }else{
                if (connectDist > s.getWidth()) {
                    connectDist = s.getWidth();
                }else{
                    connectDist = 0;
                }
            }
        }
        
    } //Scene 0
    
    private class FreefallScene extends Scene{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    //Setup variables
        private ArrayList<Clickable> clickableList;
        
        private Image helpImage;
        private Image helpText;
        private int helpPos;
        
        private Image bg;
        private int bgX;
        
        private BufferedImage overlay;
        private Pen pen;
        
        private Image panel;
        private Button[] panelButtons;
        private Image pnlTime;
        private int pnlLeftEnd;
        
        private int screen;
        /*
        0 = game
        1 = draw
        2 = calculator
        3 = formulae
        4 = help
        */
        
        private Button btnExit;
    //Game variables
        private BoxSprite box;
        private Color curColor;
        private Color nextColor;
        private boolean paused;
        private boolean wasPaused;
        private Color[] colorArr;
        private int targetGap;
        private Point ptTarget;
        private Sprite targetLine;
        private Sprite oldTargetLine;
        private boolean boxMove;
        private boolean playing;
        private int score;
        private ArrayList<Sprite> particleList;
        private BasicTextfield txfMagnitude,txfDegrees;
        private ToggleButton tglDegreesSet,tglMagnitudeSet;
        private boolean goodAnswer;
        private long timeTaken;
        private Button btnReset;
        //Formulae screen
        private Image imgFormulae;
        private int formulaePosition;
        private Point ptMouse;
        //Calculator
        private CalculatorTextfield txfCalc;
        private BasicTextfield[] txfCalcStoreArr;
        private ToggleButton tglCalcSto;
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Super">
        @Override
        public void customInit() {
        //init variables
            clickableList = new ArrayList();
            screen = 0;
            panelButtons = new Button[5];
            bgX = 0;
            ptMouse = new Point(0,0);
            
        //<editor-fold defaultstate="collapsed" desc="Panel setup">
            Image pnlRight =  new ImageIcon(getClass().getResource("/Images/Games/PanelRight.png")).getImage();//Used for spacing
            BufferedImage tempImg = new BufferedImage(s.getWidth(),
                    pnlRight.getHeight(null),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D) tempImg.getGraphics();
            g.drawImage(pnlRight, s.getWidth()-pnlRight.getWidth(null), 0, null);
            g.drawImage(Main.resizeImage( new ImageIcon(getClass().getResource("/Images/Games/PanelLeft.png")).getImage(),
                    s.getWidth()-pnlRight.getWidth(null), pnlRight.getHeight(null), BufferedImage.TYPE_INT_RGB),
                    0, 0, null);
            g.dispose();
            pnlLeftEnd = s.getWidth()-pnlRight.getWidth(null);
            panel = tempImg;
            tempImg.flush();
            
            pnlTime = new ImageIcon(getClass().getResource("/Images/Games/PanelTime.png")).getImage();
            Image imgButton = new ImageIcon(getClass().getResource("/Images/Games/PanelExit.png")).getImage();
            btnExit = new Button(imgButton,s.getWidth()- imgButton.getWidth(null),s.getHeight()-panel.getHeight(null)-imgButton.getHeight(null),true) {

                @Override
                protected void clickEvent() {
                    newScene = 0;
                }
            };
//</editor-fold>
            
        //Background
            tempImg = new BufferedImage(s.getWidth()*5,s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            g = (Graphics2D) tempImg.getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, s.getWidth()*5, tempImg.getHeight());
            g.setColor(Color.red);
            bg = tempImg;
            tempImg.flush();
            g.dispose();
        //<editor-fold defaultstate="collapsed" desc="Help screen">
            helpPos = 0;
            
            tempImg = new BufferedImage((int)(s.getWidth()- 65 -60*widthAdj),(int)(s.getHeight()-panel.getHeight(null)-75*heightAdj),BufferedImage.TYPE_3BYTE_BGR);
            g = (Graphics2D) tempImg.getGraphics();
            g.setColor(new Color(63, 72, 204));
            g.fillRect(0, 0, tempImg.getWidth(), tempImg.getHeight());
            g.setColor(Color.black);
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            FontMetrics fm = g.getFontMetrics(new Font("DBXLNightfever",Font.PLAIN,36));
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,36));
            g.drawString("Freefall Help", (int)(tempImg.getWidth()/2 - fm.getStringBounds("Freefall Help", g).getCenterX()), (int)(30+fm.getStringBounds("Freefall Help", g).getMaxY()));
            g.drawLine(15, 40, tempImg.getWidth()-15, 40);
            
            g.dispose();
            helpImage = tempImg;
            tempImg.flush();
            
            helpText = Main.getTextImage("Freefall is a highly involved physics based game that requires a high level \n" +
                    "\n" +
                    "knowledge of equations of motions and the ability to apply them quickly.\n" +
                    "\n" +
                    "You will be given a falling box and you will able to adjust its velocity as it \n" +
                    "\n" +
                    "falls. The goal of the game is to get the box through a hole below the box. The \n" +
                    "\n" +
                    "gap is very small and requires extreme precision.\n" +
                    "\n" +
                    "You start the game on the 'Game' screen where a box will begin falling and stop.\n" +
                    "Two text boxes will appear that you can enter a magnitude and direction which you \n" +
                    "\n" +
                    "can the select as your desired direction or magnitude with the adjacent buttons.\n" +
                    "\n" +
                    "The box will then continue to fall with the new velocity added. If the box falls \n" +
                    "\n" +
                    "through the hole then you will get a point, if not you lose.\n" +
                    "If you are able to get the box through the hole within 1 minute 30 seconds then you get 2 \n" +
                    "\n" +
                    "points.\n" +
                    "\n" +
                    "You are given the displacements of the block to the target hole and the \n" +
                    "\n" +
                    "velocities of the block to allow you to calculate the magnitude and direction.\n" +
                    "\n" +
                    "Good luck!\n" +
                    "\n" +
                    "Other screens:\n" +
                    "\n" +
                    "Drawing pad: Allows you to draw on the screen to help with equations.\n" +
                    "\n" +
                    "Calculator: Provides an in-game calculator to assist in calculating the magnitude \n" +
                    "\n" +
                    "and direction.\n" +
                    "\n" +
                    "Formulae: The formulae screen provides all the formulae that are available from \n" +
                    "\n" +
                    "research to help you with the game.", helpImage.getWidth(null)-15, Color.black, new Font("Arial",Font.PLAIN,16),true);
            
//</editor-fold>
       
            
            
        //<editor-fold defaultstate="collapsed" desc="Button setup">
            //Game
            Button tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnGame.png")).getImage(),-65,-65,true) {//Game off
                @Override
                protected void clickEvent() {
                    screenTransition(0);
                }
            };
            panelButtons[0] = tempBtn;
            //Draw
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnDraw.png")).getImage(),0,15,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(1);
                }
            };
            panelButtons[1] = tempBtn;
            //Calculator
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnCalculator.png")).getImage(),0,95,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(2);
                }
            };
            panelButtons[2] = tempBtn;
            //Formulae
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnFormulae.png")).getImage(),0,175,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(3);
                }
            };
            panelButtons[3] = tempBtn;
            //Help
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnHelp.png")).getImage(),0,255,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(4);
                }
            };
            panelButtons[4] = tempBtn;
            
            
        //draw setup
            //DrawClear
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnClear.png")).getImage(),s.getWidth()*2 - 124,s.getHeight()-panel.getHeight(null)-btnExit.getHeight()-49,true) {
                @Override
                protected void clickEvent() {
                    clearOverlay();
                }
            };
            clickableList.add(tempBtn);
            overlay = new BufferedImage(s.getWidth(),s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            pen = new Pen((Graphics2D)overlay.getGraphics(),0,0,2,Color.green);
            
            //<editor-fold defaultstate="collapsed" desc="Calculator">
            
//</editor-fold>
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Game setup">
            Random r = new Random();
            colorArr = new Color[]{
                Color.BLUE,
                Color.GREEN,
                Color.MAGENTA,
                Color.ORANGE,
                Color.RED,
                Color.yellow,
                Color.getHSBColor(r.nextFloat(), 1f, 1f),
                Color.getHSBColor(r.nextFloat(), 1f, 1f),
                Color.getHSBColor(r.nextFloat(), 1f, 1f),
                Color.getHSBColor(r.nextFloat(), 1f, 1f)
            };
            imgButton = new ImageIcon(getClass().getResource("/Images/Games/btnReset.png")).getImage();
            btnReset = new Button(imgButton,(s.getWidth() - imgButton.getWidth(null))/2,(s.getHeight()-imgButton.getHeight(null))/2,false) {
                @Override
                protected void clickEvent() {
                    beginGame();
                    setVisible(false);
                }
            };
            
            txfMagnitude = new BasicTextfield((int)(170 +  40*widthAdj),
                    (int)(300*heightAdj),
                    Main.createBasicTextfieldImage((int)(s.getWidth()-75 - 80*widthAdj - 75 - pnlTime.getWidth(null)), (25),Color.red),
                    new Font("DBXLNightfever",Font.PLAIN,16),
                    6,6,6,6);
            txfDegrees = new BasicTextfield((int)(170 +  40*widthAdj),
                    (int)(350*heightAdj),
                    Main.createBasicTextfieldImage((int)(s.getWidth()-75 - 80*widthAdj - 75 - pnlTime.getWidth(null)), (25),Color.red),
                    new Font("DBXLNightfever",Font.PLAIN,16),
                    6,6,6,6);
            txfDegrees.setVisible(false);
            txfMagnitude.setVisible(false);
            //85 is the buttons space
            //75 is panel width
            //80*widthAdj is side spacing
            imgButton = new ImageIcon(getClass().getResource("/Images/Games/btnSetNormal.png")).getImage();
            tglMagnitudeSet = new ToggleButton(imgButton,85+(int)(40*widthAdj),(int)(300*heightAdj),false) {

                @Override
                protected void toggleEvent() {
                    String txt = txfMagnitude.getText();
                    try{
                        Double.parseDouble(txt);
                        if (tglDegreesSet.isToggled()) {
                            unpause();
                        }
                    }catch(NumberFormatException x){
                        setToggled(false);
                    }
                }

                @Override
                protected void untoggleEvent() {
                    
                }
            };
            tglDegreesSet = new ToggleButton(imgButton,85+(int)(40*widthAdj),(int)(350*heightAdj),false) {

                @Override
                protected void toggleEvent() {
                    String txt = txfDegrees.getText();
                    try{
                        Double.parseDouble(txt);
                        if (tglMagnitudeSet.isToggled()) {
                            unpause();
                        }
                    }catch(NumberFormatException x){
                        setToggled(false);
                    }
                }

                @Override
                protected void untoggleEvent() {
                    
                }
            };
            imgButton = new ImageIcon(getClass().getResource("/Images/Games/btnSetToggled.png")).getImage();
            tglMagnitudeSet.setToggledImage(imgButton);
            tglDegreesSet.setToggledImage(imgButton);
            
            beginGame();
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Formulae setup">
            formulaePosition = 0;
            ArrayList<String> formulaList = new ArrayList();
            for (ResearchEntity res : Main.dbm.researchList.toArray(new ResearchEntity[0])) {
                if (res.getRelatedformula() != null) {
                    if (!res.getRelatedformula().equals("")) {
                        formulaList.add(res.getRelatedformula());
                    }
                }
            }
            if (formulaList.isEmpty()) {
                formulaList.add("No formulae found.");
            }
            tempImg = new BufferedImage(s.getWidth()-200,30* formulaList.size(),BufferedImage.TYPE_3BYTE_BGR);
            g = tempImg.createGraphics();
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,20));
            g.setColor(new Color(187,0,0));
            fm = g.getFontMetrics();
            
            g.drawLine(0, 0, tempImg.getWidth(), 0);
            for (int i = 0; i < formulaList.size(); i++) {
                g.drawLine(0, (i+1)*30-1, tempImg.getWidth(), (i+1)*30-1);
                g.drawString(formulaList.get(i), (int)(tempImg.getWidth()/2-fm.getStringBounds(formulaList.get(i), g).getWidth()/2), 20+ 30*i);
            }
            g.dispose();
            imgFormulae = tempImg;
            tempImg.flush();
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Calculator">
            txfCalc = new CalculatorTextfield(100 + s.getWidth()*2,//ans
                    150,
                    Main.createBasicTextfieldImage(s.getWidth()-225,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6);
            txfCalcStoreArr = new BasicTextfield[]{
                    new BasicTextfield(525 + s.getWidth()*2,//ans
                    200,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//A
                    250,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//B
                    300,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//C
                    350,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6)
            };
            
            tglCalcSto = new ToggleButton(new ImageIcon(getClass().getResource(//store (Added at 0 in list so the it can be used as no other clickable is inserted)
                    "/Images/Calculator/btnStoOut.png")).getImage(),
                    410 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void toggleEvent() {
                }

                @Override
                protected void untoggleEvent() {
                }
            };
            tglCalcSto.setToggledImage(new ImageIcon(getClass().getResource(//store (Added at 0 in list so the it can be used as no other clickable is inserted)
                    "/Images/Calculator/btnStoIn.png")).getImage());
            
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//7
                    "/Images/Calculator/btn7.png")).getImage(),
                    100 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("7");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//4
                    "/Images/Calculator/btn4.png")).getImage(),
                    100 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("4");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//1
                    "/Images/Calculator/btn1.png")).getImage(),
                    100 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("1");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//0
                    "/Images/Calculator/btn0.png")).getImage(),
                    100 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("0");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//8
                    "/Images/Calculator/btn8.png")).getImage(),
                    150 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("8");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//5
                    "/Images/Calculator/btn5.png")).getImage(),
                    150 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("5");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//2
                    "/Images/Calculator/btn2.png")).getImage(),
                    150 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("2");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//.
                    "/Images/Calculator/btnDot.png")).getImage(),
                    150 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append(".");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//9
                    "/Images/Calculator/btn9.png")).getImage(),
                    200 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("9");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//6
                    "/Images/Calculator/btn6.png")).getImage(),
                    200 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("6");
                    
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//3
                    "/Images/Calculator/btn3.png")).getImage(),
                    200 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("3");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//=
                    "/Images/Calculator/btnEqual.png")).getImage(),
                    200 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    try{
                        LinkedHashMap<String,String> binds = new LinkedHashMap();
                        binds.put("ANS", txfCalcStoreArr[0].getText());
                        binds.put("A", txfCalcStoreArr[1].getText());
                        binds.put("B", txfCalcStoreArr[2].getText());
                        binds.put("C", txfCalcStoreArr[3].getText());
                        txfCalcStoreArr[0].setText(""+txfCalc.calculate(binds));
                    }catch(NumberFormatException | AssertionError nfe){
                        txfCalcStoreArr[0].setText("Format incorrect");
                        nfe.printStackTrace(System.err);
                    }catch(ArithmeticException ae){
                        txfCalcStoreArr[0].setText("Math error");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//x
                    "/Images/Calculator/btnTimes.png")).getImage(),
                    260 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("*");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//div
                    "/Images/Calculator/btnDivide.png")).getImage(),
                    260 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("/");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//+
                    "/Images/Calculator/btnPlus.png")).getImage(),
                    260 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("+");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//-
                    "/Images/Calculator/btnMinus.png")).getImage(),
                    260 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("-");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//sin
                    "/Images/Calculator/btnSin.png")).getImage(),
                    310 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("s");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//cos
                    "/Images/Calculator/btnCos.png")).getImage(),
                    310 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("d");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//tan
                    "/Images/Calculator/btnTan.png")).getImage(),
                    310 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("f");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//open brac
                    "/Images/Calculator/btnOpenBrac.png")).getImage(),
                    310 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("(");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//asin
                    "/Images/Calculator/btnAsin.png")).getImage(),
                    360 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("z");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//acos
                    "/Images/Calculator/btnAcos.png")).getImage(),
                    360 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("c");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//atan
                    "/Images/Calculator/btnAtan.png")).getImage(),
                    360 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("v");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//close brac
                    "/Images/Calculator/btnCloseBrac.png")).getImage(),
                    360 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append(")");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//^
                    "/Images/Calculator/btnPower.png")).getImage(),
                    410 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("^");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Sqrt
                    "/Images/Calculator/btnSqrt.png")).getImage(),
                    410 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("2√");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//xRoot
                    "/Images/Calculator/btnxRoot.png")).getImage(),
                    410 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("√");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Ans store
                    "/Images/Calculator/btnAns.png")).getImage(),
                    470 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    if (!tglCalcSto.isToggled()) {
                        txfCalc.append("ANS");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//A store
                    "/Images/Calculator/btnA.png")).getImage(),
                    470 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[1].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("A");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//B store
                    "/Images/Calculator/btnB.png")).getImage(),
                    470 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[2].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("B");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//C store
                    "/Images/Calculator/btnC.png")).getImage(),
                    470 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[3].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("C");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            for (BasicTextfield txf : txfCalcStoreArr) {
                txf.setText(""+0.0);
            }
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//CE
                    "/Images/Calculator/btnCE.png")).getImage(),
                    s.getWidth()*3-70, 150 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.setText("");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Back
                    "/Images/Calculator/btnBack.png")).getImage(),
                    s.getWidth()*3-120, 150 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.backspace();
                    tglCalcSto.setToggled(false);
                }
            });
//</editor-fold>
            
            //Final setup
            for (int i = 0; i < 5; i++) {
                updatePanelButtonHoverImage(i);
            }
        }
        
        @Override
        public void update(long timePassed) {
            if (playing) {
                
                if (boxMove){//only for begin
                    box.update(timePassed);
                    boxMove = box.getYPosition() <= (int)(100*heightAdj);
                }else{
                    //<editor-fold defaultstate="collapsed" desc="Cheat">
                    /*double xDisp = (ptTarget.x + 10 - box.getXPosition())/100d;
                    double yDisp = (ptTarget.y - box.getYPosition() - box.getHeight())/100d;
                    double vYf = Math.sqrt((box.getYVelocity()*10d*box.getYVelocity()*10d)+2*(box.getYAcceleration()*10000d)*yDisp);
                    double time = (vYf - (box.getYVelocity()*10d))/(box.getYAcceleration()*10000d);
                    double ans = xDisp/(yDisp/((vYf + box.getYVelocity()*10d)/2));
                    ans= ans - box.getXVelocity()*10d;
                    txfMagnitude.setText(""+ans);
                    txfDegrees.setText("0");
                    tglDegreesSet.setToggled(true);
                    /*System.out.println("-----------------------------------------------------------");
                    System.out.println("box x  " + box.getXPosition()/100d);
                    System.out.println("box y  " + box.getYPosition()/100d);
                    System.out.println("box vx  " + box.getXVelocity()*10d);
                    System.out.println("box vy  " + box.getYVelocity()*10d);
                    System.out.println("box accy  " + box.getYAcceleration()*10000d);
                    System.out.println("Target  " + ptTarget);
                    System.out.println("Targetgap  " + targetGap/100d);
                    System.out.println("xDisp  " + xDisp);
                    System.out.println("yDisp  " + yDisp);
                    System.out.println("Vfy  " + vYf);
                    System.out.println("Time  " + time);*/
                    
//</editor-fold>
                    if (!paused) {//Moves down
                        box.update(timePassed);
                        if (box.getYPosition() <= 200 && box.getYPosition() > 100 && !wasPaused) {
                            pause();
                        }
                    }else{
                        timeTaken+= timePassed;
                    }
                    if (box.getYPosition()+box.getHeight() >= ptTarget.y) {
                        registerTargetHit();
                    }
                }
            }
            for (Sprite p : particleList.toArray(new Sprite[0])) {//Updates and deletes particles.
                if(p.getFirstAttachmentByTag("Lifespan") != null){
                    if (((LifespanAttachment)p.getFirstAttachmentByTag("Lifespan")).isDead()) {
                        particleList.remove(p);
                    }else{
                        p.update(timePassed);
                    }
                }else{
                    p.update(timePassed);
                }
            }
        }

        @Override
        public void draw(Graphics2D g) {
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.setColor(Color.black);
            g.drawImage(bg, bgX, 0, null);
            
            g.drawImage(btnExit.getImage(), btnExit.getXPosition(), btnExit.getYPosition(), null);
            g.drawImage(overlay, s.getWidth() + bgX, 0, null);
            
            //<editor-fold defaultstate="collapsed" desc="Draw help Image">
            g.drawImage(helpImage, (s.getWidth()*4 + bgX)+(int)(65 + 15*widthAdj),(int)(35*heightAdj), null);
            BufferedImage tempImg = new BufferedImage(helpImage.getWidth(null)-30,helpImage.getHeight(null)-50,BufferedImage.TYPE_4BYTE_ABGR);//Used to get font metrics
            Graphics2D g2 = tempImg.createGraphics();
            g2.drawImage(helpText, 0, helpPos, null);
            g2.dispose();
            g.drawImage(tempImg,(s.getWidth()*4 + bgX)+(int)(65 + 15*widthAdj) + 15,(int)(35*heightAdj) + 45, null);
            
//</editor-fold>
            
            if (playing) {
                //<editor-fold defaultstate="collapsed" desc="Playing game">
                if (boxMove) {
                    //Box moving
                    g.drawImage(box.getSpriteImage(), box.getXPosition() + bgX, box.getYPosition(), null);
                }else{
                    //Box still
                    g.drawImage(box.getSpriteImage(), box.getXPosition() + bgX, (int)(100*heightAdj), null);
                    g.drawImage(targetLine.getSpriteImage(), 75 + bgX, targetLine.getYPosition() - box.getYPosition() + (int)(100*heightAdj), null);
                    g.drawImage(oldTargetLine.getSpriteImage(), 75 + bgX, oldTargetLine.getYPosition() - box.getYPosition() + (int)(100*heightAdj), null);
                }
                //Draw text fields
                if (txfMagnitude.isVisible() && txfDegrees.isVisible()) {
                    g.setColor(Color.red);
                    g.setFont(new Font("DBXLNightfever",Font.PLAIN,18));
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString("Magnitude:", 170+(int)(40*widthAdj)+bgX, 290*heightAdj );
                    g.drawString("Direction:", 170+(int)(40*widthAdj)+bgX, 340*heightAdj );
                    g.drawImage(txfMagnitude.getImage(), txfMagnitude.getXPosition() + bgX, txfMagnitude.getYPosition(), null);
                    g.drawImage(txfDegrees.getImage(), txfDegrees.getXPosition() + bgX, txfDegrees.getYPosition(), null);
                    g.drawImage(tglMagnitudeSet.getImage(), tglMagnitudeSet.getXPosition() + bgX, tglMagnitudeSet.getYPosition(), null);
                    g.drawImage(tglDegreesSet.getImage(), tglDegreesSet.getXPosition() + bgX, tglDegreesSet.getYPosition(), null);
                }
//</editor-fold>
            }else{
                g.drawImage(targetLine.getSpriteImage(), 75 + bgX, targetLine.getYPosition() - box.getYPosition() + (int)(100*heightAdj) + box.getHeight(), null);
            }
            //Boundries and text
            for (int i = 0; i < 5; i++) {
                g.setColor(Color.gray);
                g.drawLine(75-i + bgX, 0, 75-i + bgX,s.getHeight());
                g.drawLine(s.getWidth() - pnlTime.getWidth(null) + i + bgX, 0,s.getWidth() - pnlTime.getWidth(null) + i + bgX, s.getHeight());
            }
            
            //<editor-fold defaultstate="collapsed" desc="Side stats">
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,14));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            
            g.drawString("Points", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight(null) +(int)(10*heightAdj + fm.getAscent()));
            if (score < 0) {
                g.drawString("0", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*2);
            }else{
                g.drawString(""+score, s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*2);
            }
            
            g.drawString("X Velocity", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*3);
            g.drawString(String.format("%.5f m/s", box.getXVelocity()*10d).replace("-", "­"), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*4);
            
            g.drawString("Y Velocity", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*5);
            g.drawString(String.format("%.5f m/s", box.getYVelocity()*10d).replace("-", "­"), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*6);
            
            g.drawString("X Displacement", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*7);
            g.drawString(String.format("%.5f m", (Math.abs(box.getXPosition()-ptTarget.x))/100f).replace("-", "­"), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*8);
            
            g.drawString("Y Displacement", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*9);
            g.drawString(String.format("%.5f m",(Math.abs(targetLine.getYPosition()- box.getYPosition() - box.getHeight()))/100f).replace("-", "­"), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*10);
            
            g.drawString("Time taken", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*11);
            g.drawString(String.format("%.2f s",timeTaken/1000f), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*12);
            
//</editor-fold>
            
            //Draw particles
            for (Sprite p : particleList.toArray(new Sprite[0])) {
                g.drawImage(p.getSpriteImage(), p.getXPosition()+bgX, p.getYPosition(), null);
            }
            if (btnReset.isVisible()) {
                g.drawImage(btnReset.getImage(), btnReset.getXPosition()+ bgX, btnReset.getYPosition(), null);
            }
            
            //<editor-fold defaultstate="collapsed" desc="Draw Formulae">
            
            g.setFont(new Font("DS MAN",Font.PLAIN,36));
            g.setColor(new Color(187,0,0));
            fm = g.getFontMetrics();
            
            g.drawString("Formulae", bgX + s.getWidth()*3 + (int)(s.getWidth()/2 - fm.getStringBounds("Formulae", g).getWidth()/2), 50);
            tempImg = new BufferedImage(imgFormulae.getWidth(null),s.getHeight()-200,BufferedImage.TYPE_3BYTE_BGR);
            g2 = tempImg.createGraphics();
            g2.drawImage(imgFormulae, 0, formulaePosition, null);
            g.drawImage(tempImg, 100 + bgX + s.getWidth()*3, 100, null);
            g2.dispose();
            tempImg.flush();
//</editor-fold>
//Draw buttons
            for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                if (clc instanceof Button) {
                    Button btn = ((Button)clc);
                    g.drawImage(btn.getImage(),btn.getXPosition() + bgX, btn.getYPosition(), null);
                }
                if (clc instanceof ToggleButton) {
                    ToggleButton btn = ((ToggleButton)clc);
                    g.drawImage(btn.getImage(),btn.getXPosition() + bgX, btn.getYPosition(), null);
                }
            }
            g.drawImage(tglCalcSto.getImage(), tglCalcSto.getXPosition() + bgX, tglCalcSto.getYPosition(), null);
//Draw calculator            
            g.drawImage(txfCalc.getImage(), txfCalc.getXPosition() + bgX, txfCalc.getYPosition(), null);
            for (BasicTextfield txf : txfCalcStoreArr.clone()) {
                g.drawImage(txf.getImage(), txf.getXPosition() + bgX, txf.getYPosition(), null);
            }
            
            //<editor-fold defaultstate="collapsed" desc="Panel draw">
            FontMetrics fm2 = g.getFontMetrics();
            //Panel
            g.setColor(Color.white);
            g.drawImage(panel, 0, s.getHeight()-panel.getHeight(null), null);//pnlHeight = 59
            g.setFont(new Font("DS MAN",Font.PLAIN,36));
            g.drawString("Freefall", 10, s.getHeight()-12);
            //Screen
            switch (screen) {
                case 0://game
                    g.drawString("Game", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 1: //Draw
                    g.drawString("Draw pad", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 2: // Calc
                    g.drawString("Calculator", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 3: //Formulae
                    g.drawString("Formulae", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 4: //Help
                    g.drawString("Help", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                default:
            }
            //Info (info starts at pxl 232 of pnlRight info pane is 390) 390x56
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            int pnlAdjx = s.getWidth()- 390;
            int pnlAdjy = s.getHeight()- 56 + fm2.getAscent();
            g.drawString(String.format("%-8s%30s", "Total:", "" + Main.curProfile.getFreefallpoints()), pnlAdjx +8, pnlAdjy -3);
            g.drawString(String.format("%-8s%30s", "Deaths:", "" + Main.curProfile.getFreefalldeaths()), pnlAdjx +8, pnlAdjy + 22);
            g.drawString(String.format("%-8s%30.2f", "Average:", (float)Main.curProfile.getFreefallpoints()/Main.curProfile.getFreefalldeaths()), pnlAdjx +200, pnlAdjy-3);
            g.drawString(String.format("%-8s%30s", "Best:", Main.curProfile.getFreefallhighscore()), pnlAdjx +200, pnlAdjy + 22);
            
            g.drawImage(pnlTime, s.getWidth()-pnlTime.getWidth(null), 0, null);
            String time =String.format("%1$02d:%2$02d:%3$02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND));
            g.drawString(time, s.getWidth()-90, 16);
            
//</editor-fold>
        //Panel Buttons
            for (Button btn : panelButtons.clone()) {
                g.drawImage(btn.getImage(), btn.getXPosition(), btn.getYPosition(), null);
            }
            
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Game">
        
        private Image getBoxImage(Color c){
            //Gets the box image of the box sprite
            BufferedImage tempImg = new BufferedImage(75,75,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) tempImg.getGraphics();
            g.setColor(c);
            for (int i = 0; i < 5; i++) {
                g.drawRoundRect(i, i, tempImg.getHeight()-2*i, tempImg.getHeight()-2*i, 10, 10);
            }
            g.dispose();
            return tempImg;
        }
        
        private Image getTargetLineImage(){
            
            //Gets line image for target
            BufferedImage tempImg = new BufferedImage(s.getWidth()-pnlTime.getWidth(null) - 75,5,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) tempImg.getGraphics();
            for (int i = 0; i < 5; i++) {
                g.setColor(Color.gray);
                g.drawLine(0, i, s.getWidth()-pnlTime.getWidth(null) - 75, i);
                g.setColor(nextColor);
                g.drawLine(ptTarget.x-75, i, ptTarget.x -75 + box.getWidth() + targetGap, i);
            }
            g.dispose();
            return tempImg;
        }
        
        
        private void beginGame(){
            curColor = colorArr[(int)Math.round(Math.random()*(colorArr.length-1))];
            nextColor = curColor;
            //Create box sprite
            Animation a = new Animation();
            a.addScene(getBoxImage(curColor), 100);
            box = new BoxSprite(a,(int)(Math.random()*(s.getWidth()-pnlTime.getWidth(null) -75 -a.getImage().getWidth(null)))+75, 
            -100,
            0,0,(short)100);
            box.setMaxBound(s.getWidth() - pnlTime.getWidth(null)- box.getWidth());
            box.setMinBound(75);
            box.setYAcceleration(0.000098f);//10m/s  = 1px/s PHYSICS
            boxMove = true;
            //Create new target line
            targetGap = 50;
            BufferedImage tempImg = new BufferedImage(5,5,BufferedImage.TYPE_INT_ARGB);
            ptTarget = new Point(box.getXPosition(),(int)(90*heightAdj));
            a = new Animation();
            a.addScene(tempImg, 1000);
            tempImg.flush();
            targetLine = new Sprite(a);
            targetLine.setXPosition(0);
            targetLine.setYPosition(ptTarget.y);
            box.setTargetPoint(ptTarget);
            //setup old target line
            oldTargetLine = new Sprite(a);
            oldTargetLine.setXPosition(-1000);
            oldTargetLine.setYPosition(-100);
            
            particleList = new ArrayList();
            
            score = -2;
            paused = false;
            wasPaused = true;
            playing = true;
        }
        
        private void pause(){
            paused = true;
            wasPaused = true;
            timeTaken = 0L;
            
            txfMagnitude.setVisible(true);
            txfDegrees.setVisible(true);
            tglMagnitudeSet.setVisible(true);
            tglDegreesSet.setVisible(true);
            
        }
        
        private void unpause(){
            try{
                float dir = Float.parseFloat(txfDegrees.getText());
                double mag = Double.parseDouble(txfMagnitude.getText());
                double vYi = box.getYVelocity()*10d + Vector.getYComponent(mag, dir);// Velocity y initial
                double vXi = box.getXVelocity()*10d + Vector.getXComponent(mag, dir);// Velocity x initial
                box.setXVelocity(vXi/10d);//PHYSICS
                box.setYVelocity(vYi/10d);//PHYSICS
                
                /*double xDisp = (ptTarget.x + 10 - box.getXPosition())/100d;
                double yDisp = (ptTarget.y - box.getYPosition() - box.getHeight())/100d;
                double vYf = Math.sqrt((box.getYVelocity()*10d*box.getYVelocity()*10d)+2*(box.getYAcceleration()*10000d)*yDisp);
                double time = (vYf - (box.getYVelocity()*10d))/(box.getYAcceleration()*10000d);
                double ans = xDisp/(yDisp/((vYf + box.getYVelocity()*10d)/2));*/
                //To prevent resource wasting by creating many variables the above equations have been merged into one.
                double ans = (ptTarget.x + 10 - box.getXPosition())/100d/((ptTarget.y - box.getYPosition() - box.getHeight())/100d/((Math.sqrt((box.getYVelocity()*10d*box.getYVelocity()*10d)+2*(box.getYAcceleration()*10000d)*(ptTarget.y - box.getYPosition() - box.getHeight())/100d) + box.getYVelocity()*10d)/2));
                if (vXi > ans-0.1 && vXi < ans+0.1) {
                    goodAnswer = true;
                }
                
                paused = false;
                txfMagnitude.setVisible(false);
                txfDegrees.setVisible(false);
                txfMagnitude.setText("");
                txfDegrees.setText("");
                tglMagnitudeSet.setVisible(false);
                tglDegreesSet.setVisible(false);
            }catch(NumberFormatException x){
                //Exception will not occur due to error checking on toggle buttons
                //If does happen the buttons will merely not be hid and set untoggled
            }
            tglMagnitudeSet.setToggled(false);
            tglDegreesSet.setToggled(false);
        }
        
        public void registerTargetHit(){
            if (box.getTargetX() >= ptTarget.x && box.getTargetX() + box.getWidth() <= ptTarget.x+targetGap + box.getWidth() || goodAnswer) {
                //Within target zone
                goodAnswer = false;
                //Extra point if time within 1:30 minutes
                if (timeTaken < 90000) {
                    score++;
                }
                //New target line
                curColor = nextColor;
                do {
                    nextColor = colorArr[(int)Math.round(Math.random()*(colorArr.length-1))];
                } while (nextColor.equals(curColor));
                ptTarget.y = 500 + 20*score;
                ptTarget.x = (int) Math.round(Math.random()*(s.getWidth() - pnlTime.getWidth(null)- box.getWidth()-targetGap -75)+75);
                
                Animation a = new Animation();
                a.addScene(getBoxImage(curColor), 1000);
                box.resetCostumeList(a);
                box.setYPosition(0);
                box.setTargetPoint(ptTarget);
                //Set old target line to the previous new one
                oldTargetLine = targetLine.clone();
                oldTargetLine.setYPosition(box.getHeight());
                
                if (score > 0) {
                    a = new Animation();
                    BufferedImage tempImg = new BufferedImage(s.getWidth()-pnlTime.getWidth(null) - 75,5,BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D g = tempImg.createGraphics();
                    for (int i = 0; i < 5; i++) {
                        g.setColor(curColor);
                        g.drawLine(0, i, s.getWidth()-pnlTime.getWidth(null) - 75, i);
                    }   
                    a.addScene(tempImg, 1000);  
                    g.dispose();
                    tempImg.flush();
                    oldTargetLine.resetCostumeList(a);
                }
                
                
                a = new Animation();
                a.addScene(getTargetLineImage(), 1000);                
                targetLine.setYPosition(ptTarget.y);
                targetLine.resetCostumeList(a);
                
                score ++;
                wasPaused = false;
                //Lessen the extra gap of target
                if (targetGap > 10) {
                    targetGap-=2;
                }else{
                    targetGap = 4;
                }
            }else{
                //lose
                playing = false;
                //<editor-fold defaultstate="collapsed" desc="Particle Gen">
                Animation a = new Animation();
                BufferedImage tempImg = new BufferedImage(5,5,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D)tempImg.getGraphics();
                g.setColor(curColor);
                Random r = new Random();
                g.fillPolygon(
                        new int[]{r.nextInt(2),3+r.nextInt(2),3+r.nextInt(2),r.nextInt(2)},
                        new int[]{r.nextInt(2),r.nextInt(2),3+r.nextInt(2),3+r.nextInt(2)}, 4);
                g.dispose();
                a.addScene(tempImg, 1000);
                tempImg.flush();
                
                //EdgeBehaviour
                EdgeBehaviourAttachment spriteEB = new EdgeBehaviourAttachment("globalEB",EdgeBehaviourAttachment.EB_SLIDE,75,0,s.getWidth() - pnlTime.getWidth(null)-75,s.getHeight());
                //Sprite init
                for (int i = 0; i < 50* Main.curProfile.getSettingparticleamount(); i++) {
                    Sprite sp = new Sprite(a,
                            box.getXPosition() + box.getWidth()/2,
                            (int)(100*heightAdj) + box.getHeight(),
                            (float)Math.random()*0.5f -0.25f,
                            (float)Math.random()*-0.5f,
                            (short) 100);
                    sp.setYAcceleration(0.00025f);
                    sp.addAttachment(spriteEB);
                    sp.addAttachment(new LifespanAttachment("deathLS",5000));
                    particleList.add(sp);
                }
//</editor-fold>
                //Database update
                Main.curProfile.setFreefallpoints(Main.curProfile.getFreefallpoints()+score);
                Main.curProfile.setFreefalldeaths((short)(Main.curProfile.getFreefalldeaths()+1));
                if (score > Main.curProfile.getFreefallhighscore()) {
                    Main.curProfile.setFreefallhighscore((short)score);
                }
                Main.dbm.updateRecords();
                btnReset.setVisible(true);
            }
            
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Other">
        private void clearOverlay(){
            overlay = new BufferedImage(s.getWidth(),s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            pen.setGraphics((Graphics2D)overlay.getGraphics());
        }
        
        private void updatePanelButtonHoverImage(int sc){
            int oldBgx = bgX;
            bgX = s.getWidth()*sc*-1;//Used to draw the certain screen
            BufferedImage tempImg = new BufferedImage((int)(275*widthAdj)+65, (int)(250*heightAdj),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D)tempImg.getGraphics();
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.drawImage(panelButtons[sc].getDefaultImage(),0, 0, null);
            String title;
            switch (sc) {
                case 0:
                    title = "Game";
                    g.setColor(new Color(82,37,82));
                    break;
                case 1:
                    title = "Drawing pad";
                    g.setColor(new Color(15,117,0));
                    break;
                case 2:
                    title = "Calculator";
                    g.setColor(new Color(255,201,14));
                    break;
                case 3:
                    title = "Formulae";
                    g.setColor(new Color(187,0,0));
                    break;
                case 4:
                    title = "Help";
                    g.setColor(new Color(63,72,204));
                    break;
                default:
                    title = "";
            }
            g.fillRect(65, 0, tempImg.getWidth()-65, tempImg.getHeight());
            BufferedImage fakeBG = new BufferedImage(s.getWidth(), s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = (Graphics2D)fakeBG.getGraphics();
            g2.setColor(Color.black);
            
            draw(g2);//Draws to the tempImg
            
            g2.dispose();
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,36));
            g.setColor(Color.black);
            g.drawString(title, 80, 40);//draw title
            //draw resized bg
            g.drawImage(Main.resizeImage(fakeBG, (int)(255*widthAdj), (int)(175*heightAdj), BufferedImage.TYPE_4BYTE_ABGR),65+(int)(10*widthAdj),65,null);
            panelButtons[sc].setHoverImage(tempImg);
            g.dispose();
            tempImg.flush();
            bgX = oldBgx;
        }
        
        public void screenTransition(int newScreen){
            pen.setPenDown(false);
            updatePanelButtonHoverImage(screen);
            panelButtons[screen].setYPosition(panelButtons[newScreen].getYPosition());
            int dispBG = (s.getWidth()*newScreen*-1) -bgX;
            //Button goes out
            for (int i = 0; i < 13; i++) {
                panelButtons[newScreen].setXPosition(panelButtons[newScreen].getXPosition()-5);
                bgX += dispBG/26;
                draw(s.getGraphics());
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                }
            }
            //button comes in
            for (int i = 0; i < 13; i++) {
                panelButtons[screen].setXPosition(panelButtons[screen].getXPosition()+5);
                bgX += dispBG/26;
                draw(s.getGraphics());
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                }
            }
            bgX = s.getWidth()*newScreen*-1;//Goes opposite to direction of movement
            panelButtons[newScreen].setXPosition(-65);
            panelButtons[screen].setXPosition(0);
            screen = newScreen;
        }
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Listeners">
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pen.setPenDown(false);
                for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                    clc.mouseClicked(e.getXOnScreen()-bgX, e.getYOnScreen());
                }
                int c = 0;
                boolean isClicked = false;
                do {    
                    if (panelButtons[c].inBounds(e.getX(), e.getY())) {
                        isClicked = true;
                        panelButtons[c].mouseClicked(e.getX(), e.getY());
                        panelButtons[c].setStatus((byte) 0);
                    }
                    //Prevents lingering hover image
                    c++;
                } while (c < 5 && !isClicked);
                
                btnReset.mouseClicked(e.getX(), e.getY());
                btnExit.mouseClicked(e.getX(), e.getY());
                
                txfMagnitude.mouseClicked(e.getX()-bgX, e.getY());
                txfDegrees.mouseClicked(e.getX()-bgX, e.getY());
                tglMagnitudeSet.mouseClicked(e.getX()-bgX, e.getY());
                tglDegreesSet.mouseClicked(e.getX()-bgX, e.getY());
                
                tglCalcSto.mouseClicked(e.getX()-bgX, e.getY());
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            for (Clickable btn : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                btn.mouseClicked(e.getX(), e.getY());
            }
            
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pen.setPenDown(false);
                for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                    clc.mouseReleased(e.getXOnScreen()-bgX, e.getYOnScreen());
                }
                
                btnReset.mouseReleased(e.getX(), e.getY());
                btnExit.mouseReleased(e.getX(), e.getY());
                
                txfMagnitude.mouseReleased(e.getX(), e.getY());
                txfDegrees.mouseReleased(e.getX(), e.getY());
                tglMagnitudeSet.mouseReleased(e.getX(), e.getY());
                tglDegreesSet.mouseReleased(e.getX(), e.getY());
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && screen == 0) {
                txfMagnitude.backspace();
                txfDegrees.backspace();
            }
            if (e.getKeyChar() == '-') {
                e.setKeyChar('­');//- is not part of DBLX
            }
            if ("1234567890­.".contains(""+e.getKeyChar()) && screen == 0) {
                txfMagnitude.typed(e.getKeyChar());
                txfDegrees.typed(e.getKeyChar());
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (screen == 1) {
                pen.update(e.getXOnScreen(), e.getYOnScreen());//+65 cos of panel limit
                pen.setPenDown(true);
            }
            if (screen == 3) {//formula drag scroll
                formulaePosition += e.getYOnScreen() - ptMouse.y; 
                if (imgFormulae.getHeight(null) <= s.getHeight()-200 || formulaePosition >0) {
                    formulaePosition = 0;
                }else{
                    if (s.getHeight()-200 >= imgFormulae.getHeight(null) +formulaePosition) {
                        formulaePosition = s.getHeight()-200 - imgFormulae.getHeight(null);
                    }
                }
            }
            //help drag scroll
            if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK && screen == 4) {
                if (e.getXOnScreen() >= (int)(65 + 15*widthAdj) && e.getXOnScreen() < (int)(65 + 15*widthAdj) + helpImage.getWidth(null) &&
                        e.getYOnScreen() >= (int)(35*heightAdj) + 45 && e.getYOnScreen() < (int)(35*heightAdj) + helpImage.getHeight(null)) {
                    helpPos += e.getYOnScreen() - ptMouse.y;
                    if (helpText.getHeight(null) < helpImage.getHeight(null)-50) {
                        helpPos = 0;
                    }else{
                        if (helpPos > 0) {
                            helpPos = 0;
                        }
                        if (helpPos + helpText.getHeight(null) < helpImage.getHeight(null)-50) {
                            helpPos = (helpImage.getHeight(null))-helpText.getHeight(null)-50;
                        }
                    }
                    
                }
            }
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                clc.mouseMoved(e.getXOnScreen(), e.getYOnScreen());
            }
            
            for (Button btn : panelButtons.clone()) {
                if (btn.getXPosition()>=0 && e.getXOnScreen() <= 65 && e.getYOnScreen() >= btn.getYPosition() && e.getYOnScreen() <= btn.getYPosition()+65) {//Prevents the hover image staying since it expands over all bttons
                    btn.mouseMoved(e.getXOnScreen(), e.getYOnScreen());
                }else{
                    btn.mouseMoved(e.getXOnScreen(), -1);//Take off hover image
                }
            }
            
            btnReset.mouseMoved(e.getX(), e.getY());
            btnExit.mouseMoved(e.getX(), e.getY());
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (screen == 3) {
                formulaePosition += e.getWheelRotation()*5;
                if (imgFormulae.getHeight(null) <= s.getHeight()-200 || formulaePosition >0) {
                    formulaePosition = 0;
                }else{
                    if (s.getHeight()-200 >= imgFormulae.getHeight(null) +formulaePosition) {
                        formulaePosition = s.getHeight()-200 - imgFormulae.getHeight(null);
                    }
                }
            }
        }
        
//</editor-fold>
    }
    
    private class RefractionScene extends Scene{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    //Setup variables
        private ArrayList<Clickable> clickableList;
        
        private Image helpImage;
        private Image helpText;
        private int helpPos;
        
        private Image bg;
        private int bgX;
        
        private BufferedImage overlay;
        private Pen pen;
        
        private Image panel;
        private Button[] panelButtons;
        private Image pnlTime;
        private int pnlLeftEnd;
        private int botTitleSize;//Font resizes to fit into the panel
        
        private int screen;
        private Button btnExit;
        /*
        0 = game
        1 = draw
        2 = calculator
        3 = circuit
        4 = formulae
        5 = help
        */
        //Formulae screen
        private Image imgFormulae;
        private int formulaePosition;
        private Point ptMouse;
        //Calculator
        private CalculatorTextfield txfCalc;
        private BasicTextfield[] txfCalcStoreArr;
        private ToggleButton tglCalcSto;
        //Game
        /*
        goes from 200 to w - 100
        beam bounce off walls
        choose random indices
        add one every 5 levels
        time limit
        */
        private Button btnReset;
        private Button btnNext;
        private long timeLeft;
        private ArrayList<RefractionLayer> refList;
        private ArrayList<RefractionLayer> availableRefList;
        private int numLayers;
        private int[] curLayers;//Link
        private int[] ansLayers;//Link
        private int startX,targetX;
        private int startAngle;
        private Image gameImg;
        private int score;
        private CircuitLink gameCircuit;
        private float circuitAns;
        private int circuitUnlock;
        private float circuitCur;
        private Image circuitImage;
        
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Super">
        @Override
        public void customInit() {
        //init variables
            clickableList = new ArrayList();
            screen = 0;
            panelButtons = new Button[6];
            bgX = 0;
            ptMouse = new Point(0,0);
            
        //<editor-fold defaultstate="collapsed" desc="Panel setup">
            Image pnlRight =  new ImageIcon(getClass().getResource("/Images/Games/PanelRight.png")).getImage();//Used for spacing
            BufferedImage tempImg = new BufferedImage(s.getWidth(),
                    pnlRight.getHeight(null),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D) tempImg.getGraphics();
            g.drawImage(pnlRight, s.getWidth()-pnlRight.getWidth(null), 0, null);
            g.drawImage(Main.resizeImage( new ImageIcon(getClass().getResource("/Images/Games/PanelLeft.png")).getImage(),
                    s.getWidth()-pnlRight.getWidth(null), pnlRight.getHeight(null), BufferedImage.TYPE_INT_RGB),
                    0, 0, null);
            //Resize title
            botTitleSize = 36;
            Font f = new Font("DS MAN",Font.PLAIN,36);
            while (f.getStringBounds("Refraction", g.getFontRenderContext()).getWidth() > 165) {
                botTitleSize --;
                f = new Font("DS MAN",Font.PLAIN,botTitleSize);
            }
            System.out.println(botTitleSize);
            g.dispose();
            pnlLeftEnd = s.getWidth()-pnlRight.getWidth(null);
            panel = tempImg;
            tempImg.flush();
            
            
            pnlTime = new ImageIcon(getClass().getResource("/Images/Games/PanelTime.png")).getImage();
            Image imgButton = new ImageIcon(getClass().getResource("/Images/Games/PanelExit.png")).getImage();
            btnExit = new Button(imgButton,s.getWidth()- imgButton.getWidth(null),s.getHeight()-panel.getHeight(null)-imgButton.getHeight(null),true) {

                @Override
                protected void clickEvent() {
                    newScene = 0;
                }
            };
//</editor-fold>
            
        //Background
            tempImg = new BufferedImage(s.getWidth()*6,s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            g = (Graphics2D) tempImg.getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, s.getWidth()*6, tempImg.getHeight());
            g.setColor(Color.red);
            g.dispose();
            bg = tempImg;
            tempImg.flush();
            
        //<editor-fold defaultstate="collapsed" desc="Help screen">
            helpPos = 0;
            //draw help img
            tempImg = new BufferedImage((int)(s.getWidth()- 65 -60*widthAdj),(int)(s.getHeight()-panel.getHeight(null)-75*heightAdj),BufferedImage.TYPE_3BYTE_BGR);
            g = (Graphics2D) tempImg.getGraphics();
            g.setColor(new Color(63, 72, 204));
            g.fillRect(0, 0, tempImg.getWidth(), tempImg.getHeight());
            g.setColor(Color.black);
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            FontMetrics fm = g.getFontMetrics(new Font("DBXLNightfever",Font.PLAIN,36));
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,36));
            g.drawString("Refraction Help", (int)(tempImg.getWidth()/2 - fm.getStringBounds("Refraction Help", g).getCenterX()), (int)(30+fm.getStringBounds("Refraction Help", g).getMaxY()));
            g.drawLine(15, 40, tempImg.getWidth()-15, 40);
            
            g.dispose();
            helpImage = tempImg;
            tempImg.flush();
            
            helpText = Main.getTextImage("Refraction is a game based highly on trial-and-error and teaching the basics of circuits and light refraction.\n" +
"\n" +
"You will begin with a single refractive index at your disposal: Vacuum. You must use the circuit game to unlock more indices to bend the path of light on the 'game' \n" +
"\n" +
"screen before time runs out.\n" +
"\n" +
"In the circuit game you are given a circuit and a target current that you must reach in order to unlock one more refractive index. An active wire is light gray and an \n" +
"\n" +
"inactive wire is dark gray.\n" +
"\n" +
"When you have unlocked enough indices yo can go back to the 'game' screen and click on the layers to change their refractive index to one that you have unlocked.\n" +
"\n" +
"You must bend the light from your starting point to the end point on the bottom of the screen.\n" +
"\n" +
"Good luck!\n" +
"\n" +
"Other screens:\n" +
"\n" +
"Drawing pad: Allows you to draw on the screen to help with equations.\n" +
"\n" +
"Calculator: Provides an in-game calculator to assist in calculating the magnitude and direction.\n" +
"\n" +
"Formulae: The formulae screen provides all the formulae that are available from research to help you with the game.", helpImage.getWidth(null)-15, Color.black, new Font("Arial",Font.PLAIN,16),true);
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Button setup">
            //Game
            Button tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnGame.png")).getImage(),-65,-65,true) {//Game off
                @Override
                protected void clickEvent() {
                    screenTransition(0);
                }
            };
            panelButtons[0] = tempBtn;
            //Draw
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnDraw.png")).getImage(),0,15,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(1);
                }
            };
            panelButtons[1] = tempBtn;
            //Calculator
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnCalculator.png")).getImage(),0,95,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(2);
                }
            };
            panelButtons[2] = tempBtn;
            //Calculator
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnCircuit.png")).getImage(),0,175,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(3);
                }
            };
            panelButtons[3] = tempBtn;
            //Formulae
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnFormulae.png")).getImage(),0,255,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(4);
                }
            };
            panelButtons[4] = tempBtn;
            //Help
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnHelp.png")).getImage(),0,335,true) {
                @Override
                protected void clickEvent() {
                    screenTransition(5);
                }
            };
            panelButtons[5] = tempBtn;
            
            
        //draw setup
            //DrawClear
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnClear.png")).getImage(),s.getWidth()*2 - 124,s.getHeight()-panel.getHeight(null)-btnExit.getHeight()-49,true) {
                @Override
                protected void clickEvent() {
                    clearOverlay();
                }
            };
            clickableList.add(tempBtn);
            overlay = new BufferedImage(s.getWidth(),s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            pen = new Pen((Graphics2D)overlay.getGraphics(),0,0,2,Color.green);
            
            //<editor-fold defaultstate="collapsed" desc="Calculator">
            
//</editor-fold>
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Formulae setup">
            formulaePosition = 0;
            ArrayList<String> formulaList = new ArrayList();
            for (ResearchEntity res : Main.dbm.researchList.toArray(new ResearchEntity[0])) {
                if (res.getRelatedformula() != null) {
                    if (!res.getRelatedformula().equals("")) {
                        formulaList.add(res.getRelatedformula());
                    }
                }
            }
            if (formulaList.isEmpty()) {
                formulaList.add("No formulae found.");
            }
            tempImg = new BufferedImage(s.getWidth()-200,30* formulaList.size(),BufferedImage.TYPE_3BYTE_BGR);
            g = tempImg.createGraphics();
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,20));
            g.setColor(new Color(187,0,0));
            fm = g.getFontMetrics();
            
            g.drawLine(0, 0, tempImg.getWidth(), 0);
            for (int i = 0; i < formulaList.size(); i++) {
                g.drawLine(0, (i+1)*30-1, tempImg.getWidth(), (i+1)*30-1);
                g.drawString(formulaList.get(i), (int)(tempImg.getWidth()/2-fm.getStringBounds(formulaList.get(i), g).getWidth()/2), 20+ 30*i);
            }
            g.dispose();
            imgFormulae = tempImg;
            tempImg.flush();
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Calculator">
            txfCalc = new CalculatorTextfield(100 + s.getWidth()*2,//ans
                    150,
                    Main.createBasicTextfieldImage(s.getWidth()-225,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6);
            txfCalcStoreArr = new BasicTextfield[]{
                    new BasicTextfield(525 + s.getWidth()*2,//ans
                    200,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//A
                    250,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//B
                    300,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6),
                    new BasicTextfield(525 + s.getWidth()*2,//C
                    350,
                    Main.createBasicTextfieldImage(s.getWidth() - 550,
                            45,new Color(255,201,14)),
                    new Font("Arial",Font.PLAIN,20),
                    6,6,6,6)
            };
            
            tglCalcSto = new ToggleButton(new ImageIcon(getClass().getResource(//store (Added at 0 in list so the it can be used as no other clickable is inserted)
                    "/Images/Calculator/btnStoOut.png")).getImage(),
                    410 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void toggleEvent() {
                }

                @Override
                protected void untoggleEvent() {
                }
            };
            tglCalcSto.setToggledImage(new ImageIcon(getClass().getResource(//store (Added at 0 in list so the it can be used as no other clickable is inserted)
                    "/Images/Calculator/btnStoIn.png")).getImage());
            
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//7
                    "/Images/Calculator/btn7.png")).getImage(),
                    100 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("7");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//4
                    "/Images/Calculator/btn4.png")).getImage(),
                    100 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("4");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//1
                    "/Images/Calculator/btn1.png")).getImage(),
                    100 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("1");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//0
                    "/Images/Calculator/btn0.png")).getImage(),
                    100 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("0");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//8
                    "/Images/Calculator/btn8.png")).getImage(),
                    150 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("8");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//5
                    "/Images/Calculator/btn5.png")).getImage(),
                    150 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("5");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//2
                    "/Images/Calculator/btn2.png")).getImage(),
                    150 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("2");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//.
                    "/Images/Calculator/btnDot.png")).getImage(),
                    150 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append(".");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//9
                    "/Images/Calculator/btn9.png")).getImage(),
                    200 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("9");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//6
                    "/Images/Calculator/btn6.png")).getImage(),
                    200 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("6");
                    
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//3
                    "/Images/Calculator/btn3.png")).getImage(),
                    200 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("3");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//=
                    "/Images/Calculator/btnEqual.png")).getImage(),
                    200 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    try{
                        LinkedHashMap<String,String> binds = new LinkedHashMap();
                        binds.put("ANS", txfCalcStoreArr[0].getText());
                        binds.put("A", txfCalcStoreArr[1].getText());
                        binds.put("B", txfCalcStoreArr[2].getText());
                        binds.put("C", txfCalcStoreArr[3].getText());
                        txfCalcStoreArr[0].setText(""+txfCalc.calculate(binds));
                    }catch(NumberFormatException | AssertionError nfe){
                        txfCalcStoreArr[0].setText("Format incorrect");
                        nfe.printStackTrace(System.err);
                    }catch(ArithmeticException ae){
                        txfCalcStoreArr[0].setText("Math error");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//x
                    "/Images/Calculator/btnTimes.png")).getImage(),
                    260 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("*");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//div
                    "/Images/Calculator/btnDivide.png")).getImage(),
                    260 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("/");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//+
                    "/Images/Calculator/btnPlus.png")).getImage(),
                    260 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("+");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//-
                    "/Images/Calculator/btnMinus.png")).getImage(),
                    260 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("-");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//sin
                    "/Images/Calculator/btnSin.png")).getImage(),
                    310 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("s");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//cos
                    "/Images/Calculator/btnCos.png")).getImage(),
                    310 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("d");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//tan
                    "/Images/Calculator/btnTan.png")).getImage(),
                    310 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("f");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//open brac
                    "/Images/Calculator/btnOpenBrac.png")).getImage(),
                    310 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("(");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//asin
                    "/Images/Calculator/btnAsin.png")).getImage(),
                    360 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("z");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//acos
                    "/Images/Calculator/btnAcos.png")).getImage(),
                    360 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("c");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//atan
                    "/Images/Calculator/btnAtan.png")).getImage(),
                    360 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("v");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//close brac
                    "/Images/Calculator/btnCloseBrac.png")).getImage(),
                    360 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append(")");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//^
                    "/Images/Calculator/btnPower.png")).getImage(),
                    410 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("^");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Sqrt
                    "/Images/Calculator/btnSqrt.png")).getImage(),
                    410 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("2√");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//xRoot
                    "/Images/Calculator/btnxRoot.png")).getImage(),
                    410 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.append("√");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Ans store
                    "/Images/Calculator/btnAns.png")).getImage(),
                    470 + s.getWidth()*2, 200 ,true) {
                @Override
                protected void clickEvent() {
                    if (!tglCalcSto.isToggled()) {
                        txfCalc.append("ANS");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//A store
                    "/Images/Calculator/btnA.png")).getImage(),
                    470 + s.getWidth()*2, 250 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[1].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("A");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//B store
                    "/Images/Calculator/btnB.png")).getImage(),
                    470 + s.getWidth()*2, 300 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[2].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("B");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//C store
                    "/Images/Calculator/btnC.png")).getImage(),
                    470 + s.getWidth()*2, 350 ,true) {
                @Override
                protected void clickEvent() {
                    if (tglCalcSto.isToggled()) {
                        txfCalcStoreArr[3].setText(txfCalcStoreArr[0].getText());
                    }else{
                        txfCalc.append("C");
                    }
                    tglCalcSto.setToggled(false);
                }
            });
            for (BasicTextfield txf : txfCalcStoreArr) {
                txf.setText(""+0.0);
            }
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//CE
                    "/Images/Calculator/btnCE.png")).getImage(),
                    s.getWidth()*3-70, 150 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.setText("");
                    tglCalcSto.setToggled(false);
                }
            });
            clickableList.add(new Button(new ImageIcon(getClass().getResource(//Back
                    "/Images/Calculator/btnBack.png")).getImage(),
                    s.getWidth()*3-120, 150 ,true) {
                @Override
                protected void clickEvent() {
                    txfCalc.backspace();
                    tglCalcSto.setToggled(false);
                }
            });
//</editor-fold>
            
        //<editor-fold defaultstate="collapsed" desc="Game setup">
            imgButton = new ImageIcon(getClass().getResource("/Images/Games/btnReset.png")).getImage();
            btnReset = new Button(imgButton,(s.getWidth() - imgButton.getWidth(null))/2,(s.getHeight()-imgButton.getHeight(null))/2,false) {
                @Override
                protected void clickEvent() {
                    beginGame();
                    setVisible(false);
                }
            };
            imgButton = new ImageIcon(getClass().getResource("/Images/Games/btnNext.png")).getImage();
            btnNext = new Button(imgButton,(s.getWidth() - imgButton.getWidth(null))/2,(s.getHeight()-imgButton.getHeight(null))/2,false) {
                @Override
                protected void clickEvent() {
                    setupPuzzle();
                    setVisible(false);
                }
            };
            
            tempBtn = new Button(new ImageIcon(getClass().getResource("/Images/Games/btnNewPuzzle.png")).getImage(),(int)(s.getWidth()*3.5 - 146),s.getHeight()-panel.getHeight(null)-32,true) {
                @Override
                protected void clickEvent() {
                    setupCircuit();
                }
            };
            clickableList.add(tempBtn);
            
            refList = new ArrayList();
            InputStream fr = BluLight.class.getResourceAsStream("/BluLight/RefractiveIndices.txt");
            int ch;
            String fileContents = "";
            try {
                while ((ch = fr.read()) != -1) {
                    fileContents += (char)ch;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: File not found.\n Jar file may be corrupted.\nRedownload BluLight to fix.", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
            Random r = new Random();
            for (String ref : fileContents.split("\n")) {
                refList.add(new RefractionLayer(ref.split(";")[0],
                        Float.parseFloat(ref.split(";")[1]),
                        Color.getHSBColor(r.nextFloat(), 1f, 1f)));
            }
            refList.get(0).setClrLayer(Color.BLACK);
            beginGame();
//</editor-fold>
            //Final setup
            for (int i = 0; i < 6; i++) {
                updatePanelButtonHoverImage(i);
            }
        }
        @Override
        public void update(long timePassed) {
            timeLeft -= timePassed;
            if (timeLeft <= 0L) {
                //Database update
                Main.curProfile.setRefractionpoints(Main.curProfile.getRefractionpoints()+score);
                Main.curProfile.setRefractiondeaths((short)(Main.curProfile.getRefractiondeaths()+1));
                if (score > Main.curProfile.getRefractionhighscore()) {
                    Main.curProfile.setRefractionhighscore((short)score);
                }
                Main.dbm.updateRecords();
                
                btnReset.setVisible(true);
                timeLeft = 0L;
            }
        }
        @Override
        public void draw(Graphics2D g) {
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.setColor(Color.black);
            g.drawImage(bg, bgX, 0, null);
            g.drawImage(gameImg, 75 + bgX, 0, null);
            for (int i = 0; i < 5; i++) {
                g.setColor(Color.gray);
                g.drawLine(75-i + bgX, 0, 75-i + bgX,s.getHeight());
                g.drawLine(s.getWidth() - pnlTime.getWidth(null) + i + bgX, 0,s.getWidth() - pnlTime.getWidth(null) + i + bgX, s.getHeight());
            }
            
            //<editor-fold defaultstate="collapsed" desc="Circuit game">
            g.setColor(Color.gray);
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < 5; i++) {
                g.setColor(Color.gray);
                g.drawLine(75-i + 3*s.getWidth() + bgX, 0, 75-i + bgX+ 3*s.getWidth(),s.getHeight());
                g.drawLine(s.getWidth() - pnlTime.getWidth(null) + i + bgX+ 3*s.getWidth(), 0,s.getWidth() - pnlTime.getWidth(null) + i + bgX + 3*s.getWidth(), s.getHeight());
                g.setColor(Color.lightGray);
                g.drawLine(75+s.getWidth()*3 + bgX, 50+(s.getHeight()-200)/2 +i, 150+s.getWidth()*3 + bgX, 50+(s.getHeight()-200)/2 +i);
            }
            g.setColor(Color.white);
            g.drawString(String.format("%.2f  A", circuitCur),100+circuitImage.getWidth(null)+3 + bgX+ 3*s.getWidth(), 50 + (s.getHeight()-200)/2  +fm.getHeight()/2);
            
            g.drawImage(circuitImage, 100 + s.getWidth()*3 + bgX, 0, null);
            
            
            //<editor-fold defaultstate="collapsed" desc="Side stats">
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,14));
            fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            
            g.drawString("Points", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight(null) +(int)(10*heightAdj + fm.getAscent()));
            g.drawString(""+score, s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*2);
            
            g.drawString("Time left:", s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*3);
            g.drawString(String.format("%.2f s", Math.abs(timeLeft/1000f)), s.getWidth() + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*4);
            
            
            //Circuit
            g.drawString("Points", s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight(null) +(int)(10*heightAdj + fm.getAscent()));
            g.drawString(""+score, s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*2);
            
            g.drawString("Time left:", s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*3);
            g.drawString(String.format("%.2f s", Math.abs(timeLeft/1000f)), s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*4);
            
            if (circuitUnlock == -1) {
                g.drawString("Next  unlock  at:", s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*5);
                g.drawString(String.format("%.2f A", circuitAns), s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*6);
            }else{
                if (circuitUnlock == -2) {
                    g.drawString("All unlocked", s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*5);
                }else{
                    g.drawString("Unlocked:", s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*5);
                    g.setColor(availableRefList.get(circuitUnlock).getClrLayer());
                    g.drawString(availableRefList.get(circuitUnlock).getName(), s.getWidth()*4 + 15 - pnlTime.getWidth(null) + bgX, pnlTime.getHeight( null) +(int)(10*heightAdj + fm.getAscent())*6);
                    g.setColor(Color.gray);
                }
            }
            
//</editor-fold>
//</editor-fold>
            g.drawImage(btnExit.getImage(), btnExit.getXPosition(), btnExit.getYPosition(), null);
            g.drawImage(overlay, s.getWidth() + bgX, 0, null);
            g.drawImage(helpImage, (s.getWidth()*5 + bgX)+(int)(65 + 15*widthAdj),(int)(35*heightAdj), null);
            BufferedImage tempImg = new BufferedImage(helpImage.getWidth(null)-30,helpImage.getHeight(null)-50,BufferedImage.TYPE_4BYTE_ABGR);//Used to get font metrics
            Graphics2D g2 = tempImg.createGraphics();
            g2.drawImage(helpText, 0, helpPos, null);
            g2.dispose();
            g.drawImage(tempImg,(s.getWidth()*5 + bgX)+(int)(65 + 15*widthAdj) + 15,(int)(35*heightAdj) + 45, null);
            tempImg.flush();
            
            if (btnReset.isVisible()) {
                g.drawImage(btnReset.getImage(), btnReset.getXPosition()+ bgX, btnReset.getYPosition(), null);
            }
            if (btnNext.isVisible()) {
                g.drawImage(btnNext.getImage(), btnNext.getXPosition()+ bgX, btnNext.getYPosition(), null);
            }
            
            //<editor-fold defaultstate="collapsed" desc="Formulae">
            
            g.setFont(new Font("DS MAN",Font.PLAIN,36));
            g.setColor(new Color(187,0,0));
            fm = g.getFontMetrics();
            //Draw formula and formula scroll
            g.drawString("Formulae", bgX + s.getWidth()*4 +(int)(s.getWidth()/2 - fm.getStringBounds("Formulae", g).getWidth()/2), 50);
            tempImg = new BufferedImage(imgFormulae.getWidth(null),s.getHeight()-200,BufferedImage.TYPE_3BYTE_BGR);
            g2 = tempImg.createGraphics();
            g2.drawImage(imgFormulae, 0, formulaePosition, null);
            g.drawImage(tempImg, 100 + bgX + s.getWidth()*4, 100, null);
            g2.dispose();
            tempImg.flush();
//</editor-fold>
            
            //button draw
            for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                if (clc instanceof Button) {
                    Button btn = ((Button)clc);
                    g.drawImage(btn.getImage(),btn.getXPosition() + bgX, btn.getYPosition(), null);
                }
                if (clc instanceof ToggleButton) {
                    ToggleButton btn = ((ToggleButton)clc);
                    g.drawImage(btn.getImage(),btn.getXPosition() + bgX, btn.getYPosition(), null);
                }
            }
            g.drawImage(tglCalcSto.getImage(), tglCalcSto.getXPosition() + bgX, tglCalcSto.getYPosition(), null);
            
            g.drawImage(txfCalc.getImage(), txfCalc.getXPosition() + bgX, txfCalc.getYPosition(), null);
            for (BasicTextfield txf : txfCalcStoreArr.clone()) {
                g.drawImage(txf.getImage(), txf.getXPosition() + bgX, txf.getYPosition(), null);
            }
            
            //<editor-fold defaultstate="collapsed" desc="Panel draw">
            FontMetrics fm2 = g.getFontMetrics();
            //Panel
            g.setColor(Color.white);
            g.drawImage(panel, 0, s.getHeight()-panel.getHeight(null), null);//pnlHeight = 59
            g.setFont(new Font("DS MAN",Font.PLAIN,botTitleSize));
            g.drawString("Refraction", 10, s.getHeight()-12);
            //Screen
            switch (screen) {
                case 0://game
                    g.drawString("Game", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 1: //Draw
                    g.drawString("Draw pad", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 2: // Calc
                    g.drawString("Calculator", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 3: //Circuit game
                    g.drawString("Circuits", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 4: //Formulae
                    g.drawString("Formulae", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                case 5: //Help
                    g.drawString("Help", pnlLeftEnd+15, s.getHeight()-12);
                    break;
                default:
            }
            //Info (info starts at pxl 232 of pnlRight info pane is 390) 390x56
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            int pnlAdjx = s.getWidth()- 390;
            int pnlAdjy = s.getHeight()- 56 + fm2.getAscent();
            g.drawString(String.format("%-8s%30s", "Total:", "" + Main.curProfile.getRefractionpoints()), pnlAdjx +8, pnlAdjy -3);
            g.drawString(String.format("%-8s%30s", "Deaths:", "" + Main.curProfile.getRefractiondeaths()), pnlAdjx +8, pnlAdjy + 22);
            g.drawString(String.format("%-8s%30.2f", "Average:", (float)Main.curProfile.getRefractionpoints()/Main.curProfile.getRefractiondeaths()), pnlAdjx +200, pnlAdjy-3);
            g.drawString(String.format("%-8s%30s", "Best:", Main.curProfile.getRefractionhighscore()), pnlAdjx +200, pnlAdjy + 22);
            
            g.drawImage(pnlTime, s.getWidth()-pnlTime.getWidth(null), 0, null);
            String time =String.format("%1$02d:%2$02d:%3$02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND));
            g.drawString(time, s.getWidth()-90, 16);
            
//</editor-fold>
        //Buttons
            for (Button btn : panelButtons.clone()) {
                g.drawImage(btn.getImage(), btn.getXPosition(), btn.getYPosition(), null);
            }
        }//g disposed of later
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Game">
        private void beginGame(){
            
            timeLeft = 0L;
            score = 0;
            //Provides limited amounts of indices
            for (RefractionLayer ref : refList) {
                ref.setAvailable(false);
                ref.setUnlocked(false);
            }
            availableRefList = new ArrayList();
            availableRefList.add(refList.get(0));
            refList.get(0).setAvailable(true);
            refList.get(0).setUnlocked(true);
            int c = 0;
            while (availableRefList.size() != 5) {
                if (refList.size() == c) {
                    c = 0;
                }
                if (!refList.get(c).isAvailable() && Math.round(Math.random() * 2 * refList.size()) != 1) {
                    refList.get(c).setAvailable(true);
                    availableRefList.add(refList.get(c));
                }
                c++;
            }
            numLayers = 3;
            setupPuzzle();
            
            
            
            //<editor-fold defaultstate="collapsed" desc="Circuit game">
            setupCircuit();
//</editor-fold>
        }
        
        private void setupPuzzle(){
            if (score == 0) {
                timeLeft = 500000;
            }else{
                timeLeft = 100000 + 50000*score;
            }
            //Layers
            curLayers = new int[numLayers];
            ansLayers = new int[numLayers];
            ansLayers[0] = 0;//Vacuum
            curLayers[0] = 0;//Vacuum
            for (int i = 1; i < numLayers; i++) {
                ansLayers[i] = (int)Math.round(Math.random()*(availableRefList.size()-1)-0.5)+1;
                curLayers[i] = 0;
            }
            //start position
            startX = (int)Math.round(Math.random()*(s.getWidth()-158-pnlTime.getWidth(null)) + 109);
            do {                
                startAngle = (int)Math.round(Math.random()*(100)-50);
            } while (startAngle < 10 && startAngle > -10);
            
//Test refraction path
            boolean isRight = false;
            double curX = startX;
            int tries = 0;
            int layerArea = (s.getHeight()-panel.getHeight(null))/numLayers;
            while (!isRight) {
                if (tries > 25) {
                    startX = (int)Math.round(Math.random()*(s.getWidth()-115-pnlTime.getWidth(null))+20);
                }
                do {                
                    startAngle = (int)Math.round(Math.random()*(100));
                } while (startAngle < 10 && startAngle > -10);
                double maxX = curX;
                double minX = curX;
                double curAngle;
                //Simulate
                curX =  startX;
                curAngle = startAngle;
                
                curX +=  layerArea*Math.tan(Math.toRadians(curAngle));//gets the x change
                if (curX > maxX) {
                    maxX = curX;
                }
                if (curX < minX) {
                    minX = curX;
                }
                
                for (int i = 1; i < numLayers; i++) {
                    curAngle =Math.toDegrees( Math.asin((availableRefList.get(ansLayers[i-1]).getIndex()*Math.sin(Math.toRadians(curAngle)))/availableRefList.get(ansLayers[i]).getIndex()));
                    curX +=  layerArea*Math.tan(Math.toRadians(curAngle));//gets the x change
                    
                    if (curX > maxX) {
                        maxX = curX;
                    }
                    if (curX < minX) {
                        minX = curX;
                    }
                }
                if (maxX < s.getWidth()-pnlTime.getWidth(null)-75 &&
                        minX > 0) {
                    isRight = true;
                }
                tries++;
            }
            targetX = (int) curX;
            
            drawGameImage();
            //Sort ansLayers for checkGame
            for (int i = 0; i < ansLayers.length-1; i++) {
                for (int j = i+1; j < ansLayers.length; j++) {
                    if (ansLayers[i] > ansLayers[j]) {
                        int temp = ansLayers[i];
                        ansLayers[i] = ansLayers[j];
                        ansLayers[j] = temp;
                    }
                }
            }
            
        }
        
        private void setupCircuit(){
            circuitUnlock = -1;
            gameCircuit = new CircuitLink();
            for (int i = 0; i < 9; i++) {
                gameCircuit.addConnectionPoint("" + i);
            }
            gameCircuit.addLink(""+0, ""+1, (int)Math.round(Math.random()*10));
            gameCircuit.addLink(""+0, ""+3, 0);
            gameCircuit.addLink(""+1, ""+4, 0);
            gameCircuit.addLink(""+1, ""+2, (int)Math.round(Math.random()*10));
            gameCircuit.addLink(""+2, ""+5, 0);
            gameCircuit.addLink(""+3, ""+4, (int)Math.round(Math.random()*10));
            gameCircuit.addLink(""+3, ""+6, 0);
            gameCircuit.addLink(""+4, ""+7, 0);
            gameCircuit.addLink(""+4, ""+5, (int)Math.round(Math.random()*10));
            gameCircuit.addLink(""+5, ""+8, 0);
            gameCircuit.addLink(""+6, ""+7, (int)Math.round(Math.random()*10));
            gameCircuit.addLink(""+7, ""+8, (int)Math.round(Math.random()*10));
            do {                
                gameCircuit.genRandomPath("3", "5");
                circuitAns = gameCircuit.getCurrent(6, "3", "5");
                gameCircuit.resetActiveLinks();
                
                if (!Float.isFinite(circuitCur)) {//Infinity if has path of no resistance
                        circuitCur = 0f;
                }
            } while (circuitAns == 0);
            
            updateCircuit();
        }
        
        private void checkGame(){
            int[] userAns = curLayers.clone();//Sort ansLayers to match curLayers
            for (int i = 0; i < userAns.length-1; i++) {
                for (int j = i+1; j < userAns.length; j++) {
                    if (userAns[i] > userAns[j]) {
                        int temp = userAns[i];
                        userAns[i] = userAns[j];
                        userAns[j] = temp;
                    }
                }
            }
            if (Arrays.equals(userAns, ansLayers) && !btnNext.isVisible() & !btnReset.isVisible()) {
                score++;
                if (score%10 == 0 && numLayers != 20) {//Increase num of layers every 10 points
                    numLayers++;
                }
                if (score%5 == 0 && availableRefList.size() != refList.size()) {//Increase available indices every 10 points
                    int size = availableRefList.size()+1;
                    int c = 0;
                    while (availableRefList.size() != size) {
                        if (refList.size() == c) {
                            c = 0;
                        }
                        if (!refList.get(c).isAvailable() && Math.round(Math.random() * 2 * refList.size()) != 1) {
                            refList.get(c).setAvailable(true);
                            availableRefList.add(refList.get(c));
                        }
                        c++;
                    }
                }
                btnNext.setVisible(true);
            }
        }
        
        private void drawGameImage(){
            BufferedImage tempImg = new BufferedImage(s.getWidth()-pnlTime.getWidth(null)-75,s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D) tempImg.createGraphics();
            
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,14));
            int layerArea = (s.getHeight()-panel.getHeight(null))/numLayers;
            for (int i = 0; i < numLayers; i++) {
                g.setColor(availableRefList.get(curLayers[i]).getClrLayer());
                g.fillRect(0, i*layerArea,s.getWidth()-pnlTime.getWidth(null), layerArea+20);
                g.setColor(Color.white);
                g.drawLine(0, i*layerArea, s.getWidth()-pnlTime.getWidth(null), i*layerArea);
            }
            
            Color layerColor = availableRefList.get(curLayers[curLayers.length-1]).getClrLayer();
            g.setColor(new Color(255-layerColor.getRed(),255-layerColor.getGreen(),255-layerColor.getBlue()));
            g.fillOval(targetX-4,s.getHeight()-panel.getHeight(null) -4, 8, 8);
            g.setColor(Color.white);
            g.fillOval(startX-4,-4, 8, 8);
            //Beam
            //Simulate
            //layers start at 150
            double curX = startX;
            double oldX;
            double curAngle;
            curAngle = startAngle;
            
            oldX = curX;
            curX +=  layerArea*Math.tan(Math.toRadians(curAngle));//gets the x change
            g.drawLine((int)oldX, 0, (int)curX, layerArea);
            g.drawString(availableRefList.get(curLayers[0]).getName(), 10,15);
            g.drawString("Refractive Index:", 10, 30);
            g.drawString(""+availableRefList.get(curLayers[0]).getIndex(), 10, 45);
            for (int i = 1; i < numLayers; i++) {
                layerColor = availableRefList.get(curLayers[i]).getClrLayer();
                g.setColor(new Color(255-layerColor.getRed(),255-layerColor.getGreen(),255-layerColor.getBlue()));
                oldX = curX;
                curAngle =Math.toDegrees( Math.asin((availableRefList.get(curLayers[i-1]).getIndex()*Math.sin(Math.toRadians(curAngle)))/availableRefList.get(curLayers[i]).getIndex()));
                curX +=  layerArea*Math.tan(Math.toRadians(curAngle));//gets the x change
                g.drawLine((int)oldX, i*layerArea, (int)curX, (i+1)*layerArea);
                g.drawString(availableRefList.get(curLayers[i]).getName(), 10, layerArea*i+15);
                g.drawString("Refractive Index:", 10, layerArea*i+30);
                g.drawString(""+availableRefList.get(curLayers[i]).getIndex(), 10, layerArea*i+45);
            }
            gameImg = tempImg;
            tempImg.flush();
            g.dispose();
        }
        
        private void updateCircuit(){
            BufferedImage tempImg = new BufferedImage(s.getWidth()-300,s.getHeight()-100,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = tempImg.createGraphics();
            int branchWidth = (s.getWidth()-400)/2;
            int branchHeight = (s.getHeight()-200)/2;
            //Draw circuit image
            ArrayList<byte[]> l = gameCircuit.getLinks();
            for (int i = 0; i < l.size(); i++) {//Used to get linked list elements
                byte[] link = l.get(i);
                if (gameCircuit.getActiveLinks().get(i)) {
                    g.setColor(Color.LIGHT_GRAY);
                }else{
                    g.setColor(Color.DARK_GRAY);
                }
                for (int j = 0; j < 5; j++) {
                    if (link[1] == link[0] +1) {//Horizontal
                        g.drawLine(
                                50+ ((link[0]%3)*branchWidth),
                                50+ ((link[0]/3)*branchHeight) + j,
                                50+ ((link[1]%3)*branchWidth),
                                50+ ((link[1]/3)*branchHeight) + j);
                    }else{
                        g.drawLine(
                                50+ ((link[0]%3)*branchWidth) + j,
                                50+ ((link[0]/3)*branchHeight),
                                50+ ((link[1]%3)*branchWidth) + j,
                                50+ ((link[1]/3)*branchHeight));
                    }
                }
                
                if (gameCircuit.getLinkResistances().get(i) != 0f) {
                    FontMetrics fm = g.getFontMetrics();
                    g.setColor(Color.black);
                    
                    String res = String.format("%.2f   ohm", gameCircuit.getLinkResistances().get(i));
                    int stringWidth = (int)fm.getStringBounds(res, g).getWidth();
                    g.setFont(new Font("DBXLNightfever",Font.PLAIN,14));
                    int width = ((link[1]%3)*branchWidth)- ((link[0]%3)*branchWidth);
                    
                    g.fillRect(50+ ((link[0]%3)*branchWidth) + width/2 -stringWidth/2 -2,//x
                            50+ ((link[0]/3)*branchHeight)-7,//y
                            stringWidth +4,//width
                            fm.getHeight()+7);//height
                    g.setColor(Color.white);
                    g.drawString(res, 50+ ((link[0]%3)*branchWidth) +width/2 -stringWidth/2, 50+ ((link[0]/3)*branchHeight) + fm.getAscent() -5);
                }
            }
            
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < 5; i++) {
                g.drawLine(50+ 2*branchWidth, 50+branchHeight + i, 50+ 2*branchWidth+100,50+branchHeight + i);
            }
            
            for (int i = 0; i < 9; i++) {
                g.fillOval(50+ (i%3)*branchWidth - 3,//x
                            50+ ((i/3)*branchHeight)-3,//y
                            10,//width
                            10);//height
            }
            g.dispose();
            circuitImage = tempImg;
            tempImg.flush();
            //Get circuit status
            circuitCur = gameCircuit.clone().getCurrent(6, "3", "5");
            if (!Float.isFinite(circuitCur)) {
                circuitCur = 0f;
            }
            
            
            if (circuitUnlock < 0) {
                if ((int)(circuitCur*10) == (int)(circuitAns*10)) {
                    int c =0;
                    while (c < availableRefList.size() && availableRefList.get(c).isUnlocked()) {
                        c++;
                    }
                    if (c >= availableRefList.size()) {
                        circuitUnlock = -2;
                    }else{
                        circuitUnlock = c;
                        availableRefList.get(c).setUnlocked(true);
                    }
                }
            }
        }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Other">
        private void clearOverlay(){
            overlay = new BufferedImage(s.getWidth(),s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            pen.setGraphics((Graphics2D)overlay.getGraphics());
        }
        
        private void updatePanelButtonHoverImage(int sc){
            int oldBgx = bgX;
            bgX = s.getWidth()*sc*-1;//Used to draw the certain screen
            BufferedImage tempImg = new BufferedImage((int)(275*widthAdj)+65, (int)(250*heightAdj),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D)tempImg.getGraphics();
            if (Main.curProfile.getSettingantialiasing()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.drawImage(panelButtons[sc].getDefaultImage(),0, 0, null);
            String title;
            switch (sc) {
                case 0:
                    title = "Refraction game";
                    g.setColor(new Color(82,37,82));
                    break;
                case 1:
                    title = "Drawing pad";
                    g.setColor(new Color(15,117,0));
                    break;
                case 2:
                    title = "Calculator";
                    g.setColor(new Color(255,201,14));
                    break;
                case 3:
                    title = "Circuit game";
                    g.setColor(new Color(217,8,0));
                    break;
                case 4:
                    title = "Formulae";
                    g.setColor(new Color(187,0,0));
                    break;
                case 5:
                    title = "Help";
                    g.setColor(new Color(63,72,204));
                    break;
                default:
                    title = "";
            }
            g.fillRect(65, 0, tempImg.getWidth()-65, tempImg.getHeight());
            BufferedImage fakeBG = new BufferedImage(s.getWidth(), s.getHeight()-panel.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = (Graphics2D)fakeBG.getGraphics();
            g2.setColor(Color.black);
            
            draw(g2);//Draws to the tempImg
            
            g2.dispose();
            g.setFont(new Font("DBXLNightfever",Font.PLAIN,36));
            g.setColor(Color.black);
            g.drawString(title, 80, 40);
            g.drawImage(Main.resizeImage(fakeBG, (int)(255*widthAdj), (int)(175*heightAdj), BufferedImage.TYPE_4BYTE_ABGR),65+(int)(10*widthAdj),65,null);
            panelButtons[sc].setHoverImage(tempImg);
            g.dispose();
            tempImg.flush();
            bgX = oldBgx;
        }
        
        public void screenTransition(int newScreen){
            pen.setPenDown(false);
            updatePanelButtonHoverImage(screen);
            panelButtons[screen].setYPosition(panelButtons[newScreen].getYPosition());
            int dispBG = (s.getWidth()*newScreen*-1) -bgX;
            //button goes in
            for (int i = 0; i < 13; i++) {
                panelButtons[newScreen].setXPosition(panelButtons[newScreen].getXPosition()-5);
                bgX += dispBG/26;
                draw(s.getGraphics());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                }
            }
            //button goes out
            for (int i = 0; i < 13; i++) {
                panelButtons[screen].setXPosition(panelButtons[screen].getXPosition()+5);
                bgX += dispBG/26;
                draw(s.getGraphics());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                }
            }
            bgX = s.getWidth()*newScreen*-1;//Goes opposite to direction of movement
            panelButtons[newScreen].setXPosition(-65);
            panelButtons[screen].setXPosition(0);
            screen = newScreen;
        }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listeners">
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pen.setPenDown(false);
                for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                    clc.mouseClicked(e.getXOnScreen()-bgX, e.getYOnScreen());
                }
                int c = 0;
                boolean isClicked = false;
                do {    
                    if (panelButtons[c].inBounds(e.getX(), e.getY())) {
                        isClicked = true;
                        panelButtons[c].mouseClicked(e.getX(), e.getY());
                        panelButtons[c].setStatus((byte) 0);
                    }
                    //Prevents lingering hover image
                    c++;
                } while (c < 6 && !isClicked);
                
                btnExit.mouseClicked(e.getX(), e.getY());
                
                tglCalcSto.mouseClicked(e.getX()-bgX, e.getY());
                
                if (!btnReset.isVisible() && !btnNext.isVisible()) {
                    
                    //<editor-fold defaultstate="collapsed" desc="Indices">
                    int layerArea = (s.getHeight()-panel.getHeight(null))/numLayers;
                    if (e.getXOnScreen() > 75 && e.getXOnScreen() < s.getWidth()-pnlTime.getWidth(null) &&
                            e.getYOnScreen()> layerArea && e.getYOnScreen() < s.getHeight()-panel.getHeight(null) &&
                            screen == 0 && !btnNext.isVisible() && !btnReset.isVisible()) {
                        for (int i = 1; i < curLayers.length; i++) {
                            if (e.getYOnScreen() > i*layerArea && e.getYOnScreen() < (i+1)*layerArea) {
                                do {
                                    curLayers[i]++;
                                    if (curLayers[i] == availableRefList.size()) {
                                        curLayers[i] = 0;
                                    }
                                } while (!availableRefList.get(curLayers[i]).isUnlocked());
                            }
                            drawGameImage();

                        }
                    }
        //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="Circuit">
                    if (screen == 3) {
                        int branchWidth = (s.getWidth()-400)/2;
                        int branchHeight = (s.getHeight()-200)/2;
                        ArrayList<byte[]> l = gameCircuit.getLinks();
                        for (int i = 0; i < l.size(); i++) {//Used to get linked list elements
                            byte[] link = l.get(i);
                                if (link[1] == link[0] +1) {//Horizontal
                                    if (e.getXOnScreen() >= 150 + (link[0]%3)*branchWidth &&
                                            e.getXOnScreen() <= 150 + (link[1]%3)*branchWidth &&
                                            e.getYOnScreen() >= 50 + (link[0]/3)*branchHeight &&
                                            e.getYOnScreen() <= 50 + (link[0]/3)*branchHeight +5) {
                                        gameCircuit.setLinkActive("" + link[0], "" + link[1], !gameCircuit.getActiveLinks().get(i));
                                        updateCircuit();
                                    }
                                }else{
                                    if (e.getYOnScreen() >= 50 + (link[0]/3)*branchHeight &&
                                            e.getYOnScreen() <= 50 + (link[1]/3)*branchHeight &&
                                            e.getXOnScreen() >= 150 + (link[0]%3)*branchWidth &&
                                            e.getXOnScreen() <= 150 + (link[0]%3)*branchWidth +5) {
                                        gameCircuit.setLinkActive("" + link[0], "" + link[1], !gameCircuit.getActiveLinks().get(i));
                                        updateCircuit();
                                    }
                                }
                        }
                    }
    //</editor-fold>
                }
                
                btnReset.mouseClicked(e.getX(), e.getY());
                btnNext.mouseClicked(e.getX(), e.getY());
                checkGame();
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            for (Clickable btn : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                btn.mouseClicked(e.getX(), e.getY());
            }
            
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pen.setPenDown(false);
                for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                    clc.mouseReleased(e.getXOnScreen()-bgX, e.getYOnScreen());
                }
                
                btnReset.mouseReleased(e.getX(), e.getY());
                btnExit.mouseReleased(e.getX(), e.getY());
                
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (screen == 1) {
                pen.update(e.getXOnScreen(), e.getYOnScreen());//+65 cos of panel limit
                pen.setPenDown(true);
            }
            if (screen == 3) {
                formulaePosition += e.getYOnScreen() - ptMouse.y; 
                if (imgFormulae.getHeight(null) <= s.getHeight()-200 || formulaePosition >0) {
                    formulaePosition = 0;
                }else{
                    if (s.getHeight()-200 >= imgFormulae.getHeight(null) +formulaePosition) {
                        formulaePosition = s.getHeight()-200 - imgFormulae.getHeight(null);
                    }
                }
            }
            if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK && screen == 5) {
                if (e.getXOnScreen() >= (int)(65 + 15*widthAdj) && e.getXOnScreen() < (int)(65 + 15*widthAdj) + helpImage.getWidth(null) &&
                        e.getYOnScreen() >= (int)(35*heightAdj) + 45 && e.getYOnScreen() < (int)(35*heightAdj) + helpImage.getHeight(null)) {
                    helpPos += e.getYOnScreen() - ptMouse.y;
                    if (helpText.getHeight(null) < helpImage.getHeight(null)-50) {
                        helpPos = 0;
                    }else{
                        if (helpPos > 0) {
                            helpPos = 0;
                        }
                        if (helpPos + helpText.getHeight(null) < helpImage.getHeight(null)-50) {
                            helpPos = (helpImage.getHeight(null))-helpText.getHeight(null)-50;
                        }
                    }
                    
                }
            }
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            for (Clickable clc : clickableList.toArray(new Clickable[0])) {//Avoids concurrent exception
                clc.mouseMoved(e.getXOnScreen(), e.getYOnScreen());
            }
            
            for (Button btn : panelButtons.clone()) {
                if (btn.getXPosition()>=0 && e.getXOnScreen() <= 65 && e.getYOnScreen() >= btn.getYPosition() && e.getYOnScreen() <= btn.getYPosition()+65) {//Prevents the hover image staying since it expands over all bttons
                    btn.mouseMoved(e.getXOnScreen(), e.getYOnScreen());
                }else{
                    btn.mouseMoved(e.getXOnScreen(), -1);//Take off hover image
                }
            }
            
            btnReset.mouseMoved(e.getX(), e.getY());
            btnExit.mouseMoved(e.getX(), e.getY());
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (screen == 3) {
                formulaePosition += e.getWheelRotation()*5;
                if (imgFormulae.getHeight(null) <= s.getHeight()-200 || formulaePosition >0) {
                    formulaePosition = 0;
                }else{
                    if (s.getHeight()-200 >= imgFormulae.getHeight(null) +formulaePosition) {
                        formulaePosition = s.getHeight()-200 - imgFormulae.getHeight(null);
                    }
                }
            }
        }
        
//</editor-fold>
        
        private class RefractionLayer{
            private float index;
            private String name;
            private boolean available;
            private boolean unlocked;
            private Color clrLayer;
            
            public RefractionLayer(String name, float indexValue,Color lyrColor){
                this.name = name;
                index = indexValue;
                clrLayer = lyrColor;
                available = false;
                unlocked = false;
            }
            
            /**
     *Sets the object's clrLayer.
     * @param  newClrLayer new clrLayer
     * */
            public void setClrLayer (Color newClrLayer){
                    clrLayer = newClrLayer;
            }

            /**
                 *Gets the object's clrLayer.
                 * @return  The object's clrLayer.
                 * */
            public Color getClrLayer (){
                    return clrLayer;
            }

            /**
                 *Sets the object's name.
                 * @param  newName new name
                 * */
            public void setName (String newName){
                    name = newName;
            }

            /**
                 *Gets the object's name.
                 * @return  The object's name.
                 * */
            public String getName (){
                    return name;
            }

            /**
                 *Sets the object's index.
                 * @param  newIndex new index
                 * */
            public void setIndex (float newIndex){
                    index = newIndex;
            }

            /**
                 *Gets the object's index.
                 * @return  The object's index.
                 * */
            public float getIndex (){
                    return index;
            }

            /**
            *Sets whether the object is available.
            * @param  newAvailable new available
            * */
            public void setAvailable (boolean newAvailable){
                    available = newAvailable;
            }

            /**
            *Gets whether the object is available.
            * @return  The object's available.
            * */
            public boolean isAvailable (){
                    return available;
            }

            /**
            *Sets whether the object is unlocked.
            * @param  newUnlocked new unlocked
            * */
            public void setUnlocked (boolean newUnlocked){
                    unlocked = newUnlocked;
            }

            /**
            *Gets whether the object is unlocked.
            * @return  The object's unlocked.
            * */
            public boolean isUnlocked (){
                    return unlocked;
            }
        }
    }
    
    private class ResearchScene extends Scene{
        private Image bg;
        private Button btnBack;
        private Button btnBackToCat;
        private LinkedHashMap <Button,String> sidePanelMap;
        private int sidePanelPosition;
        private Image imgItemBox;
        private Image curText;
        private String curTextResID;
        private int curTextPosition;
        private Point ptMouse;
        private String curCategory;
        private String moving;
        private boolean ctrlDown;
        
        private Button newCatButton;
        private BasicTextfield newCatTextfield;
        private Image newCatBg;
        private boolean newCatVisible;
        
        private Button[] voteButtonList;
        
        @Override
        protected void customInit() {
            //init Variables
            ptMouse= new Point(0,0);
            sidePanelMap = new LinkedHashMap();
            sidePanelPosition = 0;
            curText = new BufferedImage(400,400,BufferedImage.TYPE_3BYTE_BGR);
            curTextPosition = 0;
            //Background
            BufferedImage tempImg = new BufferedImage(s.getWidth(),s.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = tempImg.createGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, s.getWidth(), s.getHeight());
            g.drawImage(new ImageIcon(getClass().getResource("/Images/Research/ResearchTitle.png")).getImage(), 0, 0, null);
            g.dispose();
            bg = tempImg;
            tempImg.flush();
            
            //btnBack
            Image btnImage = new ImageIcon(getClass().getResource("/Images/Research/BackButton.png")).getImage();
            btnBack = new Button(btnImage,s.getWidth()-btnImage.getWidth(null),0,true){
                @Override
                protected void clickEvent() {
                    newScene = 0;
                }
            };
            
            //btnBackToCat
            btnBackToCat = new Button(new ImageIcon(getClass().getResource("/Images/Research/BackToCatButton.png")).getImage(),0,s.getHeight()-50,false){
                @Override
                protected void clickEvent() {
                    loadCategories();
                }
            };
            
            //ItemBox
            tempImg = new BufferedImage(s.getWidth()-300, (s.getHeight()-150),BufferedImage.TYPE_3BYTE_BGR);
            g = tempImg.createGraphics();
            g.setColor(new Color(215,0,5));
            for (int i = 0; i < 5; i++) {
                g.drawLine(i, 20, i, tempImg.getHeight()-20);//vert line
                g.drawLine(20, i, tempImg.getWidth(), i);//top line
                g.drawLine(20, tempImg.getHeight()-i, tempImg.getWidth(), tempImg.getHeight()-i);//bot Line
                g.drawLine(i, 20, 20, i);//top corner
                g.drawLine(i,tempImg.getHeight()-20,20,tempImg.getHeight()-i);//top corner
            }
            imgItemBox = tempImg;
            g.dispose();
            tempImg.flush();
            
            //<editor-fold defaultstate="collapsed" desc="CurText Startup setup">
            curText = Main.getTextImage("This is the research area. It contains all the research that is stored on your device and allows you to view, categorise and delete your research.\n\n"
            + "To view your research navigate through categories (Uncategories research is stored in Unsorted.) using the panel on the left. Yo can scroll through the options by dragging your mouse over the panel.\n"
            + "To select categories or research simply click on them. To go back to category view press the \"Back to categories\" button.\n\n"
            + "To Categorise your research simply go to the entry you want to move to a new category and ctrl + click it.\n"
            + "You will then be taken back to the category selection and which ever category you choose now the selected research will be moved to.\n"
            + "You will also notice that there are two extra options \"Add category\" and \"Delete\"\n"
            + "\"Add category\" brings up another panel which allows you to add a new category. (Note: clicking outside of the panel will close it.)\n"
            + "\"Delete\" will delete the research from your selection.\n\n"
            + "When you select a research item you will be able to vote on it if you have not already. To vote click a number that referes to your liking of the article.\n"
            + "1 being terribly unhelpful and 5 being extremely helpful.\n"
            + "Once you have voted you will be shown the overall voting rating.",
                    imgItemBox.getWidth(null)-20, new Color(215,0,5), new Font("Arial",Font.PLAIN,14), false);
//</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="New category panel (newCat)">
            newCatButton = new Button(new ImageIcon(getClass().getResource("/Images/Research/newCatergoryButton.png")).getImage(), s.getWidth()/2 - 206, s.getHeight()/2 +5,true) {
                @Override
                protected void clickEvent() {
                    if (!newCatTextfield.getText().equals("")) {
                        int prIndex = Main.dbm.getPersonalResearchByResearchID(moving);
                        Main.dbm.personalResearchList.get(prIndex).setCategory(newCatTextfield.getText());
                        Main.dbm.updateRecords();
                        loadCategory(newCatTextfield.getText());
                        newCatVisible = false;
                    }
                }
            };
            newCatTextfield = new BasicTextfield(s.getWidth()/2 - 206,
                    s.getHeight()/2 - 44,
                    new ImageIcon(getClass().getResource("/Images/Research/newCatergoryTextfield.png")).getImage(),
                    new Font("DBXLNightfever",Font.PLAIN,16),5,5,5,5);
            newCatBg = new ImageIcon(getClass().getResource("/Images/Research/newCatergoryPanel.png")).getImage();
            newCatVisible = false;
//</editor-fold>
            
            //Vote buttons
            voteButtonList = new Button[5];
            for (int i = 0; i < 5; i++) {
                tempImg = new BufferedImage(20,20,BufferedImage.TYPE_3BYTE_BGR);
                g = tempImg.createGraphics();
                g.setColor(new Color(215,0,5));
                g.fillRect(0, 0, 20, 20);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,18));
                g.setColor(Color.black);
                g.drawString("" + (i+1), 5, 18);
                g.dispose();
                voteButtonList[i] = new Button(tempImg,s.getWidth() -25*(5-i),100+imgItemBox.getHeight(null) + 10,false) {
                    @Override
                    protected void clickEvent() {
                        byte c = 1;
                        while (!voteButtonList[c-1].equals(this)) {
                            c++;
                        }
                        System.out.println(c);
                        Main.dbm.voteResearch(Main.dbm.researchList.get(Main.dbm.getResearchByID(curTextResID)), c);
                        for (Button vb : voteButtonList) {
                            vb.setVisible(false);
                        }
                    }
                };
                tempImg.flush();
            }
            
            //Finalize
            loadCategories();
        }

        @Override
        public void update(long timePassed) {
        }

        @Override
        public void draw(Graphics2D g) {
            g.drawImage(bg, 0, 0, null);
            g.drawImage(btnBack.getImage(), btnBack.getXPosition(), btnBack.getYPosition(), null);
            //CategoryButtons
            g.setColor(new Color(215,0,5));
            g.setFont(new Font("DS MAN",Font.PLAIN,30));
            if (curCategory == null) {
                g.drawString("Categories:", 5, 120);
            }else{
                g.drawString(curCategory + ":", 5, 120);
            }
            BufferedImage tempImg = new BufferedImage(225,s.getHeight()-200,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2 = tempImg.createGraphics();
            for (Button b : sidePanelMap.keySet().toArray(new Button[0])) {
                g2.drawImage(b.getImage(),b.getXPosition(),b.getYPosition() + sidePanelPosition,null);
            }
            g.drawImage(tempImg, 0, 125, null);
            g2.dispose();
            tempImg.flush();
            
            if (btnBackToCat.isVisible()) {
                g.drawImage(btnBackToCat.getImage(), btnBackToCat.getXPosition(), btnBackToCat.getYPosition(), null);
            }
            
            //Draw item area
            g.drawImage(imgItemBox, s.getWidth()-imgItemBox.getWidth(null), 100, null);
            //Draw text
            g.setFont(new Font("DS MAN",Font.PLAIN,24));
            if (curTextResID == null) {
                g.drawString("Welcome to your personal library!", s.getWidth() - imgItemBox.getWidth(null), 92);
            }else{
                g.drawString(curTextResID.substring(36).trim() + " by " + curTextResID.substring(0,36).trim().substring(12).trim(), s.getWidth() - imgItemBox.getWidth(null), 92);
            }
            tempImg = new BufferedImage(imgItemBox.getWidth(null)-20,imgItemBox.getHeight(null)-40,BufferedImage.TYPE_3BYTE_BGR);//Used to get font metrics
            g2 = tempImg.createGraphics();
            g2.drawImage(curText, 0, curTextPosition, null);
            g2.dispose();
            g.drawImage(tempImg, s.getWidth()-imgItemBox.getWidth(null)+20,120, null);
            tempImg.flush();
            //Vote
            if (voteButtonList[0].isVisible()) {
                for (Button vb : voteButtonList.clone()) {
                    g.drawImage(vb.getImage(), vb.getXPosition(), vb.getYPosition(), null);
                }
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,20));
                g.drawString("Vote: ", s.getWidth()-183, 125+imgItemBox.getHeight(null));
            }else{
                if (curTextResID != null) {
                    g.drawString(String.format("Rating: %.2f" , Main.dbm.researchList.get(Main.dbm.getResearchByID(curTextResID)).getRating()), s.getWidth()-183, 125+imgItemBox.getHeight(null));
                }
            }
            
            //newCat panel
            if (newCatVisible) {
                g.drawImage(newCatBg, s.getWidth()/2 - newCatBg.getWidth(null)/2, s.getHeight()/2 - newCatBg.getHeight(null)/2, null);
                g.drawImage(newCatButton.getImage(), newCatButton.getXPosition(), newCatButton.getYPosition(), null);
                g.drawImage(newCatTextfield.getImage(), newCatTextfield.getXPosition(), newCatTextfield.getYPosition(), null);
            }
        }
        
        private void loadCategories(){
            btnBackToCat.setVisible(false);
            sidePanelPosition = 0;
            curCategory = null;
            sidePanelMap.clear();
            ArrayList<String> categoryList = new ArrayList();
            for (PersonalResearchEntity pr : Main.dbm.personalResearchList.toArray(new PersonalResearchEntity[0])) {
                if (pr.getUserid().equals(Main.curProfile.getId())) {
                    if (pr.getCategory() == null) {
                        if (!categoryList.contains("Unsorted") && !pr.getDeleted()) {
                            categoryList.add("Unsorted");
                        }
                    }else{
                        if (!categoryList.contains(pr.getCategory())  && !pr.getDeleted()) {
                            categoryList.add(pr.getCategory());
                        }
                    }
                }
            }
            
            Image imgButton = new ImageIcon(getClass().getResource("/Images/Research/CategoryButton.png")).getImage();
            
            for (int i = 0; i < categoryList.size();i++) {
                BufferedImage tempImg = new BufferedImage(225,50,BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = tempImg.createGraphics();

                if (Main.curProfile.getSettingantialiasing()) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                g.setColor(Color.black);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
                FontMetrics fm = g.getFontMetrics();
            
                g.drawImage(imgButton, 0, 0, null);
                g.drawString(categoryList.get(i), 5, imgButton.getHeight(null)/2 +3);
                sidePanelMap.put(new Button(tempImg,0,60*i,true){

                    @Override
                    protected void clickEvent() {
                        loadCategory(sidePanelMap.get(this));
                    }
                },categoryList.get(i));
                g.dispose();
                tempImg.flush();
            }
        }
        
        private void listCategoriesForMove(){
            sidePanelMap.clear();
            btnBackToCat.setVisible(false);
            curCategory = null;
            ArrayList<String> categoryList = new ArrayList();
            for (PersonalResearchEntity pr : Main.dbm.personalResearchList.toArray(new PersonalResearchEntity[0])) {
                if (pr.getUserid().equals(Main.curProfile.getId())) {
                    if (pr.getCategory() == null) {
                        if (!categoryList.contains("Unsorted")  && !pr.getDeleted()) {
                            categoryList.add("Unsorted");
                        }
                    }else{
                        if (!categoryList.contains(pr.getCategory())  && !pr.getDeleted()) {
                            categoryList.add(pr.getCategory());
                        }
                    }
                }
            }
            
            Image imgButton = new ImageIcon(getClass().getResource("/Images/Research/CategoryButton.png")).getImage();
            //Draw category buttons
            for (int i = 0; i < categoryList.size();i++) {
                BufferedImage tempImg = new BufferedImage(225,50,BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = tempImg.createGraphics();

                if (Main.curProfile.getSettingantialiasing()) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                g.setColor(Color.black);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            
                g.drawImage(imgButton, 0, 0, null);
                g.drawString(categoryList.get(i), 5, imgButton.getHeight(null)/2 +3);
                sidePanelMap.put(new Button(tempImg,0,60*i,true){

                    @Override
                    protected void clickEvent() {
                        int prIndex = Main.dbm.getPersonalResearchByResearchID(moving);
                        if (sidePanelMap.get(this).equals("Unsorted")) {
                            Main.dbm.personalResearchList.get(prIndex).setCategory(null);
                        }else{
                            Main.dbm.personalResearchList.get(prIndex).setCategory(sidePanelMap.get(this));
                        }
                        Main.dbm.updateRecords();
                        loadCategory(sidePanelMap.get(this));
                    }
                },categoryList.get(i));
                g.dispose();
                tempImg.flush();
            }

                                        //<editor-fold defaultstate="collapsed" desc="Add Create new">
            BufferedImage tempImg = new BufferedImage(225,50,BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = tempImg.createGraphics();

                if (Main.curProfile.getSettingantialiasing()) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                g.setColor(Color.black);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            
                g.drawImage(imgButton, 0, 0, null);
                g.drawString("<New category>", 5, imgButton.getHeight(null)/2 +3);
            g.dispose();
            sidePanelMap.put(new Button(tempImg,0,60*sidePanelMap.size()-2,true){
                
                @Override
                protected void clickEvent() {
                    newCatVisible = true;
                }
            },"CREATENEW");
            tempImg.flush();
//</editor-fold>
                                        //<editor-fold defaultstate="collapsed" desc="Draw Delete">
            tempImg = new BufferedImage(225,50,BufferedImage.TYPE_3BYTE_BGR);
                g = tempImg.createGraphics();

                if (Main.curProfile.getSettingantialiasing()) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                g.setColor(Color.black);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,16));
            
                g.drawImage(imgButton, 0, 0, null);
                g.drawString("<Delete>", 5, imgButton.getHeight(null)/2 +3);
                g.dispose();
            sidePanelMap.put(new Button(tempImg,0,60*sidePanelMap.size()-1,true){
                
                @Override
                protected void clickEvent() {
                    int prIndex = Main.dbm.getPersonalResearchByResearchID(moving);
                    Main.dbm.personalResearchList.get(prIndex).setDeleted(true);
                    Main.dbm.updateRecords();
                    Main.dbm.testDeletedResearch();
                    loadCategories();
                }
            },"DELETE");
            tempImg.flush();
//</editor-fold>
        }
        
        private void loadCategory(String cat){
            btnBackToCat.setVisible(true);
            curCategory = cat;
            sidePanelMap.clear();
            ArrayList<String> itemList = new ArrayList();
            if (!cat.equals("Unsorted")) {//Put all research without category into unsorted
                for (PersonalResearchEntity pr : Main.dbm.personalResearchList.toArray(new PersonalResearchEntity[0])) {
                    if (pr.getCategory() != null) {
                        if (pr.getUserid().equals(Main.curProfile.getId()) 
                                && pr.getCategory().equals(cat) 
                                && !pr.getDeleted()) {
                            itemList.add(pr.getResearchid());
                        }
                    }
                }
            }else{
                for (PersonalResearchEntity pr : Main.dbm.personalResearchList.toArray(new PersonalResearchEntity[0])) {
                    if (pr.getCategory() == null) {
                        if (pr.getUserid().equals(Main.curProfile.getId()) 
                                && !pr.getDeleted()) {
                            itemList.add(pr.getResearchid());
                        }
                    }
                }
            }
            
            Image imgButton = new ImageIcon(getClass().getResource("/Images/Research/CategoryButton.png")).getImage();
            
            for (int i = 0; i < itemList.size();i++) {
                String title = itemList.get(i).substring(36).trim();
                
                //<editor-fold defaultstate="collapsed" desc="TempImg setup">
                BufferedImage tempImg = new BufferedImage(225,50,BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = tempImg.createGraphics();
                
                if (Main.curProfile.getSettingantialiasing()) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }
                
                g.setColor(Color.black);
                g.setFont(new Font("DBXLNightfever",Font.PLAIN,14));
                FontMetrics fm = g.getFontMetrics();
//</editor-fold>
            //Draw the btton list
                g.drawImage(imgButton, 0, 0, null);
                g.drawString(title, 5, imgButton.getHeight(null)/2 +3);
                sidePanelMap.put(new Button(tempImg,0,60*i,true){

                    @Override
                    protected void clickEvent() {
                        if (ctrlDown) {
                            moving = sidePanelMap.get(this);
                            listCategoriesForMove();
                        }else{
                            showResearch(sidePanelMap.get(this));
                        }
                    }
                },itemList.get(i));
                g.dispose();
                tempImg.flush();
            }
            sidePanelPosition = 0;
        }
        
        private void showResearch(String id){
            ResearchEntity res = Main.dbm.researchList.get(Main.dbm.getResearchByID(id));
            curTextResID = res.getId();
            String resText = res.getText();
            
            curText = Main.getTextImage(resText, imgItemBox.getWidth(null)-20, new Color(215,0,5), new Font("Arial",Font.PLAIN,14),false);
            if (!Main.dbm.personalResearchList.get(Main.dbm.getPersonalResearchByResearchID(id)).getVoted()) {
                for (Button vb : voteButtonList) {
                    vb.setVisible(true);
                }
            }else{
                for (Button vb : voteButtonList) {
                    vb.setVisible(false);
                }
            }
        }
        
//<editor-fold defaultstate="collapsed" desc="Listeners">
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (newCatVisible) {
                    if (e.getXOnScreen() < s.getWidth()/2 - newCatBg.getWidth(null)/2 ||
                        e.getXOnScreen() > s.getWidth()/2 + newCatBg.getWidth(null)/2 ||
                        e.getYOnScreen() < s.getHeight()/2 - newCatBg.getHeight(null)/2 ||
                        e.getYOnScreen() > s.getHeight()/2 + newCatBg.getHeight(null)/2) {
                    newCatVisible = false;
                    }else{
                        newCatTextfield.mouseClicked(e.getXOnScreen(), e.getYOnScreen());
                        newCatButton.mouseClicked(e.getXOnScreen(), e.getYOnScreen());
                    }
                }
                btnBack.mouseClicked(e.getXOnScreen(), e.getYOnScreen());
                if (e.getY() < s.getHeight()-75) {
                    for (Button b : sidePanelMap.keySet().toArray(new Button[0])) {
                        b.mouseClicked(e.getXOnScreen(), e.getYOnScreen()-sidePanelPosition-125);
                    }
                }
                btnBackToCat.mouseClicked(e.getXOnScreen(), e.getYOnScreen());
                for (Button vb : voteButtonList) {
                    vb.mouseClicked(e.getXOnScreen(), e.getYOnScreen());
                }
                btnBack.mouseReleased(e.getXOnScreen(), e.getYOnScreen());
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (newCatVisible) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    newCatTextfield.backspace();
                }else{
                    if ("qwertyuioplkjhgfdsazxcvbnm 1234567890".contains((""+e.getKeyChar()).toLowerCase())) {
                        newCatTextfield.typed(e.getKeyChar());
                    }
                    
                }
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) { 
            ctrlDown = e.isControlDown();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                if (e.getYOnScreen() >=125 && e.getYOnScreen() < s.getHeight()-150 && e.getXOnScreen() <= 225) {
                    sidePanelPosition += e.getYOnScreen() - ptMouse.y;
                    if (sidePanelMap.size()*60 <=  s.getHeight()-200) {
                        sidePanelPosition = 0;
                    }else{
                        if (sidePanelPosition >= 0) {
                            sidePanelPosition = 0;
                        }
                        if (sidePanelPosition <= (60*sidePanelMap.size() - (s.getHeight()-200))*-1) {
                            sidePanelPosition = (60*sidePanelMap.size() - (s.getHeight()-200))*-1;
                        }
                    }
                }
                //Scroll
                if (e.getYOnScreen() >= 110 && e.getYOnScreen() < 90 + imgItemBox.getHeight(null) && e.getXOnScreen() >= s.getWidth()-imgItemBox.getWidth(null)+20) {
                    curTextPosition += e.getYOnScreen() - ptMouse.y;
                    if (imgItemBox.getHeight(null)-40 >=  curText.getHeight(null)) {
                        curTextPosition = 0;
                    }else{
                        if (curTextPosition >= 0) {
                            curTextPosition = 0;
                        }
                        if (curTextPosition + curText.getHeight(null) <= imgItemBox.getHeight(null)-40) {
                            curTextPosition =  imgItemBox.getHeight(null)-40 -curText.getHeight(null) ;
                        }
                    }
                }
            }
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            
            ptMouse.x = e.getXOnScreen();
            ptMouse.y = e.getYOnScreen();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }
//</editor-fold>
        
    }
//</editor-fold>
}