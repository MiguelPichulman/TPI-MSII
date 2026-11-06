import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE GESTION DE SOLICITUDES (SISTEMA BASE) ===");

        SolicitudManager manager = new SolicitudManager();

        // 1. Registro de solicitudes
        Solicitud s1 = manager.registrarSolicitud("9841", "Martin Gomez", "mgomez@alumnos.utn.edu.ar",
                "EQUIVALENCIA", "Solicitud de equivalencia para Algebra Lineal", 2);

        Solicitud s2 = manager.registrarSolicitud("7723", "Lucia Fernandez", "lfernandez@alumnos.utn.edu.ar",
                "CERTIFICADO", "Certificado de alumno regular para tramite de pasaje", 1);

        Solicitud s3 = manager.registrarSolicitud("6512", "Esteban Quito", "equito@alumnos.utn.edu.ar",
                "REINSCRIPCION", "Reincorporacion por baja temporal", 1);

        // 2. Transicion de estado
        System.out.println("\n--- Transicion de estados ---");
        manager.actualizarEstado(s1.id, "EN_PROCESO", "En analisis por comision departamental");
        manager.actualizarEstado(s1.id, "APROBADA", "Equivalencia otorgada segun resolucion");

        // 3. Reasignacion de area
        System.out.println("\n--- Reasignacion de area ---");
        manager.reasignarArea(s2.id, "DESPACHO", "Despacho General", "despacho@frd.utn.edu.ar");

        // 4. Filtrado y consulta
        System.out.println("\n--- Reporte filtrado por estado 'APROBADA' ---");
        List<Solicitud> aprobadas = manager.obtenerSolicitudesPorEstadoYArea("APROBADA", null);
        for (Solicitud s : aprobadas) {
            System.out.println("Solicitud #" + s.id + " | Estudiante: " + s.estudiante.nombreCompleto +
                    " | Area: " + s.areaAsignada.nombre + " | Estado: " + s.estado.codigo +
                    " | Resolucion: " + s.resolucionNotas);
        }

        System.out.println("\n=== DEMOSTRACION FINALIZADA: CALIDAD EXTERNA SATISFACTORIA ===");
    }
}