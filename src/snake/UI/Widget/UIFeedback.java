/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI.Widget;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import snake.Game.Audio.BackgroundAudioManager;

/**
 *
 * @author luan
 */
public class UIFeedback extends MouseAdapter{
    private static UIFeedback me;
    private Clip clickSoundFeedback;
    private Clip enterSoundFeedback;
        
    private UIFeedback(){
        File clickFeedbackFile = new File("effects\\ui\\ui_action.wav");
        
        if (clickFeedbackFile.exists()){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clickFeedbackFile);
                clickSoundFeedback = AudioSystem.getClip();
                clickSoundFeedback.open(audioInputStream);
            } catch (Exception ex){}
        }
        
        File enterFedbackFile = new File("effects\\ui\\ui_enter.wav");
        if (enterFedbackFile.exists()){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(enterFedbackFile);
                enterSoundFeedback = AudioSystem.getClip();
                enterSoundFeedback.open(audioInputStream);
            } catch (Exception ex){}
        }
            
        
    }
    
    public static UIFeedback getFeedback(){
        if (me == null) me = new UIFeedback();
        return me;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e); 
        e.getComponent().setBackground(Color.MAGENTA);
        playEnterFeedbackSound();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e); 
        e.getComponent().setBackground(Color.WHITE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e); 
        e.getComponent().setBackground(Color.WHITE);
        playClickFeedbackSound();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
        playClickFeedbackSound();
    }

    
    
    
    
    private void playClickFeedbackSound(){
        if (clickSoundFeedback != null){
            clickSoundFeedback.setFramePosition(0);
            clickSoundFeedback.start();
        }
    }
    
    private void playEnterFeedbackSound(){
        if (enterSoundFeedback != null){
            enterSoundFeedback.setFramePosition(0);
            enterSoundFeedback.start();
        }
    }
}
