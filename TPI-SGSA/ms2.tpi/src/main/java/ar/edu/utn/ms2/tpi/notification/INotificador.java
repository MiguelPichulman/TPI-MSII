package ar.edu.utn.ms2.tpi.notification;

import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON ADAPTER & INVERSION DE DEPENDENCIAS (DIP) ----//
// Abstraccion que desacopla la logica de negocio de la libreria externa.
// El modulo central ahora depende de esta interfaz y no de clases concretas.
public interface INotificador {
    void notificar(String destinatario, String mensaje, NivelPrioridad prioridad);
}