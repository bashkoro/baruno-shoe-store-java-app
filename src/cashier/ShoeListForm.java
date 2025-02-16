/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashier;

import connection.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class ShoeListForm extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeForm
     */
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public ShoeListForm() {
        initComponents();
        
        //koneksi database
        conn = koneksi.getKoneksi();
        
        //menampilkan data
        showTableData();
    }

 private void showTableData() {
        DefaultTableModel model = new DefaultTableModel();

        // menambahkan kolom ke dalam tabel
        model.addColumn("ID Shoe");
        model.addColumn("ID Supplier");
        model.addColumn("Brand");
        model.addColumn("Sole");
        model.addColumn("Size");
        model.addColumn("Stock");
        model.addColumn("Price");


        try {
            // query untuk mengambil data dari tabel EmployeeData
            String sql = "SELECT * FROM Item.Shoes";

            // membuat prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // menjalankan query dan menyimpan hasilnya dalam objek ResultSet
            ResultSet rs = ps.executeQuery();

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Shoe"),
                    rs.getString("ID_Supplier"),
                    rs.getString("Brand"),
                    rs.getString("Sole"),
                    rs.getString("Size"),
                    rs.getString("Stock"),
                    rs.getInt("Price"),
                    
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // menampilkan data dalam JTable
        jTable1.setModel(model);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        BG_EmpForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo_sepatu-removebg-preview.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Shoes List");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(144, 144, 144)
                .addComponent(jLabel1)
                .addContainerGap(287, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 70));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 580, 590));

        jButton5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 18)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow_back_ios_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton5.setText("BACK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 700, -1, 40));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 750));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(ShoeListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShoeListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShoeListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShoeListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShoeListForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
