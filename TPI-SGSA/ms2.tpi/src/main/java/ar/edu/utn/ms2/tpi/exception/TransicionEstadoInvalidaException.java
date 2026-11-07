package ar.edu.utn.ms2.tpi.exception;

//---- REFACT 3: PATRON STATE (EXCEPCION DE TRANSICION) ----//
// Comunica formalmente cuando una solicitud intenta realizar un cambio de estado
// que viola el ciclo de vida del reglamento academico.
public class TransicionEstadoInvalidaException extends RuntimeException {
    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}