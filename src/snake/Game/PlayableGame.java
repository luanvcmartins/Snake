/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import snake.Game.Audio.BackgroundAudioManager;
import snake.Principal;
import snake.pessoa.Pessoa;

/**
 *
 * @author luan
 */
public class PlayableGame extends javax.swing.JPanel {
    private int tamanho = 25;
    private int food_amount = 1;
    private final Principal principal;
    
    private JPanel[][] matriz;
    private char[][] map;
    
    private enum Elemento { Snake, Parede, Comida }
    private enum Direcao { acima, abaixo, esquerda, direita } 
    private class Posicao { public int x, y; public Posicao(int x, int y) { this.x = x; this.y = y; }}
    
    private Direcao direcaoAtual = Direcao.direita;
    private ArrayList<Posicao> snake;
    private Random r = new Random();
    private Posicao snakeHead, snakeTail;
    private Thread cMainGameLoop;
    
    private boolean isGameOver = false, isPaused = false;
    private int defaultSleepTimer = 150;
    private int sleepTimer = 150;
    private int rushingSpeed = 50;
    private boolean isRushing = false;
    
    private boolean incComidaEffect = true;
    private int comidaEffectCount = 0;
    private int tamanhoRequeridoSnake = 1000;
    private boolean historyMode = false;
        
    private Color corFundo = Color.BLACK;
    private Color corComida = Color.YELLOW;
    private Color corSnake = Color.ORANGE;
    private Color corParede = Color.MAGENTA;
    
    private Clip feedSoundEffect[];
    private Clip walkSoundEffect;
    private Clip gamePausedMusic;
    

    public int getSnakeSize() {
        return snake.size();
    }

    
    
    public PlayableGame(Principal principal) {
        initComponents();
        
        this.principal = principal;
        
        //Instanciar elementos do jogo?
        instanciate();
        
        //Iniciar lógica de jogo:
        game_start();
    }
    
    public PlayableGame(Principal principal, String fileUrl) {
        initComponents();
        
        this.principal = principal;
        
        //Carregar lvl especificado:
        loadGameLvl(fileUrl);
        
        
        //Instanciar elementos do jogo:
        instanciate(); 
        
        //Iniciar loop principal:
        game_start();
    }

    public boolean isHistoryMode() {
        return historyMode;
    }
    
    private void loadGameLvl(String lvlFile){
        /* Dado um arquivo de lvl, essa função é responsável por carrega-lo. */
        try {            
            boolean expectingMap = false;
            BufferedReader br = new BufferedReader(new FileReader(lvlFile));
            String line;
            int linhaAtual = 0;
            File f = new File(lvlFile);
            if(f.exists()){principal.setFaseAtual(Integer.valueOf(f.getName().split("\\.")[0]));}
            else{
                JOptionPane.showMessageDialog(null, "Arquivo inexistente!");
                System.exit(0);
            }
            while ((line = br.readLine()) != null){
                if (!expectingMap){
                    String[] dataStruct = line.split(":");

                    //Contem dados de variaveis
                    if (dataStruct.length == 2){
                        switch (dataStruct[0]){
                            case "sleep_timer":
                                sleepTimer = Integer.parseInt(dataStruct[1]);
                                defaultSleepTimer = sleepTimer;
                                break;
                            case "size_required":
                                tamanhoRequeridoSnake = Integer.parseInt(dataStruct[1]);
                                break;
                            case "map_size":
                                tamanho = Integer.parseInt(dataStruct[1]);
                                map = new char[tamanho][tamanho];
                                break;
                            case "rushing_speed":
                                rushingSpeed = Integer.parseInt(dataStruct[1]);
                                break;
                            case "food_amount":
                                food_amount = Integer.parseInt(dataStruct[1]);
                                break;
                            case "name":
                                
                                break;
                            case "map_color":
                            case "snake_color":
                            case "wall_color":
                            case "food_color":
                                String[] corFundoStruct = dataStruct[1].split(";");
                                int red = Integer.parseInt(corFundoStruct[0]),
                                        g = Integer.parseInt(corFundoStruct[1]),
                                        b = Integer.parseInt(corFundoStruct[2]);
                                if (dataStruct[0].equals("map_color"))
                                    corFundo = new Color(red, g, b);
                                else if (dataStruct[0].equals("snake_color"))
                                    corSnake = new Color(red, g, b);
                                else if (dataStruct[0].equals("wall_color"))
                                    corParede = new Color(red, g, b);
                                else if (dataStruct[0].equals("food_color"))
                                    corComida = new Color(red, g, b);
                                break;
                        }
                    } else {
                        if (line.equals("MAP")){
                            expectingMap = true;
                        }
                    }
                } else {
                    //Partindo desse momento, somente o mapa será lido:
                    char[] elementos = line.toCharArray();
                    for (int i = 0; i < elementos.length; i++){
                        map[linhaAtual][i] = elementos[i];
                    }
                    linhaAtual++;
                }
            }
            
            //desenharQuadro();
        
        } catch (Exception ex){
            JOptionPane.showMessageDialog(principal, "Erro ao carregar lvl:\n" + ex.getMessage());
        }
        
    }
    
