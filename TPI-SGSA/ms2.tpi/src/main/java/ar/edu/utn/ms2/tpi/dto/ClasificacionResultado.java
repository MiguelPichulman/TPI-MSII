package ar.edu.utn.ms2.tpi.dto;

import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON STRATEGY (OBJETO DE VALOR) ----//
// Encapsula la tupla (ar.edu.utn.ms2.tpi.model.Area, Prioridad) resultante de la clasificacion algoritmica
// de una tipologia de solicitud academica.
public class ClasificacionResultado {
    private final Area areaAsignada;
    private final NivelPrioridad prioridad;

    public ClasificacionResultado(Area areaAsignada, NivelPrioridad prioridad) {
        this.areaAsignada = areaAsignada;
        this.prioridad = prioridad;
    }

    public Area getAreaAsignada() {
        return areaAsignada;
    }

    public NivelPrioridad getPrioridad() {
        return prioridad;
    }
}