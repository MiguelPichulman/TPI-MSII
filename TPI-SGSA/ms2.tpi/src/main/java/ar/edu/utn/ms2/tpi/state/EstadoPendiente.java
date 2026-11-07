package ar.edu.utn.ms2.tpi.state;

import ar.edu.utn.ms2.tpi.exception.TransicionEstadoInvalidaException;
import ar.edu.utn.ms2.tpi.model.Solicitud;

//---- REFACT 3: PATRON STATE (ESTADO CONCRETO) ----//
// Implementa el comportamiento cuando la solicitud esta recien ingresada.
// Solo admite pasar a EN_PROCESO; dictaminar directamente dispara excepcion.
public class EstadoPendiente implements EstadoSolicitud {

    @Override
    public String getCodigo() {
        return "PENDIENTE";
    }

    @Override
    public String getDescripcion() {
        return "ar.edu.utn.ms2.tpi.model.Solicitud recien ingresada";
    }

    @Override
    public void pasarAEnProceso(Solicitud contexto) {
        contexto.setEstado(new EstadoEnProceso());
    }

    @Override
    public void dictaminar(Solicitud contexto, boolean aprobada, String observacion) {
        throw new TransicionEstadoInvalidaException("Regla violada: primero debe estar EN_PROCESO.");
    }
}