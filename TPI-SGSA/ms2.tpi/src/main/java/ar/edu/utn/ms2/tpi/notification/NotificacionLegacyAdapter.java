package ar.edu.utn.ms2.tpi.notification;

import ar.edu.utn.ms2.tpi.exception.NotificacionException;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;

//---- REFACT 4: PATRON ADAPTER (WRAPPER DE TERCEROS) ----//
// Adapta la interfaz rigida de ar.edu.utn.ms2.tpi.notification.ServicioNotificacionLegacy al contrato ar.edu.utn.ms2.tpi.notification.INotificador.
// Facilita sustituir el proveedor y permite mockearlo en pruebas unitarias.
public class NotificacionLegacyAdapter implements INotificador {
    private final ServicioNotificacionLegacy servicioLegacy;

    public NotificacionLegacyAdapter(ServicioNotificacionLegacy servicioLegacy) {
        this.servicioLegacy = servicioLegacy;
    }

    @Override
    public void notificar(String destinatario, String mensaje, NivelPrioridad prioridad) {
        try {
            // Se adapta el valor del Enum ar.edu.utn.ms2.tpi.model.NivelPrioridad al int que espera el legacy
            servicioLegacy.enviarAlertaExterna(destinatario, mensaje, prioridad.getValorNumerico());
        } catch (Exception ex) {
            // Se propaga el error con contexto tecnico, erradicando el catch vacio
            throw new NotificacionException("Fallo en la notificacion externa: " + ex.getMessage(), ex);
        }
    }
}