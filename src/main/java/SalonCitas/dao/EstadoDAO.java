package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Estado;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {

    private  connectionManager conn;
    private ResultSet rs;
    private PreparedStatement ps;

    public EstadoDAO(){conn = connectionManager.getInstance();}

    //Metodo para traer todos los Estados ─ se utiliza para poblar el JComboBox.
    public List<Estado> ListarTodos() throws SQLException
    {
        List<Estado> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT IdEstado, NombreEstado, TipoEstado " +
                    "FROM Estado " +
                    "ORDER BY TipoEstado, NombreEstado ");
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
        }catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return lista;
    }

    //Metodo para filtrar por tipo.
    public List<Estado> listarPorTipo(String tipo) throws SQLException
    {
        List<Estado> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT IdEstado, NombreEstado, TipoEstado " +
                    "FROM Estado " +
                    "WHERE TipoEstado = ? ");

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

        }catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return lista;
    }

    //Metodo para buscar registros en la tabla "Estado".
    public ArrayList<Estado> search(String nombreEstado) throws SQLException
    {
        ArrayList<Estado> records = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT IdEStado, NombreEstado, TipoEstado " +
                    "FROM Estado " +
                    "WHERE NombreEstado LIKE ? ");
            ps.setString(1, "%" + nombreEstado + "%");

            rs = ps.executeQuery();

            while(rs.next())
            {
                Estado estado  = new Estado();
                estado.setIdEstado(rs.getInt(1));
                estado.setNombreEstado(rs.getString(2));
                estado.setTipoEstado(rs.getString(3));
            }
            ps.close();
            rs.close();
        }catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }
}