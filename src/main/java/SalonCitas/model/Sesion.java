package SalonCitas.model;

public class Sesion {

    private static Cliente clienteActual;

    public static void setClienteActual(Cliente cliente) {
        clienteActual = cliente;
    }

    public static Cliente getClienteActual() {
        return clienteActual;
    }
}
