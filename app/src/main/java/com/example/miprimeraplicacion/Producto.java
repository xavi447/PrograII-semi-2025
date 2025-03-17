package com.example.miprimeraplicacion;
public class Producto {
    String idProducto;
    String codigo;
    String descripcion;
    String marca;
    String presentacion;
    double precio;
    String foto;
    String nombre;
    public Producto(String idProducto, String codigo, String nombre, String descripcion, String marca, String presentacion, double precio, String foto) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre; // Asegurar que se asigne
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.foto = foto;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre; // Nuevo método
    }

    public void setNombre(String nombre) {
        this.nombre = nombre; // Nuevo método
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
