/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author luan
 */
public class ItemLevel extends javax.swing.JPanel {

    private String nomeLvl;
    private File lvlFile;
    
    private int tamanho;
    private String objetivo;
    private String velocidade;
    private String corrida;
    private ImageIcon imgPreview;
    
    public ItemLevel() {
        initComponents();
    }
    
    public ItemLevel(File lvlFile) {
        initComponents();
        this.lvlFile = lvlFile;
        loadLvlData();
    }
    
    private void loadLvlData(){
        if (lvlFile == null) return;
        new Thread(() -> {
            loadGameLvl();
        
            //instanciateLvlPreview();
            jLabel1.setIcon(imgPreview);
            txtNome.setText(nomeLvl);
            txtObj.setText("± " + objetivo);
            txtVelocidade.setText("» " + velocidade);
            txtTamanho.setText("͏   " + tamanho);
            txtCorrida.setText("»» " + corrida);
        }).start();
    }
    
    public String getLvlFile(){
        return lvlFile.getAbsolutePath();
    }
    
    private void loadGameLvl(){
        /* Dado um arquivo de lvl, essa função é responsável por carrega-lo. */
        try {            
            BufferedReader br = new BufferedReader(new FileReader(lvlFile));
            String line;
            while ((line = br.readLine()) != null){
                String[] dataStruct = line.split(":");

                //Contem dados de variaveis
                if (dataStruct.length == 2){
                    switch (dataStruct[0]){
                        case "size_required":
                            objetivo = dataStruct[1];
                            break;
                        case "map_size":
                            tamanho = Integer.parseInt(dataStruct[1]);
                            break;
                        case "img_preview":
                            imgPreview = new ImageIcon(Base64.getDecoder().decode(dataStruct[1]));
                            imgPreview = new ImageIcon(imgPreview.getImage().getScaledInstance(120, 89, Image.SCALE_DEFAULT));
                            break;
                        case "sleep_timer":
                            velocidade = dataStruct[1];
                            break;
                        case "rushing_speed":
                            corrida = dataStruct[1];
                            break;
                        case "name":
                            nomeLvl = dataStruct[1];
                            break;
                    }
                } else {
                    if (line.equals("MAP")){
                        br.close();
                        break;
                    }
                }
            }        
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNome = new javax.swing.JLabel();
        txtObj = new javax.swing.JLabel();
        txtTamanho = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtVelocidade = new javax.swing.JLabel();
        txtCorrida = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setMaximumSize(new java.awt.Dimension(142, 136));
        setMinimumSize(new java.awt.Dimension(142, 136));

        txtNome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNome.setText("Nome");

        txtObj.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtObj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtObj.setText("Objetivo");
        txtObj.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTamanho.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTamanho.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTamanho.setText("Tamanho");
        txtTamanho.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setPreferredSize(new java.awt.Dimension(120, 89));

        txtVelocidade.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtVelocidade.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtVelocidade.setText("Objetivo");
        txtVelocidade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtCorrida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCorrida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtCorrida.setText("Tamanho");
        txtCorrida.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtTamanho, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(txtObj, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtVelocidade, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCorrida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtObj)
                    .addComponent(txtVelocidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTamanho)
                    .addComponent(txtCorrida))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel txtCorrida;
    private javax.swing.JLabel txtNome;
    private javax.swing.JLabel txtObj;
    private javax.swing.JLabel txtTamanho;
    private javax.swing.JLabel txtVelocidade;
    // End of variables declaration//GEN-END:variables
}
