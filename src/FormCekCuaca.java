import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import javax.swing.SwingWorker;

public class FormCekCuaca extends javax.swing.JFrame {
    
private final String API_KEY = "ca5ecf0af191a3576acbad94c382ab0f";
private final String FAVORITES_FILE = "favorites.txt";


    public FormCekCuaca() {
        initComponents();
        setLocationRelativeTo(null);
        isiKotaDefault();
        tblData.setModel(new DefaultTableModel(new Object[]{"Kota","Suhu (°C)","Kondisi"}, 0));
        loadFavoritesToCombo();
    }
    
    // isi combo dengan beberapa kota default
private void isiKotaDefault() {
    cmbKota.removeAllItems();
    cmbKota.addItem("Pilih Kota...");
    cmbKota.addItem("Jakarta");
    cmbKota.addItem("Bandung");
    cmbKota.addItem("Surabaya");
    cmbKota.addItem("Denpasar");
}

// load favorites dari file ke combo
private void loadFavoritesToCombo() {
    File f = new File(FAVORITES_FILE);
    if (!f.exists()) return;
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) cmbKota.addItem(line.trim());
        }
    } catch (Exception e) {
        // tidak fatal
    }
}

// tambahkan favorite ke combo dan file
private void addFavorite(String kota) {
    if (kota == null || kota.trim().isEmpty()) return;
    cmbKota.addItem(kota);
    try (PrintWriter pw = new PrintWriter(new FileWriter(FAVORITES_FILE, true))) {
        pw.println(kota);
    } catch (Exception e) {
        // ignore
    }
}

// ambil data cuaca dan update UI menggunakan SwingWorker supaya UI tidak freeze
private void fetchAndDisplayWeather(String kota) {
    new SwingWorker<Void, Void>() {
        private String errorMsg = null;
        private double temp = 0;
        private String main = "";
        private String icon = "";

        @Override
        protected Void doInBackground() {
            try {
                String url = "https://api.openweathermap.org/data/2.5/weather?q="
                             + java.net.URLEncoder.encode(kota, "UTF-8")
                             + "&units=metric&appid=" + API_KEY;
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() != 200) {
                    errorMsg = "HTTP error: " + resp.statusCode();
                    return null;
                }
                String body = resp.body();
                JSONObject obj = new JSONObject(body);
                temp = obj.getJSONObject("main").getDouble("temp");
                JSONArray arr = obj.getJSONArray("weather");
                JSONObject w0 = arr.getJSONObject(0);
                main = w0.getString("main");
                icon = w0.getString("icon");
            } catch (Exception ex) {
                errorMsg = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void done() {
            if (errorMsg != null) {
                javax.swing.JOptionPane.showMessageDialog(FormCekCuaca.this, "Gagal ambil data: " + errorMsg, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            lblSuhu.setText(String.format("%.1f °C", temp));
            lblKondisi.setText(main);
            // set icon dari OpenWeather (online)
            try {
                java.net.URL url = new java.net.URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
                lblIcon.setIcon(new javax.swing.ImageIcon(url));
            } catch (Exception ex) {
                lblIcon.setIcon(null);
            }
            // tambahkan ke tabel
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();
            model.addRow(new Object[]{kota, temp, main});
        }
    }.execute();
}

// simpan tabel ke CSV
private void saveTableToCsv(File f) throws Exception {
    try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
        DefaultTableModel model = (DefaultTableModel) tblData.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            pw.printf("%s,%s,%s%n", model.getValueAt(i,0), model.getValueAt(i,1), model.getValueAt(i,2));
        }
    }
}

// muat CSV ke tabel
private void loadCsvToTable(File f) throws Exception {
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        DefaultTableModel model = (DefaultTableModel) tblData.getModel();
        model.setRowCount(0);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length >= 3) model.addRow(new Object[]{parts[0], parts[1], parts[2]});
        }
    }
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
        lblJudul = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        lblKota = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        cmbKota = new javax.swing.JComboBox<>();
        btnCek = new javax.swing.JButton();
        lblSuhu = new javax.swing.JLabel();
        lblKondisi = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();
        btnFav = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnMuat = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        lblJudul.setText("APLIKASI CEK CUACA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(136, 136, 136)
                .addComponent(lblJudul)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblJudul)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        panel.setBackground(new java.awt.Color(204, 204, 204));

        lblKota.setText("KOTA");

        cmbKota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbKotaItemStateChanged(evt);
            }
        });

        btnCek.setText("CEK CUACA");
        btnCek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekActionPerformed(evt);
            }
        });

        btnFav.setText("FAVORITE");
        btnFav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFavActionPerformed(evt);
            }
        });

        btnSimpan.setText("SIMPAN CSV");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnMuat.setText("MUAT CSV");
        btnMuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuatActionPerformed(evt);
            }
        });

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblData);

        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(lblKota)
                        .addGap(15, 15, 15)
                        .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKota, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnCek, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblSuhu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblKondisi)
                        .addGap(122, 122, 122)
                        .addComponent(lblIcon)
                        .addGap(16, 16, 16))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(btnFav, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKota)
                    .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCek)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSuhu)
                    .addComponent(lblIcon)
                    .addComponent(lblKondisi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFav)
                    .addComponent(btnSimpan)
                    .addComponent(btnMuat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnKeluar))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekActionPerformed
        // TODO add your handling code here:
            String kota;
    if (cmbKota.getSelectedIndex() > 0) {
        kota = cmbKota.getSelectedItem().toString();
    } else {
        kota = txtKota.getText().trim();
    }
    if (kota.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Masukkan atau pilih nama kota!");
        return;
    }
    fetchAndDisplayWeather(kota);
    }//GEN-LAST:event_btnCekActionPerformed

    private void cmbKotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbKotaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && cmbKota.getSelectedIndex() > 0) {
        String kota = cmbKota.getSelectedItem().toString();
        fetchAndDisplayWeather(kota);
    }
    }//GEN-LAST:event_cmbKotaItemStateChanged

    private void btnFavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFavActionPerformed
        // TODO add your handling code here:
            String kota = txtKota.getText().trim();
    if (kota.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Masukkan nama kota di field input!");
        return;
    }
    addFavorite(kota);
    javax.swing.JOptionPane.showMessageDialog(this, "Kota ditambahkan ke favorit.");
    }//GEN-LAST:event_btnFavActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    if (fc.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
        try {
            saveTableToCsv(fc.getSelectedFile());
            javax.swing.JOptionPane.showMessageDialog(this, "Data tersimpan.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnMuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    if (fc.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
        try {
            loadCsvToTable(fc.getSelectedFile());
            javax.swing.JOptionPane.showMessageDialog(this, "Berhasil dimuat.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal muat: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnMuatActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
    lblSuhu.setText("");
    lblKondisi.setText("");
    lblIcon.setIcon(null);
    ((DefaultTableModel) tblData.getModel()).setRowCount(0);

    txtKota.setText("");
    cmbKota.setSelectedIndex(0);

    txtKota.requestFocus();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        int konf = javax.swing.JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
        if (konf == javax.swing.JOptionPane.YES_OPTION) System.exit(0);
    }//GEN-LAST:event_btnKeluarActionPerformed

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
            java.util.logging.Logger.getLogger(FormCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormCekCuaca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormCekCuaca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCek;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnFav;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnMuat;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbKota;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKondisi;
    private javax.swing.JLabel lblKota;
    private javax.swing.JLabel lblSuhu;
    private javax.swing.JPanel panel;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtKota;
    // End of variables declaration//GEN-END:variables
}
