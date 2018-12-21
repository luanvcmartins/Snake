/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import snake.Principal;
import snake.UI.Widget.UIFeedback;
import static snake.UI.Widget.UIFeedback.getFeedback;
import snake.pessoa.Pessoa;

/**
 *
 * @author luan
 */
public class HistoricoPartidas extends javax.swing.JPanel {

    /**
     * Creates new form TelaInicial
     */
    
    Principal principal;
   
    
    public HistoricoPartidas(Principal principal) {
        initComponents();
        
        this.principal = principal;
        
        iniciar();
        
    }
    
   
    private void iniciar(){
        jComboBox2.setEnabled(false);
        
        modo.setFocusable(true);
        
        jComboBox.removeAllItems();
        jComboBox.addItem("Escolha tipo");
        jComboBox.addItem("Por Pessoa");
        jComboBox.addItem("Por Fase");
        jComboBox.addActionListener(al1);
        
        jComboBox2.addActionListener(al2);
        
        voltar.addMouseListener(UIFeedback.getFeedback());
        
        modo.removeAllItems();
        modo.addItem("Normal");
        modo.addItem("Modo História");
        
        modo.addActionListener(al1);
        
        
    }
    
    private ActionListener al1 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String s = (String)((JComboBox)e.getSource()).getSelectedItem();
            try {
                tratarItemSelecionado(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    
    private ActionListener al2 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            
            try {
                tratarItemSelecionadoBox2((String)((JComboBox)e.getSource()).getSelectedItem());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
                
            
        }
    };
    
    private void tratarItemSelecionadoBox2(String item) throws Exception{
        if(item != null && !item.equals("Escolha")){
            if(((String)jComboBox.getSelectedItem()).equals("Por Pessoa")){
                updateTablePorPessoa(principal.getBd().getRankingWithoutLimit(item));
            }
            if(((String)jComboBox.getSelectedItem()).equals("Por Fase")){
                System.out.println(item);
                if(item.equals("Partida Rápida"))
                    updateTablePorNivel(principal.getBd().getRankingWithoutLimit(-1));
                else
                    updateTablePorNivel(principal.getBd().getRankingWithoutLimit(Integer.valueOf(item)));
            }
        }
    }
    
    private void tratarItemSelecionado(String item) throws Exception{
        switch(item){
            case "Por Pessoa":
                updateComboBox2(item);
                break;
            case "Por Fase":
                updateComboBox2(item);
                break;
            case "Escolha tipo":
                jComboBox2.setEnabled(false);
                break;
            case "Modo História":
                jComboBox2.setEnabled(false);
                jComboBox.setEnabled(false);
                updateTableModoHistoria();
                jTable1.updateUI();
                break;
            case "Normal":
                jComboBox2.setEnabled(false);
                jComboBox.setEnabled(true);
                break;
            
        }
        
    }
    
    
    private void updateComboBox2(String s) throws Exception{
        switch(s){
            case "Por Pessoa":
                updateComboBox2(principal.getBd().getAllNomesFromRanking("nome"));
                break;
            case "Por Fase":
                updateComboBox2(principal.getBd().getAllNomesFromRanking("nivel"));
                break;
            
        }
    }
    
    //update para modo historia
    private void updateTableModoHistoria() throws Exception{
        DefaultTableModel model = new DefaultTableModel(); 
        jTable1.setModel(model);
        
        // Create a couple of columns 
        model.addColumn("Nome"); 
        model.addColumn("Nível"); 

        // Append a row 
        ArrayList<Pessoa> al = principal.getBd().getRankingModoHistoria();
        
        
        for(Pessoa p : al){
            model.addRow(p.getModoHistoria());
        }
    }
    
    private void updateTablePorPessoa(ArrayList<Pessoa> al) throws Exception{
        DefaultTableModel model = new DefaultTableModel(); 
        jTable1.setModel(model);
        
        // Create a couple of columns 
         
        model.addColumn("Nível"); 
        model.addColumn("Pontuação"); 

        // Append a row 
        
        
        for(Pessoa p : al){
            model.addRow(p.getRankingPorPessoa());
        }
    }
    
    private void updateTablePorNivel(ArrayList<Pessoa> al) throws Exception{
        DefaultTableModel model = new DefaultTableModel(); 
        jTable1.setModel(model);
        
        // Create a couple of columns 
         
        model.addColumn("Nome"); 
        model.addColumn("Pontuação"); 

        // Append a row 
        
        
        for(Pessoa p : al){
            model.addRow(p.getRankingPorNivel());
        }
    }
    
    private void updateComboBox2(ArrayList<String> al) throws Exception{
        jComboBox2.setEnabled(true);
        jComboBox2.removeAllItems();
        jComboBox2.addItem("Escolha");
        for(String s : al){
            if(s.equals("-1"))
                jComboBox2.addItem("Partida Rápida");
            else
                jComboBox2.addItem(s);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHistorico = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        voltar = new javax.swing.JButton();
        modo = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jLabelHistorico.setFont(new java.awt.Font("Tahoma", 0, 80)); // NOI18N
        jLabelHistorico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHistorico.setText("Histórico de Partidas");

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

        jComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        voltar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        voltar.setText("Voltar");
        voltar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        voltar.setContentAreaFilled(false);
        voltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        voltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltarActionPerformed(evt);
            }
        });

        modo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelHistorico, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(voltar, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modo, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(modo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(voltar)
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void voltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltarActionPerformed
        principal.remove(this);
        principal.add(principal.getTi());
        principal.revalidate();
        principal.repaint();
    }//GEN-LAST:event_voltarActionPerformed


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabelHistorico;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox modo;
    private javax.swing.JButton voltar;
    // End of variables declaration//GEN-END:variables
}
