package ar.edu.utn.ms2.tpi.state;

import ar.edu.utn.ms2.tpi.exception.TransicionEstadoInvalidaException;
import ar.edu.utn.ms2.tpi.model.Solicitud;

//---- REFACT 3: PATRON STATE (ESTADO TERMINAL) ----//
// Representa un estado final de rechazo, inmutable ante nuevas transiciones.
public class EstadoRechazada implements EstadoSolicitud {

    @Override
    public String getCodigo() {
        return "RECHAZADA";
    }

    @Override
    public String getDescripcion() {
        return "ar.edu.utn.ms2.tpi.model.Solicitud rechazada";
    }

    @Override
    public void pasarAEnProceso(Solicitud contexto) {
        throw new TransicionEstadoInvalidaException("No se puede modificar una solicitud ya RECHAZADA.");
    }

    @Override
    public void dictaminar(Solicitud contexto, boolean aprobada, String observacion) {
        throw new TransicionEstadoInvalidaException("La solicitud ya se encuentra RECHAZADA y cerrada.");
    }
}