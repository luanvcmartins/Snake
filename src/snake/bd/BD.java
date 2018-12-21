/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import snake.pessoa.Pessoa;

/**
 *
 * @author Julio Carnevali
 */
public class BD {
    Connection c;
    
    public BD(){
        try{
            createTables();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void Conectar() throws Exception{
        Class.forName("org.sqlite.JDBC");
        if (c == null || c.isClosed()){ 
            c = DriverManager.getConnection("jdbc:sqlite:banco.db");
        }        
    }
        
    private void desconectar() throws Exception{
        if (!c.isClosed()){
            c.close();
        }
    }
    
    private void deletaRegistros(String tabela) throws Exception{
        Conectar(); 
        Statement e = c.createStatement();
        e.execute("DELETE FROM "+tabela+";");
        desconectar();
    }
    
    
    public void dropTable(String tabela) throws Exception{
        
        deletaRegistros(tabela);
        Conectar();
        Statement stm = c.createStatement();
        stm.execute("DROP TABLE IF EXISTS "+tabela+";");
        desconectar();
    }
    
   
    public void createTables() throws Exception{
        
        Conectar(); 
               
        Statement stm = c.createStatement();
        stm.execute("CREATE TABLE IF NOT EXISTS ranking (nome TEXT, pontos INTEGER, nivel INTEGER);");
        stm.execute("CREATE TABLE IF NOT EXISTS modo_historia (nome TEXT, nivel INTEGER, data TEXT);");
        
        desconectar();
    }
    
    public boolean insereNoRanking(Pessoa p){
        ArrayList <String> pessoas = getNomesRanking(p.getNivel());
        try {
            if(pessoas.contains(p.getNome())){
                removeNoRanking(p.getNome(), p.getNivel());
            }
            Conectar();

            PreparedStatement stm = c.prepareStatement("INSERT INTO ranking (nome, pontos, nivel) VALUES (?, ?, ?); ");
            stm.setString(1, p.getNome());
            stm.setInt(2, p.getPontos());
            stm.setInt(3, p.getNivel());

            int i = stm.executeUpdate();

            desconectar();

            return (i>0);
        } catch (Exception ex){
            return false;
        }
    }
    
    public void removeNoRanking(String nome, int nivel) throws Exception{
        Conectar();
        
        Statement stm = c.createStatement();
        stm.execute("DELETE FROM ranking WHERE nome='"+nome+"' AND nivel="+nivel+";");
        
        desconectar();
        
    }
    
    public boolean insereNoModoHistoria(Pessoa p) throws Exception{
        
        
        ArrayList<String> al = getNomesModoHistoria();
        Conectar();
        if(al.contains(p.getNome())){
            Statement stm = c.createStatement();
            stm.execute("DELETE FROM modo_historia WHERE nome='"+p.getNome()+"';");
        }
        
        
        PreparedStatement stm = c.prepareStatement("INSERT INTO modo_historia (nome, nivel, data) VALUES (?, ?, ?); ");
        stm.setString(1, p.getNome());
        stm.setInt(2, p.getNivel());
        stm.setString(3, p.getData());
        
        int i = stm.executeUpdate();
        
        desconectar();
        
        return (i>0);
    }
    
    
    public ArrayList<Pessoa> getRanking(int nivel){
        ArrayList<Pessoa> ranking = new ArrayList<>();
        try {
            Conectar();

            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM ranking WHERE nivel='"+nivel+"' ORDER BY pontos DESC LIMIT 5;");

            while (rs.next()){
                Pessoa p = new Pessoa(rs.getString("nome"), rs.getInt("pontos"), rs.getInt("nivel"));


                ranking.add(p);            
            }

            desconectar();
        } catch (Exception ex){
            
        }
        return ranking;
    }
    public ArrayList<Pessoa> getRankingWithoutLimit(int nivel) throws Exception{
        ArrayList<Pessoa> ranking = new ArrayList<>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM ranking WHERE nivel='"+nivel+"' ORDER BY pontos DESC;");
        
        while (rs.next()){
            Pessoa p = new Pessoa(rs.getString("nome"), rs.getInt("pontos"), rs.getInt("nivel"));
            
            
            ranking.add(p);            
        }
        
        desconectar();
        
        return ranking;
    }
    
    public ArrayList<Pessoa> getRankingWithoutLimit(String nome) throws Exception{
        ArrayList<Pessoa> ranking = new ArrayList<Pessoa>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM ranking WHERE nome='"+nome+"' ORDER BY pontos DESC;");
        
        while (rs.next()){
            Pessoa p = new Pessoa(rs.getString("nome"), rs.getInt("pontos"), rs.getInt("nivel"));
            
            
            ranking.add(p);            
        }
        
        desconectar();
        
        return ranking;
    }
    
    public int getRankingIndividual(String nome, int nivel) {
        int retorno = 0;
        try {
            Conectar();

            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT pontos FROM ranking WHERE nivel='"+nivel+"' AND nome='"+nome+"';");

            while (rs.next()){
                retorno = (rs.getInt("pontos"));

            }

            desconectar();
        } catch (Exception ex){}
        return retorno;
    }
    
    public ArrayList<Pessoa> getModoHistoria() {
        ArrayList<Pessoa> pessoas = new ArrayList<>();
        try {
            Conectar();

            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM modo_historia;");

            while (rs.next()){
                Pessoa p = new Pessoa(rs.getString("nome"), rs.getInt("nivel"), rs.getString("data"));


                pessoas.add(p);            
            }

            desconectar();
        } catch (Exception ex){}
        return pessoas;
    }
    
    public ArrayList<Pessoa> getRankingModoHistoria() throws Exception{
        ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM modo_historia ORDER BY nivel DESC;");
        
        while (rs.next()){
            Pessoa p = new Pessoa(rs.getString("nome"), rs.getInt("nivel"), rs.getString("data"));
            
            
            pessoas.add(p);            
        }
        
        desconectar();
        
        return pessoas;
    }
    
    
    
    public ArrayList<String> getNomesRanking(int nivel) {
        ArrayList<String> nomes = new ArrayList<String>();
        try {
            Conectar();

            Statement stm = c.createStatement();
            ResultSet rs = null;
            try{
                rs = stm.executeQuery("SELECT * FROM ranking WHERE nivel='"+nivel+"';");
            }catch(Exception e){
                return new ArrayList<String>();
            }
            while (rs.next()){
                nomes.add(rs.getString("nome"));            
            }

            desconectar();
        } catch (Exception ex){}
        return nomes;
    }
    
    public ArrayList<String> getAllNomesFromRanking(String coluna) throws Exception{
          
        ArrayList<String> nomes = new ArrayList<String>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = null;
        try{
            rs = stm.executeQuery("SELECT DISTINCT "+coluna+" FROM ranking;");
        }catch(Exception e){
            return new ArrayList<String>();
        }
        while (rs.next()){
            nomes.add(rs.getString(coluna));            
        }
        
        desconectar();
        
        return nomes;
    }
    
    public ArrayList<Integer> getNiveisFromRanking() throws Exception{
          
        ArrayList<Integer> niveis = new ArrayList<Integer>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = null;
        try{
            rs = stm.executeQuery("SELECT DISTINCT nivel FROM ranking;");
        }catch(Exception e){
            return new ArrayList<Integer>();
        }
        while (rs.next()){
            niveis.add(rs.getInt("nivel"));            
        }
        
        desconectar();
        
        return niveis;
    }
    
    public ArrayList<String> getNomesModoHistoria() throws Exception{
        ArrayList<String> nomes = new ArrayList<String>();
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = null;
        try{
            rs = stm.executeQuery("SELECT * FROM modo_historia;");
        }catch(Exception e){
            return new ArrayList<String>();
        }
        while (rs.next()){
            nomes.add(rs.getString("nome"));            
        }
        
        desconectar();
        
        return nomes;
    }
    
    public void imprimiTudoRanking() throws Exception{
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = null;
        try{
            rs = stm.executeQuery("SELECT * FROM ranking;");
        }catch(Exception e){
            return;
        }
        while (rs.next()){
            System.out.println(rs.getString("nome"));            
            System.out.println(rs.getInt("pontos"));            
            System.out.println(rs.getInt("nivel"));            
        }
        
        desconectar();
        
    }
    
    public void imprimiTudoModoHistoria() throws Exception{
        Conectar();
        
        Statement stm = c.createStatement();
        ResultSet rs = null;
        try{
            rs = stm.executeQuery("SELECT * FROM modo_historia;");
        }catch(Exception e){
            return;
        }
        while (rs.next()){
            System.out.println(rs.getString("nome"));            
            System.out.println(rs.getInt("nivel"));  
            System.out.println(rs.getString("data"));  
            
        }
        
        desconectar();
        
    }
    
    public static void main(String[] args) throws Exception{
        BD bd = new BD();
//        bd.imprimiTudo("ranking");
        bd.deletaRegistros("ranking");
        
        
//        bd.dropTable("modo_historia");
//        bd.insereNoModoHistoria(new Pessoa("julio", 15));
//        bd.imprimiTudoModoHistoria();
        
    }
}
