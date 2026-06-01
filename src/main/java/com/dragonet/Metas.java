package com.dragonet;

public class Metas {
    private String Titulo;
    private String Descricao;
    private String dataAdicao;
    private boolean status;

    public Metas (String Titulo, String Descricao, String dataAdicao, boolean status){
        this.Titulo = Titulo;
        this.Descricao = Descricao;
        this.dataAdicao = dataAdicao;
        this.status = status;
    }
    
    //geters e setters

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        this.Titulo = titulo;
    }

    public String getDescricao(){
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public void setDataAdicao(String dataAdicao) {
        this.dataAdicao = dataAdicao;
    }

    public String getDataAdicao() {
        return dataAdicao;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public boolean getStatus() {
        return status;
    }
}
