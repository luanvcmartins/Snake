/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Menu;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import snake.Principal;
import static snake.UI.Widget.UIFeedback.getFeedback;

/**
 *
 * @author luan
 */
public class TelaInicial extends javax.swing.JPanel {

    /**
     * Creates new form TelaInicial
     */
    
    Principal principal;
    private final Random r = new Random();
    
    public TelaInicial(Principal principal) {
        initComponents();
        
        this.principal = principal;
        
        this.btnNovoJogo.addMouseListener(getFeedback());
        this.btnHistorico.addMouseListener(getFeedback());
        this.btnCreditos.addMouseListener(getFeedback());
        this.btnSair.addMouseListener(getFeedback());
        this.btnMapBuilder.addMouseListener(getFeedback());
        this.btnPartidaRapida.addMouseListener(getFeedback());
        this.btnSeletorDeFase.addMouseListener(getFeedback());
        this.btnNovoJogo.addMouseListener(customFeedback);
        this.btnHistorico.addMouseListener(customFeedback);
        this.btnCreditos.addMouseListener(customFeedback);
        this.btnSair.addMouseListener(customFeedback);
        this.btnMapBuilder.addMouseListener(customFeedback);
        this.btnPartidaRapida.addMouseListener(customFeedback);
        this.btnSeletorDeFase.addMouseListener(customFeedback);
    }
    
    private MouseAdapter customFeedback = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            /*
            int i = r.nextInt(10);
            if (i == 0)
                principal.changeMenuMusic();
            else if (i == 1)
                principal.changeMenuMusicLvl();*/
        }
        
    };
        
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNovoJogo = new javax.swing.JButton();
        btnPartidaRapida = new javax.swing.JButton();
        btnMapBuilder = new javax.swing.JButton();
        btnHistorico = new javax.swing.JButton();
        btnCreditos = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        btnSeletorDeFase = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        btnNovoJogo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnNovoJogo.setText("Modo História");
        btnNovoJogo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNovoJogo.setContentAreaFilled(false);
        btnNovoJogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoJogoActionPerformed(evt);
            }
        });

        btnPartidaRapida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPartidaRapida.setText("Partida Rápida");
        btnPartidaRapida.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPartidaRapida.setContentAreaFilled(false);
        btnPartidaRapida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPartidaRapida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartidaRapidaActionPerformed(evt);
            }
        });

        btnMapBuilder.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnMapBuilder.setText("Construção de Fases");
        btnMapBuilder.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnMapBuilder.setContentAreaFilled(false);
        btnMapBuilder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMapBuilder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapBuilderActionPerformed(evt);
            }
        });

        btnHistorico.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnHistorico.setText("Histórico de Partidas");
        btnHistorico.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnHistorico.setContentAreaFilled(false);
        btnHistorico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoricoActionPerformed(evt);
            }
        });

        btnCreditos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCreditos.setText("Créditos");
        btnCreditos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCreditos.setContentAreaFilled(false);
        btnCreditos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditosActionPerformed(evt);
            }
        });

        btnSair.setBackground(new java.awt.Color(204, 204, 204));
        btnSair.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSair.setText("Sair");
        btnSair.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSair.setContentAreaFilled(false);
        btnSair.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        btnSeletorDeFase.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSeletorDeFase.setText("Jogar fase (escolha)");
        btnSeletorDeFase.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSeletorDeFase.setContentAreaFilled(false);
        btnSeletorDeFase.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSeletorDeFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeletorDeFaseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 108)); // NOI18N
        jLabel1.setText("<html><center>Snake!<br>\n<small>APSOO - T1 - G2</small></center>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(243, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMapBuilder, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPartidaRapida, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovoJogo, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSeletorDeFase, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnNovoJogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPartidaRapida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSeletorDeFase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMapBuilder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHistorico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreditos)
                .addGap(18, 18, 18)
                .addComponent(btnSair)
                .addContainerGap(74, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovoJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoJogoActionPerformed
        try {
            principal.startModoHistória();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnNovoJogoActionPerformed

    private void btnPartidaRapidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartidaRapidaActionPerformed
        principal.startGameRapido();
    }//GEN-LAST:event_btnPartidaRapidaActionPerformed

    private void btnMapBuilderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapBuilderActionPerformed
        principal.startMapBuilder();
    }//GEN-LAST:event_btnMapBuilderActionPerformed

    private void btnHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoricoActionPerformed
        principal.startHistoricoPartida();
    }//GEN-LAST:event_btnHistoricoActionPerformed

    private void btnCreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditosActionPerformed
        principal.startCreditosScreen();
    }//GEN-LAST:event_btnCreditosActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnSeletorDeFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeletorDeFaseActionPerformed
        principal.startLvlSelection();
    }//GEN-LAST:event_btnSeletorDeFaseActionPerformed


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreditos;
    private javax.swing.JButton btnHistorico;
    private javax.swing.JButton btnMapBuilder;
    private javax.swing.JButton btnNovoJogo;
    private javax.swing.JButton btnPartidaRapida;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSeletorDeFase;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
