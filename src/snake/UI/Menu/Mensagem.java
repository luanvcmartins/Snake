/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Menu;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import snake.Principal;
import static snake.UI.Widget.UIFeedback.getFeedback;

/**
 *
 * @author luan
 */
public class Mensagem extends javax.swing.JLayeredPane {
    Principal principal;
    
    
    
    public Mensagem(Principal principal) {
        initComponents();
        this.principal = principal;
        jPanel1.setBackground(new Color(1f, 1f, 1f, 0.9f));
    }
    
    public void setMessage(String text){
        jLabel1.setText(text);
    }
    
    public void setCurrentOptions(ArrayList<Action> cActions){
        ActionButtons.removeAll();
        ActionButtons.setLayout(new GridLayout());
        ((GridLayout)ActionButtons.getLayout()).setRows(cActions.size());
        
        for (Action cAction : cActions){
            JButton action = new JButton(cAction.text);
            action.addActionListener(cAction.action);
            action.setBorder(new EtchedBorder());
            action.setContentAreaFilled(false);
            action.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            action.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); 
            action.setPreferredSize(new Dimension(100, 60));
            action.setBackground(Color.WHITE);
            action.addMouseListener(getFeedback());
            ActionButtons.add(action);
        }
    }
    
    public void setRanking(int nivel){
        ActionButtons.removeAll();
        ActionButtons.setLayout(new BorderLayout());
        ActionButtons.add(principal.getRanking().getLabelOfRanking(), "North");
        principal.getRanking().updateTable(nivel);
        ActionButtons.add(principal.getRanking().getTableOfRanking(), "Center");
        ActionButtons.add(principal.getRanking().getBtnSair(), "South");
        
    }
    
    public static class Action {
        public String text;
        public ActionListener action;
        
        public Action(){}
        public Action (String text, ActionListener action){
            this.text = text;
            this.action = action;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ActionButtons = new javax.swing.JPanel();

        jLabel1.setBackground(new java.awt.Color(204, 255, 204));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Parabéns!");

        ActionButtons.setMaximumSize(new java.awt.Dimension(45, 19));
        ActionButtons.setMinimumSize(new java.awt.Dimension(45, 19));
        ActionButtons.setOpaque(false);
        ActionButtons.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 238, Short.MAX_VALUE)
                        .addComponent(ActionButtons, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addGap(0, 240, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(211, 211, 211)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ActionButtons, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap(329, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionButtons;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
