package com.example.backend;

public class TempMessages {

    String message;
    Integer vida;
    Integer acessos;

    public TempMessages(String message, Integer vida, Integer acessos) {
        this.message = message;
        this.vida = vida;
        this.acessos = acessos;
    }

    @Override
    public String toString() {
        return "TempMessages{" +
                "message='" + message + '\'' +
                ", vida=" + vida +
                ", acessos=" + acessos +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public Integer getVida() {
        return vida;
    }

    public Integer getAcessos() {
        return acessos;
    }

    public void decrementarAcessos(){
        acessos--;
    }
}
