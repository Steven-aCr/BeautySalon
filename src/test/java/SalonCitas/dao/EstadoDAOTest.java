package SalonCitas.dao;

import org.junit.jupiter.api.Test;

import SalonCitas.model.Estado;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoDAOTest {

    private EstadoDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EstadoDAO();
    }

    @Test
    void search() throws SQLException{
        List<Estado> lista = dao.search("Pendiente");
    }

    @Test
    void listarPorTipo() throws SQLException {
        List<Estado> list = dao.listarPorTipo("Cita");
        assertNotNull(list);
        list.forEach(e -> assertEquals("Cita", e.getTipoEstado()));
    }

    @Test
    void listarTodos() throws SQLException {
        List<Estado> lista = dao.ListarTodos();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}