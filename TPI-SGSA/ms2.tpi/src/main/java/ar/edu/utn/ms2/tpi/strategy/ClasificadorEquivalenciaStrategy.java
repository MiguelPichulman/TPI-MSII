package ar.edu.utn.ms2.tpi.strategy;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON STRATEGY (ESTRATEGIA CONCRETA) ----//
// Encapsula la regla de negocio para tramites de EQUIVALENCIA:
// Deriva a Secretaria Academica y ajusta prioridad (ALTA/MEDIA) segun urgencia.
public class ClasificadorEquivalenciaStrategy implements ClasificadorSolicitudStrategy {
    @Override
    public ClasificacionResultado clasificar(int urgenciaSolicitada) {
        Area academica = new Area("ACADEMICA", "Secretaria Academica", "academica@frd.utn.edu.ar");
        NivelPrioridad prioridad = (urgenciaSolicitada >= 2) ? NivelPrioridad.ALTA : NivelPrioridad.MEDIA;
        return new ClasificacionResultado(academica, prioridad);
    }
}