    public void setIsHistoryMode(boolean historyMode){
        this.historyMode = historyMode;
    }
    
    public void restartGame(){        
        desenharQuadro();
        
        //Resetar valores aos padrões:
        direcaoAtual = Direcao.direita;
        
        sleepTimer = defaultSleepTimer;
        
        incComidaEffect = true;
        comidaEffectCount = 0;
        
        isGameOver = false;
        isPaused = false;
        isRushing = false;
                
        if (gamePausedMusic != null)
            gamePausedMusic.stop();
        
        //Lógica de inicio de novo jogo:
        game_start();
    }
    
    public void configPadrao(){
        tamanho = 25;
        
        map = null;
        
        defaultSleepTimer = 150;
        rushingSpeed = 50;

        tamanhoRequeridoSnake = 1000;
        corFundo = Color.BLACK;
        corComida = Color.YELLOW;
        corSnake = Color.ORANGE;
        corParede = Color.MAGENTA;
    }
    
    public void restartGame(String lvlFile){     
        /*Função responsável por reiniciar o PlayableGame com arquivo
        de fase.*/
        
        //Nós carregamos a fase atual:        
        loadGameLvl(lvlFile);
        
        this.removeAll();
        instanciate_matriz();
        
        //Voltamos ao fluxo comum de reinicio de jogo.
        restartGame();
    }
    
    public void restartGameRapido(){   
        /* Esa função é responsável por iniciar o modo rápido novamente. */
        configPadrao();
        this.removeAll();
        instanciate_matriz();
        restartGame();
    }
    
    public void cancelarJogo(){
        //Essa função é chamada para terminar uma partida
        //manualmente (sair do jogo)
        if (!isGameOver){
            isGameOver = true;
        }
    }
    
    public void cancelarPauseMusic(){
        if (gamePausedMusic != null){
            gamePausedMusic.stop();
        }
    }
    
    public void desenharQuadro(){
        //Função responsável por iniciar o quadro (pintar quadro)
        if (map != null){
            //Caso o map seja não seja nulo, nós temos um padrão pra seguir:            
            for(int i = 0; i < tamanho; i++){
                for(int j = 0; j < tamanho; j++){
                    if (map[i][j] == '.'){
                        matriz[i][j].setBackground(corFundo);
                    } else if (map[i][j] == ':'){
                        matriz[i][j].setBackground(corParede);
                    }
                }
            }
        } else {
            //Com o mapa nulo, quer dizer que não temos padrão a seguir:
            for(int i = 0; i < tamanho; i++){
                for(int j = 0; j < tamanho; j++){
                    matriz[i][j].setBackground(corFundo);
                }
            }
        }
    }
    
    private void progressHistoryMode(){
        cancelarJogo();
        
        Pessoa pessoa = principal.getPessoa();
        pessoa.setNivel(principal.getFaseAtual());
        pessoa.atualizaData();
        try{
            principal.getBd().insereNoModoHistoria(pessoa);
        }catch(Exception e){e.printStackTrace();}
        principal.showGameLvlWinScreen("A próxima partida será mais dificil!<br>");
    }
    
