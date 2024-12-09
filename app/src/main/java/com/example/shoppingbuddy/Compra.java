package com.example.shoppingbuddy;

import java.util.List;

public class Compra {
    private int id;
    private String nombre;
    private String fecha;

    public Compra(int id, String nombre, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }
}