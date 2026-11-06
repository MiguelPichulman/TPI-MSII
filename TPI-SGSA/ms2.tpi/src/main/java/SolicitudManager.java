import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitudManager {

    private List<Solicitud> dbSolicitudes = new ArrayList<>();
    private int contadorId = 1;

    // Dependencia rigida (viola DIP)
    private ServicioNotificacionLegacy servicioAlerta = new ServicioNotificacionLegacy();

    // Long Method, Long Parameter List y Primitive Obsession
    public Solicitud registrarSolicitud(String legajo, String nombre, String email,
                                        String tipologia, String descripcion, int urgenciaSolicitada) {

        // Codigo duplicado de validaciones
        if (legajo == null || legajo.trim().isEmpty() || legajo.length() < 4) {
            System.out.println("Error: Legajo invalido.");
            return null;
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("Error: Formato de correo incorrecto.");
            return null;
        }
        if (descripcion == null || descripcion.length() < 10) {
            System.out.println("Error: La descripcion es insuficiente.");
            return null;
        }

        Solicitud sol = new Solicitud();
        sol.id = contadorId++;
        sol.estudiante = new Estudiante(legajo, nombre, email);
        sol.tipologia = tipologia;
        sol.descripcion = descripcion;
        sol.fechaCreacion = LocalDate.now();
        sol.estado = new EstadoSolicitud("PENDIENTE", "Solicitud recien ingresada");
        sol.resolucionNotas = "";

        // Condicionales anidados (viola OCP)
        if (tipologia.equalsIgnoreCase("CERTIFICADO")) {
            sol.areaAsignada = new Area("ALUMNOS", "Departamento de Alumnos", "alumnos@frd.utn.edu.ar");
            sol.nivelPrioridad = 1;
        } else if (tipologia.equalsIgnoreCase("EQUIVALENCIA")) {
            sol.areaAsignada = new Area("ACADEMICA", "Secretaria Academica", "academica@frd.utn.edu.ar");
            if (urgenciaSolicitada >= 2) {
                sol.nivelPrioridad = 3;
            } else {
                sol.nivelPrioridad = 2;
            }
        } else if (tipologia.equalsIgnoreCase("REINSCRIPCION")) {
            sol.areaAsignada = new Area("SISTEMAS", "Departamento de Sistemas", "sistemas@frd.utn.edu.ar");
            sol.nivelPrioridad = 2;
        } else {
            sol.areaAsignada = new Area("MESA_GENERAL", "Mesa de Entradas", "mesa@frd.utn.edu.ar");
            sol.nivelPrioridad = 1;
        }

        dbSolicitudes.add(sol);

        try {
            servicioAlerta.enviarAlertaExterna(email, "Solicitud creada #" + sol.id, sol.nivelPrioridad);
        } catch (Exception e) {
            // Silencio de excepciones
        }

        return sol;
    }

    public boolean actualizarEstado(int idSolicitud, String codigoNuevoEstado, String observacion) {
        Solicitud encontrada = null;
        for (Solicitud s : dbSolicitudes) {
            if (s.id == idSolicitud) {
                encontrada = s;
                break;
            }
        }

        if (encontrada == null) {
            return false;
        }

        if (codigoNuevoEstado.equals("APROBADA") || codigoNuevoEstado.equals("RECHAZADA")) {
            if (encontrada.estado.codigo.equals("PENDIENTE")) {
                System.out.println("Regla violada: primero debe estar EN_PROCESO.");
                return false;
            }
        }

        encontrada.estado = new EstadoSolicitud(codigoNuevoEstado, "Estado modificado");
        encontrada.resolucionNotas = observacion;
        return true;
    }

    public boolean reasignarArea(int idSolicitud, String codigoNuevaArea, String nombreArea, String emailArea) {
        for (Solicitud s : dbSolicitudes) {
            if (s.id == idSolicitud) {
                s.areaAsignada = new Area(codigoNuevaArea, nombreArea, emailArea);
                return true;
            }
        }
        return false;
    }

    public List<Solicitud> obtenerSolicitudesPorEstadoYArea(String codigoEstado, String codigoArea) {
        List<Solicitud> resultado = new ArrayList<>();
        for (Solicitud s : dbSolicitudes) {
            boolean coincideEstado = (codigoEstado == null || codigoEstado.isEmpty())
                    || s.estado.codigo.equalsIgnoreCase(codigoEstado);
            boolean coincideArea = (codigoArea == null || codigoArea.isEmpty())
                    || s.areaAsignada.codigo.equalsIgnoreCase(codigoArea);

            if (coincideEstado && coincideArea) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public List<Solicitud> getTodas() {
        return dbSolicitudes;
    }
}