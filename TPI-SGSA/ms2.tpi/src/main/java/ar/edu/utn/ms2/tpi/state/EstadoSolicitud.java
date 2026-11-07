package ar.edu.utn.ms2.tpi.state;

import ar.edu.utn.ms2.tpi.model.Solicitud;

//---- REFACT 3: PATRON STATE (INTERFAZ BASE) ----//
// Contrato polimorfico para el ciclo de vida de la solicitud. Reemplaza
// los switchs y condicionales de strings manuales en el manager por polimorfismo.
public interface EstadoSolicitud {
    String getCodigo();
    String getDescripcion();

    void pasarAEnProceso(Solicitud contexto);
    void dictaminar(Solicitud contexto, boolean aprobada, String observacion);
}