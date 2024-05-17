package model;

public class Task{
    private int codTrabajo;
    private String categoria;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private double tiempo;
    private int prioridad;
    private Trabajador trabajador;


    public Task(String category, String description, String start, int priority, Trabajador trabajador) {
        this.categoria = category;
        this.descripcion = description;
        this.fechaInicio = start;
        this.prioridad = priority;
        this.trabajador = trabajador;
    }

    public Task(int codTrabajo, String category, String description, String start, int priority, Trabajador trabajador) {
        this.codTrabajo = codTrabajo;
        this.categoria = category;
        this.descripcion = description;
        this.fechaInicio = start;
        this.prioridad = priority;
        this.trabajador = trabajador;
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

    public int getCodTrabajo() {
        return codTrabajo;
    }

    public void setCodTrabajo(int codTrabajo) {
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
                ", category='" + categoria + '\'' +
                ", cod='" + codTrabajo + '\'' +
                ", description='" + descripcion + '\'' +
                ", start=" + fechaInicio +
                ", priority=" + prioridad +
                ", employee=" + trabajador;
    }
}
