/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import snake.Principal;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author luan
 */
public class MapBuilder extends javax.swing.JPanel {
    private final Principal principal;
    private int tamanhoGridAtual;
    private JPanel[][] matriz;
    private char map[][];
    
    private boolean editingExistentLvl = false;
    private String currentLvl = null;
    private int currentArquivo;
    
    private Color corFundo = Color.WHITE;
    private Color corCobra = Color.BLACK;
    private Color corParede = Color.MAGENTA;
    private Color corComida = Color.BLUE;
    
    public MapBuilder(Principal principal) {
        initComponents();
        this.principal = principal;
        
        btnNovoLevel.addMouseListener(snake.UI.Widget.UIFeedback.getFeedback());
        btnSalvar.addMouseListener(snake.UI.Widget.UIFeedback.getFeedback());
        btnEditarLvl.addMouseListener(snake.UI.Widget.UIFeedback.getFeedback());
        btnVoltar.addMouseListener(snake.UI.Widget.UIFeedback.getFeedback());
    }

    private void iniciarNovoMapa(){
        mapa.setEnabled(true);
        atributos.setEnabled(true);
        btnSalvar.setEnabled(true);
        btnNovoLevel.setEnabled(false);
        btnEditarLvl.setEnabled(false);
        
        txtTamanhoSnake.setEnabled(true);
        txtSleepTimer.setEnabled(true);
        txtRushingSpeed.setEnabled(true);
        txtComidasSimultaneas.setEnabled(true);
        
        btnCorFundo.setEnabled(true);
        btnCorParede.setEnabled(true);
        btnCorSnake.setEnabled(true);
        btnCorComida.setEnabled(true);
        
        //Iniciar GRID:
        iniciarGrid();
    }
    
    private void finalizarEdicaoMapa(){
        mapa.setEnabled(false);
        atributos.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnNovoLevel.setEnabled(true);
        btnEditarLvl.setEnabled(true);
        
        txtTamanhoSnake.setEnabled(false);
        txtSleepTimer.setEnabled(false);
        txtRushingSpeed.setEnabled(false);
        txtComidasSimultaneas.setEnabled(false);
        
        btnCorFundo.setEnabled(false);
        btnCorParede.setEnabled(false);
        btnCorSnake.setEnabled(false);
        btnCorComida.setEnabled(false);
        
        mapa.removeAll();
    }
    
    private void iniciarGrid(){
        matriz = new JPanel[tamanhoGridAtual][tamanhoGridAtual];
        
        //Iniciar layout
        ((GridLayout)mapa.getLayout()).setColumns(tamanhoGridAtual);
        ((GridLayout)mapa.getLayout()).setRows(tamanhoGridAtual);
        
        if (map == null){
            //Se map é nulo, quer dizer que é uma novo mapa.
            for(int i = 0; i < tamanhoGridAtual; i++){
                for(int j = 0; j < tamanhoGridAtual; j++){
                    matriz[i][j] = new JPanel();
                    matriz[i][j].setBackground(corFundo);                
                    matriz[i][j].addMouseListener(mouseAdapter);

                    mapa.add(matriz[i][j]);
                }
            }
        } else {
            //Se map não é nulo, devemos carregar um modelo:
            for(int i = 0; i < tamanhoGridAtual; i++){
                for(int j = 0; j < tamanhoGridAtual; j++){
                    matriz[i][j] = new JPanel();
                    if (map[i][j] == ':') matriz[i][j].setBackground(corParede);        
                    else matriz[i][j].setBackground(corFundo);    
                    matriz[i][j].addMouseListener(mouseAdapter);

                    mapa.add(matriz[i][j]);
                }
            }
        }
    }
    
    MouseAdapter mouseAdapter = new MouseAdapter() {
        boolean isMouseDown = false;
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e); 
            isMouseDown = true;
            
            Component source = e.getComponent();

