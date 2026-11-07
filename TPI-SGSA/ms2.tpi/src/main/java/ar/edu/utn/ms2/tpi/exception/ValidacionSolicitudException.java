package ar.edu.utn.ms2.tpi.exception;

//---- REFACT 2: EXTRACT CLASS (MANEJO DE ERRORES LIMPIO) ----//
// Excepcion especifica de dominio para senalizar fallos de validacion,
// eliminando la mala practica de retornar null o codigos de error (-1, false).
public class ValidacionSolicitudException extends RuntimeException {
    public ValidacionSolicitudException(String mensaje) {
        super(mensaje);
    }
}