package ar.edu.utn.ms2.tpi.strategy;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON STRATEGY (ESTRATEGIA CONCRETA) ----//
// Encapsula la regla de negocio para tramites de REINCORPORACION/REINSCRIPCION:
// Deriva al Departamento de Sistemas con prioridad MEDIA.
public class ClasificadorReinscripcionStrategy implements ClasificadorSolicitudStrategy {
    @Override
    public ClasificacionResultado clasificar(int urgenciaSolicitada) {
        Area sistemas = new Area("SISTEMAS", "Departamento de Sistemas", "sistemas@frd.utn.edu.ar");
        return new ClasificacionResultado(sistemas, NivelPrioridad.MEDIA);
    }
}