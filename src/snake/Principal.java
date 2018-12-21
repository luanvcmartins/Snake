/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import snake.Game.Audio.BackgroundAudioManager;
import snake.Game.MapBuilder;
import snake.UI.Menu.Mensagem;
import snake.UI.Menu.Mensagem.Action;
import snake.UI.Menu.NovoJogo;
import snake.Game.PlayableGame;
import snake.UI.Menu.Creditos;
import snake.UI.Menu.HistoricoPartidas;
import snake.UI.Menu.Ranking;
import snake.UI.Menu.SelecionarFase;
import snake.UI.Menu.TelaInicial;
import snake.bd.BD;
import snake.pessoa.Pessoa;

/**
 *
 * @author luan
 */
public class Principal extends javax.swing.JFrame {

    
    public Object locker = new Object();
    private Mensagem msg;
    private TelaInicial ti;
    private PlayableGame game;
    private SelecionarFase selection;
    private MapBuilder mapBuilder;
    private HistoricoPartidas historicoPartidas;
    private Creditos creditos;
    private Ranking ranking;
    private BD bd;
    
    private Pessoa pessoa;
    private int faseAtual;
    private File lvlFiles[]; 
    
    
    public Principal() throws Exception {
        initComponents();
        instanciar();
        iniciar();
        
        bd = new BD();
        
        try { BackgroundAudioManager.loadAudioFromResources("effects\\ti"); }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public PlayableGame getGame() {
        return game;
    }

    public TelaInicial getTi() {
        return ti;
    }

    
    
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(int faseAtual) {
        this.faseAtual = faseAtual;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public BD getBd() {
        return bd;
    }
    
    //FUNÇÕES DE INICIALIZAÇÃO
    private void instanciar(){        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        
        ti = new TelaInicial(this);
        msg = new Mensagem(this);
    }
    
    private void iniciar(){
        this.add(ti);
        this.setGlassPane(msg);
        this.getContentPane().setBackground(Color.WHITE);
        this.revalidate(); 
        this.repaint();
    }
    
    //FUNÇÕES DE GERENCIAMENTO DE ESTADOS   
    
    
    
    public void startModoHistória() throws Exception{
        NovoJogo nj = new NovoJogo(this);
        this.setGlassPane(nj);
        this.getGlassPane().setVisible(true);
        this.revalidate();
        this.repaint();
    }
    
    public void startMapBuilder(){
        if (mapBuilder == null) mapBuilder = new MapBuilder(this);
        this.remove(ti);
        this.add(mapBuilder);
        
        this.revalidate();
        this.repaint();
    }
    
    public void startHistoricoPartida(){
        if (historicoPartidas == null) historicoPartidas = new HistoricoPartidas(this);
        this.remove(ti);
        this.add(historicoPartidas);
        
        this.revalidate();
        this.repaint();
    }
    
    public void startLvlSelection(){
        if (selection == null){ selection = new SelecionarFase(this); }
        selection.loadAvailableLvls();
        this.setGlassPane(selection);
        this.getGlassPane().setVisible(true);
        
        this.repaint();
    }
    
    public void startCreditosScreen(){
        if (creditos == null){ creditos = new Creditos(this); }
        this.setGlassPane(creditos);
        this.getGlassPane().setVisible(true);
        
        BackgroundAudioManager.pausarTudo();
        creditos.startCreditsScreen();
        this.repaint();
    }
    
    
    //EXBIR MENSAGENS
    public void showGameOverMessage(String mensagem) {
       
        setMessageAsGlass();
        msg.setMessage("<html><div style='text-align: center;'>GAME OVER!<br><small>" + mensagem);
        this.getGlassPane().setVisible(true);
        
        
        BackgroundAudioManager.playOneShot(new File("effects\\ui\\sad_feedback.mp3"));
        
        ArrayList<Action> actions = new ArrayList<>();
                
        actions.add(new Action("Reiniciar Partida", (ActionEvent e) -> {
            //Opção de reiniciar a partida
            BackgroundAudioManager.stopOneShot();
            game.restartGame();
            getGlassPane().setVisible(false);
        }));
        actions.add(new Action("Voltar ao Menu", (ActionEvent e) -> {
            //Opção de voltar ao menu
            BackgroundAudioManager.stopOneShot();
            game.cancelarJogo();
            this.restartGameApp();
            getGlassPane().setVisible(false);
        }));
        
        actions.add(new Action("Sair do Jogo", (ActionEvent e) -> {
            //Opção de sair do jogo
            System.exit(0);
        }));
        
        if(!game.isHistoryMode()){
            //pegando o nome do cidadao
            String nome = JOptionPane.showInputDialog("Informe seu nome");
            
            if(nome != null){
                //se nao apertou em cancelar
                ArrayList<String> al = bd.getNomesRanking(getFaseAtual());
                    
                //enquanto este nome existir ou for igual a nada
                while(al.contains(nome) || nome.equals("")){
                    if(al.contains(nome)){
                        if(JOptionPane.showConfirmDialog(null, "Este nome já existe, quer inserir um novo?"
                                , "Nome Existente", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            nome = JOptionPane.showInputDialog("Insira um novo nome:");
                        }else{
                            //cidadao quer inserir um nome ja existente, ou seja, trocar a pontuacao antiga
                            
                            int pontos = bd.getRankingIndividual(nome, faseAtual);
                            //trocará a pontuação somente se for maior que a anterior
                            if(game.getSnakeSize() > pontos){
                                break;
                            }else{
                                nome = null;                                
                                break;
                            }
                        }
                        
                    }
                    if(nome.equals(""))
                        nome = JOptionPane.showInputDialog("Nome inválido.");
                    
                    if(nome == null){
                        break;
                    }
                }
                
                if(nome != null){
                    //insere no Banco de Dados
                    Pessoa p = new Pessoa(nome, game.getSnakeSize(), getFaseAtual());

                    bd.insereNoRanking(p);

                }
                
            }
            
            //mostra ranking
            if (ranking == null){ ranking = new Ranking(this); }
            msg.setRanking(getFaseAtual());
            msg.revalidate();
            msg.repaint();

            //espera acabar de ver o ranking
            synchronized(locker){
                try {
                    locker.wait();
                } catch (InterruptedException ex) {
                    System.out.println("Erro no Locker");
                }
            }
            
        }
        
        //mostra os botoes
        msg.setCurrentOptions(actions);
        msg.revalidate();
        msg.repaint();
    }
    
    public void showGamePauseMenu(String mensagem){
        setMessageAsGlass();
        msg.setMessage("<html><div style='text-align: center;'>Jogo pausado.<br><small>"+mensagem);
        this.getGlassPane().setVisible(true);
        
        ArrayList<Action> actions = new ArrayList<>();
        
        actions.add(new Action("Continuar Partida", (ActionEvent e) -> {
            //Opção de voltar ao jogo
            game.alternarExecucaoDoJogo();
            getGlassPane().setVisible(false);
        }));
        
        actions.add(new Action("Reiniciar Partida", (ActionEvent e) -> {
            //Opção de reiniciar a partida
            game.restartGame();
            getGlassPane().setVisible(false);
        }));
        actions.add(new Action("Voltar ao Menu", (ActionEvent e) -> {
            //Opção de voltar ao menu
            game.cancelarJogo();
            this.restartGameApp();
            getGlassPane().setVisible(false);
        }));
        
        actions.add(new Action("Sair do Jogo", (ActionEvent e) -> {
            //Opção de voltar ao menu
            System.exit(0);
        }));
        
        msg.setCurrentOptions(actions);
        msg.revalidate();
        msg.repaint();
    }
    
    public void showGameLvlWinScreen(String mensagem){
        setMessageAsGlass();
        
        
        BackgroundAudioManager.playOneShot(new File("effects\\ui\\win_feedback.mp3"));
        if (getFaseAtual()+1 == lvlFiles.length){
            msg.setMessage("<html><div style='text-align: center;'>Você TERMINOU esse jogo!<br><small>"+mensagem);
        } else {
            msg.setMessage("<html><div style='text-align: center;'>Você passou!<br><small>"+mensagem);
        }
        this.getGlassPane().setVisible(true);
        
        ArrayList<Action> actions = new ArrayList<>();
        
        if (getFaseAtual()+1 != lvlFiles.length){
            actions.add(new Action("Continuar para a Próxima Partida", (ActionEvent e) -> {
                //Opção de voltar ao jogo
                BackgroundAudioManager.stopOneShot();
                this.progredirGameModoHistoria();
                getGlassPane().setVisible(false);
            }));
        }
        
        actions.add(new Action("Reiniciar essa Partida", (ActionEvent e) -> {
            //Opção de reiniciar a partida
            BackgroundAudioManager.stopOneShot();
            game.restartGame();
            getGlassPane().setVisible(false);
        }));
        actions.add(new Action("Voltar ao Menu", (ActionEvent e) -> {
            //Opção de voltar ao menu
            BackgroundAudioManager.stopOneShot();
            game.cancelarJogo();
            this.restartGameApp();
            getGlassPane().setVisible(false);
        }));
        
        actions.add(new Action("Sair do Jogo", (ActionEvent e) -> {
            //Opção de voltar ao menu
            System.exit(0);
        }));
        
        msg.setCurrentOptions(actions);
        msg.revalidate();
        msg.repaint();
    }
    
    public void setMessageAsGlass(){
        //Quando é necessário exibir uma mensagem,
        //certificar-se sobre o glass panel atual.
        if (!(getGlassPane() instanceof Mensagem)){
            setGlassPane(msg);
        }
    }
    
    public void hideGamePauseMenu(){
        this.getGlassPane().setVisible(false);
    }
    
    public void restartGameApp(){
        setMessageAsGlass();
        this.getGlassPane().setVisible(false);
        if (game != null) {
            this.remove(game);
            game.cancelarPauseMusic();
        }
        if (mapBuilder != null) this.remove(mapBuilder);
        iniciar();   
        
        try { BackgroundAudioManager.loadAudioFromResources("effects\\ti"); }
        catch (Exception ex){}
    }
    public void restartGameApp(boolean restartMusicRequired){
        setMessageAsGlass();
        this.getGlassPane().setVisible(false);
        if (game != null) {
            this.remove(game);
            game.cancelarPauseMusic();
        }
        if (mapBuilder != null) this.remove(mapBuilder);
        iniciar();   
        
        if (restartMusicRequired)
            try { BackgroundAudioManager.loadAudioFromResources("effects\\ti"); }
            catch (Exception ex){}
    }
        
    //INICIAR PARTIDAS:
    public void startGameRapido(){
        if (game == null) { game = new PlayableGame(this); }
        else { game.restartGameRapido(); }
        setFaseAtual(-1);
        
        
        
    }
    
    public void startGame(){
        if (game == null) { game = new PlayableGame(this); }
        else { game.restartGame(); }
        
    }
    
    
    
    public void startGame(String lvlFile){
        if (game == null) { game = new PlayableGame(this, lvlFile); }
        else { game.restartGame(lvlFile); }
        
        game.setIsHistoryMode(false);
        
        
    }
    
    public void startGameModoHistória(Pessoa p){
        //Chamado pelo botão "Nova Partida" 
        //para iniciar a lógica de jogo (com lvls agora):
        setFaseAtual(p.getNivel()+1);
        pessoa = p;
        
        getGlassPane().setVisible(false);
        
        //Carrega os níveis:
        File lvls = new File("lvl");
        if (lvls.exists()){
            if (lvlFiles == null)
                lvlFiles = lvls.listFiles(File::isFile);
            
            if(lvlFiles.length <= getFaseAtual()){
                getGlassPane().setVisible(true);
                JOptionPane.showMessageDialog(this, "Não existem mais níveis!\n"
                    + "Você pode criar mais no construtor de níveis!");
                getGlassPane().setVisible(false);
                return;
            }
            
            startGame(lvlFiles[getFaseAtual()].getAbsolutePath());
            game.setIsHistoryMode(true);
        } else {
            JOptionPane.showMessageDialog(this, "Os níveis não estão disponíveis!\n"
                    + "Você pode criar mais no construtor de níveis!");
        }
    }
    
    public void progredirGameModoHistoria(){
        
        setFaseAtual(getFaseAtual()+1);
        startGame(lvlFiles[getFaseAtual()].getAbsolutePath());
        game.setIsHistoryMode(true);
    }
    
    public void startGameLvlSelection(String lvlFile){
        //Chamado pelo lvlSelection para iniciar uma 
        //partida com o lvl selecionado.
        this.hideGamePauseMenu();
        startGame(lvlFile);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 255, 51));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(781, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Principal p = null;
                try {
                    p = new Principal();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                p.dispose();
                p.setUndecorated(true);
                p.setVisible(true);
            }
        });
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
