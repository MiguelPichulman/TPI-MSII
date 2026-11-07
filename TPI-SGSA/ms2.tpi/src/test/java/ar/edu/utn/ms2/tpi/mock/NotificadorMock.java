package ar.edu.utn.ms2.tpi.mock;

import ar.edu.utn.ms2.tpi.model.NivelPrioridad;
import ar.edu.utn.ms2.tpi.notification.INotificador;

// Test Double: Mock para auditar interacciones sin llamadas de infraestructura
public class NotificadorMock implements INotificador {

    private int llamadas = 0;
    private String ultimoDestinatario;
    private String ultimoMensaje;
    private NivelPrioridad ultimaPrioridad;

    @Override
    public void notificar(String destinatario, String mensaje, NivelPrioridad prioridad) {
        this.llamadas++;
        this.ultimoDestinatario = destinatario;
        this.ultimoMensaje = mensaje;
        this.ultimaPrioridad = prioridad;
    }

    public int getLlamadas() {
        return llamadas;
    }

    public String getUltimoDestinatario() {
        return ultimoDestinatario;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public NivelPrioridad getUltimaPrioridad() {
        return ultimaPrioridad;
    }
}