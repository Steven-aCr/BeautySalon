package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Estado;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad {@link Estado}.
 * <p>
 * Encapsula las operaciones de lectura sobre la tabla {@code Estado},
 * la cual almacena los distintos estados posibles para una {@code Persona}
 * (Activo / Inactivo) y para una {@code Cita} (Pendiente / Confirmada / Cancelada),
 * diferenciados por la columna {@code TipoEstado}.
 * </p>
 *
 * <p>Cada método abre su propia conexión mediante {@link connectionManager}
 * y la cierra en el bloque {@code finally}, por lo que cada llamada es
 * independiente y no debe reutilizar conexiones entre métodos.</p>
 */
public class EstadoDAO {

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
    public EstadoDAO() {
        conn = connectionManager.getInstance();
    }

    /**
     * Obtiene todos los registros de la tabla {@code Estado}, sin filtrar.
     * <p>
     * Pensado principalmente para poblar un {@code JComboBox} con todos
     * los estados disponibles, ordenados por tipo y nombre.
     * </p>
     *
     * @return lista de todos los {@link Estado} registrados, ordenados
     *         por {@code TipoEstado} y luego por {@code NombreEstado}.
     *         Si la tabla está vacía, retorna una lista vacía (no {@code null}).
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Estado> ListarTodos() throws SQLException
    {
        List<Estado> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT IdEstado, NombreEstado, TipoEstado " +
                            "FROM Estado " +
                            "ORDER BY TipoEstado, NombreEstado");
            rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new Estado(
                        rs.getInt("IdEstado"),
                        rs.getString("NombreEstado"),
                        rs.getString("TipoEstado")));
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
     * Obtiene los estados que pertenecen a un tipo específico.
     * <p>
     * Por ejemplo, {@code listarPorTipo("Cita")} retorna únicamente
     * los estados aplicables a una cita (Pendiente, Confirmada, Cancelada),
     * mientras que {@code listarPorTipo("Persona")} retorna los estados
     * aplicables a clientes/empleados (Activo, Inactivo).
     * </p>
     *
     * @param tipo valor exacto de la columna {@code TipoEstado} a filtrar
     *             (por ejemplo: {@code "Cita"} o {@code "Persona"}).
     * @return lista de {@link Estado} cuyo {@code TipoEstado} coincide
     *         exactamente con el parámetro recibido. Si no hay coincidencias,
     *         retorna una lista vacía (no {@code null}).
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Estado> listarPorTipo(String tipo) throws SQLException
    {
        List<Estado> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT IdEstado, NombreEstado, TipoEstado " +
                            "FROM Estado " +
                            "WHERE TipoEstado = ?");

            ps.setString(1, tipo);
            rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new Estado(
                        rs.getInt("IdEstado"),
                        rs.getString("NombreEstado"),
                        rs.getString("TipoEstado")
                ));
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
     * Busca estados cuyo nombre contenga el texto indicado (búsqueda parcial,
     * sin distinguir mayúsculas/minúsculas según la configuración del motor SQL).
     * <p>
     * Internamente usa el operador SQL {@code LIKE} con comodines
     * ({@code %texto%}), por lo que coincide con cualquier nombre que
     * contenga la subcadena recibida en cualquier posición.
     * </p>
     *
     * @param nombreEstado texto (parcial o completo) a buscar dentro de
     *                      {@code NombreEstado}.
     * @return lista de {@link Estado} cuyo nombre contiene el texto buscado.
     *         Si no hay coincidencias, retorna una lista vacía (no {@code null}).
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public ArrayList<Estado> search(String nombreEstado) throws SQLException
    {
        ArrayList<Estado> records = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT IdEstado, NombreEstado, TipoEstado " +
                            "FROM Estado " +
                            "WHERE NombreEstado LIKE ?");

            ps.setString(1, "%" + nombreEstado + "%");
            rs = ps.executeQuery();

            while (rs.next())
            {
                Estado estado = new Estado();
                estado.setIdEstado(rs.getInt(1));
                estado.setNombreEstado(rs.getString(2));
                estado.setTipoEstado(rs.getString(3));
                records.add(estado);
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
        return records;
    }
}