package ar.edu.utn.ms2.tpi.state;

import ar.edu.utn.ms2.tpi.model.Solicitud;

//---- REFACT 3: PATRON STATE (ESTADO CONCRETO) ----//
// Representa el tramite bajo analisis. Es el unico estado habilitado para
// transicionar hacia la aprobacion o el rechazo definitivo.
public class EstadoEnProceso implements EstadoSolicitud {

    @Override
    public String getCodigo() {
        return "EN_PROCESO";
    }

    @Override
    public String getDescripcion() {
        return "ar.edu.utn.ms2.tpi.model.Solicitud en analisis por comision o area";
    }

    @Override
    public void pasarAEnProceso(Solicitud contexto) {
        // Operacion idempotente si ya se encuentra en evaluacion
    }

    @Override
    public void dictaminar(Solicitud contexto, boolean aprobada, String observacion) {
        contexto.setResolucionNotas(observacion);
        if (aprobada) {
            contexto.setEstado(new EstadoAprobada());
        } else {
            contexto.setEstado(new EstadoRechazada());
        }
    }
}