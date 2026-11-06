// Simula una librería o servicio de un tercero (código rígido)
public class ServicioNotificacionLegacy {
    public boolean enviarAlertaExterna(String receptor, String mensaje, int nivelUrgencia) {
        System.out.println("[ALERTA EXTERNA -> " + receptor + "] (" + nivelUrgencia + "): " + mensaje);
        return true;
    }
}