import java.time.LocalDate;

public class Solicitud {
    public int id;
    public Estudiante estudiante;
    public String tipologia;            // "CERTIFICADO", "EQUIVALENCIA", "REINSCRIPCION"
    public String descripcion;
    public EstadoSolicitud estado;
    public Area areaAsignada;
    public int nivelPrioridad;
    public LocalDate fechaCreacion;
    public String resolucionNotas;
}