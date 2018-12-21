/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Menu;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import snake.Principal;
import snake.UI.Widget.UIFeedback;
import snake.pessoa.Pessoa;

/**
 *
 * @author Julio Carnevali
 */
public class Ranking {
    private final Principal principal;
    
    private JButton btnSair;
    private JLabel labelOfRanking;
    private JTable tableOfRanking;
    
    public Ranking(Principal principal) {
        
        this.principal = principal;
        
        
        labelOfRanking = new JLabel("Ranking");
        btnSair = new JButton("Voltar");
        btnSair.setBorder(new EtchedBorder());
        btnSair.setContentAreaFilled(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.setFont(new java.awt.Font("Segoe UI Emoji", 0, 16));
        btnSair.setBackground(Color.WHITE);
        btnSair.addMouseListener(UIFeedback.getFeedback());
        btnSair.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized(principal.locker){
                    principal.locker.notify();
                }
            }
        });
    }

    public JButton getBtnSair() {
        return btnSair;
    }

    public JLabel getLabelOfRanking() {
        return labelOfRanking;
    }

    public JScrollPane getTableOfRanking() {
        return new JScrollPane(tableOfRanking);
    }
    
    
    public void updateTable(int lvl) {
        DefaultTableModel model = new DefaultTableModel(); 
        tableOfRanking = new JTable(model);
        
        model.addColumn("Nome"); 
        model.addColumn("Pontuação"); 

        try {
            ArrayList<Pessoa> al = principal.getBd().getRanking(lvl);

            for(Pessoa p : al){
                model.addRow(p.getAll());
            }
        } catch (Exception ex){
        }
    }
    
   
}
