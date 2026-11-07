package ar.edu.utn.ms2.tpi.strategy;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;

//---- REFACT 4: PATRON STRATEGY (CONTRATO) ----//
// Interfaz comun para familias de algoritmos de clasificacion.
// Permite cumplir el principio Abierto/Cerrado (OCP).
public interface ClasificadorSolicitudStrategy {
    ClasificacionResultado clasificar(int urgenciaSolicitada);
}