import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import java.text.ParseException;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class AgendaPribadiFrame extends javax.swing.JFrame {

    /**
     * Creates new form AgendaPribadiFrame
     */
    public AgendaPribadiFrame() {
        initComponents();
        inisialisasi();
        muatData();
    }

    // Inisialisasi komponen tambahan
    private void inisialisasi() {
        // Atur model untuk combo box kategori
        comboKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keluarga", "Pribadi", "Kerja" }));
        
        // Atur model untuk spinner jam tanpa detik
        jSpinner1.setModel(new javax.swing.SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jSpinner1, "HH:mm"); // Ubah format ke "HH:mm"
        jSpinner1.setEditor(timeEditor);
        jSpinner1.setValue(new java.util.Date());
        
        // Tambahkan listener untuk tabel
        tabelAgenda.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tabelAgenda.getSelectedRow();
                if (selectedRow >= 0) {
                    teksNama.setText(tabelAgenda.getValueAt(selectedRow, 1).toString());
                    teksDeskripsi.setText(tabelAgenda.getValueAt(selectedRow, 2).toString());
                    comboKategori.setSelectedItem(tabelAgenda.getValueAt(selectedRow, 3).toString());
                    
                    // Mengatur tanggal
                    String dateString = tabelAgenda.getValueAt(selectedRow, 4).toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Sesuaikan format
                    java.util.Date tanggalUtil = null;
                    try {
                        tanggalUtil = dateFormat.parse(dateString);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Format tanggal tidak valid: " + ex.getMessage());
                    }

                    if (tanggalUtil != null) {
                        jDateChooser1.setDate(tanggalUtil);
                    }

                    // Mengatur waktu tanpa detik
                    String timeString = tabelAgenda.getValueAt(selectedRow, 5).toString();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Ubah format ke "HH:mm"
                    java.util.Date waktuUtil = null;
                    try {
                        waktuUtil = timeFormat.parse(timeString);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Format waktu tidak valid: " + ex.getMessage());
                    }

                    if (waktuUtil != null) {
                        jSpinner1.setValue(waktuUtil);
                    }
                }
            }
        });
    }
     
    // Metode untuk memuat data ke tabel
    // Metode untuk memuat data ke tabel
    private void muatData() {
        // Tambahkan kolom "Status" dengan tipe Boolean
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama", "Deskripsi", "Kategori", "Tanggal", "Jam", "Status"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Boolean.class; // Kolom status berupa checkbox
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom status yang dapat diedit
                return column == 6;
            }
        };

        List<Agenda> listAgenda = Agenda.getAllAgenda();
        for (Agenda a : listAgenda) {
            model.addRow(new Object[]{a.getId(), a.getNama(), a.getDeskripsi(), a.getKategori(), a.getTanggal(), a.getJam(), a.isStatus()});
        }
        tabelAgenda.setModel(model);

        // Set renderer untuk menerapkan coretan pada baris yang statusnya true
        tabelAgenda.setDefaultRenderer(Object.class, new StrikethroughRenderer());

        // Tambahkan listener untuk perubahan pada checkbox
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 6) { // Kolom status
                    Boolean status = (Boolean) model.getValueAt(row, column);
                    int id = (int) model.getValueAt(row, 0);

                    // Update status di database
                    Agenda agenda = new Agenda(id, null, null, null, null, null, status);
                    agenda.updateStatus();
                    // Refresh tampilan tabel
                    tabelAgenda.repaint();
                }
            }
        });
    }
    
    // Custom renderer untuk menerapkan coretan
    class StrikethroughRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Boolean status = (Boolean) table.getModel().getValueAt(row, 6);
        if (status != null && status) {
            // Apply strikethrough
            Font font = c.getFont();
            Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            c.setFont(font.deriveFont(attributes));
        } else {
            // Remove strikethrough
            Font font = c.getFont();
            Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
            attributes.remove(TextAttribute.STRIKETHROUGH);
            c.setFont(font.deriveFont(attributes));
        }
        return c;
    }
}
    
    // Metode untuk mengosongkan field input
    private void kosongkanField() {
        teksNama.setText("");
        teksDeskripsi.setText("");
        comboKategori.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jSpinner1.setValue(new java.util.Date());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboKategori = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        teksNama = new javax.swing.JTextField();
        teksDeskripsi = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelAgenda = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("UTS"));

        jLabel1.setText("Aplikas Agenda Pribadi");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Nama Agenda");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Deskripsi");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Kategori");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Pilih Tanggal dan Jam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jLabel5, gridBagConstraints);

        comboKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(comboKategori, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jDateChooser1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jSpinner1, gridBagConstraints);

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jButton1, gridBagConstraints);

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jButton2, gridBagConstraints);

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(jButton3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(teksNama, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel2.add(teksDeskripsi, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        tabelAgenda.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabelAgenda);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel3.add(jScrollPane2, gridBagConstraints);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        jPanel4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel4.add(jTextField3, gridBagConstraints);

        jButton4.setText("Cari");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel4.add(jButton4, gridBagConstraints);

        jButton5.setText("Simpan data");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel4.add(jButton5, gridBagConstraints);

        jButton6.setText("Muat Data");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jPanel4.add(jButton6, gridBagConstraints);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String nama = teksNama.getText();
        String deskripsi = teksDeskripsi.getText();
        String kategori = comboKategori.getSelectedItem().toString();
        java.util.Date tanggalUtil = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Ubah format ke "HH:mm"
        String jam = sdf.format(jSpinner1.getValue());

        if (tanggalUtil != null) {
            java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());
            Agenda agenda = new Agenda(nama, deskripsi, kategori, tanggal, jam, false); // Status default false
            agenda.simpanAgenda();
            muatData();
            kosongkanField(); // Mengosongkan field setelah menambah data
            JOptionPane.showMessageDialog(this, "Agenda berhasil ditambahkan");
        } else {
            JOptionPane.showMessageDialog(this, "Pilih tanggal terlebih dahulu");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = tabelAgenda.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(tabelAgenda.getValueAt(selectedRow, 0).toString());
            String nama = teksNama.getText();
            String deskripsi = teksDeskripsi.getText();
            String kategori = comboKategori.getSelectedItem().toString();
            java.util.Date tanggalUtil = jDateChooser1.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Ubah format ke "HH:mm"
            String jam = sdf.format(jSpinner1.getValue());
            Boolean status = (Boolean) tabelAgenda.getValueAt(selectedRow, 6);

            if (tanggalUtil != null) {
                java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());
                Agenda agenda = new Agenda(id, nama, deskripsi, kategori, tanggal, jam, status);
                agenda.updateAgenda();
                muatData();
                kosongkanField(); // Mengosongkan field setelah mengedit data
                JOptionPane.showMessageDialog(this, "Agenda berhasil diupdate");
            } else {
                JOptionPane.showMessageDialog(this, "Pilih tanggal terlebih dahulu");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih agenda yang akan diubah");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = tabelAgenda.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(tabelAgenda.getValueAt(selectedRow, 0).toString());
            Agenda agenda = new Agenda(id, null, null, null, null, null, false);
            agenda.hapusAgenda();
            muatData();
            kosongkanField(); // Mengosongkan field setelah menghapus data
            JOptionPane.showMessageDialog(this, "Agenda berhasil dihapus");
        } else {
            JOptionPane.showMessageDialog(this, "Pilih agenda yang akan dihapus");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String keyword = jTextField3.getText();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama", "Deskripsi", "Kategori", "Tanggal", "Jam", "Status"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Boolean.class; // Kolom status berupa checkbox
                }
                return super.getColumnClass(columnIndex);
            }
        };
        List<Agenda> listAgenda = Agenda.cariAgenda(keyword);
        for (Agenda a : listAgenda) {
            model.addRow(new Object[]{a.getId(), a.getNama(), a.getDeskripsi(), a.getKategori(), a.getTanggal(), a.getJam(), a.isStatus()});
        }
        tabelAgenda.setModel(model);
        tabelAgenda.setDefaultRenderer(Object.class, new StrikethroughRenderer());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser chooser = new JFileChooser();
        int retrival = chooser.showSaveDialog(this);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".csv")) {
                List<Agenda> listAgenda = Agenda.getAllAgenda();
                fw.append("ID,Nama,Deskripsi,Kategori,Tanggal,Jam,Status\n");
                for (Agenda a : listAgenda) {
                    fw.append(a.getId() + "," + a.getNama() + "," + a.getDeskripsi() + "," + a.getKategori() + "," + a.getTanggal() + "," + a.getJam() + "," + a.isStatus() + "\n");
                }
                JOptionPane.showMessageDialog(this, "Data berhasil diekspor");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        JFileChooser chooser = new JFileChooser();
        int retrival = chooser.showOpenDialog(this);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                String line;
                br.readLine(); // Skip header
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Sesuaikan format ini
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 7) {
                        String nama = data[1];
                        String deskripsi = data[2];
                        String kategori = data[3];
                        java.util.Date tanggalUtil = dateFormat.parse(data[4]);
                        java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());
                        String jam = data[5];
                        boolean status = Boolean.parseBoolean(data[6]);
                        Agenda agenda = new Agenda(nama, deskripsi, kategori, tanggal, jam, status);
                        agenda.simpanAgenda();
                    }
                }
                muatData();
                JOptionPane.showMessageDialog(this, "Data berhasil diimpor");
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengimpor data: " + ex.getMessage());
            }
        }
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
            java.util.logging.Logger.getLogger(AgendaPribadiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgendaPribadiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgendaPribadiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgendaPribadiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgendaPribadiFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboKategori;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTable tabelAgenda;
    private javax.swing.JTextField teksDeskripsi;
    private javax.swing.JTextField teksNama;
    // End of variables declaration//GEN-END:variables
}
