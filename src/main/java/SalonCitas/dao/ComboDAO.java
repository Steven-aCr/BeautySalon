package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Persona;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO de apoyo utilizado para poblar los {@code JComboBox} de
 * Cliente y Empleado en el formulario de Citas.
 * <p>
 * Solo retorna registros cuyo {@code Estado} asociado sea
 * {@code "Activo"}, ya que no tiene sentido agendar una cita
 * con un cliente o empleado inactivo.
 * </p>
 *
 * <p><b>Nota importante:</b> por simplicidad, los métodos de esta
 * clase reutilizan el campo {@code IdPersona} del objeto {@link Persona}
 * para almacenar, según corresponda, el {@code IdCliente} o el
 * {@code IdEmpleado} (no el {@code IdPersona} real de la tabla
 * {@code Persona}). Esto facilita usar directamente ese valor al
 * insertar una nueva {@code Cita}, pero debe tenerse en cuenta para
 * no confundirlo con el ID real de la tabla {@code Persona}.</p>
 *
 * <p>Cada método abre su propia conexión mediante {@link connectionManager}
 * y la cierra en el bloque {@code finally}, por lo que cada llamada es
 * independiente y no debe reutilizar conexiones entre métodos.</p>
 */
public class ComboDAO {

    /** Manejador de conexión a la base de datos (Singleton). */
    private connectionManager conn;

    /** Resultado de una consulta SQL, reutilizado entre métodos. */
    private ResultSet rs;

    /** Statement preparado para ejecutar consultas. */
    private PreparedStatement ps;

    /**
     * Constructor por defecto.
     * Obtiene la instancia única de {@link connectionManager}.
     */
    public ComboDAO() {
        conn = connectionManager.getInstance();
    }

    /**
     * Obtiene la lista de clientes activos, para poblar el
     * {@code JComboBox} de Cliente en el formulario de Citas.
     * <p>
     * Cada {@link Persona} retornada contiene en su campo
     * {@code IdPersona} el valor real de {@code IdCliente}
     * (ver nota de la clase). El nombre y apellido se incluyen
     * para mostrar el nombre completo en el combo.
     * </p>
     *
     * @return lista de {@link Persona} (clientes activos), ordenada
     *         por apellido y nombre. Si no hay clientes activos,
     *         retorna una lista vacía (no {@code null}).
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Persona> listarClientes() throws SQLException
    {
        List<Persona> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT cl.IdCliente, p.Nombre, p.Apellido " +
                            "FROM Cliente cl " +
                            "JOIN Persona p ON cl.IdPersona = p.IdPersona " +
                            "JOIN Estado e ON cl.IdEstado = e.IdEstado " +
                            "WHERE e.NombreEstado = 'Activo' " +
                            "ORDER BY p.Apellido, p.Nombre");

            rs = ps.executeQuery();

            while (rs.next())
            {
                Persona p = new Persona();
                p.setIdPersona(rs.getInt("IdCliente")); // se reutiliza IdPersona para guardar IdCliente
                p.setNombre   (rs.getString("Nombre"));
                p.setApellido (rs.getString("Apellido"));
                lista.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return lista;
    }

    /**
     * Obtiene la lista de empleados activos, para poblar el
     * {@code JComboBox} de Empleado en el formulario de Citas.
     * <p>
     * Cada {@link Persona} retornada contiene en su campo
     * {@code IdPersona} el valor real de {@code IdEmpleado}
     * (ver nota de la clase). El nombre y apellido se incluyen
     * para mostrar el nombre completo en el combo.
     * </p>
     *
     * @return lista de {@link Persona} (empleados activos), ordenada
     *         por apellido y nombre. Si no hay empleados activos,
     *         retorna una lista vacía (no {@code null}).
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Persona> listarEmpleados() throws SQLException
    {
        List<Persona> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT em.IdEmpleado, p.Nombre, p.Apellido " +
                            "FROM Empleado em " +
                            "JOIN Persona p ON em.IdPersona = p.IdPersona " +
                            "JOIN Estado e ON em.IdEstado = e.IdEstado " +
                            "WHERE e.NombreEstado = 'Activo' " +
                            "ORDER BY p.Apellido, p.Nombre");

            rs = ps.executeQuery();

            while (rs.next())
            {
                Persona p = new Persona();
                p.setIdPersona(rs.getInt("IdEmpleado")); // se reutiliza IdPersona para guardar IdEmpleado
                p.setNombre   (rs.getString("Nombre"));
                p.setApellido (rs.getString("Apellido"));
                lista.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return lista;
    }
}