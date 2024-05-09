package model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Task {
    @SerializedName("codTrabajo") // Corregido aquí
    private String codTrabajo; // Corregido aquí
    private String categoria; // Corregido aquí
    private String descripcion; // Corregido aquí
    private String fechaInicio; // Corregido aquí
    private String fechaFin; // Corregido aquí
    private double tiempo; // Corregido aquí
    private int prioridad; // Corregido aquí
    private Trabajador trabajador;


    public Task(String category, String description, String start, String end, double time, int priority) {
        this.categoria = category;
        this.descripcion = description;
        this.fechaInicio = start;
        this.fechaFin = end;
        this.tiempo = time;
        this.prioridad = priority;
        this.trabajador = null;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getCodTrabajo() {
        return codTrabajo;
    }

    public void setCodTrabajo(String codTrabajo) {
        this.codTrabajo = codTrabajo;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    @Override
    public String toString() {
        return "Task{" +
                "cod_task='" + codTrabajo + '\'' +
                ", category='" + categoria + '\'' +
                ", description='" + descripcion + '\'' +
                ", start=" + fechaInicio +
                ", end=" + fechaFin +
                ", time='" + tiempo + '\'' +
                ", priority=" + prioridad +
                '}';
    }
}
