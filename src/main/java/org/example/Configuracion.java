package org.example;

public class Configuracion {
    String usado;
    int recetas;
    public Configuracion() {

    }

    public String getUsado() {
        return usado;
    }

    public void setUsado(String usado) {
        this.usado = usado;
    }

    public int getRecetas() {
        return recetas;
    }

    public void setRecetas(int recetas) {
        this.recetas = recetas;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "usado='" + usado + '\'' +
                ", recetas=" + recetas +
                '}';
    }
}

