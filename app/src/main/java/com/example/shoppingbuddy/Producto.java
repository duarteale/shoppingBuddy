package com.example.shoppingbuddy;
public class Producto {
    private int id;
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private int iconoCambioPrecio;

    public Producto(int id, String nombre, int cantidad, double precioUnitario, int iconoCambioPrecio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.iconoCambioPrecio = iconoCambioPrecio;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public int getIconoCambioPrecio() {
        return iconoCambioPrecio;
    }

    public void setIconoCambioPrecio(int iconoCambioPrecio) {
        this.iconoCambioPrecio = iconoCambioPrecio;
    }
}