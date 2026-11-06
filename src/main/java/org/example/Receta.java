package org.example;

import java.io.Serializable;
import java.util.Objects;

public class Receta implements Serializable {

    private int id; /*4*/
    private String nombre;/*30*/
    private int tiempoPreparacion; /*4*/

    public Receta(int id, int tiempoPreparacion, String nombre) {
        this.id = id;
        this.setTiempoPreparacion(tiempoPreparacion);
        this.setNombre(nombre);
    }

    public Receta(int id, String nombre, int tiempoPreparacion) {
        this(id, tiempoPreparacion, nombre);
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
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        if (tiempoPreparacion < 0) {
            throw new IllegalArgumentException("El tiempo de preparación no puede ser negativo");
        }
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "Receta:\n" +
                "id: " + id + "\n" +
                "nombre: " + nombre + '\n' +
                "tiempoPreparacion: " + tiempoPreparacion +
                " minutos";
    }
}
