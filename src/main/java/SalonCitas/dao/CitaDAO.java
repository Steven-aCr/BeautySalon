package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Cita;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad {@link Cita}.
 * <p>
 * Encapsula todas las operaciones CRUD (Create, Read, Update, Delete)
 * sobre la tabla {@code Citas}, incluyendo consultas con JOIN para
 * obtener información legible (nombres) en lugar de solo IDs.
 * </p>
 *
 * <p>Cada método abre su propia conexión mediante {@link connectionManager}
 * y la cierra en el bloque {@code finally}, por lo que cada llamada es
 * independiente y no debe reutilizar conexiones entre métodos.</p>
 */
public class CitaDAO {

    /** Manejador de conexión a la base de datos (Singleton). */
    private connectionManager conn;

    /** Resultado de una consulta SQL, reutilizado entre métodos. */
    private ResultSet rs;

    /** Statement preparado para ejecutar consultas parametrizadas. */
    private PreparedStatement ps;

    /**
     * Constructor por defecto.
     * Obtiene la instancia única de {@link connectionManager}.
     */
    public CitaDAO() {
        conn = connectionManager.getInstance();
    }

    /**
     * Inserta una nueva cita en la base de datos.
     * <p>
     * Tras la inserción, recupera el ID generado automáticamente
     * y retorna el objeto {@link Cita} completo consultándolo por ID.
     * </p>
     *
     * @param cita objeto {@link Cita} con los datos a insertar
     *             (sin {@code IdCita}, ya que es autogenerado).
     * @return la {@link Cita} recién creada, incluyendo su {@code IdCita} generado,
     *         o {@code null} si no se pudo obtener tras la inserción.
     * @throws SQLException si ocurre un error al insertar o si no se
     *                       afectó ninguna fila.
     */
    public Cita Create(Cita cita) throws SQLException
    {
        Cita result = null;
        try
        {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Citas (IdCliente, IdEmpleado, Fecha, Hora, IdEstado, Observaciones) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(   1, cita.getIdCliente());
            ps.setInt(   2, cita.getIdEmpleado());
            ps.setDate(  3, cita.getFecha());
            ps.setTime(  4, cita.getHora());
            ps.setInt(   5, cita.getIdEstado());
            ps.setString(6, cita.getObservaciones());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0)
            {
                try (ResultSet generatedKeys = ps.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        int idGenerated = generatedKeys.getInt(1);
                        result = getById(idGenerated);
                    }
                }
            } else {
                throw new SQLException("Error al crear un nuevo registro de Cita. ID no obtenido.");
            }


        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }

    /**
     * Busca una cita por su identificador único.
     *
     * @param idCita identificador de la cita a buscar.
     * @return el objeto {@link Cita} encontrado con todos sus campos
     *         poblados, o {@code null} si no existe ninguna cita con ese ID.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public Cita getById(int idCita) throws SQLException
    {
        Cita cita = new Cita();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT IdCita, IdCliente, IdEmpleado, Fecha, Hora, IdEstado, Observaciones " +
                            "FROM Citas " +
                            "WHERE IdCita = ?");

            ps.setInt(1, idCita);
            rs = ps.executeQuery();

            if (rs.next())
            {
                cita = mapear(rs);
            } else
            {
                cita = null;
            }
            ps.close();
            rs.close();

        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return cita;
    }

    /**
     * Obtiene el listado completo de citas, incluyendo información
     * legible mediante JOIN: nombre completo del cliente, nombre
     * completo del empleado y nombre del estado.
     * <p>
     * Pensado para poblar tablas (JTable) en la interfaz gráfica,
     * ordenado por fecha y hora descendente (las citas más
     * recientes primero).
     * </p>
     *
     * @return lista de {@link Cita}, cada una con los campos
     *         {@code nombreCliente}, {@code nombreEmpleado} y
     *         {@code nombreEstado} adicionales ya asignados.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Cita> listarTodo() throws SQLException
    {
        List<Cita> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT c.IdCita, c.IdCliente, c.IdEmpleado, c.Fecha, c.Hora, " +
                            "c.IdEstado, c.Observaciones, " +
                            "pc.Nombre + ' ' + pc.Apellido AS NombreCliente, " +
                            "pe.Nombre + ' ' + pe.Apellido AS NombreEmpleado, " +
                            "e.NombreEstado " +
                            "FROM Citas c " +
                            "JOIN Cliente cl ON c.IdCliente = cl.IdCliente " +
                            "JOIN Persona pc ON cl.IdPersona = pc.IdPersona " +
                            "JOIN Empleado em ON c.IdEmpleado = em.IdEmpleado " +
                            "JOIN Persona pe ON em.IdPersona = pe.IdPersona " +
                            "JOIN Estado e ON c.IdEstado = e.IdEstado " +
                            "ORDER BY c.Fecha DESC, c.Hora DESC");

            rs = ps.executeQuery();

            while (rs.next())
            {
                Cita cita = mapear(rs);
                cita.setNombreCliente (rs.getString("NombreCliente"));
                cita.setNombreEmpleado(rs.getString("NombreEmpleado"));
                cita.setNombreEstado  (rs.getString("NombreEstado"));
                lista.add(cita);
            }
            ps.close();
            rs.close();

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
     * Actualiza los datos de una cita existente, identificada por
     * su {@code IdCita}.
     *
     * @param cita objeto {@link Cita} con el {@code IdCita} de la cita
     *             a actualizar y los nuevos valores para el resto de campos.
     * @return {@code true} si se actualizó al menos una fila,
     *         {@code false} si no se encontró ninguna cita con ese ID.
     * @throws SQLException si ocurre un error al ejecutar la actualización.
     */
    public boolean Actualizar(Cita cita) throws SQLException
    {
        try
        {
            ps = conn.connect().prepareStatement(
                    "UPDATE Citas SET IdCliente = ?, IdEmpleado = ?, Fecha = ?, " +
                            "Hora = ?, IdEstado = ?, Observaciones = ? " +
                            "WHERE IdCita = ?");

            ps.setInt(   1, cita.getIdCliente());
            ps.setInt(   2, cita.getIdEmpleado());
            ps.setDate(  3, cita.getFecha());
            ps.setTime(  4, cita.getHora());
            ps.setInt(   5, cita.getIdEstado());
            ps.setString(6, cita.getObservaciones());
            ps.setInt(   7, cita.getIdCita());

            boolean actualizado = ps.executeUpdate() > 0;
            ps.close();
            return actualizado;

        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
    }

    /**
     * Elimina una cita de la base de datos según su identificador.
     *
     * @param id identificador de la cita a eliminar.
     * @return {@code true} si se eliminó al menos una fila,
     *         {@code false} si no existía ninguna cita con ese ID.
     * @throws SQLException si ocurre un error al ejecutar la eliminación.
     */
    public boolean Eliminar(int id) throws SQLException
    {
        try
        {
            ps = conn.connect().prepareStatement("DELETE FROM Citas WHERE IdCita = ?");
            ps.setInt(1, id);

            boolean eliminado = ps.executeUpdate() > 0;
            ps.close();
            return eliminado;

        } catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
    }

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Cita},
     * leyendo únicamente las columnas propias de la tabla {@code Citas}
     * (no incluye los campos derivados de JOIN como nombres de
     * cliente/empleado/estado; esos se asignan por separado en
     * {@link #listarTodo()}).
     *
     * @param rs ResultSet posicionado en la fila a mapear.
     * @return un nuevo objeto {@link Cita} con los campos básicos asignados.
     * @throws SQLException si ocurre un error al leer alguna columna.
     */
    private Cita mapear(ResultSet rs) throws SQLException
    {
        Cita c = new Cita();
        c.setIdCita       (rs.getInt   ("IdCita"));
        c.setIdCliente    (rs.getInt   ("IdCliente"));
        c.setIdEmpleado   (rs.getInt   ("IdEmpleado"));
        c.setFecha        (rs.getDate  ("Fecha"));
        c.setHora         (rs.getTime  ("Hora"));
        c.setIdEstado     (rs.getInt   ("IdEstado"));
        c.setObservaciones(rs.getString("Observaciones"));
        return c;
    }
}