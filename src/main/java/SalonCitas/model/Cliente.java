package SalonCitas.model;

public class Cliente {

        private int    idCliente;
        private int    idPersona;
        private int    idEstado;
        private String email;
        private String password;


        private String nombreCompleto;


        public Cliente() {}


        public Cliente(int idEstado, String email, String password) {
            this.idEstado   = idEstado;
            this.email      = email;
            this.password = password;
        }

        // ── Constructor completo (con id, para SELECT/UPDATE) ───────────────────
        public Cliente(int idCliente, int idPersona, int idEstado, String email, String password) {
            this.idCliente  = idCliente;
            this.idPersona  = idPersona;
            this.idEstado   = idEstado;
            this.email      = email;
            this.password = password;
        }

        public int    getIdCliente()      { return idCliente; }
        public void   setIdCliente(int idCliente)   { this.idCliente  = idCliente; }

        public int    getIdPersona()      { return idPersona; }
        public void   setIdPersona(int idPersona)   { this.idPersona  = idPersona; }

        public int    getIdEstado()       { return idEstado; }
        public void   setIdEstado(int idEstado)     { this.idEstado   = idEstado; }

        public String getEmail()          { return email; }
        public void   setEmail(String email)        { this.email      = email; }

        public String getPassword()     { return password; }
        public void   setPassword(String password) { this.password = password; }

        public String getNombreCompleto() { return nombreCompleto; }
        public void   setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

        @Override
        public String toString() {
            return email;
        }
}