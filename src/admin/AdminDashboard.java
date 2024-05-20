/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import connection.koneksi;
import java.sql.Connection;
import javax.swing.JOptionPane;
import login.StartDashboard;

/**
 *
 * @author USER
 */
public class AdminDashboard extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashboard
     */
    Connection conn;
    
    public AdminDashboard() {
        initComponents();
        
        //koneksi database
        conn = koneksi.getKoneksi();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        btnEmployeeData = new javax.swing.JButton();
        btnSupplierData = new javax.swing.JButton();
        btnShoeData = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        BG_AdmForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEmployeeData.setBackground(new java.awt.Color(51, 102, 255));
        btnEmployeeData.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        btnEmployeeData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/group_add_FILL0_wght200_GRAD200_opsz48.png"))); // NOI18N
        btnEmployeeData.setText("Employee Data");
        btnEmployeeData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnEmployeeData, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, -1, -1));

        btnSupplierData.setBackground(new java.awt.Color(51, 102, 255));
        btnSupplierData.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        btnSupplierData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/store_FILL0_wght200_GRAD200_opsz48.png"))); // NOI18N
        btnSupplierData.setText("Supplier Data");
        btnSupplierData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnSupplierData, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, -1, -1));

        btnShoeData.setBackground(new java.awt.Color(51, 102, 255));
        btnShoeData.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        btnShoeData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/category_FILL0_wght200_GRAD200_opsz48.png"))); // NOI18N
        btnShoeData.setText("Shoes Data");
        btnShoeData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShoeDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnShoeData, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 230, -1, -1));

        btnLogout.setBackground(new java.awt.Color(0, 51, 102));
        btnLogout.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(204, 204, 204));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logout_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 120, 30));

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/person_login_image-removebg-preview.png"))); // NOI18N
        jLabel1.setText("Administrator Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabel1)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 130));

        BG_AdmForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_form1.png"))); // NOI18N
        BG_AdmForm.setText("jLabel2");
        BG_AdmForm.setPreferredSize(new java.awt.Dimension(948, 621));
        getContentPane().add(BG_AdmForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 440));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int respon = JOptionPane.showConfirmDialog(null,  "Do you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (respon == JOptionPane.YES_OPTION) {
            StartDashboard admForm = new StartDashboard();
            admForm.setVisible(true);
            this.dispose();
        } else if (respon == JOptionPane.NO_OPTION) {
            //return to this frame
        }
        

    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnEmployeeDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeDataActionPerformed
        EmployeeForm empForm = new EmployeeForm();
        empForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEmployeeDataActionPerformed

    private void btnSupplierDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierDataActionPerformed
        SupplierForm shoeForm = new SupplierForm();
        shoeForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSupplierDataActionPerformed

    private void btnShoeDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShoeDataActionPerformed
        ShoeForm suppForm = new ShoeForm();
        suppForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnShoeDataActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_AdmForm;
    private javax.swing.JButton btnEmployeeData;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnShoeData;
    private javax.swing.JButton btnSupplierData;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
