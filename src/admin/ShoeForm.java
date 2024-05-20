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
public class ShoeForm extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeForm
     */
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public ShoeForm() {
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
            stmt = conn.prepareCall("{call SearchShoe(?)}");
            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = new DefaultTableModel();

            // menambahkan kolom ke dalam tabel
            model.addColumn("Shoe ID");
            model.addColumn("Supplier ID");
            model.addColumn("Brand");
            model.addColumn("Sole");
            model.addColumn("Size");
            model.addColumn("Stock");
            model.addColumn("Price");

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Shoe"),
                    rs.getString("ID_Supplier"),
                    rs.getString("Brand"),
                    rs.getString("Sole"),
                    rs.getInt("Size"),
                    rs.getInt("Stock"),
                    rs.getInt("Price"),
                };
                model.addRow(row);
            }

            // menutup objek ResultSet dan prepared statement
            rs.close();
            stmt.close();

            // menampilkan data dalam JTable
            jTable1.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
        
    private void updateTableData(){

        try {
            int row = jTable1.getSelectedRow();
            String idShoe = jTable1.getValueAt(row, 0).toString();
            
            String newIdSupp = txtSupplier.getText().trim();
            String newBrand = txtBrand.getText().trim();
            String newSole = txtSole.getText().trim();
            int newSize = Integer.parseInt(txtSize.getText().trim());
            int newStock = Integer.parseInt(txtStock.getText().trim());
            int newPrice = Integer.parseInt(txtPrice.getText().trim());

            // memanggil prosedur update
            stmt = conn.prepareCall("{call UpdateShoesData(?,?,?,?,?,?,?)}");
            stmt.setString(1, idShoe);
            stmt.setString(2, newIdSupp);
            stmt.setString(3, newBrand);
            stmt.setString(4, newSole);
            stmt.setInt(5, newSize);
            stmt.setInt(6, newStock);
            stmt.setInt(7, newPrice);
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
            int row = jTable1.getSelectedRow();
            String idShoe = jTable1.getValueAt(row, 0).toString();
            String suppid = jTable1.getValueAt(row, 1).toString();
            String brand = jTable1.getValueAt(row, 2).toString();
            String sole = jTable1.getValueAt(row, 3).toString();
            String size = jTable1.getValueAt(row, 4).toString();
            String stock = jTable1.getValueAt(row, 5).toString();
            String price = jTable1.getValueAt(row, 6).toString();
            
            txtSupplier.setText(suppid);
            txtBrand.setText(brand);
            txtSole.setText(sole);
            txtSize.setText(size);
            txtStock.setText(stock);
            txtPrice.setText(price);
    }
    
    private void deleteTableData(){
        // mendapatkan baris yang dipilih di tabel jTable1
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel.");
            return;
        }
        // mendapatkan nilai ID_Employee dari baris yang dipilih
        int ID_Shoe = (int) jTable1.getValueAt(row, 0);

        try {
            // membuat prepared statement untuk memanggil stored procedure DeleteEmployeeData
            String sql = "EXEC DeleteShoesData ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ID_Shoe);

            // menjalankan stored procedure untuk menghapus data dari tabel EmployeeData
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari tabel.");
                // menghapus baris yang dipilih dari tabel jTable1
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
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
        model.addColumn("Shoe ID");
        model.addColumn("Supplier ID");
        model.addColumn("Brand");
        model.addColumn("Sole");
        model.addColumn("Size");
        model.addColumn("Stock");
        model.addColumn("Price");

        try {
            // query untuk mengambil data dari tabel EmployeeData
            String sql = "SELECT ID_Shoe, ID_Supplier, Brand, Sole, Size, Stock, Price FROM Item.Shoes";

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
                    rs.getInt("Size"),
                    rs.getInt("Stock"),
                    rs.getInt("Price"),
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
        jTable1.setModel(model);
}
    
        private void addData() {
            try {
                String IdSupp = txtSupplier.getText().trim();
                String Brand = txtBrand.getText().trim();
                String Sole = txtSole.getText().trim();
                int Size = Integer.parseInt(txtSize.getText().trim());
                int Stock = Integer.parseInt(txtStock.getText().trim());
                int Price = Integer.parseInt(txtPrice.getText().trim());

                //Memanggil stored procedure dengan mengisi parameter-parameter
                stmt = conn.prepareCall("{call InsertNewShoe(?, ?, ?, ?, ?, ?)}");
                stmt.setString(1, IdSupp);
                stmt.setString(2, Brand);
                stmt.setString(3, Sole);
                stmt.setInt(4, Size);
                stmt.setInt(5, Stock);
                stmt.setInt(6, Price);
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
        btnBACK = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        txtBrand = new javax.swing.JTextField();
        txtSole = new javax.swing.JTextField();
        txtSize = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnCLEAR = new javax.swing.JButton();
        btnADD = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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
        jLabel1.setText("Shoes Data");

        btnBACK.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnBACK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow_back_ios_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        btnBACK.setText("BACK");
        btnBACK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBACKActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 434, Short.MAX_VALUE)
                .addComponent(btnBACK)
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
                    .addComponent(btnBACK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jLabel3.setText("Supplier");

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel4.setText("Brand");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel5.setText("Sole");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel6.setText("Size");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel7.setText("Stock");

        txtSupplier.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtBrand.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtSole.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        txtSole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoleActionPerformed(evt);
            }
        });

        txtSize.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtStock.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel13.setText("Price");

        txtPrice.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(txtSize, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(txtSole)
                    .addComponent(txtBrand)
                    .addComponent(txtSupplier)
                    .addComponent(txtStock))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSole, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 330, 490));

        btnCLEAR.setBackground(new java.awt.Color(0, 153, 204));
        btnCLEAR.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnCLEAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnCLEAR.setText("CLEAR");
        btnCLEAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCLEARActionPerformed(evt);
            }
        });
        getContentPane().add(btnCLEAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 580, 160, -1));

        btnADD.setBackground(new java.awt.Color(0, 153, 204));
        btnADD.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnADD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnADD.setText("ADD");
        btnADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnADDActionPerformed(evt);
            }
        });
        getContentPane().add(btnADD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 160, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 800, 580));

        btnUPDROW.setBackground(new java.awt.Color(0, 153, 204));
        btnUPDROW.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnUPDROW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnUPDROW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUPDROWActionPerformed(evt);
            }
        });
        getContentPane().add(btnUPDROW, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 720, 50, -1));

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
        getContentPane().add(btnUPDATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 720, 320, -1));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 820));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoleActionPerformed

    private void btnADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnADDActionPerformed
        addData();
    }//GEN-LAST:event_btnADDActionPerformed

    private void btnUPDROWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUPDROWActionPerformed
        updateRow();
    }//GEN-LAST:event_btnUPDROWActionPerformed

    private void btnBACKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBACKActionPerformed
        AdminDashboard admForm = new AdminDashboard();
        admForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBACKActionPerformed

    private void btnUPDATEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUPDATEActionPerformed
        updateTableData();
    }//GEN-LAST:event_btnUPDATEActionPerformed

    private void btnDELETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDELETEActionPerformed
        deleteTableData();
    }//GEN-LAST:event_btnDELETEActionPerformed

    private void btnCLEARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCLEARActionPerformed
        txtSupplier.setText("");
        txtBrand.setText("");
        txtSole.setText("");
        txtSize.setText("");
        txtStock.setText("");
        txtPrice.setText("");
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
            java.util.logging.Logger.getLogger(ShoeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShoeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShoeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShoeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ShoeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton btnADD;
    private javax.swing.JButton btnBACK;
    private javax.swing.JButton btnCLEAR;
    private javax.swing.JButton btnDELETE;
    private javax.swing.JButton btnUPDATE;
    private javax.swing.JButton btnUPDROW;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSize;
    private javax.swing.JTextField txtSole;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
