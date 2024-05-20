/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashier;

import admin.*;
import connection.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author USER
 */
public class TransactionReportForm extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeForm
     */
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public TransactionReportForm() {
        initComponents();
        
        //koneksi database
        conn = koneksi.getKoneksi();
        
        //menampilkan tabel
        showReport();
        
        //fitur search
        initializeSearch();
        
    }
    
    private void showReport() {
        DefaultTableModel model = new DefaultTableModel();

        // menambahkan kolom ke dalam tabel
        model.addColumn("Order ID");
        model.addColumn("Cart ID");
        model.addColumn("Transaction Date");
        model.addColumn("Employee ID");
        model.addColumn("Employee Name");
        model.addColumn("Payment Method");
        model.addColumn("Shoe ID");
        model.addColumn("Brand");
        model.addColumn("Quantity");
        model.addColumn("Total Payment");

        try {
            // query untuk mengambil data dari tabel EmployeeData
            String sql = "EXEC GenerateTransactionReport";

            // membuat prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // menjalankan query dan menyimpan hasilnya dalam objek ResultSet
            ResultSet rs = ps.executeQuery();

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID_Order"), 
                    rs.getInt("ID_Cart"), 
                    rs.getString("Transaction_Date"), 
                    rs.getInt("ID_Employee"), 
                    rs.getString("Emp_Name"), 
                    rs.getString("Payment_Method"), 
                    rs.getInt("ID_Shoe"), 
                    rs.getString("Brand"), 
                    rs.getInt("Quantity"), 
                    rs.getInt("Total_Payment")
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
        tblReportTrans.setModel(model);
    }
    
    
    private void searchData(String keyword) {
        try {
            stmt = conn.prepareCall("{call SearchInTransaction(?)}");
            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = new DefaultTableModel();

            // menambahkan kolom ke dalam tabel
            model.addColumn("Order ID");
            model.addColumn("Cart ID");
            model.addColumn("Transaction Date");
            model.addColumn("Employee ID");
            model.addColumn("Employee Name");
            model.addColumn("Payment Method");
            model.addColumn("Shoe ID");
            model.addColumn("Brand");
            model.addColumn("Quantity");
            model.addColumn("Total Payment");

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID_Order"), 
                    rs.getInt("ID_Cart"), 
                    rs.getString("Transaction_Date"), 
                    rs.getInt("ID_Employee"), 
                    rs.getString("Emp_Name"), 
                    rs.getString("Payment_Method"), 
                    rs.getInt("ID_Shoe"), 
                    rs.getString("Brand"), 
                    rs.getInt("Quantity"), 
                    rs.getInt("Total_Payment")
                };
                model.addRow(row);
            }

            // menutup objek ResultSet dan prepared statement
            rs.close();
            stmt.close();

            // menampilkan data dalam JTable
            tblReportTrans.setModel(model);
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void sortTransaction() {
    try {
        // get the selected values from the UI components
        int selectedMonth = cmbMonth.getSelectedIndex() + 1;
        int selectedYear = (Integer) sprYear.getValue();;
        String selectedPaymentMethod = cmbPayMethd.getSelectedItem().toString();
        String selectedOrderId = txtOrderID.getText();

        // create a callable statement for the stored procedure
        stmt = conn.prepareCall("{call SortTransaction(?, ?, ?, ?)}");

        // set the parameters for the stored procedure
        stmt.setInt(1, selectedMonth);
        stmt.setInt(2, selectedYear);
        stmt.setString(3, selectedPaymentMethod);
        stmt.setString(4, selectedOrderId);

        // execute the stored procedure and retrieve the results
        ResultSet rs = stmt.executeQuery();
        DefaultTableModel model = new DefaultTableModel();

        // add columns to the model
        model.addColumn("Order ID");
        model.addColumn("Cart ID");
        model.addColumn("Transaction Date");
        model.addColumn("Employee ID");
        model.addColumn("Employee Name");
        model.addColumn("Payment Method");
        model.addColumn("Shoe ID");
        model.addColumn("Brand");
        model.addColumn("Quantity");
        model.addColumn("Total Payment");

        // add rows to the model
        while (rs.next()) {
            Object[] row = {
                rs.getString("ID_Order"),
                rs.getInt("ID_Cart"),
                rs.getDate("Transaction_Date"),
                rs.getString("ID_Employee"),
                rs.getString("Emp_Name"),
                rs.getString("Payment_Method"),
                rs.getInt("ID_Shoe"),
                rs.getString("Brand"),
                rs.getInt("Quantity"),
                rs.getInt("Total_Payment")
            };
            model.addRow(row);
        }

        // set the table model to the model with the sorted data
        tblReportTrans.setModel(model);

        // close the result set and statement
        rs.close();
        stmt.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReportTrans = new javax.swing.JTable();
        btnBACK = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbPayMethd = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbMonth = new javax.swing.JComboBox<>();
        sprYear = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtOrderID = new javax.swing.JTextField();
        btnSORT = new javax.swing.JButton();
        BG_EmpForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo_sepatu-removebg-preview.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaction Report");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(379, 379, 379)
                .addComponent(jLabel1)
                .addContainerGap(482, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 70));

        tblReportTrans.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblReportTrans);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 1000, 610));

        btnBACK.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 16)); // NOI18N
        btnBACK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow_back_ios_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        btnBACK.setText("BACK");
        btnBACK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBACKActionPerformed(evt);
            }
        });
        getContentPane().add(btnBACK, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 760, 160, 40));

        txtSearch.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 20)); // NOI18N
        txtSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 850, 40));

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 18)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search_FILL0_wght400_GRAD0_opsz48.png"))); // NOI18N
        jLabel11.setText("SEARCH");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 80, -1, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 51, 204));

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 17)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Sort By");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel10)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jLabel6.setText("Payment Method");

        cmbPayMethd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "OVO", "Dana" }));
        cmbPayMethd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPayMethdActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jLabel5.setText("Month");

        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthActionPerformed(evt);
            }
        });

        sprYear.setModel(new javax.swing.SpinnerNumberModel(2023, 2020, 2023, 1));
        sprYear.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                sprYearAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jLabel4.setText("Year");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jLabel7.setText("Order ID");

        txtOrderID.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        txtOrderID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderIDActionPerformed(evt);
            }
        });

        btnSORT.setBackground(new java.awt.Color(0, 153, 255));
        btnSORT.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnSORT.setText("Sort");
        btnSORT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSORTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(txtOrderID)
                            .addComponent(btnSORT, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbPayMethd, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sprYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(39, 39, 39))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbPayMethd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sprYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtOrderID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnSORT)
                .addGap(32, 32, 32))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 160, 390));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 810));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBACKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBACKActionPerformed
        CashierDashboard dashboardCsh = new CashierDashboard();
        dashboardCsh.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBACKActionPerformed

    private void cmbPayMethdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPayMethdActionPerformed

    }//GEN-LAST:event_cmbPayMethdActionPerformed

    private void btnSORTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSORTActionPerformed
        sortTransaction();
    }//GEN-LAST:event_btnSORTActionPerformed

    private void sprYearAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_sprYearAncestorAdded

    }//GEN-LAST:event_sprYearAncestorAdded

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed

    }//GEN-LAST:event_cmbMonthActionPerformed

    private void txtOrderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderIDActionPerformed

    }//GEN-LAST:event_txtOrderIDActionPerformed

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
            java.util.logging.Logger.getLogger(TransactionReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new TransactionReportForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton btnBACK;
    private javax.swing.JButton btnSORT;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbPayMethd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner sprYear;
    private javax.swing.JTable tblReportTrans;
    private javax.swing.JTextField txtOrderID;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
