package SalonCitas.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class connectionManager {

    private static final String STR_CONNECTION =
            "jdbc:sqlserver://CitasSalonBD.mssql.somee.com:1433;"
                    +   "databaseName=CitasSalonBD;"
                    +   "user=JencyFC_SQLLogin_1;"
                    +   "password=gsxs4rp5ki;"
                    +   "encrypt=true;"
                    +   "trustServerCertificate=true";
    private Connection connection;
    private static connectionManager instances;

    private connectionManager()
    {
        this.connection = null;
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch (ClassNotFoundException ex)
        {
            throw new RuntimeException("Error al cargar el Driver JDBC de SQL Server.");
        }
    }

    public synchronized Connection connect() throws SQLException
    {
        if (this.connection == null || this.connection.isClosed())
        {
            try
            {
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            }catch (SQLException ex)
            {
                throw new SQLException("Error al conectar con la base de datos." + ex.getMessage(), ex);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException
    {
        if(this.connection != null)
        {
            try
            {
                this.connection.close();
            }catch (SQLException ex)
            {
                throw new SQLException(ex.getMessage(), ex);
            }finally {
                this.connection = null;
            }
        }
    }

    public static synchronized connectionManager getInstance()
    {
        if (instances == null)
        {
            instances = new connectionManager();
        }
        return instances;
    }
}
