package com.example.taller2.Auxiliares;

public class UsuarioAux
{
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String cedula;
    private String clave;
    private String latitud;
    private String longitud;
    private String foto;

    public UsuarioAux() {

    }

    public UsuarioAux(String usuarioP, String nombreP, String apellidoP, String emailP, String cedulaP, String claveP, String latitudP, String longitudP, String fotoP) {
        this.usuario = usuarioP;
        this.nombre = nombreP;
        this.apellido = apellidoP;
        this.email = emailP;
        this.cedula = cedulaP;
        this.clave = claveP;
        this.latitud = latitudP;
        this.longitud = longitudP;
        this.foto = fotoP;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
