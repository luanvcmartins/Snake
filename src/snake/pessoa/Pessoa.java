/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.pessoa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Julio Carnevali
 */
public class Pessoa {
    private String nome;
    private int pontos;
    private int nivel;
    private String data;
    
    public Pessoa(String nome, int pontos, int nivel){
        this.nome = nome;
        this.pontos = pontos;
        this.nivel = nivel;
    }
    
    public Pessoa(String nome, int nivel, String data){
        this.nome = nome;
        this.data = data;
        this.nivel = nivel;
    }

    public Pessoa(String nome, int nivel){
        this.nome = nome;
        this.data = getDataAtual();
        this.nivel = nivel;
    }
    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    
    
    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public int getNivel() {
        return nivel;
    }
    
    public Object[] getAll(){
        return new Object[]{nome,pontos};
    }
    
    public Object[] getModoHistoria(){
        return new Object[]{nome,nivel};
    }
    
    public Object[] getRankingPorPessoa(){
        if(nivel == -1){
            return new Object[]{"Partida Rápida",pontos};
        }
        return new Object[]{nivel,pontos};
    }
    
    public Object[] getRankingPorNivel(){
        return new Object[]{nome,pontos};
    }
    
    public String getDataAtual(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void atualizaData(){
        this.data = getDataAtual();
    }
}
