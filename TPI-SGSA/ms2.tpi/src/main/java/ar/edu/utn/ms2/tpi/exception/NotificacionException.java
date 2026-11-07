package ar.edu.utn.ms2.tpi.exception;

//---- REFACT 4: MANEJO DE ERRORES PROFESIONAL ----//
// Excepcion que encapsula fallos en canales externos, impidiendo silenciar
// errores con bloques catch vacios.
public class NotificacionException extends RuntimeException {
    public NotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}