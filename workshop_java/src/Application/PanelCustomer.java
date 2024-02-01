package Application;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import mysqlConnect.MysqlConnect;
import validation.Validation;

public class PanelCustomer extends javax.swing.JPanel {

   Connection conn = null; //ເກັບການເຊື່ອມຕໍ່ຖານຂໍ້ມູນ
    PreparedStatement pst = null; //ກຽມຊຸດຄຳສັ່ງ
    ResultSet rs = null; //ເກັບຜົນໄດ້ຮັບ
    
    String id;
    
    public PanelCustomer(String i) {
        initComponents();
        
        id = i;
        txtTelephone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "020 12345678");
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ຄົ້ນຫາຂໍ້ມູນ");
        txtSearch.setFont(new Font("Phetsarath OT", Font.TRUETYPE_FONT, 13));
        
        FlatSVGIcon icon = new FlatSVGIcon("image_svg/search.svg");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, icon);
        
        //ກຳນົດປຸ່ມແກ້ໄຂ ແລະ ປຸ່ມລຶບບໍ່ໃຫ້ໃຊ້ງານ
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        
        
        //ກຳນົດໃຫ້ປຸ່ມຊາຍ ແລະ ຍິງ
        txtMale.setActionCommand("ຊາຍ");
        txtFemale.setActionCommand("ຍິງ");
        
        
        //ປ່ຽນຟອນ ແລະ ສີພື້ນຫົວຕາຕະລາງ      
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Phetsarath OT", Font.BOLD, 16));
        header.setOpaque(false);
        header.setBackground(new Color(108, 117, 125));
        header.setForeground(new Color(243, 243, 243));
        //ເສັ້ນຕາຕະລາງ
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
        jTable1.setGridColor(new Color(139, 138, 137));

        //ສະແດງຜົນຢູ່ກາງຖັນ
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); //ໃຫ້ຂໍ້ມູນຢູ່ຖັນ ລະຫັດສະແດງທີ່ກາງຫ້ອງ
        jTable1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); //ໃຫ້ຂໍ້ມູນຢູ່ຖັນ ເພດ ສະແດງທີ່ກາງຫ້ອງ

        //ສະແດງຜົນຂໍ້ມູນຢູ່ຕິດດ້ານຂວາຂອງຖັນ
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        //ເຊື່ອມ database
        conn = MysqlConnect.connectDB();
        updateTable();
        autoId();
    }
    
    //ສ້າງເມັດທອດສະເເດງຂໍ້ມູນຕາຕະລາງ
    private void updateTable(){
        try {
            String sql = "SELECT *FROM customer ORDER BY cus_id DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            jTable1.setRowHeight(30); //ຂະໜາດຄວາມສູງຂອງແຖວ
            d.setRowCount(0); //ລືບແຖວຂອງຕາຕະລາງອອກກ່ອນ
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("cus_id"));
                v.add(rs.getString("cus_name"));
                v.add(rs.getString("cus_lname"));
                v.add(rs.getString("gender"));
                v.add(rs.getString("address"));
                v.add(rs.getString("tel"));
                d.addRow(v);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    
    }
    
    //ສ້າງເມັດທອດສະເເດງລະຫັດລູກຄ້າເເບບ auto
    private void autoId(){
        try {
            String sql = "SELECT MAX(cus_id) AS max_id FROM customer";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            if (rs.getString("max_id") == null) {
                txtId.setText("CUS0000001");
            } else {
                int id = Integer.parseInt(rs.getString("max_id").substring(3, rs.getString("max_id").length()));
                id++;
                txtId.setText("CUS" + String.format("%07d", id));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //ສ້າງເມັດທອດກວດສອບຟອມ
    private boolean checkInputs() {
        return txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty()
                || gender.getSelection() == null || txtAddress.getText().isEmpty()
                || txtTelephone.getText().isEmpty();
    }
    
    //ສ້າງເມັດທອດລືບຄ່າຂໍ້ມູນໃນຟອມ
    private void clearForm() {
        autoId();
        txtFirstname.setText("");
        txtLastname.setText("");
        gender.clearSelection();
        txtAddress.setText("");
        txtTelephone.setText("");
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        jTable1.clearSelection();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gender = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtFirstname = new javax.swing.JTextField();
        txtLastname = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        txtFemale = new javax.swing.JRadioButton();
        txtMale = new javax.swing.JRadioButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ຈັດການຂໍ້ມູນລູກຄ້າ\n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Phetsarath OT", 0, 13))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel1.setText("ເພດ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, -1, -1));

        jLabel3.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel3.setText("ເບີໂທລະສັບ");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 150, -1, -1));

        jLabel4.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel4.setText("ຊື່ລູກຄ້າ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Phetsarath OT Bold Italic", 0, 13)); // NOI18N
        jPanel2.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 260, -1));

        txtFirstname.setFont(new java.awt.Font("Phetsarath OT Bold Italic", 0, 13)); // NOI18N
        jPanel2.add(txtFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 260, -1));

        txtLastname.setFont(new java.awt.Font("Phetsarath OT Bold Italic", 0, 13)); // NOI18N
        jPanel2.add(txtLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 200, -1));

        jLabel6.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel6.setText("ລະຫັດລູກຄ້າ");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel7.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel7.setText("ທີ່ຢູ່");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        txtTelephone.setFont(new java.awt.Font("Phetsarath OT Bold Italic", 0, 13)); // NOI18N
        jPanel2.add(txtTelephone, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 150, 260, -1));

        txtAddress.setColumns(20);
        txtAddress.setRows(5);
        jScrollPane2.setViewportView(txtAddress);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 260, 90));

        gender.add(txtFemale);
        txtFemale.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        txtFemale.setText("ຍິງ");
        txtFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFemaleActionPerformed(evt);
            }
        });
        jPanel2.add(txtFemale, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, -1, -1));

        gender.add(txtMale);
        txtMale.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        txtMale.setText("ຊາຍ");
        txtMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaleActionPerformed(evt);
            }
        });
        jPanel2.add(txtMale, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, -1, -1));

        btnCancel.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        btnCancel.setText("ຍົກເລີກ");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, -1, -1));

        btnAdd.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        btnAdd.setText("ເພິ່ມ");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, -1, -1));

        btnEdit.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        btnEdit.setText("ແກ້ໄຂ");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel2.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 200, -1, -1));

        btnDelete.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        btnDelete.setText("ລືບ");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 200, -1, -1));

        jLabel8.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jLabel8.setText("ນາມສະກຸນ");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 806, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Phetsarath OT", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ລະຫັດ ", "ຊື່ລູກຄ້າ", "ນາມສະກຸນ", "ເພດ", "ທີ່ຢູ່", "ເບີໂທ"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txtSearch.setFont(new java.awt.Font("Phetsarath OT Bold Italic", 0, 13)); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void txtFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFemaleActionPerformed

    private void txtMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaleActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if(checkInputs()){
            JOptionPane.showMessageDialog(null, "ກະລຸນນາປ້ອນໃຫ້ຄົບຖ້ວນດ້ວຍ", "ຫວ່າງເປົ່າ", JOptionPane.WARNING_MESSAGE);
            return;
        }else if (!Validation.telephoneValidation(txtTelephone.getText())){
            JOptionPane.showMessageDialog(null, "ກະລຸນນາປ້ອນເບີໂທລະສັບໃຫ້ຖືກຕ້ອງດ້ວຍ", "ຫວ່າງເປົ່າ", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String sql = "INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            pst.setString(2, txtFirstname.getText());
            pst.setString(3, txtLastname.getText());
            pst.setString(4, gender.getSelection().getActionCommand());
            pst.setString(5, txtAddress.getText());
            pst.setString(6, txtTelephone.getText());
            
            pst.executeUpdate();
            
             FlatSVGIcon icon = new FlatSVGIcon("image_svg/done.svg");
            JOptionPane.showMessageDialog(null, "ຂໍ້ມູນຖືກບັນທຶກລົງໃນຖານຂໍ້ມູນສໍາເລັດແລ້ວ", "ສໍາເລັດ", JOptionPane.WIDTH, icon);
            clearForm();
            updateTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_btnAddActionPerformed
    
    //Update method
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if(checkInputs()){
            JOptionPane.showMessageDialog(null, "ກະລຸນນາປ້ອນໃຫ້ຄົບຖ້ວນດ້ວຍ", "ຫວ່າງເປົ່າ", JOptionPane.WARNING_MESSAGE);
            return;
        }else if (!Validation.telephoneValidation(txtTelephone.getText())){
            JOptionPane.showMessageDialog(null, "ກະລຸນນາປ້ອນເບີໂທລະສັບໃຫ້ຖືກຕ້ອງດ້ວຍ", "ຫວ່າງເປົ່າ", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String sql = "UPDATE customer SET cus_name=?, cus_lname=?, gender=?, address=?, tel=? WHERE cus_id=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtFirstname.getText());
            pst.setString(2, txtLastname.getText());
            pst.setString(3, gender.getSelection().getActionCommand());
            pst.setString(4, txtAddress.getText());
            pst.setString(5, txtTelephone.getText());
            pst.setString(6, txtId.getText());
            
            pst.executeUpdate();
            
             FlatSVGIcon icon = new FlatSVGIcon("image_svg/done.svg");
            JOptionPane.showMessageDialog(null, "ປັບປຸງໃນຖານຂໍ້ມູນສໍາເລັດແລ້ວ", "ສໍາເລັດ", JOptionPane.WIDTH, icon);
            clearForm();
            updateTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
        int selectIndex = jTable1.getSelectedRow(); //ເກັບແຖວທີ່ເລືອກໄວ້ໃນຕົວປ່ຽນ selectIndex
        if (selectIndex == -1) {
            return;
        }
        txtId.setText(d.getValueAt(selectIndex, 0).toString());
        txtFirstname.setText(d.getValueAt(selectIndex, 1).toString());
        txtLastname.setText(d.getValueAt(selectIndex, 2).toString());

        String gender = d.getValueAt(selectIndex, 3).toString();
        if (gender.equals("ຊາຍ")) {
            txtMale.setSelected(true);
        } else {
            txtFemale.setSelected(true);
        }

        txtAddress.setText(d.getValueAt(selectIndex, 4).toString());
        txtTelephone.setText(d.getValueAt(selectIndex, 5).toString());

        //ໃຫ້ປຸ່ມເພີ້ມໃຊ້ງານບໍ່ໄດ້ ແລະ ປຸ່ມແກ້ໄຂ, ລືບ ໃຊ້ງານໄດ້
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.ButtonGroup gender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JRadioButton txtFemale;
    private javax.swing.JTextField txtFirstname;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLastname;
    private javax.swing.JRadioButton txtMale;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables

 
}
