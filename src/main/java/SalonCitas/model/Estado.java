package SalonCitas.model;

public class Estado {

    private int IdEstado;
    private String NombreEstado;
    private String TipoEstado;

    public Estado()
    {
    }

    public Estado(int idEstado, String nombreEstado, String tipoEstado) {
        IdEstado = idEstado;
        NombreEstado = nombreEstado;
        TipoEstado = tipoEstado;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int idEstado) {
        IdEstado = idEstado;
    }

    public String getNombreEstado() {
        return NombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        NombreEstado = nombreEstado;
    }

    public String getTipoEstado() {
        return TipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        TipoEstado = tipoEstado;
    }

    // El JComboBox muestra este texto.
    @Override
    public String toString()
    {
        return NombreEstado + "(" + TipoEstado + ")";
    }
}
