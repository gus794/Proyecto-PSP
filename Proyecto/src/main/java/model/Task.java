package model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Task {
    @SerializedName("codTrabajo")
    private String codTrabajo;
    String categoria;
    String descripcion;
    Date fechaInicio;
    Date fechaFin;
    int tiempo;
    int prioridad;
    Employee trabajador;


    public Task(String category, String description, Date start, Date end, int time, int priority) {
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
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
