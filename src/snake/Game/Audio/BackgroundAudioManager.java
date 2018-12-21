/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.Game.Audio;

import com.sun.javafx.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BackgroundAudioManager {
    private static BackgroundAudioManager me;
    private static JFXPanel panel;
    private static String resouceName;
    public MediaPlayer layerslvls[][];
    
    private MediaPlayer mpOneshot;
    private Random r;
    private int cLayer = 0;
    private int cLvl = 0;
    private int cLayersPlaying = 0;
    
    
    private BackgroundAudioManager(){}
    
    public static void loadAudioFromResources(String folder) throws Exception{
        if (me == null){
            me = new BackgroundAudioManager();
            panel = new javafx.embed.swing.JFXPanel();
            me.r = new Random();
        }
        
        resouceName = folder;
        File effectsFolder = new File(folder);
        if (!effectsFolder.exists()){
            throw new Exception("A pasta de efeitos especificada não existe.");
        }
        
        
        pararTudo();
        
        File layerslvlAvailable[] = effectsFolder.listFiles();
        me.layerslvls = new MediaPlayer[layerslvlAvailable.length][];
        me.cLayersPlaying = 0;
        me.cLayer = 0;
        me.cLvl = 0;
        
        
        int lvlAtual = 0;
        for (File currentLayerLvl : layerslvlAvailable){
            //No lvl atual...
            File layersAvailable[] = currentLayerLvl.listFiles(File::isFile);
            me.layerslvls[lvlAtual] = new MediaPlayer[layersAvailable.length];
            
            //Carregar todos os layers do lvl atual:
            for (int i = 0; i < layersAvailable.length; i++){
                try {
                    String uriString = layersAvailable[i].toURI().toString();
                    me.layerslvls[lvlAtual][i] = new MediaPlayer(new Media(uriString));
                    me.layerslvls[lvlAtual][i].setCycleCount(MediaPlayer.INDEFINITE);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            lvlAtual++;
        }
        
        
        me.reproduzirLayer(0);
    }
    
    public static String getCurrentResource(){
        return resouceName;
    }
    
    public void alterarEstado(int atual){
        if (layerslvls[cLvl][atual] != null){
            if (layerslvls[cLvl][atual].getStatus() == MediaPlayer.Status.PLAYING){
                layerslvls[cLvl][atual].stop();
                cLayersPlaying--;
            }
            else 
                reproduzirLayer(atual);
        }
    }
    
    public void reproduzirLayer(int atual){
        layerslvls[cLvl][atual].seek(layerslvls[cLvl][0].getCurrentTime());
        layerslvls[cLvl][atual].play();
        cLayersPlaying++;
    }
    
    public static void alterarEstadoAleatoriamente(){
        if (me != null && me.layerslvls != null){
            int selecionado;
            do {
                selecionado = me.r.nextInt(me.layerslvls[me.cLvl].length);
            } while (me.cLayersPlaying == 1 &&
                        me.layerslvls[me.cLvl][selecionado].getStatus() == 
                        MediaPlayer.Status.PLAYING);
            me.alterarEstado(selecionado);
        }
    }
    
    public static void incrementarLayer(){
        if (me != null && me.layerslvls != null){
            if (me.cLayer < me.layerslvls[me.cLvl].length){
                if (me.layerslvls[me.cLvl][me.cLayer].getStatus() != MediaPlayer.Status.PLAYING){
                    me.layerslvls[me.cLvl][me.cLayer].seek(me.layerslvls[me.cLvl][0].getCurrentTime());
                    me.layerslvls[me.cLvl][me.cLayer].play();
                }
                me.cLayer++;
            }
        }
    }
    
    public static void incrementarLvl(){
        if (me != null && me.layerslvls != null){
            if (me.cLvl+1 < me.layerslvls.length){
                //Parar os layers do lvl atual:
                for (int i = 0; i < me.layerslvls[me.cLvl].length; i++){
                    if (me.layerslvls[me.cLvl][i].getStatus() == MediaPlayer.Status.PLAYING)
                        me.layerslvls[me.cLvl][i].stop();
                }
                if (me.cLvl+1 < me.layerslvls.length){
                    me.cLvl++;                    
                } else{
                    me.cLvl = 0;
                }
                me.cLayer = 0;
                me.reproduzirLayer(0);
            }
        }
    }
    
    public static void falsoIncrementar(){
        if (me != null && me.layerslvls != null){
            me.falsoIncremento();
        }
    }
    
    public void falsoIncremento(){
        Duration cDur = layerslvls[cLvl][cLayer].getCurrentTime();
        layerslvls[cLvl][cLayer].stop();
        cLayer++;
        if (cLayer >= layerslvls[cLvl].length){
            cLvl++;
            cLayer = 0;
            if (cLvl >= layerslvls.length) {
                cLvl = 0;
            }
        } 
        layerslvls[cLvl][cLayer].seek(cDur);
        layerslvls[cLvl][cLayer].play();
    }
    
    public static void decrementar(){
        
    }
    
    public static void voltarTudo(){
        if (me != null && me.layerslvls !=  null){            
            for (int lvl = 0; lvl < me.layerslvls.length; lvl++){
                for (int layer = 0; layer < me.layerslvls[lvl].length; layer++){
                    MediaPlayer mp =me.layerslvls[lvl][layer];
                    if (mp.getStatus() == MediaPlayer.Status.PAUSED){
                        mp.play();                        
                        me.cLayersPlaying++;
                    }
                }
            }
        }
    }
    
    public static void pausarTudo(){
        if (me != null && me.layerslvls !=  null){            
            for (int lvl = 0; lvl < me.layerslvls.length; lvl++){
                for (int layer = 0; layer < me.layerslvls[lvl].length; layer++){
                    MediaPlayer mp =me.layerslvls[lvl][layer];
                    if (mp.getStatus() == MediaPlayer.Status.PLAYING){
                        mp.pause();
                        me.cLayersPlaying--;
                    }
                }
            }
        }
    }
    
    public static void pararTudo(){
        if (me != null && me.layerslvls !=  null){            
            for (int lvl = 0; lvl < me.layerslvls.length; lvl++){
                for (int layer = 0; layer < me.layerslvls[lvl].length; layer++){
                    MediaPlayer mp =me.layerslvls[lvl][layer];
                    if (mp.getStatus() == MediaPlayer.Status.PLAYING){
                        mp.stop();
                        me.cLayersPlaying--;
                    }
                }
            }
        }
    }
    
    public static void playOneShot(File file){
        pararTudo();
        try {
            if (file != null && me != null){
                String source = file.toURI().toString();
                if (me.mpOneshot == null ||
                        !me.mpOneshot.getMedia().getSource().equals(source))
                    me.mpOneshot = new MediaPlayer(new Media(source));

                me.mpOneshot.setCycleCount(MediaPlayer.INDEFINITE);
                me.mpOneshot.play();
            }
        } catch (Exception ex){
            
        }
    }
    public static void playOneShot(File file, boolean loop){
        try {
            if (file != null && me != null){
                String source = file.toURI().toString();
                if (me.mpOneshot == null ||
                        !me.mpOneshot.getMedia().getSource().equals(source))
                    me.mpOneshot = new MediaPlayer(new Media(source));

                if (loop)
                    me.mpOneshot.setCycleCount(MediaPlayer.INDEFINITE);
                me.mpOneshot.play();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void stopOneShot(){
        if (me != null && me.mpOneshot != null){
            me.mpOneshot.stop();
        }
    }
}
