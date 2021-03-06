/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BluLight;

import static BluLight.Main.dbm;
import Entities.UserProfileEntity;
import UndefinedLibrary.Scrap.ScreenManager;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author Jonathan Botha
 */
public class RegistrationForm extends javax.swing.JFrame{
public boolean running;
    /**
     * Creates new form RegistrationForm
     */
    public RegistrationForm() {
        initComponents();
        setIconImage(new ImageIcon(LoginForm.class.getResource("/Images/Logo.png")).getImage());
        setLocation(Main.paneX -getWidth()/2, Main.paneY -getHeight()/2);
        running = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txfUsername = new javax.swing.JTextField();
        chkShare = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create new profile");
        setAlwaysOnTop(true);

        lblUsername.setText("Username: ");

        txfUsername.setToolTipText("Username of 1-12 Characters");
        txfUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txfUsernameKeyReleased(evt);
            }
        });

        chkShare.setText("Shared across all users");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setText("Save new profile");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkShare, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfUsername))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkShare)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (Main.dbm.profileList.isEmpty()) {
            System.exit(0);
        }else{
            Main.paneX = getX() +getWidth()/2;
            Main.paneY = getY() +getHeight()/2;
            dispose();
            new LoginForm().setVisible(true);
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txfUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfUsernameKeyReleased
        if (txfUsername.getText().length() > 24 || txfUsername.getText().length() < 0 || txfUsername.getText().contains(" ")) {
            btnSave.setEnabled(false);
        }else{
            btnSave.setEnabled(true);
        }
    }//GEN-LAST:event_txfUsernameKeyReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (Main.curMAC.equals("************")) {
            JOptionPane.showMessageDialog(rootPane, "This computer does not contain a builtin MAC.\nThis user will not be prtected by BluLight's MAC protection.", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        //ID
        String id = String.format("%12s%24s", Main.curMAC,txfUsername.getText());
        UserProfileEntity newUser = new UserProfileEntity(id,
                chkShare.isSelected(),
                ScreenManager.getDefaultDisplayMode().getWidth() + "x" + ScreenManager.getDefaultDisplayMode().getHeight(),//Res checked in begin of programme
                true, (short)2, (short)0, 0, (short)0, (short)0, 0, (short)0);
        //Share
        if (!chkShare.isSelected()) {
            newUser.setOwner(Main.curPCUsername);
        }
        //Check for existing user
        if (!Main.dbm.getUserIDs().contains(newUser.getId())) {
            if (!newUser.getId().substring(12).trim().toUpperCase().equals("BLULIGHT")) {//DEV account
                Main.dbm.addProfileRecord(newUser);
                dispose();
            }else{
                JOptionPane.showMessageDialog(rootPane, "Username is not allowed","Error", JOptionPane.ERROR_MESSAGE);
                txfUsername.setText("");
            }
        }else{
            JOptionPane.showMessageDialog(rootPane, "Username already exists.","Error", JOptionPane.ERROR_MESSAGE);
            txfUsername.setText("");
        }
        if (!dbm.profileList.isEmpty()) {
            new LoginForm().setVisible(true);
        }else{
            System.exit(0);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkShare;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables
}
