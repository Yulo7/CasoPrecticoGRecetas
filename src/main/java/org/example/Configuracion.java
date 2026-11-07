package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