            if (source.getBackground() == corFundo){
                source.setBackground(corParede);
            } else {
                source.setBackground(corFundo);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            isMouseDown = false;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (isMouseDown){
                Component source = e.getComponent();

                if (source.getBackground() == corFundo){
                    source.setBackground(corParede);
                } else {
                    source.setBackground(corFundo);
                }
            }
        } 
    };
    
    private void salvarLevel(){
        File dir = new File("lvl");
        if (!dir.exists()) dir.mkdir();
        int nomeArquivo = dir.list().length;
        
        String lvlName = "";
        if (editingExistentLvl){
            int i = JOptionPane.showConfirmDialog(principal, "Você quer salvar como uma cópia?\n"
                    + "Caso não queira, o mapa será atualizado (salvo por cima).", "", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION){
                do {
                    lvlName = JOptionPane.showInputDialog(principal,"Insira um novo nome para esse mapa.\n", "");
                } while (lvlName == null);
            } else if (i == JOptionPane.NO_OPTION) {
                lvlName = currentLvl;
                nomeArquivo = currentArquivo;
            } else {
                return;
            }
        } else {
            do {
                lvlName = JOptionPane.showInputDialog(principal,"Insira um nome para esse mapa.\n", "");
            } while (lvlName == null);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("DATA").append("\r\n");
        sb.append("name:").append(lvlName).append("\r\n");
        sb.append("img_preview:").append(getImgPreview()).append("\r\n");
        sb.append("sleep_timer:").append(txtSleepTimer.getText()).append("\r\n");
        sb.append("rushing_speed:").append(txtRushingSpeed.getText()).append("\r\n");
        sb.append("size_required:").append(txtTamanhoSnake.getText()).append("\r\n");
        sb.append("food_amount:").append(txtComidasSimultaneas.getText()).append("\r\n"); 
        sb.append("map_size:").append(matriz.length).append("\r\n");              
        sb.append("map_color:").append(corFundo.getRed()).append(";").append(corFundo.getGreen()).append(";").append(corFundo.getBlue()).append("\r\n");
        sb.append("snake_color:").append(corCobra.getRed()).append(";").append(corCobra.getGreen()).append(";").append(corCobra.getBlue()).append("\r\n");
        sb.append("wall_color:").append(corParede.getRed()).append(";").append(corParede.getGreen()).append(";").append(corParede.getBlue()).append("\r\n");
        sb.append("food_color:").append(corComida.getRed()).append(";").append(corComida.getGreen()).append(";").append(corComida.getBlue()).append("\r\n");
        sb.append("\r\n\r\n");
        sb.append("MAP\r\n");
        for(int i = 0; i < tamanhoGridAtual; i++){
            for(int j = 0; j < tamanhoGridAtual; j++){
                Color elementoAtual = matriz[i][j].getBackground();
                if (elementoAtual == corFundo){
                    sb.append(".");
                }
                else if (elementoAtual == corParede){
                    sb.append(":");
                }
            }
            sb.append("\r\n");
        }
        
        try {
            
            FileWriter fw = new FileWriter("lvl\\" + nomeArquivo + ".lvl");
            fw.write(sb.toString());
            fw.close();
            finalizarEdicaoMapa();
            
            JOptionPane.showMessageDialog(principal, "Mapa gravado com sucesso!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(principal, "Oops!\nOcorreu um erro ao gravar esse mapa:\n\n"+ex.getMessage());
        }
    }
    
    private String getImgPreview() {
        
        BufferedImage image = new BufferedImage(mapa.getWidth(), mapa.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        Graphics g = image.getGraphics();

        mapa.paintComponents(g);
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            return DatatypeConverter.printBase64Binary(output.toByteArray());
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }
    
    private void loadMap(File lvlFile){
        /* Dado um arquivo de lvl, essa função é responsável por carrega-lo. */
        try {            
            boolean expectingMap = false;
            BufferedReader br = new BufferedReader(new FileReader(lvlFile));
            String line;
            int linhaAtual = 0;
            while ((line = br.readLine()) != null){
                if (!expectingMap){
                    String[] dataStruct = line.split(":");
                    currentArquivo = Integer.valueOf(lvlFile.getName().split("\\.")[0]);
                    //Contem dados de variaveis
                    if (dataStruct.length == 2){
                        switch (dataStruct[0]){
                            case "name":
                                currentLvl = dataStruct[1];
                                break;
                            case "sleep_timer":
                                txtSleepTimer.setText(dataStruct[1]);
                                break;
                            case "size_required":
                                txtTamanhoSnake.setText(dataStruct[1]);
                                break;
                            case "map_size":
                                tamanhoGridAtual = Integer.parseInt(dataStruct[1]);
                                map = new char[tamanhoGridAtual][tamanhoGridAtual];
                                break;
                            case "rushing_speed":
                                txtRushingSpeed.setText(dataStruct[1]);
                                break;
                            case "food_amount":
                                txtComidasSimultaneas.setText(dataStruct[1]);
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
                                    corCobra = new Color(red, g, b);
                                else if (dataStruct[0].equals("wall_color"))
                                    corParede = new Color(red, g, b);
                                else if (dataStruct[0].equals("food_color"))
                                    corComida = new Color(red, g, b);
                                
                                btnCorComida.setBackground(corComida);
                                btnCorFundo.setBackground(corFundo);
                                btnCorParede.setBackground(corParede);
                                btnCorSnake.setBackground(corCobra);
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
        } catch (Exception ex){
            JOptionPane.showMessageDialog(principal, "Oops, ocorreu um erro ao ler esse arquivo: \n" + ex.getMessage());
        }
    }
    
    
    private void updateMapEditor(Color colorToChange, Color newColor){
        //Chamado ao alterar a cor de um dos elementos:
        for(int i = 0; i < tamanhoGridAtual; i++){
            for(int j = 0; j < tamanhoGridAtual; j++){
                if (matriz[i][j].getBackground() == colorToChange){
                    matriz[i][j].setBackground(newColor);
                }
            }
        }
        mapa.repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        atributos = new javax.swing.JPanel();
        txtTamanhoSnake = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSleepTimer = new javax.swing.JTextField();
        btnCorFundo = new javax.swing.JButton();
        btnCorParede = new javax.swing.JButton();
        btnCorSnake = new javax.swing.JButton();
        btnCorComida = new javax.swing.JButton();
        txtRushingSpeed = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtComidasSimultaneas = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        mapa = new javax.swing.JPanel();
        btnNovoLevel = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        btnEditarLvl = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(781, 640));
        setMinimumSize(new java.awt.Dimension(781, 640));
        setPreferredSize(new java.awt.Dimension(781, 640));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(820, 455));

        atributos.setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos"));
        atributos.setEnabled(false);

        txtTamanhoSnake.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTamanhoSnake.setText("3");
        txtTamanhoSnake.setToolTipText("<html>\nTamanho maximo da snake necessário para avançar<br>\naté a próxima fase.");
        txtTamanhoSnake.setEnabled(false);

        jLabel3.setText("Tamanho Snake:");

        jLabel4.setText("Velocidade:");

        txtSleepTimer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSleepTimer.setText("200");
        txtSleepTimer.setToolTipText("<html>\n<b>Velocidade da snake</b><br>\nQuanto menor o número, mais rápido ela será.");
        txtSleepTimer.setEnabled(false);

        btnCorFundo.setText("Cor fundo");
        btnCorFundo.setEnabled(false);
        btnCorFundo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorFundoActionPerformed(evt);
            }
        });

        btnCorParede.setText("Cor Parede");
        btnCorParede.setEnabled(false);
        btnCorParede.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorParedeActionPerformed(evt);
            }
        });

        btnCorSnake.setText("Cor Snake");
        btnCorSnake.setEnabled(false);
        btnCorSnake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorSnakeActionPerformed(evt);
            }
        });

        btnCorComida.setText("Cor Comida");
        btnCorComida.setEnabled(false);
        btnCorComida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorComidaActionPerformed(evt);
            }
        });

        txtRushingSpeed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRushingSpeed.setText("50");
        txtRushingSpeed.setToolTipText("<html>\n<b>Velocidade da snake</b><br>\nQuanto menor o número, mais rápido ela será.");
        txtRushingSpeed.setEnabled(false);

        jLabel5.setText("Corrida:");

        txtComidasSimultaneas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtComidasSimultaneas.setText("1");
        txtComidasSimultaneas.setToolTipText("<html>\n<b>Velocidade da snake</b><br>\nQuanto menor o número, mais rápido ela será.");
        txtComidasSimultaneas.setEnabled(false);

        jLabel6.setText("Comidas simutaneas");

        javax.swing.GroupLayout atributosLayout = new javax.swing.GroupLayout(atributos);
        atributos.setLayout(atributosLayout);
        atributosLayout.setHorizontalGroup(
            atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atributosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtTamanhoSnake, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSleepTimer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComidasSimultaneas, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRushingSpeed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCorFundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCorParede, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCorSnake, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCorComida, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                .addContainerGap())
        );
        atributosLayout.setVerticalGroup(
            atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atributosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(atributosLayout.createSequentialGroup()
                        .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(atributosLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTamanhoSnake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(atributosLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSleepTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(atributosLayout.createSequentialGroup()
                        .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(atributosLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRushingSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(atributosLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtComidasSimultaneas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnCorParede)
                                .addComponent(btnCorComida)))
                        .addContainerGap())))
            .addGroup(atributosLayout.createSequentialGroup()
                .addGroup(atributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCorFundo)
                    .addComponent(btnCorSnake))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(atributos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(atributos, javax.swing.GroupLayout.PREFERRED_SIZE, 82, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        mapa.setBorder(javax.swing.BorderFactory.createTitledBorder("Mapa"));
        mapa.setEnabled(false);
        mapa.setMaximumSize(new java.awt.Dimension(644, 395));
        mapa.setMinimumSize(new java.awt.Dimension(585, 395));
        mapa.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mapa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapa, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        btnNovoLevel.setText("Construir novo level");
        btnNovoLevel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNovoLevel.setContentAreaFilled(false);
        btnNovoLevel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoLevelActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar");
        btnSalvar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalvar.setEnabled(false);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnVoltar.setBackground(new java.awt.Color(255, 255, 255));
        btnVoltar.setText("VOLTAR");
        btnVoltar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVoltar.setContentAreaFilled(false);
        btnVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnEditarLvl.setText("Editar level existente");
        btnEditarLvl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnEditarLvl.setContentAreaFilled(false);
        btnEditarLvl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarLvl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarLvlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNovoLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarLvl, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNovoLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarLvl, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovoLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoLevelActionPerformed
        String entrada = JOptionPane.showInputDialog(principal,"Insira o tamanho do mapa a ser criado.\nEsse valor não pode ser alterado após configurado.\nO mapa sempre será quadrado.", "25");
        
        if (entrada != null){
            try { 
                editingExistentLvl = false;
                map = null;
                tamanhoGridAtual = Integer.parseInt(entrada);
                iniciarNovoMapa();
                this.revalidate();
                this.repaint();
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(principal, "Por favor insira um número válido.");
            }
        }        
    }//GEN-LAST:event_btnNovoLevelActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (JOptionPane.showConfirmDialog(principal, "Confirma que deseja salvar as alterações desse level?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            if (Integer.parseInt(txtSleepTimer.getText()) - Integer.parseInt(txtRushingSpeed.getText()) <= 10){
                JOptionPane.showConfirmDialog(principal, "A velocidade de corrida é muito alta pra velocidade de da cobra.\n"
                        + "Tente um valor mais baixo para a corrida, ou um valor mais alto pra valocidade da cobra.");
                return;
            }
            
            salvarLevel();
        }
            
    }//GEN-LAST:event_btnSalvarActionPerformed
    
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        if (btnSalvar.isEnabled()){
            if (JOptionPane.showConfirmDialog(principal,"Você tem certeza que deseja abandonar o progresso nesse mapa?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                finalizarEdicaoMapa();
            }
        } else {
            principal.restartGameApp(false);
        }
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnCorFundoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorFundoActionPerformed
        Color selectedColor = JColorChooser.showDialog(principal, "Cor do mapa", corFundo);
        if (selectedColor != null){
            updateMapEditor(corFundo, selectedColor);
            corFundo = selectedColor;    
            btnCorFundo.setBackground(corFundo);
        }
    }//GEN-LAST:event_btnCorFundoActionPerformed

    private void btnCorParedeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorParedeActionPerformed
        Color selectedColor = JColorChooser.showDialog(principal, "Cor das paredes", corParede);
        if (selectedColor != null){
            updateMapEditor(corParede, selectedColor);
            corParede = selectedColor;   
            btnCorParede.setBackground(corParede);
        }
    }//GEN-LAST:event_btnCorParedeActionPerformed

    private void btnCorSnakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorSnakeActionPerformed
        Color selectedColor = JColorChooser.showDialog(principal, "Cor da snake", corCobra);
        if (selectedColor != null){
            updateMapEditor(corCobra, selectedColor);
            corCobra = selectedColor;  
            btnCorSnake.setBackground(corCobra);
        }
    }//GEN-LAST:event_btnCorSnakeActionPerformed

    private void btnCorComidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorComidaActionPerformed
        Color selectedColor = JColorChooser.showDialog(principal, "Cor da comida", corComida);
        if (selectedColor != null){
            updateMapEditor(corComida, selectedColor);
            corComida = selectedColor;     
            btnCorComida.setBackground(corComida);
        }
    }//GEN-LAST:event_btnCorComidaActionPerformed

    private void btnEditarLvlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarLvlActionPerformed
        JFileChooser fc = new JFileChooser("lvl");
        if (fc.showOpenDialog(principal) == JFileChooser.APPROVE_OPTION){
            editingExistentLvl = true;
            loadMap(fc.getSelectedFile());
            iniciarNovoMapa();
            this.revalidate();
            this.repaint();
        }
    }//GEN-LAST:event_btnEditarLvlActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel atributos;
    private javax.swing.JButton btnCorComida;
    private javax.swing.JButton btnCorFundo;
    private javax.swing.JButton btnCorParede;
    private javax.swing.JButton btnCorSnake;
    private javax.swing.JButton btnEditarLvl;
    private javax.swing.JButton btnNovoLevel;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel mapa;
    private javax.swing.JTextField txtComidasSimultaneas;
    private javax.swing.JTextField txtRushingSpeed;
    private javax.swing.JTextField txtSleepTimer;
    private javax.swing.JTextField txtTamanhoSnake;
    // End of variables declaration//GEN-END:variables
}
