package ar.edu.utn.ms2.tpi.strategy;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON STRATEGY (ESTRATEGIA POR DEFECTO) ----//
// Estrategia fallback ante tipologias no contempladas expresamente:
// Deriva a Mesa de Entradas con prioridad BAJA.
public class ClasificadorGeneralStrategy implements ClasificadorSolicitudStrategy {
    @Override
    public ClasificacionResultado clasificar(int urgenciaSolicitada) {
        Area mesa = new Area("MESA_GENERAL", "Mesa de Entradas", "mesa@frd.utn.edu.ar");
        return new ClasificacionResultado(mesa, NivelPrioridad.BAJA);
    }
}