    private void instanciate(){
        instanciate_matriz();
        
        //Instanciar efeitos sonoros:
        instanciate_soundEffects();
        
        //Registrar eventos do teclado:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboard_events);
    }
    
    private void instanciate_soundEffects(){
        File effectsDir = new File("effects");
        if (!effectsDir.exists()) {
            JOptionPane.showMessageDialog(principal, "Efeitos sonoros não foram registrados e por isso não estão disponíveis.");
            return;
        } 
        
        File feedEffectDir = new File("effects\\feed");
        if (!feedEffectDir.exists()) {
            JOptionPane.showMessageDialog(principal, "Efeitos sonoros de comida não foram registrados e por isso não estão disponíveis.");
            return;
        }
        
        File feedEffects[] = feedEffectDir.listFiles(File::isFile);
        feedSoundEffect = new Clip[feedEffects.length];
        int i=0;
        for (File cFeedEffect : feedEffects){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(cFeedEffect);
                feedSoundEffect[i] = AudioSystem.getClip();
                feedSoundEffect[i].open(audioInputStream);
                i++;
            } catch (Exception ex){ ex.printStackTrace();}
        }
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("effects\\walk\\tick.wav"));
            walkSoundEffect = AudioSystem.getClip();
            walkSoundEffect.open(audioInputStream);
        } catch (Exception ex){ ex.printStackTrace();}
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("effects\\ui\\credits.wav"));
            gamePausedMusic = AudioSystem.getClip();
            gamePausedMusic.open(audioInputStream);
        } catch (Exception ex){ ex.printStackTrace();}
        
    }
    
    private void instanciate_matriz(){
        //Essa função é responsável por instanciar a matriz.
        matriz = new JPanel[tamanho][tamanho];
        
        this.setPreferredSize(new Dimension(1000,1000));
        
        //Iniciar layout
        this.setLayout(new GridLayout(tamanho, tamanho));
        
        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                matriz[i][j] = new JPanel();
                matriz[i][j].setBackground(corFundo);
                this.add(matriz[i][j]);
            }
        }
        
        desenharQuadro();
    }
    
    private void game_start(){
        try {
            if (new Random().nextBoolean())
                BackgroundAudioManager.loadAudioFromResources("effects\\pussyface");
            else
                BackgroundAudioManager.loadAudioFromResources("effects\\pb1");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        spawnSnake();
        
        for (int i = 0; i < food_amount; i++)
            spawnComida();
        
        this.revalidate();
        this.repaint();
        
        addGameToPrincipalPanel();
        
        cMainGameLoop = new Thread(mainGameLooper);
        cMainGameLoop.start();
        
        
    }
        
    private void addGameToPrincipalPanel(){
        principal.remove(principal.getTi());
        principal.add(this);
        principal.revalidate();
        principal.repaint();       
        
    }
    
    
    
    private Runnable mainGameLooper = () -> {
        
        try { 
            BackgroundAudioManager.pausarTudo();
            BackgroundAudioManager.playOneShot(new File("effects\\ui\\ui_start.mp3"), false);
            Thread.sleep(5*1000); 
            BackgroundAudioManager.voltarTudo();
        } catch (Exception ex){}
        
        while (!isGameOver){            
            //Simular nova posição:
            Posicao snakeHeading = moverSnake();
            
            //Verificar se o movimento é legal:
            if (ehMovimentoIlegal(snakeHeading)){
                //O movimento atual é ilegal. Game Over.
                    finalizarJogo(snakeHead, "Você saiu da área de jogo!");
                
                return;
            }
            
            //Verificar colisões:
            Elemento colisaoAtual = checkCollission(snakeHeading);
            
            //Lidar com as consequencias dessa colisão:
            if (colisaoAtual != null){
                //Houve uma colisão:
                if (lidarComColisao(snakeHeading, colisaoAtual)){
                    //Ao retornar true, quer dizer que o jogo acabou.
                    return;
                }
            } else {
                //Sem colisão, o movimento segue normal
                //(remove a cauda, adiciona a cabeça):
                snakeHead = snakeHeading;
                snake.add(snakeHeading);
                snake.remove(0);
            }
            
            //Com as novas posições atualizadas, desenha a snake:
            desenharCobra();
            
            //Por fim, atualiza a posição da cauda:
            snakeTail = snake.get(0);
            
            playWalkSoundEffect();
            
            try { Thread.sleep((isRushing ? sleepTimer - rushingSpeed : sleepTimer)); }
            catch (Exception ex){}
        }
    };
    
    private void playWalkSoundEffect(){
        if (walkSoundEffect != null){
            walkSoundEffect.setFramePosition(0);
            walkSoundEffect.start();
        }
    }
    
    //FUNÇÕES DE CONTROLE:
    private void spawnSnake(){
        if (matriz != null){
            snake = new ArrayList<>();
            Posicao p;
            if(matriz[0][0].getBackground() != corFundo || matriz[0][1].getBackground() != corFundo){
                p = lookForSpaceToSpawnSnake();
            }else{
                p = new Posicao(0,0);
            }
            matriz[p.x][p.y].setBackground(corSnake);
            matriz[p.x][p.y+1].setBackground(corSnake);
            snake.add(p);            
            snake.add(new Posicao(p.x,p.y+1));
            snakeTail = snake.get(0);
            snakeHead = snake.get(1);
        }
    }
    
    
    private Posicao lookForSpaceToSpawnSnake(){
        Posicao p = new Posicao(r.nextInt(tamanho), r.nextInt(tamanho-2));
        
        do{
            while(matriz[p.x][p.y].getBackground() != corFundo){p = new Posicao(r.nextInt(tamanho), r.nextInt(tamanho-2));}
            
        }while(matriz[p.x][p.y+1].getBackground() != corFundo && matriz[p.x][p.y+2].getBackground() != corFundo);
        
        return p;
    }
    
    private void spawnComida(){
        if (matriz != null){
            int x, y;
            
            //Busca valores aleatórios para spawn de comida.
            do {
                x = r.nextInt(tamanho);
                y = r.nextInt(tamanho);        
            }
            while (matriz[x][y].getBackground() != corFundo);
            //Faz o processo até encontrar uma posição váldia
            
            //Com a posição encontrada, atribui azul a ela.
            matriz[x][y].setBackground(corComida);
        }
    }
    
    private Elemento checkCollission(Posicao snakeHeading){
        Color elementoAtual = matriz[snakeHeading.x][snakeHeading.y].getBackground();
        if (elementoAtual == corFundo)
            return null;
        else if (elementoAtual == corSnake)
            return Elemento.Snake;
        else if (elementoAtual == corComida)
            return Elemento.Comida;
        else if (elementoAtual == corParede)
            return Elemento.Parede;
        return null;
    }
    
    private boolean lidarComColisao(Posicao snakeHeading, Elemento colisaoAtual){
        //Dada uma colisão, essa função define as consequencias dela
        //Ao retornar true, o jogo será terminado.
        if (colisaoAtual == Elemento.Comida){
            //A cobra colidiu com comida:
            snakeHead = snakeHeading;
            snake.add(snakeHeading);
            spawnComida();
            alterarVelocidadeSnake();
            
            playFeedEffect();
            
            if (snake.size() % 20 == 0)
                BackgroundAudioManager.incrementarLvl();
            else if (snake.size() % 7 == 0)
                BackgroundAudioManager.alterarEstadoAleatoriamente();
            
            if (historyMode && snake.size() >= tamanhoRequeridoSnake)
                progressHistoryMode();
            
            
        } else if (colisaoAtual == Elemento.Parede){
            //A snake colidiu com uma parede:
            finalizarJogo(snakeHeading, "Você colidiu com uma parede.");
            return true;
        } else if (colisaoAtual == Elemento.Snake){
            //A snake colidiu si mesma:
            finalizarJogo(snakeHeading, "Você colidiu com si mesmo.");
            return true;
        }
        return false;
    }
    
    private void playFeedEffect(){
        //Ao comer um elemento, um som de feedback é reproduzido.
        if (feedSoundEffect != null){
            if (feedSoundEffect[comidaEffectCount] != null){
                feedSoundEffect[comidaEffectCount].setFramePosition(0);
                feedSoundEffect[comidaEffectCount].start();
            }
            
            if (comidaEffectCount == feedSoundEffect.length-1)
                incComidaEffect = false;
            else if (comidaEffectCount == 0) 
                incComidaEffect = true;
            
            comidaEffectCount = comidaEffectCount + (incComidaEffect ? 1 : -1);
        }
    }
    
    private void alterarVelocidadeSnake(){
        //Aumenta a velocidade da snake após comer.
        if(sleepTimer > 150)
            sleepTimer -= 3;
        else if(sleepTimer > 100){
            sleepTimer -= 1;
        }
    }
    
    private void desenharCobra(){
        //Desenha a cabeça da cobra:
        matriz[snakeHead.x][snakeHead.y].setBackground(corSnake);
        
        if (snakeTail != null && snakeTail != snake.get(0)){
            //Com movimento normal, apaga o último item:
            matriz[snakeTail.x][snakeTail.y].setBackground(corFundo);
        }
    }
    
    private boolean ehMovimentoIlegal(Posicao posicao){
        //Se o proximo movimento é além das bordas, ele é ilegal.
        return (posicao.x < 0 || posicao.x >= tamanho || posicao.y < 0 || posicao.y >= tamanho);
    }
    
    private Posicao moverSnake(){
        switch (direcaoAtual){
            case acima:
                return new Posicao(snakeHead.x-1, snakeHead.y);
            case abaixo:
                return new Posicao(snakeHead.x+1, snakeHead.y);
            case esquerda:
                return new Posicao(snakeHead.x, snakeHead.y-1);
            case direita:
                return new Posicao(snakeHead.x, snakeHead.y+1);
        }
        return null;
    }
    
    public void alternarExecucaoDoJogo(){
        //Pausa e remove o pause do jogo.
        if (!isPaused){
            principal.showGamePauseMenu("Tamanho snake: " + snake.size() + "<br>Tamanho objetivo: " + tamanhoRequeridoSnake);
            if (cMainGameLoop != null){ cMainGameLoop.suspend(); }
            isPaused = true;
            BackgroundAudioManager.pausarTudo();
            playGamePausedMusic();
        } else {
            principal.hideGamePauseMenu();
            if (cMainGameLoop != null) { cMainGameLoop.resume(); }
            isPaused = false;
            BackgroundAudioManager.voltarTudo();
            playGamePausedMusic();
        }
    }
    
    private void playGamePausedMusic(){
        if (gamePausedMusic != null){
            if (!gamePausedMusic.isRunning()){
                gamePausedMusic.setFramePosition(0);
                gamePausedMusic.loop(10000);
            } else {
                gamePausedMusic.stop();
            }
        }
    }
    
    private void finalizarJogo(Posicao snakeHeading, String motivo) {
        //Essa função é chamada para notificar o fim da partida:
        isGameOver = true;
        
        principal.showGameOverMessage(motivo);
        matriz[snakeHeading.x][snakeHeading.y].setBackground(Color.red);
    }
    
    //EVENTOS DO TECLADO:
    KeyEventDispatcher keyboard_events = (KeyEvent e) -> {
        if (isGameOver) return false;
        
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                    if (direcaoAtual == Direcao.acima) 
                        isRushing = true;
                    else if (direcaoAtual != Direcao.abaixo)
                        direcaoAtual = Direcao.acima;
                }
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
                    if (direcaoAtual == Direcao.abaixo) 
                        isRushing = true;
                    else if (direcaoAtual != Direcao.acima)
                        direcaoAtual = Direcao.abaixo;
                }
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
                    if (direcaoAtual == Direcao.esquerda) 
                        isRushing = true;
                    else if (direcaoAtual != Direcao.direita)
                        direcaoAtual = Direcao.esquerda;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
                    if (direcaoAtual == Direcao.direita) 
                        isRushing = true;
                    else if (direcaoAtual != Direcao.esquerda)
                        direcaoAtual = Direcao.direita;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isGameOver){
                    alternarExecucaoDoJogo();
                }
                if (isRushing){ isRushing = false; }
            break;
        }
        
        return true;
    };

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}