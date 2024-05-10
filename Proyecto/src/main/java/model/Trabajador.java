package model;

import com.google.gson.annotations.SerializedName;

public class Trabajador {
    @SerializedName("idTrabajador") // Corregido aquí
    private int idTrabajador;
    private String dni;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String contraseña;
    private String email;

    public Trabajador(int idTrabajador, String dni, String nombre, String apellidos, String especialidad, String contraseña, String email) {
        this.idTrabajador = idTrabajador;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.contraseña = contraseña;
        this.email = email;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "idTrabajador=" + idTrabajador +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

