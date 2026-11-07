package org.example;

import java.io.Serializable;

public class Receta implements Serializable {
    int id; /*4*/
    String nombre;/*27*/
    int tiempoPreparacion; /*4*/

    public Receta(int id, int tiempoPreparacion, String nombre) {
        this.id = id;
        this.tiempoPreparacion = tiempoPreparacion;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "Receta:\n" +
                "id: " + id +"\n"+
                "nombre: " + nombre + '\n' +
                "tiempoPreparacion: " + tiempoPreparacion +
                " minutos";
    }
}
