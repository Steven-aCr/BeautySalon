package SalonCitas.dao;

import SalonCitas.connection.connectionManager;
import SalonCitas.model.Persona;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private connectionManager conn;
    private ResultSet rs;
    private PreparedStatement ps;

    public PersonaDAO() { conn = connectionManager.getInstance(); }

    // Metodo para traer todas las Personas.
    public List<Persona> listarTodos() throws SQLException
    {
        List<Persona> lista = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT IdPersona, Nombre, Apellido, Telefono, Direccion, Dui " +
                    "FROM Persona " +
                    "ORDER BY Apellido, Nombre ");
            rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new Persona(
                        rs.getInt("IdPersona"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("Telefono"),
                        rs.getString("Direccion"),
                        rs.getString("Dui")));
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

    // Metodo para actualizar una Persona existente.
    public boolean actualizar(Persona persona) throws SQLException
    {
        try
        {
            ps = conn.connect().prepareStatement("UPDATE Persona SET Nombre = ?, Apellido = ?, Telefono = ?, Direccion = ?, Dui = ? " +
                    "WHERE IdPersona = ? ");
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getTelefono());
            ps.setString(4, persona.getDireccion());
            ps.setString(5, persona.getDui());
            ps.setInt   (6, persona.getIdPersona());

            boolean updated = ps.executeUpdate() > 0;
            ps.close();
            return updated;

        }catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }finally {
            ps = null;
            conn.disconnect();
        }
    }

    // Metodo para buscar registros en la tabla "Persona".
    public ArrayList<Persona> search(String nombre) throws SQLException
    {
        ArrayList<Persona> records = new ArrayList<>();

        try
        {
            ps = conn.connect().prepareStatement("SELECT IdPersona, Nombre, Apellido, Telefono, Direccion, Dui " +
                    "FROM Persona " +
                    "WHERE Nombre LIKE ? OR Apellido LIKE ? ");
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            rs = ps.executeQuery();

            while (rs.next())
            {
                Persona persona = new Persona();
                persona.setIdPersona(rs.getInt(1));
                persona.setNombre(rs.getString(2));
                persona.setApellido(rs.getString(3));
                persona.setTelefono(rs.getString(4));
                persona.setDireccion(rs.getString(5));
                persona.setDui(rs.getString(6));
                records.add(persona);
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