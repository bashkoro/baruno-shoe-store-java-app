/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import connection.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class SupplierForm extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeForm
     */
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public SupplierForm() {
        initComponents();
        
        //koneksi database
        conn = koneksi.getKoneksi();
        
        //tampilan tabel
        showTableData();
        
        //fitur search
        initializeSearch();
    }
    
    private void searchData(String keyword) {
        try {
            stmt = conn.prepareCall("{call SearchSupplier(?)}");
            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = new DefaultTableModel();

            // menambahkan kolom ke dalam tabel
            model.addColumn("ID Supplier");
            model.addColumn("Supplier Name");
            model.addColumn("Address");
            model.addColumn("Phone Number");

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID_Supplier"),
                    rs.getString("Supplier_Name"),
                    rs.getString("Supplier_Address"),
                    rs.getInt("Phone_Number"),
                };
                model.addRow(row);
            }

            // menutup objek ResultSet dan prepared statement
            rs.close();
            stmt.close();

            // menampilkan data dalam JTable
            tblSupplier.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
        
    private void updateTableData(){

        try {
            int row = tblSupplier.getSelectedRow();
            
            String newSuppID = txtSupplierID.getText().trim();
            String newSuppName = txtName.getText().trim();
            String newAddress = txtAddress.getText().trim();
            int newPhoneNumber = Integer.parseInt(txtPhoneNumber.getText().trim());

            // memanggil prosedur update
            stmt = conn.prepareCall("{call UpdateSupplierData(?,?,?,?)}");
            stmt.setString(1, newSuppID);
            stmt.setString(2, newSuppName);
            stmt.setString(3, newAddress);
            stmt.setInt(4, newPhoneNumber);
            stmt.execute();

            // menampilkan pesan sukses
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate");

            // menampilkan data yang telah diupdate pada tabel
            showTableData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
    
    private void updateRow(){
            int row = tblSupplier.getSelectedRow();
            String SuppID = tblSupplier.getValueAt(row, 0).toString();
            String SuppName = tblSupplier.getValueAt(row, 1).toString();
            String Address = tblSupplier.getValueAt(row, 2).toString();
            String PhoneNumber = tblSupplier.getValueAt(row, 3).toString();
            
            txtSupplierID.setText(SuppID);
            txtName.setText(SuppName);
            txtAddress.setText(Address);
            txtPhoneNumber.setText(PhoneNumber);
    }
    
    private void deleteTableData(){
        // mendapatkan baris yang dipilih di tabel jTable1
        int row = tblSupplier.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel.");
            return;
        }
        // mendapatkan nilai ID_Employee dari baris yang dipilih
        String ID_Supplier = (String) tblSupplier.getValueAt(row, 0);

        try {
            // membuat prepared statement untuk memanggil stored procedure DeleteEmployeeData
            String sql = "EXEC DeleteSupplierData ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID_Supplier);

            // menjalankan stored procedure untuk menghapus data dari tabel EmployeeData
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari tabel.");
                // menghapus baris yang dipilih dari tabel jTable1
                DefaultTableModel model = (DefaultTableModel) tblSupplier.getModel();
                model.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data dari tabel.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } finally {
            try {
                // menutup prepared statement
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void showTableData() {
        DefaultTableModel model = new DefaultTableModel();

        // menambahkan kolom ke dalam tabel
        model.addColumn("ID Supplier");
        model.addColumn("Supplier Name");
        model.addColumn("Address");
        model.addColumn("Phone Number");

        try {
            // query untuk mengambil data dari tabel EmployeeData
            String sql = "SELECT ID_Supplier, Supplier_Name, Supplier_Address, Phone_Number FROM Item.Supplier";

            // membuat prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // menjalankan query dan menyimpan hasilnya dalam objek ResultSet
            ResultSet rs = ps.executeQuery();

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID_Supplier"),
                    rs.getString("Supplier_Name"),
                    rs.getString("Supplier_Address"),
                    rs.getInt("Phone_Number"),
                };
                model.addRow(row);
            }

            // menutup objek ResultSet dan prepared statement
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // menampilkan data dalam JTable
        tblSupplier.setModel(model);
}
    
        private void addData() {
            try {
                String SuppID = txtSupplierID.getText().trim();
                String SuppName = txtName.getText().trim();
                String Address = txtAddress.getText().trim();
                int PhoneNumber = Integer.parseInt(txtPhoneNumber.getText().trim());

                //Memanggil stored procedure dengan mengisi parameter-parameter
                stmt = conn.prepareCall("{call InsertNewSupplier(?, ?, ?, ?)}");
                stmt.setString(1, SuppID);
                stmt.setString(2, SuppName);
                stmt.setString(3, Address);
                stmt.setInt(4, PhoneNumber);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Employee added successfully!");
                showTableData();
            } catch (NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
    }
  
///buat search
    private void txtSearchDocumentChanged(javax.swing.event.DocumentEvent evt) {
        String keyword = txtSearch.getText();
        searchData(keyword);
    }

    private void addDocumentListenerToSearchField() {
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                txtSearchDocumentChanged(evt);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                txtSearchDocumentChanged(evt);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                txtSearchDocumentChanged(evt);
            }
        });
    }

    private void initializeSearch() {
        addDocumentListenerToSearchField();
    }
 ///

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
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSupplierID = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtPhoneNumber = new javax.swing.JTextField();
        btnCLEAR = new javax.swing.JButton();
        btnADD = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        btnUPDROW = new javax.swing.JButton();
        btnDELETE = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnUPDATE = new javax.swing.JButton();
        BG_EmpForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo_sepatu-removebg-preview.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Supplier Data");

        jButton5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow_back_ios_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton5.setText("BACK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(394, 394, 394)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 410, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 70));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 51, 204));

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 17)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Product Detail");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(jLabel10)
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel3.setText("Supplier ID");

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel4.setText("Name");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel5.setText("Address");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel6.setText("Phone Number");

        txtSupplierID.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtName.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        txtPhoneNumber.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(txtAddress)
                    .addComponent(txtName)
                    .addComponent(txtSupplierID))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 330, 370));

        btnCLEAR.setBackground(new java.awt.Color(0, 153, 204));
        btnCLEAR.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnCLEAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnCLEAR.setText("CLEAR");
        btnCLEAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCLEARActionPerformed(evt);
            }
        });
        getContentPane().add(btnCLEAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 460, 160, -1));

        btnADD.setBackground(new java.awt.Color(0, 153, 204));
        btnADD.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnADD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnADD.setText("ADD");
        btnADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnADDActionPerformed(evt);
            }
        });
        getContentPane().add(btnADD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 160, -1));

        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblSupplier);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 800, 580));

        btnUPDROW.setBackground(new java.awt.Color(0, 153, 204));
        btnUPDROW.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnUPDROW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnUPDROW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUPDROWActionPerformed(evt);
            }
        });
        getContentPane().add(btnUPDROW, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 720, 40, -1));

        btnDELETE.setBackground(new java.awt.Color(0, 153, 204));
        btnDELETE.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnDELETE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnDELETE.setText("DELETE");
        btnDELETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDELETEActionPerformed(evt);
            }
        });
        getContentPane().add(btnDELETE, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 720, 380, -1));

        txtSearch.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 20)); // NOI18N
        txtSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, 660, 40));

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 18)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search_FILL0_wght400_GRAD0_opsz48.png"))); // NOI18N
        jLabel11.setText("SEARCH");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 80, -1, 40));

        btnUPDATE.setBackground(new java.awt.Color(0, 153, 204));
        btnUPDATE.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnUPDATE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnUPDATE.setText("UPDATE");
        btnUPDATE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUPDATEActionPerformed(evt);
            }
        });
        getContentPane().add(btnUPDATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 720, 310, -1));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 820));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void btnADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnADDActionPerformed
        addData();
    }//GEN-LAST:event_btnADDActionPerformed

    private void btnUPDROWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUPDROWActionPerformed
        updateRow();
    }//GEN-LAST:event_btnUPDROWActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        AdminDashboard admForm = new AdminDashboard();
        admForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        //Dari DocumentListener
    }//GEN-LAST:event_txtSearchActionPerformed

    
    
    private void btnUPDATEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUPDATEActionPerformed
        updateTableData();
    }//GEN-LAST:event_btnUPDATEActionPerformed

    private void btnDELETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDELETEActionPerformed
        deleteTableData();
    }//GEN-LAST:event_btnDELETEActionPerformed

    private void btnCLEARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCLEARActionPerformed
        txtSupplierID.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhoneNumber.setText("");
    }//GEN-LAST:event_btnCLEARActionPerformed

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
            java.util.logging.Logger.getLogger(SupplierForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SupplierForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SupplierForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SupplierForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SupplierForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton btnADD;
    private javax.swing.JButton btnCLEAR;
    private javax.swing.JButton btnDELETE;
    private javax.swing.JButton btnUPDATE;
    private javax.swing.JButton btnUPDROW;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSupplierID;
    // End of variables declaration//GEN-END:variables
}
