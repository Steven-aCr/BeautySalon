package SalonCitas.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*; //importa todos los metodos estaticos

class connectionManagerTest {

    connectionManager connectionM;
    @BeforeEach
    void setUp() throws SQLException
    {
        connectionM = connectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException
    {
        //Se ejecuta despues de cada metodo de prueba.
        //Cierra la conexion y limpia los recursos.
        if (connectionM != null){
            connectionM.disconnect();
            connectionM = null; //Para asegurar que no se utilice accidentalmente.
        }
    }

    @Test
    void test() throws SQLException
    {
        Connection conn = connectionM.connect();

        assertNotNull(conn, "La conexion no debe ser nula.");

        assertFalse(conn.isClosed(), "La conexion debe estar abierta.");
        if (conn != null){
            conn.close(); //Cierra la conexion despues de la prueba.
        }
    }
}