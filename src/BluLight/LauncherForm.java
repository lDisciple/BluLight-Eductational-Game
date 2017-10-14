/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BluLight;

import Entities.PersonalResearchEntity;
import Entities.UserProfileEntity;
import UndefinedLibrary.Scrap.ScreenManager;
import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.RollbackException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jonathan Botha
 */
public class LauncherForm extends javax.swing.JFrame {

    /**
     * Creates new form LauncherForm
     */
    public LauncherForm() {
        initComponents();
        setIconImage(new ImageIcon(LoginForm.class.getResource("/Images/Logo.png")).getImage());
        
        setSize(this.getPreferredSize().width - pnlOptions.getWidth(), this.getPreferredSize().height);
        setLocation(Main.paneX -getWidth()/2, Main.paneY -getHeight()/2);
        //Gets compatible resolutions
        ArrayList<String> resList = new ArrayList();
        for (DisplayMode dm : ScreenManager.getDefaultCompatibleDisplayModes()) {
            if (!resList.contains(dm.getWidth() + "x" + dm.getHeight()) && dm.getHeight() >= 600 && dm.getWidth() >= 800) {
                resList.add(dm.getWidth() + "x" + dm.getHeight());
            }
        }
        cmbResolution.setModel(new javax.swing.DefaultComboBoxModel(resList.toArray(new String[0])));
        //Gets current settings.
        chkAntialiasing.setSelected(Main.curProfile.getSettingantialiasing());
        cmbResolution.setSelectedItem(Main.curProfile.getSettingresolution());
        cmbParticles.setSelectedIndex(Main.curProfile.getSettingparticleamount());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlLauncher = new javax.swing.JPanel();
        btnLaunch = new javax.swing.JButton();
        tglOptions = new javax.swing.JToggleButton();
        btnCreateResearch = new javax.swing.JButton();
        btnSignOut = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnShareResearch = new javax.swing.JButton();
        pnlOptions = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        chkAntialiasing = new javax.swing.JCheckBox();
        lblParticles = new javax.swing.JLabel();
        cmbParticles = new javax.swing.JComboBox();
        lblResolution = new javax.swing.JLabel();
        cmbResolution = new javax.swing.JComboBox();
        btnDelete = new javax.swing.JButton();
        btnExportUser = new javax.swing.JButton();
        btnImportUser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Launcher");
        setResizable(false);

        btnLaunch.setText("Launch BluLight");
        btnLaunch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaunchActionPerformed(evt);
            }
        });

        tglOptions.setText("Show options >>");
        tglOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglOptionsActionPerformed(evt);
            }
        });

        btnCreateResearch.setText("Create a research entry");
        btnCreateResearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateResearchActionPerformed(evt);
            }
        });

        btnSignOut.setText("Sign out");
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutActionPerformed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnShareResearch.setText("Share research");
        btnShareResearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareResearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLauncherLayout = new javax.swing.GroupLayout(pnlLauncher);
        pnlLauncher.setLayout(pnlLauncherLayout);
        pnlLauncherLayout.setHorizontalGroup(
            pnlLauncherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLauncherLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLauncherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLaunch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tglOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCreateResearch, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSignOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnShareResearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlLauncherLayout.setVerticalGroup(
            pnlLauncherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLauncherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLaunch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tglOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreateResearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShareResearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSignOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        chkAntialiasing.setText("Enable anti-aliasing");

        lblParticles.setText("Particle amount:");

        cmbParticles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Low", "Medium", "High", "Very high" }));

        lblResolution.setText("Resolution: ");

        btnDelete.setText("Delete profile...");
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnExportUser.setText("Export user profile...");
        btnExportUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportUserActionPerformed(evt);
            }
        });

        btnImportUser.setText("Import user profile...");
        btnImportUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlOptionsLayout = new javax.swing.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExportUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addComponent(lblParticles)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbParticles, 0, 136, Short.MAX_VALUE))
                    .addComponent(chkAntialiasing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addComponent(lblResolution)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbResolution, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImportUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlOptionsLayout.setVerticalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParticles)
                    .addComponent(cmbParticles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAntialiasing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblResolution)
                    .addComponent(cmbResolution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImportUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlLauncher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLauncher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tglOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglOptionsActionPerformed
        //Hide/Show the options
        if (tglOptions.isSelected()) {
            tglOptions.setText("Hide options <<");
            this.setBounds(getX(), getY(), this.getPreferredSize().width, this.getPreferredSize().height);
        }else{
            tglOptions.setText("Show options >>");
            this.setBounds(getX(), getY(), this.getPreferredSize().width - pnlOptions.getWidth(), this.getPreferredSize().height);
        }
    }//GEN-LAST:event_tglOptionsActionPerformed

    private void btnCreateResearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateResearchActionPerformed
        Main.paneX = getX() +getWidth()/2;
        Main.paneY = getY() +getHeight()/2;
        new ResearchCreatorForm().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCreateResearchActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        //Delete account
        if (JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this account?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            for (PersonalResearchEntity pr : Main.dbm.personalResearchList) {
                if (pr.getUserid().equals(Main.curProfile.getId())) {
                    Main.dbm.deletePersonalResearchRecord(pr);
                }
            }
            Main.dbm.deleteProfileRecord(Main.curProfile);
        Main.paneX = getX() +getWidth()/2;
        Main.paneY = getY() +getHeight()/2;
            dispose();
            if (Main.dbm.profileList.isEmpty()) {
                new RegistrationForm().setVisible(true);
            }else{
                new LoginForm().setVisible(true);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Main.curProfile.setSettingantialiasing(chkAntialiasing.isSelected());
        Main.curProfile.setSettingparticleamount((short)cmbParticles.getSelectedIndex());
        Main.curProfile.setSettingresolution((String)cmbResolution.getSelectedItem());
        Main.dbm.updateRecords();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSignOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutActionPerformed
        Main.paneX = getX() +getWidth()/2;
        Main.paneY = getY() +getHeight()/2;
        new LoginForm().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnSignOutActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnLaunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaunchActionPerformed
        Main.paneX = getX() +getWidth()/2;
        Main.paneY = getY() +getHeight()/2;
        new Thread(new BluLight()).start();
        dispose();
    }//GEN-LAST:event_btnLaunchActionPerformed

    private void btnExportUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportUserActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("BluLight profile","blup"));
        
        if (fc.showSaveDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            if (path.indexOf(".blup" )!= path.length()-5) {
                path += ".blup";
            }
            File f = new File(path);
            //Write the file
            try {
                try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                    f.createNewFile();
                    pw.print("MMMMMMMMMMMM" + Main.curProfile.getId().substring(13) + "¿");
                    pw.print(Main.curProfile.getShared() + "¿");
                    pw.print(Main.curProfile.getSettingresolution() + "¿");
                    pw.print(Main.curProfile.getSettingantialiasing() + "¿");
                    pw.print(Main.curProfile.getSettingparticleamount() + "¿");
                    pw.print(Main.curProfile.getFreefalldeaths() + "¿");
                    pw.print(Main.curProfile.getFreefallhighscore() + "¿");
                    pw.print(Main.curProfile.getFreefallpoints() + "¿");
                    pw.print(Main.curProfile.getRefractiondeaths() + "¿");
                    pw.print(Main.curProfile.getRefractionhighscore() + "¿");
                    pw.print(Main.curProfile.getRefractionpoints() + "¿");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Could not save file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnExportUserActionPerformed

    private void btnImportUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportUserActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("BluLight profile","blup"));
        
        if (fc.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try(BufferedReader br = new BufferedReader(new FileReader(f))){
                String line;
                String fileText = "";
                while ((line = br.readLine()) != null) {
                    fileText += line;
                }
                
                String[] profileSegments = fileText.split("¿");
                
                //Import user
                if (profileSegments.length == 11) {
                    if (Main.dbm.getUserByID(profileSegments[0]) == -1) {
                        UserProfileEntity user = new UserProfileEntity();
                        user.setId(profileSegments[0].trim().replace("MMMMMMMMMMMM", Main.curMAC));
                        user.setShared(Boolean.getBoolean(profileSegments[1].trim()));
                        if (user.getShared()) {
                            user.setOwner(null);
                        }else{
                            user.setOwner(System.getProperty("user.name"));
                        }
                        user.setSettingresolution(profileSegments[2].trim());
                        user.setSettingantialiasing(Boolean.getBoolean(profileSegments[3].trim()));
                        user.setSettingparticleamount(Short.parseShort(profileSegments[4].trim()));
                        user.setFreefalldeaths(Short.parseShort(profileSegments[5].trim()));
                        user.setFreefallhighscore(Short.parseShort(profileSegments[6].trim()));
                        user.setFreefallpoints(Integer.parseInt(profileSegments[7].trim()));
                        user.setRefractiondeaths(Short.parseShort(profileSegments[8].trim()));
                        user.setRefractionhighscore(Short.parseShort(profileSegments[9].trim()));
                        user.setRefractionpoints(Integer.parseInt(profileSegments[10].trim()));

                        Main.dbm.addProfileRecord(user);
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    } 
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Incorrect file format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (IOException io){
                JOptionPane.showMessageDialog(rootPane, "File could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (NumberFormatException | RollbackException | ArrayIndexOutOfBoundsException corr){
                JOptionPane.showMessageDialog(rootPane, "Incorrect file format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnImportUserActionPerformed

    private void btnShareResearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareResearchActionPerformed
        Main.paneX = getX() +getWidth()/2;
        Main.paneY = getY() +getHeight()/2;
        new ResearchShare().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnShareResearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateResearch;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExportUser;
    private javax.swing.JButton btnImportUser;
    private javax.swing.JButton btnLaunch;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShareResearch;
    private javax.swing.JButton btnSignOut;
    private javax.swing.JCheckBox chkAntialiasing;
    private javax.swing.JComboBox cmbParticles;
    private javax.swing.JComboBox cmbResolution;
    private javax.swing.JLabel lblParticles;
    private javax.swing.JLabel lblResolution;
    private javax.swing.JPanel pnlLauncher;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JToggleButton tglOptions;
    // End of variables declaration//GEN-END:variables
}
