package SalonCitas.model;

import java.sql.Date;
import java.sql.Time;

public class Cita {

    private int     IdCita;
    private int     IdCliente;
    private int     IdEmpleado;
    private Date    fecha;
    private Time    hora;
    private int     IdEstado;
    private String  observaciones;

    //Campos extra para mostrar en la tabla (JOIN)
    private String  nombreCliente;
    private String  nombreEmpleado;
    private String  nombreEstado;

    public Cita() {    }

    public Cita(int idCita, int idCliente, int idEmpleado, Date fecha, Time hora, int idEstado, String observaciones) {
        IdCita = idCita;
        IdCliente = idCliente;
        IdEmpleado = idEmpleado;
        this.fecha = fecha;
        this.hora = hora;
        IdEstado = idEstado;
        this.observaciones = observaciones;
    }

    public int getIdCita() {
        return IdCita;
    }

    public void setIdCita(int idCita) {
        IdCita = idCita;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int idEstado) {
        IdEstado = idEstado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreCliente()                   { return nombreCliente; }
    public void   setNombreCliente(String v)           { this.nombreCliente  = v; }

    public String getNombreEmpleado()                  { return nombreEmpleado; }
    public void   setNombreEmpleado(String v)          { this.nombreEmpleado = v; }

    public String getNombreEstado()                    { return nombreEstado; }
    public void   setNombreEstado(String v)            { this.nombreEstado   = v; }
}