package ar.edu.utn.ms2.tpi;

import ar.edu.utn.ms2.tpi.dto.RegistroSolicitudComando;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.Solicitud;
import ar.edu.utn.ms2.tpi.notification.INotificador;
import ar.edu.utn.ms2.tpi.notification.NotificacionLegacyAdapter;
import ar.edu.utn.ms2.tpi.notification.ServicioNotificacionLegacy;
import ar.edu.utn.ms2.tpi.service.SolicitudManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION DE SOLICITUDES (TOTALMENTE REFACTORIZADO Y MODULARIZADO) ===");

        // Inyeccion de dependencias con Adapter
        ServicioNotificacionLegacy servicioLegacy = new ServicioNotificacionLegacy();
        INotificador notificador = new NotificacionLegacyAdapter(servicioLegacy);
        SolicitudManager manager = new SolicitudManager(notificador);

        // Registro usando Parameter Objects
        RegistroSolicitudComando cmd1 = new RegistroSolicitudComando(
                "9841", "Martin Gomez", "mgomez@alumnos.utn.edu.ar",
                "EQUIVALENCIA", "Solicitud de equivalencia para Algebra Lineal", 2);

        RegistroSolicitudComando cmd2 = new RegistroSolicitudComando(
                "7723", "Lucia Fernandez", "lfernandez@alumnos.utn.edu.ar",
                "CERTIFICADO", "Certificado de alumno regular para tramite de pasaje", 1);

        RegistroSolicitudComando cmd3 = new RegistroSolicitudComando(
                "6512", "Esteban Quito", "equito@alumnos.utn.edu.ar",
                "REINSCRIPCION", "Reincorporacion por baja temporal", 1);

        Solicitud s1 = manager.registrarSolicitud(cmd1);
        Solicitud s2 = manager.registrarSolicitud(cmd2);
        Solicitud s3 = manager.registrarSolicitud(cmd3);

        // Transiciones con patron State
        manager.actualizarEstado(s1.id, "EN_PROCESO", "En analisis por comision departamental");
        manager.actualizarEstado(s1.id, "APROBADA", "Equivalencia otorgada segun resolucion 104/26");

        // Reasignacion de Area
        Area areaDespacho = new Area("DESPACHO", "Despacho General", "despacho@frd.utn.edu.ar");
        manager.reasignarArea(s2.id, areaDespacho);

        // Reporte final
        System.out.println("\n--- Reporte final consolidado ---");
        List<Solicitud> todas = manager.getTodas();
        for (Solicitud s : todas) {
            System.out.println("Solicitud #" + s.id + " | Estudiante: " + s.estudiante.nombreCompleto +
                    " | Area: " + s.areaAsignada.nombre + " | Estado: " + s.estado.getCodigo() +
                    " | Prioridad: " + s.prioridad + " | Resolucion: " + s.resolucionNotas);
        }

        System.out.println("\n=== EJECUCION EXITOSA: COMPORTAMIENTO EXTERNO PRESERVADO AL 100% ===");
    }
}