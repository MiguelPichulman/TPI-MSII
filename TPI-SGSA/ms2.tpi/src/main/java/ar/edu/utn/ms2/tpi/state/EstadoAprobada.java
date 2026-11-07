package ar.edu.utn.ms2.tpi.state;

import ar.edu.utn.ms2.tpi.exception.TransicionEstadoInvalidaException;
import ar.edu.utn.ms2.tpi.model.Solicitud;

//---- REFACT 3: PATRON STATE (ESTADO TERMINAL) ----//
// Representa un estado final. Bloquea cualquier modificacion o nuevo dictamen
// cumpliendo las invariantes de negocio.
public class EstadoAprobada implements EstadoSolicitud {

    @Override
    public String getCodigo() {
        return "APROBADA";
    }

    @Override
    public String getDescripcion() {
        return "ar.edu.utn.ms2.tpi.model.Solicitud dictaminada favorablemente";
    }

    @Override
    public void pasarAEnProceso(Solicitud contexto) {
        throw new TransicionEstadoInvalidaException("No se puede modificar una solicitud ya APROBADA.");
    }

    @Override
    public void dictaminar(Solicitud contexto, boolean aprobada, String observacion) {
        throw new TransicionEstadoInvalidaException("La solicitud ya se encuentra APROBADA y cerrada.");
    }
}