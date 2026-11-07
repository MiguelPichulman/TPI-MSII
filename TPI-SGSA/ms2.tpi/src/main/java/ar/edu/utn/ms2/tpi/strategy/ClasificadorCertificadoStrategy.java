package ar.edu.utn.ms2.tpi.strategy;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON STRATEGY (ESTRATEGIA CONCRETA) ----//
// Encapsula la regla de negocio para solicitudes de tipo CERTIFICADO:
// Deriva al Departamento de Alumnos con prioridad fija BAJA.
public class ClasificadorCertificadoStrategy implements ClasificadorSolicitudStrategy {
    @Override
    public ClasificacionResultado clasificar(int urgenciaSolicitada) {
        Area alumnos = new Area("ALUMNOS", "Departamento de Alumnos", "alumnos@frd.utn.edu.ar");
        return new ClasificacionResultado(alumnos, NivelPrioridad.BAJA);
    }
}