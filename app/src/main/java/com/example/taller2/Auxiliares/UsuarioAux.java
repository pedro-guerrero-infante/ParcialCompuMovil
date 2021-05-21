package com.example.taller2.Auxiliares;

public class UsuarioAux {
    String usuario, nombre,email;
    String celular;
    String clave;
    String direccion;

    public UsuarioAux() {

    }

    public UsuarioAux(String usuario, String nombre, String email, String celular, String clave, String direccion) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.clave = clave;
        this.direccion = direccion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
