package SalonCitas.model;

import javax.swing.plaf.PanelUI;

public class Persona {

    private int IdPersona;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String Direccion;
    private String Dui;

    public Persona() { }

    public Persona(int idPersona, String nombre, String apellido, String telefono, String direccion,
    String dui) {
        IdPersona = idPersona;
        Nombre = nombre;
        Apellido = apellido;
        Telefono = telefono;
        Direccion = direccion;
        this.Dui = dui;
    }

    public  int getIdPersona() { return IdPersona; }

    public void setIdPersona(int idPersona) { idPersona = idPersona; }

    public String getNombre() { return Nombre; }

    public void setNombre(String nombre) { nombre = nombre;}

    public String getApellido() { return Apellido; }

    public  void setApellido(String apellido) { Apellido = apellido;}

    public String getTelefono(){ return Telefono;}

    public void setTelefono(String telefono) { Telefono = telefono;}

    public String getDireccion(){ return Direccion;}

    public void setDireccion(String direccion){ Direccion = direccion;}

    public String getDui() { return Dui;}

    public void setDui(String dui) { this.Dui = dui;}

    @Override
    public String toString() {
        return Nombre + " " + Apellido;
    }
}
