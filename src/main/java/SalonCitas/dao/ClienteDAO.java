package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Cliente;
import SalonCitas.model.Persona;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private connectionManager conn;
    private ResultSet rs;
    private PreparedStatement ps;

    public ClienteDAO() { conn = connectionManager.getInstance(); }

    // Metodo para traer todos los Clientes.
    public List<Cliente> listarTodos() throws SQLException
    {
        List<Cliente> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT c.IdCliente, c.IdPersona, c.IdEstado, c.Email, c.Password, " +
                    "p.Nombre, p.Apellido " +
                    "FROM Cliente c " +
                    "JOIN Persona p ON c.IdPersona = p.IdPersona " +
                    "ORDER BY p.Apellido, p.Nombre ");
            rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new Cliente(
                        rs.getInt("IdCliente"),
                        rs.getInt("IdPersona"),
                        rs.getInt("IdEstado"),
                        rs.getString("Email"),
                        rs.getString("Password")));
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

    public boolean register(Persona persona,Cliente cliente) throws SQLException
    {
        Connection con = null;

        try
        {
            con = conn.connect();
            con.setAutoCommit(false);

            ps = con.prepareStatement("INSERT INTO Persona (Nombre, Apellido, Telefono, Direccion, DUI)" +
                    "VALUES (?, ?, ?, ?, ? )",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getTelefono());
            ps.setString(4, persona.getDireccion());
            ps.setString(5, persona.getDui());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int idPersonaGenerado;
            if (rs.next())
            {
                idPersonaGenerado = rs.getInt(1);
            } else {
                throw  new SQLException("No se pudo obtener el IdPersona generado.");
            }

            ps = con.prepareStatement("INSERT INTO Cliente (IdPersona, IdEstado, Email, Password) " +
                    "VALUES (?, ?, ?, ?)");
            ps.setInt(1, idPersonaGenerado);
            ps.setInt(2, cliente.getIdEstado());   // ← getIdEstado()
            ps.setString(3, cliente.getEmail());   // ← email
            ps.setString(4, cliente.getPassword()); // ← password
            ps.executeUpdate();
            con.commit();
            return true;

        }catch (SQLException ex)
        {
            if (con != null) { try { con.rollback();} catch (SQLException e) {  } }
            throw new SQLException(ex.getMessage(), ex);
        }finally {
            ps = null;
            rs = null;
            conn.disconnect();
            }
    }

    // Busca un cliente por email y password para login. Incluye nombre completo.
    public Cliente login(String email, String password) throws SQLException
    {
        Cliente cliente = null;
        try
        {
            ps = conn.connect().prepareStatement(
                    "SELECT c.IdCliente, c.IdPersona, c.IdEstado, c.Email, c.Password, " +
                            "p.Nombre, p.Apellido " +
                            "FROM Cliente c " +
                            "JOIN Persona p ON c.IdPersona = p.IdPersona " +
                            "WHERE c.Email = ? AND c.Password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next())
            {
                cliente = new Cliente(
                        rs.getInt("IdCliente"),
                        rs.getInt("IdPersona"),
                        rs.getInt("IdEstado"),
                        rs.getString("Email"),
                        rs.getString("Password"));
                cliente.setNombreCompleto(rs.getString("Nombre") + " " + rs.getString("Apellido"));
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
        return cliente;
    }

    // Metodo para buscar registros en la tabla "Cliente".
    public ArrayList<Cliente> search(String nombre) throws SQLException
    {
        ArrayList<Cliente> records = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT c.IdCliente, c.IdPersona, c.IdEstado, c.Email, c.Password, " +
                    "p.Nombre, p.Apellido " +
                    "FROM Cliente c " +
                    "JOIN Persona p ON c.IdPersona = p.IdPersona " +
                    "WHERE p.Nombre LIKE ? OR p.Apellido LIKE ? ");
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            rs = ps.executeQuery();

            while (rs.next())
            {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt(1));
                cliente.setIdPersona(rs.getInt(2));
                cliente.setIdEstado(rs.getInt(3));
                cliente.setEmail(rs.getString(4));
                cliente.setPassword(rs.getString(5));
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