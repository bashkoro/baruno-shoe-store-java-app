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
public class EmployeeForm extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeForm
     */
    private Connection conn = null;
    private PreparedStatement stmt = null;
    
    public EmployeeForm() {
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
            stmt = conn.prepareCall("{call SearchEmployee(?)}");
            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = new DefaultTableModel();

            // menambahkan kolom ke dalam tabel
            model.addColumn("ID Employee");
            model.addColumn("Job");
            model.addColumn("Gender");
            model.addColumn("Employee Name");
            model.addColumn("Age");
            model.addColumn("Phone Number");
            model.addColumn("Username");
            model.addColumn("Password");

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Employee"),
                    rs.getString("ID_Job"),
                    rs.getString("ID_Gender"),
                    rs.getString("Emp_Name"),
                    rs.getInt("Age"),
                    rs.getString("Phone_Number"),
                    rs.getString("Username"),
                    rs.getString("Emp_Password")
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
            String idEmployee = jTable1.getValueAt(row, 0).toString();
            
            String newjob = jTextField1.getText().trim();
            String newgender = jTextField2.getText().trim();
            String newempName = jTextField3.getText().trim();
            int newage = Integer.parseInt(jTextField4.getText().trim());
            int newphoneNumber = Integer.parseInt(jTextField5.getText().trim());
            String newusername = jTextField7.getText().trim();
            String newpassword = jTextField6.getText().trim();

            // memanggil prosedur update
            stmt = conn.prepareCall("{call UpdateEmployeeData(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, idEmployee);
            stmt.setString(2, newjob);
            stmt.setString(3, newgender);
            stmt.setString(4, newempName);
            stmt.setInt(5, newage);
            stmt.setInt(6, newphoneNumber);
            stmt.setString(7, newusername);
            stmt.setString(8, newpassword);
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
            String idEmployee = jTable1.getValueAt(row, 0).toString();
            String job = jTable1.getValueAt(row, 1).toString();
            String gender = jTable1.getValueAt(row, 2).toString();
            String empName = jTable1.getValueAt(row, 3).toString();
            String age = jTable1.getValueAt(row, 4).toString();
            String phoneNumber = jTable1.getValueAt(row, 5).toString();
            String username = jTable1.getValueAt(row, 6).toString();
            String empPassword = jTable1.getValueAt(row, 7).toString();
            
            jTextField1.setText(job);
            jTextField2.setText(gender);
            jTextField3.setText(empName);
            jTextField4.setText(age);
            jTextField5.setText(phoneNumber);
            jTextField6.setText(empPassword);
            jTextField7.setText(username);
    }
    
    private void deleteTableData(){
        // mendapatkan baris yang dipilih di tabel jTable1
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel.");
            return;
        }
        // mendapatkan nilai ID_Employee dari baris yang dipilih
        int ID_Employee = (int) jTable1.getValueAt(row, 0);

        try {
            // membuat prepared statement untuk memanggil stored procedure DeleteEmployeeData
            String sql = "EXEC DeleteEmployeeData ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ID_Employee);

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
        model.addColumn("ID Employee");
        model.addColumn("Job");
        model.addColumn("Gender");
        model.addColumn("Employee Name");
        model.addColumn("Age");
        model.addColumn("Phone Number");
        model.addColumn("Username");
        model.addColumn("Password");

        try {
            // query untuk mengambil data dari tabel EmployeeData
            String sql = "SELECT ID_Employee, ID_Job, ID_Gender, Emp_Name, Age, Phone_Number, Username, Emp_Password FROM Employee.EmployeeData";

            // membuat prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // menjalankan query dan menyimpan hasilnya dalam objek ResultSet
            ResultSet rs = ps.executeQuery();

            // mengisi objek DefaultTableModel dengan data dari ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Employee"),
                    rs.getString("ID_Job"),
                    rs.getString("ID_Gender"),
                    rs.getString("Emp_Name"),
                    rs.getInt("Age"),
                    rs.getString("Phone_Number"),
                    rs.getString("Username"),
                    rs.getString("Emp_Password")
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
    
        private void addEmployee() {
            try {
                String job = jTextField1.getText().trim();
                String gender = jTextField2.getText().trim();
                String name = jTextField3.getText().trim();
                int age = Integer.parseInt(jTextField4.getText().trim());
                int phone = Integer.parseInt(jTextField5.getText().trim());
                String username = jTextField7.getText().trim();
                String password = jTextField6.getText().trim();

                //Memanggil stored procedure dengan mengisi parameter-parameter
                stmt = conn.prepareCall("{call InsertNewEmployee(?, ?, ?, ?, ?, ?, ?)}");
                stmt.setString(1, job);
                stmt.setString(2, gender);
                stmt.setString(3, name);
                stmt.setInt(4, age);
                stmt.setInt(5, phone);
                stmt.setString(6, username);
                stmt.setString(7, password);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Employee added successfully!");
                showTableData();
            } catch (NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
    }

        
///buat search
    private void txtSearchDocumentChanged(javax.swing.event.DocumentEvent evt) {
        String keyword = jTextField8.getText();
        searchData(keyword);
    }

    private void addDocumentListenerToSearchField() {
        jTextField8.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        BG_EmpForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo_sepatu-removebg-preview.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Employee Data");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 396, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(20, 20, 20))
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
        jLabel10.setText("Identity");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jLabel10)
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel3.setText("Job Role");

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel4.setText("Gender");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel5.setText("Name");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel6.setText("Age");

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel7.setText("Phone Number");

        jTextField1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jTextField3)
                    .addComponent(jTextField2)
                    .addComponent(jTextField1)
                    .addComponent(jTextField5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 330, 410));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel8.setText("Username");

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N
        jLabel9.setText("Password");

        jTextField6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jTextField7.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 15)); // NOI18N

        jPanel6.setBackground(new java.awt.Color(0, 51, 204));

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 17)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Create Username & Password");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel12)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jTextField6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 330, 200));

        jButton1.setBackground(new java.awt.Color(0, 153, 204));
        jButton1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        jButton1.setText("CLEAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 720, 160, -1));

        jButton2.setBackground(new java.awt.Color(0, 153, 204));
        jButton2.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        jButton2.setText("ADD");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 720, 160, -1));

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

        jButton3.setBackground(new java.awt.Color(0, 153, 204));
        jButton3.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 720, 50, -1));

        jButton4.setBackground(new java.awt.Color(0, 153, 204));
        jButton4.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        jButton4.setText("DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 720, 380, -1));

        jTextField8.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 20)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, 660, 40));

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 18)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search_FILL0_wght400_GRAD0_opsz48.png"))); // NOI18N
        jLabel11.setText("SEARCH");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 80, -1, 40));

        jButton6.setBackground(new java.awt.Color(0, 153, 204));
        jButton6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 13)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N
        jButton6.setText("UPDATE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 720, 320, -1));

        BG_EmpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background_crud1.png"))); // NOI18N
        getContentPane().add(BG_EmpForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 820));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addEmployee();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        updateRow();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        AdminDashboard admForm = new AdminDashboard();
        admForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        String keyword = jTextField8.getText();
        searchData(keyword);
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    jTextField1.setText("");
    jTextField2.setText("");
    jTextField3.setText("");
    jTextField4.setText("");
    jTextField5.setText("");
    jTextField6.setText("");
    jTextField7.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        deleteTableData();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        updateTableData();
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG_EmpForm;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
