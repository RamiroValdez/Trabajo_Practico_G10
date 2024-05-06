package com.tallerwebi.presentacion;

public class DatosMovimiento {

    private String descripcion;
    private String tipo;
    private String categoria;
    private Double monto;

    public DatosMovimiento() {
    }

    public DatosMovimiento(String descripcion, String categoria, String tipo, Double monto) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.monto = monto;
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
