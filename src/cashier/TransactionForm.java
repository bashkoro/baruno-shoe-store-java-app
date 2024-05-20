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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import login.LoginCashier;

/**
 *
 * @author USER
 */
public class TransactionForm extends javax.swing.JFrame {
  
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public TransactionForm() {
        initComponents();
        
        //koneksi database
        conn = koneksi.getKoneksi();
        
    }
    
 public void autogenerate(){
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try{  
    // create a prepared statement with the SQL query
    String sql = "SELECT Brand, Price FROM Item.Shoes WHERE ID_Shoe = ?";
    statement = conn.prepareStatement(sql);

    // set the parameter for the prepared statement
    String id = txtShoeID.getText(); // replace with the actual ID value
    statement.setString(1, id);

    // execute the query and retrieve the results
    resultSet = statement.executeQuery();
    while (resultSet.next()){
        // get the values for shoename and price from the result set
        String shoename = resultSet.getString("Brand");
        Integer price = resultSet.getInt("Price");
        txtShoeName.setText(shoename);
        txtPrice.setText(String.valueOf(price));
    }
   
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    
    
public void addShopingCart() {
    try {
    // create a PreparedStatement object for the stored procedure
    String sql = "{ call CreateOrder(?, ?, ?, ?, ?) }";
    PreparedStatement stmt = conn.prepareStatement(sql);

    // set the values of the stored procedure parameters
    stmt.setString(1, txtOrderID.getText()); 
    stmt.setInt(2, Integer.parseInt(txtEmpID.getText()));  
    stmt.setString(3, cmbPaymentMethod.getSelectedItem().toString()); 
    stmt.setInt(4, Integer.parseInt(txtShoeID.getText())); 
    stmt.setInt(5, Integer.parseInt(txtQuantity.getText()));

    // execute the stored procedure
    stmt.executeUpdate();

    // close the PreparedStatement object
    stmt.close();
    showTableData();
} catch (SQLException ex) {
    // handle any SQL errors
    ex.printStackTrace();
}
}
   
  
    
    
private void showTableData() {
    DefaultTableModel model = new DefaultTableModel();
    try (PreparedStatement stmt = conn.prepareCall("{call GetLatestTransaction()}")) {
        ResultSet rs = stmt.executeQuery();

        // add columns to the table
        model.addColumn("ID Cart");
        model.addColumn("ID Shoe");
        model.addColumn("Brand");
        model.addColumn("Quantity");
        model.addColumn("Price");
        model.addColumn("SubTotal");
      

        // add rows to the table
        while (rs.next()) {
              Object[] row = {
                rs.getInt("ID_Cart"),
                rs.getInt("ID_Shoe"),
                rs.getString("Brand"),
                rs.getInt("Quantity"),
                rs.getInt("Price"),
                rs.getInt("SubTotal")
            };
            model.addRow(row);
        }
        // close the ResultSet and PreparedStatement
        rs.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // display the table data
    jTable1.setModel(model);
}
    
    
private void deleteTransaction() {
    // get the selected row in the jTable1 table
    int row = jTable1.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel.");
        return;
    }
    // get the value of the ID_Cart column from the selected row
    int ID_Cart = (int) jTable1.getValueAt(row, 0);

    try {
        // create a prepared statement to call the DeleteCartByIDCart stored procedure
        String sql = "{call DeleteCartByIDCart(?)}";
        PreparedStatement stmtDelete = conn.prepareCall(sql);
        stmtDelete.setInt(1, ID_Cart);

        // execute the stored procedure to delete the data from the Transactions.Cart table
        int affectedRows = stmtDelete.executeUpdate();

        if (affectedRows > 0) {
            // create a prepared statement to call the UpdateOnOrder stored procedure
            sql = "{call UpdateOnOrder(?)}";
            PreparedStatement stmtUpdate = conn.prepareCall(sql);
            stmtUpdate.setInt(1, ID_Cart);

            // execute the stored procedure to update the data in the Transactions.Orders table
            stmtUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari tabel.");
            // remove the selected row from the jTable1 table
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data dari tabel.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

    
   private int calculateTotalSales() {
    int totalSales = 0;
    try (PreparedStatement stmt = conn.prepareCall("{call GetLatestTransaction()}")) {
        ResultSet rs = stmt.executeQuery();

        // calculate total sales
        while (rs.next()) {
            int quantity = rs.getInt("Quantity");
            int price = rs.getInt("Price");
            int subTotal = quantity * price; // calculate subtotal for the current row
            totalSales += subTotal; // add subtotal to total sales
        }

        // close the ResultSet and PreparedStatement
        rs.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return totalSales;
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
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtShoeID = new javax.swing.JTextField();
        txtShoeName = new javax.swing.JTextField();
        cmbPaymentMethod = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnSHOEDTL = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        txtOrderID = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnORDDTL = new javax.swing.JButton();
        btnSHOEFILL = new javax.swing.JButton();
        btnCLEAR = new javax.swing.JButton();
        btnADD = new javax.swing.JButton();
        btnTOTAL = new javax.swing.JButton();
        btnDELETE = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtPay = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();
        btnPAY = new javax.swing.JButton();
        txtPayTotal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        btnBACK = new javax.swing.JButton();
        BG_EmpForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo_sepatu-removebg-preview.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaction");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(394, 394, 394)
                .addComponent(jLabel1)
                .addContainerGap(548, Short.MAX_VALUE))
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
                .addGap(153, 153, 153)
                .addComponent(jLabel10)
                .addContainerGap(160, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel4.setText("Payment Method");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel5.setText("Shoe ID");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel6.setText("Shoe Name");

        txtShoeID.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        txtShoeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShoeIDActionPerformed(evt);
            }
        });

        txtShoeName.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        txtShoeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShoeNameActionPerformed(evt);
            }
        });

        cmbPaymentMethod.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        cmbPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "OVO", "Dana" }));
        cmbPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPaymentMethodActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel8.setText("Price");

        txtPrice.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        btnSHOEDTL.setText("...");
        btnSHOEDTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHOEDTLActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel9.setText("Quantity");

        txtQuantity.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        txtOrderID.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        txtOrderID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderIDActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel11.setText("Order ID");

        btnORDDTL.setText("...");
        btnORDDTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnORDDTLActionPerformed(evt);
            }
        });

        btnSHOEFILL.setText("Fill");
        btnSHOEFILL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSHOEFILLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSHOEDTL, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(cmbPaymentMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtShoeName)
                    .addComponent(txtPrice)
                    .addComponent(txtQuantity)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtShoeID, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(jLabel11)
                            .addComponent(txtOrderID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnORDDTL, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSHOEFILL, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOrderID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnORDDTL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnSHOEDTL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtShoeID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSHOEFILL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtShoeName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 420, 520));

        btnCLEAR.setBackground(new java.awt.Color(0, 153, 204));
        btnCLEAR.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnCLEAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnCLEAR.setText("CLEAR");
        btnCLEAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCLEARActionPerformed(evt);
            }
        });
        getContentPane().add(btnCLEAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 700, 190, -1));

        btnADD.setBackground(new java.awt.Color(0, 153, 204));
        btnADD.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnADD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnADD.setText("ADD");
        btnADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnADDActionPerformed(evt);
            }
        });
        getContentPane().add(btnADD, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 700, 190, -1));

        btnTOTAL.setBackground(new java.awt.Color(0, 153, 204));
        btnTOTAL.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnTOTAL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/attach_money_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnTOTAL.setText("TOTAL");
        btnTOTAL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTOTALActionPerformed(evt);
            }
        });
        getContentPane().add(btnTOTAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, 540, -1));

        btnDELETE.setBackground(new java.awt.Color(0, 153, 204));
        btnDELETE.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnDELETE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        btnDELETE.setText("DELETE");
        btnDELETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDELETEActionPerformed(evt);
            }
        });
        getContentPane().add(btnDELETE, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 440, 100, -1));

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 17)); // NOI18N
        jLabel7.setText("Employee ID");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 150, 30));

        txtEmpID.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        getContentPane().add(txtEmpID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 150, 30));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, 640, 330));

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 28)); // NOI18N
        jLabel12.setText("Change:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 670, -1, -1));

        txtPay.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 22)); // NOI18N
        txtPay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPayActionPerformed(evt);
            }
        });
        getContentPane().add(txtPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 620, 570, 40));

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 30)); // NOI18N
        jLabel13.setText("Payment Total");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 480, -1, -1));

        txtChange.setBackground(new java.awt.Color(153, 153, 153));
        txtChange.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 22)); // NOI18N
        txtChange.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChangeActionPerformed(evt);
            }
        });
        getContentPane().add(txtChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 670, 530, 40));

        btnPAY.setBackground(new java.awt.Color(0, 102, 255));
        btnPAY.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        btnPAY.setText("PAY");
        btnPAY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPAYActionPerformed(evt);
            }
        });
        getContentPane().add(btnPAY, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 620, -1, 40));

        txtPayTotal.setBackground(new java.awt.Color(153, 153, 153));
        txtPayTotal.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 22)); // NOI18N
        txtPayTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPayTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPayTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtPayTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 530, 640, 40));

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 30)); // NOI18N
        jLabel14.setText("Pay");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 580, -1, 30));

        jButton5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 17)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print_FILL0_wght400_GRAD0_opsz48.png"))); // NOI18N
        jButton5.setText("PRINT");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 760, 160, 40));

        btnBACK.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 16)); // NOI18N
        btnBACK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/arrow_back_ios_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        btnBACK.setText("BACK");
        btnBACK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBACKActionPerformed(evt);
            }
        });
        getContentPane().add(btnBACK, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 767, 160, -1));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1200, 740));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtShoeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShoeIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtShoeIDActionPerformed

    private void btnADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnADDActionPerformed
        addShopingCart();
    }//GEN-LAST:event_btnADDActionPerformed

    
    //Total Belanja
    private void btnTOTALActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTOTALActionPerformed
        int totalSales = calculateTotalSales();
        txtPayTotal.setText(String.valueOf(totalSales)); 
    }//GEN-LAST:event_btnTOTALActionPerformed

    private void btnDELETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDELETEActionPerformed
        deleteTransaction();
    }//GEN-LAST:event_btnDELETEActionPerformed

    private void txtPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayActionPerformed

    private void txtChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChangeActionPerformed

    private void txtPayTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPayTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayTotalActionPerformed

    private void btnBACKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBACKActionPerformed
        CashierDashboard dashboardCsh = new CashierDashboard();
        dashboardCsh.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBACKActionPerformed

    private void btnSHOEDTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHOEDTLActionPerformed
        ShoeListForm shoeList = new ShoeListForm();
        shoeList.setVisible(true);
    }//GEN-LAST:event_btnSHOEDTLActionPerformed

    private void txtShoeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShoeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtShoeNameActionPerformed

    private void txtOrderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderIDActionPerformed

    private void btnORDDTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnORDDTLActionPerformed
        OrderListForm orderList = new OrderListForm();
        orderList.setVisible(true);
    }//GEN-LAST:event_btnORDDTLActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmbPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPaymentMethodActionPerformed
        if(cmbPaymentMethod.getSelectedItem()!="Cash"){
            txtPay.setEditable(false);
              txtChange.setEditable(false);
               txtPay.setText("EMPTY");
                txtChange.setText("EMPTY");
        }else{
             txtPay.setEditable(true);
              txtChange.setEditable(true);
               txtPay.setText("Input amount...");
                txtChange.setText("Input amount...");
        }
    }//GEN-LAST:event_cmbPaymentMethodActionPerformed

    private void btnCLEARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCLEARActionPerformed
       txtEmpID.setText("");
       txtOrderID.setText("");
       txtShoeID.setText("");
       txtShoeName.setText("");
       txtPrice.setText("");
       txtQuantity.setText("");
       txtPayTotal.setText("Input amount...");
       txtPay.setText("Input amount...");
       txtChange.setText("Input amount...");
    }//GEN-LAST:event_btnCLEARActionPerformed

    private void btnSHOEFILLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSHOEFILLActionPerformed
        autogenerate();
    }//GEN-LAST:event_btnSHOEFILLActionPerformed

    private void btnPAYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPAYActionPerformed
         String harga= txtPayTotal.getText();
         String bayaran= txtPay.getText();
         int harga1 = Integer.parseInt(harga);
         int bayaran1 = Integer.parseInt(bayaran);
         int kembalian = bayaran1-harga1;
         txtChange.setText(String.valueOf(kembalian));
    }//GEN-LAST:event_btnPAYActionPerformed

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
            java.util.logging.Logger.getLogger(TransactionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new TransactionForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton btnADD;
    private javax.swing.JButton btnBACK;
    private javax.swing.JButton btnCLEAR;
    private javax.swing.JButton btnDELETE;
    private javax.swing.JButton btnORDDTL;
    private javax.swing.JButton btnPAY;
    private javax.swing.JButton btnSHOEDTL;
    private javax.swing.JButton btnSHOEFILL;
    private javax.swing.JButton btnTOTAL;
    private javax.swing.JComboBox<String> cmbPaymentMethod;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtOrderID;
    private javax.swing.JTextField txtPay;
    private javax.swing.JTextField txtPayTotal;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtShoeID;
    private javax.swing.JTextField txtShoeName;
    // End of variables declaration//GEN-END:variables
}
